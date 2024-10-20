package io.github.teambutterpl.butterlib.paper

import io.github.teambutterpl.butterlib.api.data.BPConfig
import io.github.teambutterpl.butterlib.api.data.BPStorage
import io.github.teambutterpl.butterlib.paper.managers.ButterLibManager
import io.github.teambutterpl.butterlib.paper.managers.DataManager
import io.github.teambutterpl.butterlib.paper.managers.GUIManager
import io.github.teambutterpl.butterlib.paper.managers.ResourcePackManager
import io.github.teambutterpl.butterlib.paper.managers.SocketManager
import org.bukkit.plugin.Plugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStream

class ButterLibrary {
    companion object {
        private lateinit var plugin: ButterLib
        private lateinit var dataManager: DataManager
        private lateinit var butterLibManager: ButterLibManager
        private lateinit var socketManager: SocketManager
        private lateinit var guiManager: GUIManager
        private lateinit var resourcePackManager: ResourcePackManager
        private lateinit var logger: Logger

        fun init(plugin: ButterLib) {
            if (this::plugin.isInitialized) throw IllegalStateException("Already initialized")
            Companion.plugin = plugin
            logger = LoggerFactory.getLogger(ButterLibrary::class.java)

            dataManager = DataManager(plugin)

            butterLibManager = ButterLibManager(plugin)

            socketManager = SocketManager(plugin)
            socketManager.init()

            guiManager = GUIManager(plugin)

            resourcePackManager = ResourcePackManager(plugin)
            resourcePackManager.buildDefaultResourcePack()
        }

        fun final() {
            if (!this::plugin.isInitialized) throw IllegalStateException("Not initialized")
            socketManager.final()
            dataManager.final()
        }

        private fun toAbsolutePath(path: String) = if (path.contains("/")) path else "/$path"

        fun <T> useResource(path: String, block: (InputStream?) -> T): T {
            return this::class.java.getResourceAsStream(toAbsolutePath(path)).use(block)
        }

        fun getResourceString(path: String): String? {
            return useResource(path, { it?.bufferedReader()?.readText() })
        }

        fun getPlugin(): Plugin = plugin
        fun getLogger(): Logger = logger

        fun getDataManager(): DataManager = dataManager
        fun getStorageManager(): BPStorage = dataManager.getStorageManager()
        fun getConfigManager(): BPConfig = dataManager.getConfigManager()

        fun getSocketManager(): SocketManager = socketManager

        fun getGUIManager(): GUIManager = guiManager

        fun getResourcePackManager(): ResourcePackManager = resourcePackManager
    }
}