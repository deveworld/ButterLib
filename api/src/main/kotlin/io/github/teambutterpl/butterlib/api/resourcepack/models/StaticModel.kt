package io.github.teambutterpl.butterlib.api.resourcepack.models

interface StaticModel : BaseModel {
    override val outputPath: String
    val data: ByteArray

    override fun write(): ByteArray {
        return data
    }
}