package io.github.teambutterpl.butterlib.api.data

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.DumperOptions
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.nio.charset.StandardCharsets
import kotlin.enums.EnumEntries

class BPConfig {
    private lateinit var configFolder: File
    private var initialized = false

    private lateinit var config: MutableMap<String, Any?>
    private val yaml: Yaml

    companion object {
        @Volatile private var INSTANCE: BPConfig? = null
        fun getInstance(): BPConfig =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BPConfig().also { INSTANCE = it }
            }
        fun final() = synchronized(this) {
            if (INSTANCE == null) return
            INSTANCE!!.saveConfig()
            INSTANCE!!.initialized = false
        }
    }

    init {
        val options = DumperOptions()
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        options.isPrettyFlow = true
        yaml = Yaml(options)
    }

    fun init(folder: File, defaultConfig: String, configKeys: EnumEntries<*>) {
        if (initialized) throw IllegalStateException("Already initialized. Please use reload.")
        initialized = true
        configFolder = folder
        if (!configFolder.exists()) configFolder.mkdirs()
        val configFile = File(configFolder, "config.yml")

        if (!configFile.exists()) {
            configFile.createNewFile()
        }
        if (configFile.reader().readText().isBlank()) {
            FileWriter(configFile).use { it.write(defaultConfig) }
        }

        config = yaml.load<MutableMap<String, Any?>>(FileInputStream(configFile)) ?: mutableMapOf()

        for (key in configKeys) {
            if (key !is Key) throw IllegalArgumentException("Config keys must implement Key interface.")
            if (!config.containsKey(key.key)) {
                config[key.key] = mutableMapOf<String, Any?>()
            } else if (config[key.key] !is MutableMap<*, *>) {
                config[key.key] = mutableMapOf<String, Any?>()
            }
        }
    }

    fun reload() {
        val configFile = File(configFolder, "config.yml")
        config = yaml.load<MutableMap<String, Any?>>(FileInputStream(configFile)) ?: mutableMapOf()
    }

    fun saveConfig() {
        val configFile = File(configFolder, "config.yml")
        FileWriter(configFile, StandardCharsets.UTF_8).use {
            yaml.dump(config, it)
        }
    }

    fun setConfig(property: Key, key: String, value: Any?) {
        val section = config.getOrPut(property.key) { mutableMapOf<String, Any?>() }
        if (section is MutableMap<*, *>) {
            @Suppress("UNCHECKED_CAST")
            (section as MutableMap<String, Any?>)[key] = value
        } else {
            config[property.key] = mutableMapOf(key to value)
        }
    }

    fun getConfig(property: Key, key: String): Any? {
        val section = config[property.key]
        return if (section is Map<*, *>) {
            section[key]
        } else {
            null
        }
    }

    fun removeConfig(property: Key, key: String) {
        val section = config[property.key]
        if (section is MutableMap<*, *>) {
            @Suppress("UNCHECKED_CAST")
            (section as MutableMap<String, Any?>).remove(key)
        }
    }

    /**
     * Getters
     */

    fun getString(property: Key, key: String): String? {
        return getConfig(property, key)?.toString()
    }

    fun getInt(property: Key, key: String): Int? {
        return getConfig(property, key) as? Int
    }

    fun getBoolean(property: Key, key: String): Boolean? {
        return getConfig(property, key) as? Boolean
    }

    fun getDouble(property: Key, key: String): Double? {
        return getConfig(property, key) as? Double
    }

    fun getLong(property: Key, key: String): Long? {
        return getConfig(property, key) as? Long
    }

    fun getFloat(property: Key, key: String): Float? {
        return getConfig(property, key) as? Float
    }

    fun getShort(property: Key, key: String): Short? {
        return getConfig(property, key) as? Short
    }

    fun getByte(property: Key, key: String): Byte? {
        return getConfig(property, key) as? Byte
    }

    fun getChar(property: Key, key: String): Char? {
        return getConfig(property, key) as? Char
    }

    fun getList(property: Key, key: String): List<*>? {
        return getConfig(property, key) as? List<*>
    }

    fun getMap(property: Key, key: String): Map<*, *>? {
        return getConfig(property, key) as? Map<*, *>
    }
}