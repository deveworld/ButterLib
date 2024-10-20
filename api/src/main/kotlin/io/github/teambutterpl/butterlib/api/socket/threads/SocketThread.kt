package io.github.teambutterpl.butterlib.api.socket.threads

import io.github.teambutterpl.butterlib.api.socket.pakcets.BasePacket
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

abstract class SocketThread: Thread() {
    fun send(socket: Socket, data: BasePacket) {
        val outputStream = ObjectOutputStream(socket.getOutputStream())
        outputStream.writeObject(data)
        outputStream.flush()
    }

    fun receive(socket: Socket): BasePacket {
        val inputStream = ObjectInputStream(socket.getInputStream())
        val input = inputStream.readObject()
        if (input !is BasePacket) throw IllegalArgumentException("Received data is not serializable")
        return input
    }
}