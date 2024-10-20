package io.github.teambutterpl.butterlib.api.resourcepack.models

class PackMcmeta(
    private val description: String,
    private val format: Int
) : BaseModel {
    override val outputPath = "/pack.mcmeta"
    override fun write(): ByteArray {
        return """
            {
                "pack": {
                    "pack_format": $format,
                    "description": "$description"
                }
            }
        """.trimIndent().toByteArray()
    }
}