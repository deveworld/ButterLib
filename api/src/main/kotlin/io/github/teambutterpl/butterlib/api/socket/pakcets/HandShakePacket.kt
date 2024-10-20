package io.github.teambutterpl.butterlib.api.socket.pakcets


data class HandShakePacket(
    val serverName: String,
    val version: String = "1.0.0",
) : BasePacket