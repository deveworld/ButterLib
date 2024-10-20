package io.github.teambutterpl.butterlib.paper.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.apache.commons.lang.StringUtils

class TextSplitter(private val textInfo: TextInfo) {
    private fun applyStyle(text: String, textComponent: TextComponent): TextComponent {
        return Component.text(text).style(textComponent.style())
    }

    fun splitTextComponent(component: TextComponent, maxWidth: Int = 130): List<TextComponent> {
        val results = mutableListOf<TextComponent>()
        val lines = listOf(component.children(listOf())) + component.children()

        var currentResult: TextComponent? = null
        var currentLine = ""
        fun getLineWidth(): Int {
            val resultWidth = if (currentResult == null) 0 else textInfo.text(currentResult!!).getWidth()
            return resultWidth + textInfo.text(currentLine).getWidth()
        }
        fun addResult(textComponent: TextComponent, forceLF: Boolean = false) {
            if (TextInfo.componentToString(textComponent).isEmpty()) {
                return
            }
            currentResult = if (currentResult == null) {
                textComponent
            } else {
                currentResult!!.append(textComponent)
            }
            val width = getLineWidth()
            if (width >= maxWidth || forceLF) {
                results.add(currentResult!!)
                currentResult = null
                currentLine = ""
            }
        }
        for (line in lines) {
            val text = TextInfo.componentToString(line as TextComponent)
            val words = text.split(" ")
            for (word in words) {
                if (word == "\n") {
                    addResult(applyStyle(currentLine, line), forceLF = true)
                    currentLine = ""
                    continue
                }
                val wordWidth = textInfo.text(word).getWidth()
                if (getLineWidth() + wordWidth > maxWidth) {
                    if (wordWidth > maxWidth) {
                        val chars = word.toCharArray()
                        var currentWord = ""
                        var currentWordWidth = getLineWidth()
                        for (char in chars) {
                            val charWidth = textInfo.text(char.toString()).getWidth()
                            if (currentWordWidth + charWidth > maxWidth) {
                                addResult(applyStyle(currentWord, line), forceLF = true)
                                currentWord = ""
                                currentWordWidth = 0
                            }
                            currentWord += char
                            currentWordWidth += charWidth
                        }
                        addResult(applyStyle("$currentWord ", line))
                        currentLine = ""
                        continue
                    } else {
                        addResult(applyStyle(currentLine, line))
                        currentLine = ""
                    }
                }
                currentLine += "$word "
            }
            if (currentLine.isNotEmpty()) {
                currentLine = StringUtils.chop(currentLine)
                addResult(applyStyle(currentLine, line))
                currentLine = ""
            }
        }

        if (currentResult != null) {
            results.add(currentResult!!)
        }

        return results
    }
}