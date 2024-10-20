package io.github.teambutterpl.butterlib.api.socket.threads

import io.github.teambutterpl.butterlib.api.socket.BaseSocketManager
import io.github.teambutterpl.butterlib.api.socket.handlers.BaseSocketHandler
import io.github.teambutterpl.butterlib.api.socket.pakcets.BasePacket
import io.github.teambutterpl.butterlib.api.socket.pakcets.HandShakePacket
import io.github.teambutterpl.butterlib.api.socket.pakcets.NewServerPacket
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import kotlin.concurrent.schedule

class ServerThread(
    private val manager: BaseSocketManager
): SocketThread() {
    private val serverName: String = manager.serverName
    private val port: Int = manager.port
    private val findDelay: Long = manager.findDelay

    private val serverSocket = ServerSocket(port)
    private val clientSockets = HashMap<String, ClientThread>()
    private val listeners = HashSet<BaseSocketHandler<out BasePacket>>()
    private var isRunning = true

    override fun run() {
        if (findDelay == -1L) {
            findOtherThread()
        } else {
            Timer().schedule(0, findDelay) {
                findOtherThread(true)
            }
        }
        serverSocket.soTimeout = 500
        while (isRunning) {
            kotlin.runCatching {
                val socket = serverSocket.accept()
                val handShakePacket = HandShakePacket(serverName)
                send(socket, handShakePacket)
                val clientHandShakePacket = (receive(socket) as HandShakePacket)
                if (handShakePacket.version != clientHandShakePacket.version) {
                    socket.close()
                    return@runCatching
                }
                val clientName = clientHandShakePacket.serverName
                val thread = ClientThread(clientName, socket, this)
                thread.start()
                clientSockets[clientName] = thread
            }
        }
    }

    fun register(listener: BaseSocketHandler<out BasePacket>) {
        if (listeners.contains(listener)) return
        listeners.add(listener)
    }

    fun unregister(listener: BaseSocketHandler<out BasePacket>) {
        if (!listeners.contains(listener)) return
        listeners.remove(listener)
    }

    fun connect(port: Int, clientName: String? = null, broadcast: Boolean = false): Boolean {
        if (clientName != null && clientSockets.containsKey(clientName)) return false

        if (port == this.port) return false
        val socket = Socket("127.0.0.1", port)
        val handShakePacket = HandShakePacket(serverName)
        val serverHandShakePacket = receive(socket) as HandShakePacket
        if (handShakePacket.version != serverHandShakePacket.version) {
            socket.close()
            return false
        }
        if (serverHandShakePacket.serverName == clientName) {
            socket.close()
            return false
        }
        if (broadcast) sendAll(NewServerPacket(port))
        send(socket, handShakePacket)
        val thread = ClientThread(serverHandShakePacket.serverName, socket, this)
        thread.start()
        clientSockets[serverHandShakePacket.serverName] = thread
        return true
    }

    private fun findOtherThread(broadcast: Boolean = false) {
        Thread {
            PortScanThread.portScans().forEach { (name, port) ->
                connect(port, name, broadcast = broadcast)
            }
        }.start()
    }

    fun send(name: String, data: BasePacket) {
        for (thread in clientSockets.values) {
            if (thread.name != name) continue
            send(thread.socket, data)
        }
    }

    fun sendAll(data: BasePacket) {
        for (thread in clientSockets.values) {
            send(thread.socket, data)
        }
    }

    fun input(name: String, data: BasePacket) {
        for (listener in listeners) {
            if (listener.packetClass.isInstance(data)) listener.onReceive(name, data, manager)
        }
    }

    fun kill(name: String) {
        val thread = clientSockets[name] ?: return
        thread.receiving = false
        thread.socket.close()
        clientSockets.remove(name)
    }

    fun close() {
        isRunning = false
        for (thread in clientSockets.values) {
            thread.receiving = false
        }
        serverSocket.close()
    }
}