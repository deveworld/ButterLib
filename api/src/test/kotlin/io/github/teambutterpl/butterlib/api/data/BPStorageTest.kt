package io.github.teambutterpl.butterlib.api.data

import java.io.File
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class BPStorageTest {
    lateinit var storage: BPStorage

    @BeforeTest
    fun init() {
        storage = BPStorage.getInstance()
    }

    @AfterTest
    fun final() {
        BPStorage.final()
    }

    enum class Data(override val key: String): Key {
        A("A"),
        B("B"),
        ;
    }

    enum class NotMemory(val key: String) {
        C("C"),
        D("D"),
        ;
    }

    enum class Memory(override val key: String): Key {
        C("C"),
        D("D"),
        ;
    }

    @Test
    fun test() {
        assertFailsWith<IllegalArgumentException> {
            storage.init(
                File("test"),
                "{}",
                Data.entries,
                NotMemory.entries
            )
        }
        assertFailsWith<IllegalStateException> {
            storage.init(
                File("test"),
                "{}",
                Data.entries,
                Memory.entries
            )
        }
        BPStorage.final()
        storage.init(
            File("test"),
            "{}",
            Data.entries,
            Memory.entries
        )
    }
}