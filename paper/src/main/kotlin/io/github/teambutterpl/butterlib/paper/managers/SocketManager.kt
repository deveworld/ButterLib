package io.github.teambutterpl.butterlib.paper.managers

import io.github.teambutterpl.butterlib.api.socket.BaseSocketManager
import io.github.teambutterpl.butterlib.paper.ButterLib
import io.github.teambutterpl.butterlib.paper.ButterLibrary
import java.util.*

class SocketManager(plugin: ButterLib) : BaseSocketManager() {
    override var serverName: String
    override var findDelay: Long

    init {
        val serverName = ButterLibrary.getConfigManager().getConfig(DataManager.Config.SOCKET, "serverName") as? String?
        if (serverName != null) {
            this.serverName = serverName.toString()
        } else {
            this.serverName = UUID.randomUUID().toString()
            ButterLibrary.getConfigManager().setConfig(DataManager.Config.SOCKET, "serverName", this.serverName)
        }

        val findDelay = ButterLibrary.getConfigManager()
            .getConfig(DataManager.Config.SOCKET, "findDelay").toString().toLongOrNull()
        if (findDelay != null) {
            this.findDelay = findDelay
        } else {
            this.findDelay = 5000
            ButterLibrary.getConfigManager().setConfig(DataManager.Config.SOCKET, "findDelay", this.findDelay)
        }
    }
}