package io.github.teambutterpl.butterlib.api.socket.threads

import io.github.teambutterpl.butterlib.api.socket.pakcets.HandShakePacket
import java.net.Socket
import java.util.HashMap

class PortScanThread(val port: Int): SocketThread() {
    lateinit var serverName: String
    var isOpen = false

    override fun run() {
        kotlin.runCatching {
            val socket = Socket("127.0.0.1", port)
            socket.soTimeout = 500
            isOpen = socket.isConnected
            serverName = (receive(socket) as HandShakePacket).serverName
            socket.close()
        }.onFailure { isOpen = false }.onSuccess { isOpen = true }
    }

    companion object {
        val PORT_RANGE = 25000..30000

        fun portScans(): HashMap<String, Int> {
            val list = HashMap<String, Int>()
            for (port in PORT_RANGE) {
                val portScanThread = PortScanThread(port)
                portScanThread.start()
                portScanThread.join()
                if (portScanThread.isOpen) {
                    list[portScanThread.serverName] = portScanThread.port
                    continue
                }
                break
            }
            return list
        }
    }
}