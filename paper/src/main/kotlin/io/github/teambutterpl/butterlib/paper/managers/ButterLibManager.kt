package io.github.teambutterpl.butterlib.paper.managers

import io.github.teambutterpl.butterlib.paper.listeners.GUIListener
import io.github.teambutterpl.butterlib.paper.ButterLib
import io.github.teambutterpl.butterlib.paper.listeners.BaseListener
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ButterLibManager(plugin: ButterLib) {
    init {
        val manager = plugin.server.pluginManager
        val listeners = listOf<KClass<out BaseListener>>(
            GUIListener::class,
        )
        for (listener in listeners) {
            manager.registerEvents(listener.primaryConstructor!!.call(plugin), plugin)
        }
    }
}