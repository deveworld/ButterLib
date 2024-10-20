package io.github.teambutterpl.butterlib.paper.managers

import io.github.teambutterpl.butterlib.paper.ButterLib
import io.github.teambutterpl.butterlib.paper.ButterLibrary
import io.github.teambutterpl.butterlib.paper.gui.BaseGUI
import io.github.teambutterpl.butterlib.paper.gui.ChatGUI
import io.github.teambutterpl.butterlib.paper.gui.ConfirmGUI
import net.kyori.adventure.text.TextComponent
import org.apache.commons.io.FileUtils
import org.bukkit.entity.Player
import java.awt.Font
import java.awt.FontFormatException
import java.awt.GraphicsEnvironment
import java.io.File
import java.io.IOException
import java.io.InputStream
import kotlin.reflect.KClass

class GUIManager(plugin: ButterLib) {
    private val guis = mutableListOf<KClass<out BaseGUI>>()

    init {
        val fontFileName = ButterLibrary.getConfigManager().getString(DataManager.Config.GUI, "fontFile")!!
        val file = File(plugin.dataFolder, fontFileName)
        try {
            val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
            if (!file.exists()) {
                ButterLibrary.useResource(fontFileName) {
                    FileUtils.copyInputStreamToFile(it, file)
                }
            }
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, file))
        } catch (_: IOException) {} catch (_: FontFormatException) {} catch (_: NullPointerException) {
            ButterLibrary.getLogger().error("Font file not found.")
        }
        initGUI(ChatGUI::class)
        initGUI(ConfirmGUI::class)
    }

    fun getGUIs(): List<KClass<out BaseGUI>> = guis

    fun initGUI(gui: KClass<out BaseGUI>) {
        guis.add(gui)
    }

    fun createChatGUI(player: Player, title: TextComponent, text: TextComponent) {
        val chatGui = ChatGUI(title, text)
        chatGui.open(player)
    }

    fun createConfirmGUI(player: Player, title: TextComponent, text: TextComponent, confirmed: Runnable, canceled: Runnable) {
        val confirmGui = ConfirmGUI(title, text, confirmed, canceled)
        confirmGui.open(player)
    }
}