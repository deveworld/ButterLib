package io.github.teambutterpl.butterlib.paper

import org.bukkit.plugin.java.JavaPlugin

class ButterLib : JavaPlugin() {
    override fun onLoad() {
        saveDefaultConfig()
        reloadConfig()
    }

    override fun onEnable() {
        ButterLibrary.init(this)
    }

    override fun onDisable() {
        ButterLibrary.final()
    }
}