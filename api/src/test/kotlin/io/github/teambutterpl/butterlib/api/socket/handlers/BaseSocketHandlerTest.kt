package io.github.teambutterpl.butterlib.api.socket.handlers

import io.github.teambutterpl.butterlib.api.socket.BaseSocketManager
import io.github.teambutterpl.butterlib.api.socket.pakcets.BasePacket
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

data class HandShakePacket(
    val version: String,
    val serverName: String,
) : BasePacket

data class NotHandShakePacket(
    val versionN: String,
    val serverNameN: String,
) : BasePacket

class SocketManager: BaseSocketManager() {
    override var serverName: String = "server"
    override var findDelay: Long = 10000

    fun input(name: String, data: BasePacket) {
        server.input(name, data)
    }
}

class BaseSocketHandlerTest {
    private val outContent = ByteArrayOutputStream()
    private val errContent = ByteArrayOutputStream()
    private val originalOut: PrintStream = System.out
    private val originalErr: PrintStream = System.err
    private lateinit var manager: SocketManager

    @BeforeTest
    fun setUpStreams() {
        System.setOut(PrintStream(outContent))
        System.setErr(PrintStream(errContent))
    }

    @BeforeTest
    fun init() {
        manager = SocketManager()
        manager.init()
    }

    @AfterTest
    fun restoreStreams() {
        System.setOut(originalOut)
        System.setErr(originalErr)
    }

    @AfterTest
    fun final() {
        manager.final()
    }

    private fun assertOutput(expected: String) {
        assertEquals(expected, outContent.toString())
        outContent.reset()
    }

    @Test
    fun `BaseSocketHandler invoke`() {
        val clientName = "test"
        val handShakePacket = HandShakePacket("1.0.0", clientName)
        val notHandShakePacket = NotHandShakePacket("x.x.x", clientName)

        val handler = BaseSocketHandler<HandShakePacket> { _, packet, _ ->
            print(packet)
        }
        manager.register(handler)

        manager.input(clientName, handShakePacket)
        manager.input(clientName, notHandShakePacket)

        assertOutput(handShakePacket.toString())

        val notHandler = BaseSocketHandler<NotHandShakePacket> { _, packet, _ ->
            print(packet)
        }
        manager.register(notHandler)

        manager.input(clientName, handShakePacket)
        manager.input(clientName, notHandShakePacket)

        assertOutput(handShakePacket.toString()+notHandShakePacket.toString())

        manager.unregister(handler)

        manager.input(clientName, handShakePacket)
        manager.input(clientName, notHandShakePacket)

        assertOutput(notHandShakePacket.toString())
    }
}