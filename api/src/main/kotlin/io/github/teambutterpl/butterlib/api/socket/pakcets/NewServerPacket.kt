package io.github.teambutterpl.butterlib.api.socket.pakcets

data class NewServerPacket(
    val port: Int,
) : BasePacket