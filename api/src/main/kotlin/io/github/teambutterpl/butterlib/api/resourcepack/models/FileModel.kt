package io.github.teambutterpl.butterlib.api.resourcepack.models

import java.io.File
import java.io.InputStream

abstract class FileModel(
    open val inputPath: String,
    override val outputPath: String,
    open val fromResources: Boolean = true,
) : StaticModel {
    override var data: ByteArray = byteArrayOf()
        get() {
            return if (fromResources) {
                this::class.java.getResourceAsStream(inputPath)!!.use { it.readBytes() }
            } else {
                File(inputPath).inputStream().use { it.readBytes() }
            }
        }
}