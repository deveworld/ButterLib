package io.github.teambutterpl.butterlib.api.resourcepack.models

class ImageFile(
    override val inputPath: String,
    override val outputPath: String,
    override val fromResources: Boolean = true,
) : FileModel(inputPath, outputPath, fromResources) {
    init {
        if (!inputPath.endsWith(".png")) {
            throw IllegalArgumentException("File must be a PNG image file.")
        }
    }
}