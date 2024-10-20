package io.github.teambutterpl.butterlib.paper.gui

import io.github.teambutterpl.butterlib.api.resourcepack.CharRepo
import io.github.teambutterpl.butterlib.api.resourcepack.ResourcePack
import io.github.teambutterpl.butterlib.paper.ButterLibrary
import io.github.teambutterpl.butterlib.paper.managers.DataManager
import io.github.teambutterpl.butterlib.paper.utils.TextSplitter
import io.github.teambutterpl.butterlib.paper.utils.TextInfo
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor

abstract class TextGUI(
    private val titleComponent: TextComponent,
    private val textComponent: TextComponent
) : BaseGUI() {
    private val textInfo: TextInfo
    init {
        val fontFileName = ButterLibrary.getConfigManager().getString(DataManager.Config.GUI, "fontFile")!!
        val fontName = fontFileName.split(".")[0]
        textInfo = TextInfo(fontName, 8)
    }

    private fun addTextComponent(component: TextComponent, key: Key) {
        val width = textInfo.text(component).getWidth()
        addComponent(component.font(key))
        addComponent(Component.text(CharRepo.getNeg(width)))
    }

    private fun getKey(line: Int): Key {
        return Key.key("font_8_${line}")
    }

    override fun drawGUI() {
        super.drawGUI()
        addComponent(Component.text(getDefaultImage(ResourcePack.DefaultImage.CHAT_BACKGROUND)).color(TextColor.color(255, 255, 255)))
//        addComponent(Component.text(CharRepo.getNeg(177)))
//        addComponent(Component.text(CharRepo.ITEM_1.literal).color(TextColor.color(255, 255, 255)))
        addComponent(Component.text(CharRepo.getNeg(177)))
        addComponent(Component.text(CharRepo.getPos(16)))

        val textSplitter = TextSplitter(textInfo)
        val splitTextList = textSplitter.splitTextComponent(textComponent)
        addTextComponent(titleComponent, getKey(1))
        var i = 2
        for (splitText in splitTextList) {
            if (i > 6) throw IndexOutOfBoundsException("Too long for this gui.")
            addTextComponent(splitText, getKey(i))
            i++
        }

        addComponent(Component.text(CharRepo.getNeg(16)))
    }
}