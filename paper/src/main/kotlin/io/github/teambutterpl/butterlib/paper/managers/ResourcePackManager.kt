package io.github.teambutterpl.butterlib.paper.managers

import io.github.teambutterpl.butterlib.api.resourcepack.ResourcePack
import io.github.teambutterpl.butterlib.api.resourcepack.ResourcePackBuilder
import io.github.teambutterpl.butterlib.paper.ButterLib
import io.github.teambutterpl.butterlib.paper.ButterLibrary
import java.io.File

class ResourcePackManager(plugin: ButterLib) {
    private lateinit var resourcePack: ResourcePack
    private val resourcePackBuilder = ResourcePackBuilder()
    private lateinit var output: File

    init {
        resourcePackBuilder.resourcePackName("butterlib")
        resourcePackBuilder.outputPathRoot(plugin.dataFolder.absolutePath)
        val fontFileName = ButterLibrary.getConfigManager().getString(DataManager.Config.GUI, "fontFile")!!
        val fromResource = fontFileName == "unifont.ttf"
        resourcePackBuilder.fontFile(fontFileName, fromResource = fromResource)
        resourcePackBuilder.packMcmeta("ButterLib default resource pack")
        resourcePackBuilder.packPngFile("pack.png")
    }

    fun buildDefaultResourcePack() {
        setResourcePack(resourcePackBuilder.build())
    }

    fun checkResourcePack(): Boolean {
        return this::resourcePack.isInitialized
    }

    fun getResourcePackBuilder(): ResourcePackBuilder {
        return resourcePackBuilder
    }

    fun setResourcePack(resourcePack: ResourcePack) {
        if (checkResourcePack()) throw IllegalStateException("Resource pack already initialized")
        this.resourcePack = resourcePack
        this.output = resourcePack.output
    }

    fun getResourcePack(): ResourcePack {
        if (!checkResourcePack()) throw IllegalStateException("Resource pack not initialized")
        return resourcePack
    }
}