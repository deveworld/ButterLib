package io.github.teambutterpl.butterlib.api.resourcepack.models

import java.io.InputStream

class FontFile(
    override val inputPath: String,
    override val outputPath: String,
    override val fromResources: Boolean = true,
) : FileModel(inputPath, outputPath, fromResources) {
    init {
        if (!inputPath.endsWith(".ttf")) {
            throw IllegalArgumentException("File must be a TTF font file.")
        }
    }
}