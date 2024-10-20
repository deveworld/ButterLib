package io.github.teambutterpl.butterlib.paper.managers

import io.github.teambutterpl.butterlib.api.data.BPConfig
import io.github.teambutterpl.butterlib.api.data.BPStorage
import io.github.teambutterpl.butterlib.api.data.Key
import io.github.teambutterpl.butterlib.paper.ButterLib
import io.github.teambutterpl.butterlib.paper.ButterLibrary

class DataManager(plugin: ButterLib) {
    private val storage = BPStorage.getInstance()
    private val config = BPConfig.getInstance()

    enum class Data(override val key: String): Key {
        ;
    }
    enum class Memory(override val key: String): Key {
        ;
    }
    enum class Config(override val key: String): Key {
        SOCKET("socket"),
        GUI("gui"),
        ;
    }

    init {
        storage.init(
            plugin.dataFolder,
            ButterLibrary.getResourceString("data.json")!!,
            Data.entries,
            Memory.entries
        )
        config.init(
            plugin.dataFolder,
            ButterLibrary.getResourceString("config.yml")!!,
            Config.entries
        )
    }

    fun getStorageManager() = storage
    fun getConfigManager() = config

    fun final() {
        storage.saveData()
        config.saveConfig()
        BPStorage.final()
        BPConfig.final()
    }
}