package io.github.teambutterpl.butterlib.paper.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TextSplitterTest {

    private val textInfo = TextInfo("unifont", 10)

    private val textSplitter = TextSplitter(textInfo)

    private fun printComponent(component: Component) {
        val lines = listOf(component.children(listOf())) + component.children()

        for (child in lines) {
            println(child)
        }
    }

    private fun printComponents(components: List<TextComponent>) {
        for (component in components) {
            println(TextInfo.componentToString(component))
        }
    }

    @Test
    fun justForTest() {
        val component = Component.text("This is a first line.")
            .append(Component.text("This is a second line."))
            .append(Component.text("This is a third line."))

        printComponent(component)
    }

    @Test
    fun `test splitTextComponent with simple text`() {
        val component = Component.text("This is a test text that should be split into multiple lines.")
        val result = textSplitter.splitTextComponent(component, 170)

        assertEquals(2, result.size)
        assertEquals("This is a test text that should be split ", result[0].content())
        assertEquals("into multiple lines.", result[1].content())
    }

    @Test
    fun `test splitTextComponent with styled text`() {
        val component = Component.text("This is a ")
            .append(Component.text("styled", TextColor.color(0xFF0000)))
            .append(Component.text(" text that should be split into multiple lines."))

        val result = textSplitter.splitTextComponent(component, 190)

        printComponents(result)
        assertEquals(2, result.size)
        assertEquals("This is a styled text that should be split ", TextInfo.componentToString(result[0]))
        assertEquals("into multiple lines.", TextInfo.componentToString(result[1]))
    }

    @Test
    fun `test splitTextComponent with nested components`() {
        val component = Component.text("This is a ")
            .append(Component.text("nested ", TextColor.color(0x00FF00)))
            .append(Component.text("component ", TextColor.color(0x0000FF)))
            .append(Component.text("test."))

        val result = textSplitter.splitTextComponent(component, 150)

        printComponents(result)
        assertEquals(2, result.size)
        assertEquals("This is a nested component ", TextInfo.componentToString(result[0]))
        assertEquals("test.", TextInfo.componentToString(result[1]))
    }

    @Test
    fun `long nested components`() {
        val component = Component.text("안녕하세요. \n").color(TextColor.color(255,100,100))
            .append(Component.text("이건 파란색으로 표시되고요. ").color(TextColor.color(100,100,255)))
            .append(Component.text("이건 다르게 초록색으로 표시됩니다! ").color(TextColor.color(100,255,100)))

        val result = textSplitter.splitTextComponent(component, 170)

        printComponents(result)
    }
}