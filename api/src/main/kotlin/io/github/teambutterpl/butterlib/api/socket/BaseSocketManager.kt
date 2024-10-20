package io.github.teambutterpl.butterlib.api.socket

import io.github.teambutterpl.butterlib.api.socket.handlers.BaseSocketHandler
import io.github.teambutterpl.butterlib.api.socket.handlers.NewServerSocketHandler
import io.github.teambutterpl.butterlib.api.socket.pakcets.BasePacket
import io.github.teambutterpl.butterlib.api.socket.pakcets.NewServerPacket
import io.github.teambutterpl.butterlib.api.socket.threads.PortScanThread
import io.github.teambutterpl.butterlib.api.socket.threads.ServerThread

abstract class BaseSocketManager {
    protected lateinit var server: ServerThread
    abstract var serverName: String
    abstract var findDelay: Long
    var port: Int = PortScanThread.PORT_RANGE.first

    fun init() {
        val openPorts = PortScanThread.portScans()
        port = if (openPorts.values.isEmpty()) {
            PortScanThread.PORT_RANGE.first
        } else {
            findDelay = -1
            openPorts.values.last()+1
        }
        if (serverName.isEmpty()) {
            serverName = port.toString()
        }

        server = ServerThread(this)
        server.start()
        register(NewServerSocketHandler())

//        val handler = BaseSocketHandler<NewServerPacket> { _, packet, manager ->
//            manager.connect(packet.port)
//        }
//        register(handler)
    }

    fun final() {
        if (!this::server.isInitialized) return
        server.close()
    }

    fun renameServerName(serverName: String) {
        this.serverName = serverName
        server.close()
        init()
    }

    fun sendAll(data: BasePacket) {
        server.sendAll(data)
    }

    fun send(name: String, data: BasePacket) {
        server.send(name, data)
    }

    fun connect(port: Int, clientName: String? = null, broadcast: Boolean = false): Boolean {
        return server.connect(port, clientName, broadcast)
    }

    fun register(listener: BaseSocketHandler<out BasePacket>) {
        server.register(listener)
    }

    fun unregister(listener: BaseSocketHandler<out BasePacket>) {
        server.unregister(listener)
    }
}