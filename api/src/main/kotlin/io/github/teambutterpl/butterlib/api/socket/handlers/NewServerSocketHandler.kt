package io.github.teambutterpl.butterlib.api.socket.handlers

import io.github.teambutterpl.butterlib.api.socket.BaseSocketManager
import io.github.teambutterpl.butterlib.api.socket.pakcets.BasePacket
import io.github.teambutterpl.butterlib.api.socket.pakcets.NewServerPacket

//val NewServerSocketHandler = BaseSocketHandler<NewServerPacket> { _, packet, manager ->
//    manager.connect(packet.port)
//}

class NewServerSocketHandler : BaseSocketHandler<NewServerPacket>(NewServerPacket::class) {
    override fun onReceiveData(name: String, data: NewServerPacket, manager: BaseSocketManager) {
        manager.connect(data.port)
    }
}