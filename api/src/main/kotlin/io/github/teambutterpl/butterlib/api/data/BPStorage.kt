package io.github.teambutterpl.butterlib.api.data

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.enums.EnumEntries

class BPStorage {
    private lateinit var dataFolder: File
    private var initialized = false

    private lateinit var data: HashMap<String, HashMap<String, Any?>>
    private var memory: HashMap<String, HashMap<String, Any?>> = hashMapOf()

//    enum class Data(override val key: String): Key {
//        ;
//        override fun toString() = key
//    }
//    enum class Memory(override val key: String): Key {
//        ;
//        override fun toString() = key
//    }

    companion object {
        @Volatile private var INSTANCE: BPStorage? = null
        fun getInstance(): BPStorage =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BPStorage()
                    .also { INSTANCE = it }
            }
        fun final() = synchronized(this) {
            if (INSTANCE == null) return
            INSTANCE!!.saveData()
            INSTANCE!!.initialized = false
        }
    }

    fun init(folder: File, default: String, dataKeys: EnumEntries<*>, memoryKeys: EnumEntries<*>) {
        if (initialized) throw IllegalStateException("Already initialized, Please use reload.")
        initialized = true
        dataFolder = folder
        if (!dataFolder.exists()) dataFolder.mkdirs()
        val dataFile = File(dataFolder, "data.json")

        if (!dataFile.exists()) {
            dataFile.createNewFile()
        }
        if (dataFile.reader().readText().isBlank()) {
            FileWriter(dataFile).use { it.write(default) }
        }

        data = Gson().fromJson(
            JsonParser.parseReader(dataFile.reader(Charset.forName("UTF-8"))).asJsonObject,
            object : TypeToken<HashMap<String, HashMap<String, Any?>>>() {}.type
        )

        for (key in dataKeys) {
            if (key !is Key) throw IllegalArgumentException("Data keys must implement Key interface.")
            if (!data.contains(key.key))
                data[key.key] = hashMapOf()
        }
        for (key in memoryKeys) {
            if (key !is Key) throw IllegalArgumentException("Memory keys must implement Key interface.")
            if (!memory.contains(key.key))
                memory[key.key] = hashMapOf()
        }
    }

    fun reload() {
        val dataFile = File(dataFolder, "data.json")
        data = Gson().fromJson(
            JsonParser.parseReader(dataFile.reader(Charset.forName("UTF-8"))).asJsonObject,
            object : TypeToken<HashMap<String, HashMap<String, Any?>>>() {}.type
        )
    }

    fun saveData() {
        val dataFile = File(dataFolder, "data.json")
        FileOutputStream(dataFile).close()

        dataFile.outputStream().use { fileOutputStream ->
            fileOutputStream.write(
                Gson().toJsonTree(data).toString()
                    .toByteArray(Charset.forName("UTF-8"))
            )
        }
    }

    /**
     * Data
     */
    fun setData(property: Key, inputProperty: String, value: Any?) {
        data[property.key]?.set(inputProperty, value)
    }

    fun getData(property: Key, secondProperty: String)
            = data[property.key]?.get(secondProperty)

    fun removeData(property: Key, removeProperty: String) {
        data[property.key]?.remove(removeProperty)
    }

    /**
     * Memory
     */
    fun setMemory(property: Key, inputProperty: String, value: Any?) {
        memory[property.key]?.set(inputProperty, value)
    }

    fun getMemory(property: Key, secondProperty: String): Any?
            = memory[property.key]?.get(secondProperty)

    fun removeMemory(property: Key, removeProperty: String) {
        memory[property.key]?.remove(removeProperty)
    }
}