package io.github.teambutterpl.butterlib.api.resourcepack.models

import com.google.gson.Gson
import com.google.gson.JsonParser
import io.github.teambutterpl.butterlib.api.resourcepack.ResourcePack

class FontCustomDefault(
    private val files: List<String>
) : BaseModel {
    override val outputPath = "/assets/minecraft/font/custom/default.json"

    override fun write(): ByteArray {
        val data = JsonParser.parseString("{}").asJsonObject
        val providers = JsonParser.parseString("[]").asJsonArray
        for ((index, file) in files.withIndex()) {
            val provider = JsonParser.parseString("{}").asJsonObject
            provider.addProperty("ascent", 19)
            val chars = JsonParser.parseString("[]").asJsonArray
            val char = """\u""" + String.format("%04x", ResourcePack.unicodeToChar(index+1).code).uppercase()
            chars.add(char)
            provider.add("chars", chars)
            provider.addProperty("file", "custom/ui/$file")
            provider.addProperty("height", 256)
            provider.addProperty("type", "bitmap")
            providers.add(provider)
        }
        data.add("providers", providers)

        return Gson().toJsonTree(data).toString().replace("\\\\", "\\").toByteArray()
    }
}