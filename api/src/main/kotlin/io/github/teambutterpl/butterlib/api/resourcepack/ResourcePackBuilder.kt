package io.github.teambutterpl.butterlib.api.resourcepack

import io.github.teambutterpl.butterlib.api.resourcepack.models.BaseModel
import io.github.teambutterpl.butterlib.api.resourcepack.models.FontFile
import io.github.teambutterpl.butterlib.api.resourcepack.models.ImageFile
import io.github.teambutterpl.butterlib.api.resourcepack.models.PackMcmeta
import java.nio.file.InvalidPathException
import java.nio.file.Paths
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField
import kotlin.text.contains


class ResourcePackBuilder {
    lateinit var name: String
    lateinit var outputPathRoot: String
    lateinit var models: List<BaseModel>
    lateinit var fontFileName: String
    lateinit var packPngFile: ImageFile
    private lateinit var packMcmeta: PackMcmeta
    private lateinit var fontFile: FontFile

    val images = mutableListOf<ImageFile>()

    private fun toAbsolutePath(path: String) = if (path.contains("/")) path else "/$path"

    fun addImageFile(file: String, path: String = "/assets/minecraft/textures/custom/ui/$file"): ResourcePackBuilder {
        val fileName = toAbsolutePath(file)
        images.add(ImageFile(fileName, path))
        return this
    }

    fun outputPathRoot(outputPathRoot: String): ResourcePackBuilder {
        this.outputPathRoot = outputPathRoot
        return this
    }

    fun resourcePackName(name: String): ResourcePackBuilder {
        runCatching {
            Paths.get(name)
        }.getOrElse {
            when (it) {
                is InvalidPathException,
                is NullPointerException -> throw IllegalArgumentException("Invalid resource pack name")
                else -> throw it
            }
        }
        this.name = name
        return this
    }

    fun packMcmeta(description: String, format: Int = 34): ResourcePackBuilder {
        this.packMcmeta = PackMcmeta(description, format)
        return this
    }

    fun packPngFile(packPngFile: String): ResourcePackBuilder {
        this.packPngFile = ImageFile(toAbsolutePath(packPngFile), "/pack.png")
        return this
    }

    fun fontFile(fontFile: String, path: String = "/assets/minecraft/font/$fontFile", fromResource: Boolean = true): ResourcePackBuilder {
        fontFileName = toAbsolutePath(fontFile)
        this.fontFile = FontFile(fontFileName, path, fromResource)
        return this
    }

    fun build(): ResourcePack {
        this::class.declaredMemberProperties.forEach { property ->
            if (property.isLateinit && property.javaField?.get(this) == null && property.name != "models") {
                throw IllegalStateException("Property ${property.name} is not initialized")
            }
        }
        models = listOf(
            packPngFile,
            packMcmeta,
            fontFile,
        )
        return ResourcePack(this)
    }
}