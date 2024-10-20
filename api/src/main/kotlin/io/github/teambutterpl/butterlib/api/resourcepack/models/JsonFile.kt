package io.github.teambutterpl.butterlib.api.resourcepack.models

class JsonFile(
    override val inputPath: String,
    override val outputPath: String,
    override val fromResources: Boolean = true,
) : FileModel(inputPath, outputPath, fromResources) {
    init {
        if (!inputPath.endsWith(".json")) {
            throw IllegalArgumentException("File must be a Json data file.")
        }
    }
}