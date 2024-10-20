package io.github.teambutterpl.butterlib.api.socket.handlers

import io.github.teambutterpl.butterlib.api.socket.BaseSocketManager
import io.github.teambutterpl.butterlib.api.socket.pakcets.BasePacket
import kotlin.reflect.KClass

abstract class BaseSocketHandler<T : BasePacket> protected constructor(val packetClass: KClass<T>) {
    init {
        validate(packetClass)
    }

    protected abstract fun onReceiveData(name: String, data: T, manager: BaseSocketManager)

    fun onReceive(name: String, data: BasePacket, manager: BaseSocketManager) {
        if (packetClass.isInstance(data)) {
            @Suppress("UNCHECKED_CAST")
            onReceiveData(name, data as T, manager)
        }
    }

    companion object {
        fun <T : BasePacket> validate(packetClass: KClass<T>) {
            when {
                !BasePacket::class.java.isAssignableFrom(packetClass.java) ->
                    throw IllegalArgumentException("T must implement BasePacket")
                packetClass.java.name.startsWith("java.") || packetClass.java.name.startsWith("kotlin.") ->
                    throw IllegalArgumentException("T should not be a standard Java or Kotlin class")
            }
        }

        inline operator fun <reified T : BasePacket> invoke(
            crossinline onReceiveData: (String, T, BaseSocketManager) -> Unit
        ): BaseSocketHandler<T> {
            validate(T::class)
            return object : BaseSocketHandler<T>(T::class) {
                override fun onReceiveData(name: String, data: T, manager: BaseSocketManager) {
                    onReceiveData(name, data, manager)
                }
            }
        }
    }
}