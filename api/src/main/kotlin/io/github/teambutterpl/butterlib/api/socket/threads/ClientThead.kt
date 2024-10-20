package io.github.teambutterpl.butterlib.api.socket.threads

import java.net.Socket

class ClientThread(
    private val socketName: String,
    val socket: Socket,
    private val serverThread: ServerThread
): SocketThread() {
    var receiving: Boolean = true
    override fun run() {
        while (receiving) {
            kotlin.runCatching {
                val input = receive(socket)
                serverThread.input(socketName, input)
            }.onFailure { serverThread.kill(socketName) }
        }
        socket.close()
    }
}