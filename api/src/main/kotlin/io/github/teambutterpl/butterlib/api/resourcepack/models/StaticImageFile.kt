package io.github.teambutterpl.butterlib.api.resourcepack.models

class StaticImageFile(
    filename: String,
    override val outputPath: String,
    override var data: ByteArray,
) : ImageFile(filename, outputPath, false) {
    init {
        if (!filename.endsWith(".png")) {
            throw IllegalArgumentException("File must be a PNG image file.")
        }
    }
}