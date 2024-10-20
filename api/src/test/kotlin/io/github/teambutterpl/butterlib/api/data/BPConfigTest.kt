package io.github.teambutterpl.butterlib.api.data

import java.io.File
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class BPConfigTest {
    lateinit var config: BPConfig

    @BeforeTest
    fun init() {
        config = BPConfig.getInstance()
    }

    @AfterTest
    fun final() {
        BPStorage.final()
    }

    enum class NotConfig(val key: String) {
        C("C"),
        D("D"),
        ;
    }

    enum class Config(override val key: String): Key {
        C("C"),
        D("D"),
        ;
    }

    @Test
    fun test() {
        assertFailsWith<IllegalArgumentException> {
            config.init(
                File("test"),
                "",
                NotConfig.entries,
            )
        }
        assertFailsWith<IllegalStateException> {
            config.init(
                File("test"),
                "",
                Config.entries,
            )
        }
        BPConfig.final()
        config.init(
            File("test"),
            "",
            Config.entries,
        )
    }
}