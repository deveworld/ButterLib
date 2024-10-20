package io.github.teambutterpl.butterlib.api.resourcepack.models

interface BaseModel {
    val outputPath: String
    fun write(): ByteArray
}