package io.github.teambutterpl.butterlib.api.resourcepack

import io.github.teambutterpl.butterlib.api.resourcepack.models.BaseModel
import io.github.teambutterpl.butterlib.api.resourcepack.models.BaseModels
import io.github.teambutterpl.butterlib.api.resourcepack.models.FontCustomDefault
import io.github.teambutterpl.butterlib.api.resourcepack.models.FontJsonModels
import io.github.teambutterpl.butterlib.api.resourcepack.models.ImageFile
import io.github.teambutterpl.butterlib.api.resourcepack.models.JsonFile
import io.github.teambutterpl.butterlib.api.resourcepack.models.SpaceModels
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import java.nio.file.Paths

/**
 * File structure of a resource pack:
 *    * are dynamic files
 *      which can be changed by the user
 *
 * resource_pack_name/
 * │
 * ├── pack.mcmeta                    *
 * ├── pack.png                       *
 * │
 * └── assets/
 *     ├── minecraft/
 *     │   ├── font/
 *     │   │   ├── *.ttf              *
 *     │   │   ├── default.json
 *     │   │   ├── custom/
 *     │   │   │   └── default.json   *
 *     │   │   └── font_*.json ...    *
 *     │   │
 *     │   └── textures/
 *     │       └── custom/
 *     │           └── ui/
 *     │               └── *.png ...  *
 *     └── space/
 *         ├── font/
 *         │   └── default.json
 *         ├── lang/
 *         │   └── en_us.json
 *         └── textures/
 *             └── font/
 *                 ├── space_nosplit.png
 *                 └── space_split.png
 */

class ResourcePack(resourcePackBuilder: ResourcePackBuilder) {
    private val resourcePackName = resourcePackBuilder.name
    private val models = resourcePackBuilder.models
    private val images: List<ImageFile>
    private val imageChar = HashMap<String, Char>()
    private val outputPathRoot = File(resourcePackBuilder.outputPathRoot)
    private val dataPath = outputPathRoot.resolve(resourcePackName)
    val output = outputPathRoot.resolve("$resourcePackName.zip")

    companion object {
        private const val START_UNICODE = '\uF000'
        fun unicodeToChar(unicode: Int): Char {
            return START_UNICODE.plus(unicode)
        }
    }

    enum class DefaultImage(val path: String) {
        CHAT_BACKGROUND("chat_background.png"),
        CONFIRM("confirm.png"),
        ITEM_1("item_1.png"),
        MENU_CONTAINER("menu_container.png"),
        MENU_CONTAINER_27("menu_container_27.png");
    }

    private val outputFiles = mutableSetOf<String>()

    private fun writeModel(model: BaseModel) {
        if (outputFiles.contains(model.outputPath)) throw IllegalArgumentException("File ${model.outputPath} already exists, use different path")
        outputFiles.add(model.outputPath)
        val normalizedFilePath = model.outputPath.removePrefix("/")
        val output = Paths.get(dataPath.absolutePath, normalizedFilePath).normalize().toAbsolutePath().toFile()

        output.parentFile.mkdirs()
        if (!output.exists()) output.createNewFile()
        output.outputStream().use { it.write(model.write()) }
    }

    private fun writeModels(models: List<BaseModel>) {
        models.forEach { writeModel(it) }
    }

    fun getImage(name: String): Char {
        return imageChar[name] ?: throw IllegalArgumentException("Image $name not found")
    }

    private fun zip(inputDirectory: File, outputZipFile: File) {
        if (outputZipFile.exists()) {
            outputZipFile.delete()
        }
        outputZipFile.createNewFile()
        ZipOutputStream(BufferedOutputStream(FileOutputStream(outputZipFile))).use { zos ->
            inputDirectory.walkTopDown().forEach { file ->
                val zipFileName = file.absolutePath.removePrefix(inputDirectory.absolutePath).removePrefix("/")
                val entry = ZipEntry( "$zipFileName${(if (file.isDirectory) "/" else "" )}")
                zos.putNextEntry(entry)
                if (file.isFile) {
                    file.inputStream().use { fis -> fis.copyTo(zos) }
                }
            }
        }
    }

    init {
        val defaultImages = DefaultImage.entries.map { ImageFile("/resource_pack_default/ui/${it.path}", "/assets/minecraft/textures/custom/ui/${it.path}") }
        images = defaultImages + resourcePackBuilder.images
        val imageFileNames = images.map { File(it.inputPath).name }

        if (outputPathRoot.exists()) {
            outputPathRoot.deleteRecursively()
        }
        dataPath.mkdirs()

        val fontCustomDefault = FontCustomDefault(imageFileNames)
        val fontJsonModels = FontJsonModels(resourcePackBuilder.fontFileName)
        val spaceModels = SpaceModels()
        val fontDefault = JsonFile("/resource_pack_default/font/default.json", "/assets/minecraft/font/default.json")

        writeModel(fontCustomDefault)
        writeModel(fontDefault)
        writeModels(fontJsonModels.list())
        writeModels(spaceModels.list())

        imageFileNames.forEachIndexed { index, imageFileName ->
            imageChar[imageFileName] = unicodeToChar(index+1)
        }
        models.forEach { writeModel(it) }
        images.forEach { writeModel(it) }

        zip(dataPath, output)
        dataPath.deleteRecursively()
    }
}