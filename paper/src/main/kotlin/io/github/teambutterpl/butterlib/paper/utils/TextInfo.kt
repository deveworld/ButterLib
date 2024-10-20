package io.github.teambutterpl.butterlib.paper.utils

import net.kyori.adventure.text.TextComponent
import java.awt.Canvas
import java.awt.Font

// https://stackoverflow.com/a/71273638
class TextInfo(private val fontName: String, private val size: Int) {
    private val font: Font = Font(fontName, Font.PLAIN, size)
    private val fontMetrics = Canvas().getFontMetrics(font)
    private var text: String = ""
    private var width: Int = fontMetrics.stringWidth(text)

    companion object {
        fun componentToString(component: TextComponent): String {
            var str = component.content()
            for (child in component.children()) {
                if (child is TextComponent) {
                    str += componentToString(child)
                }
            }
            return str
        }
    }

    fun text(text: TextComponent): TextInfo {
        return text(componentToString(text))
    }

    fun text(text: String): TextInfo {
        this.text = text
        width = fontMetrics.stringWidth(text)
        return this
    }

    fun getWidth(): Int = width
}