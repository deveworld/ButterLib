package io.github.teambutterpl.butterlib.paper.gui

import io.github.teambutterpl.butterlib.paper.utils.TextInfo
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class ChatGUI(
    titleComponent: TextComponent,
    textComponent: TextComponent,
    private val closed: Runnable = Runnable {}
) : TextGUI(titleComponent, textComponent) {
    private val exit: ItemStack = ItemStack(Material.BARRIER)
    override val preventUserChange: Boolean = true

    constructor(title: String, text: String, closed: Runnable = Runnable {}) : this(Component.text(title), Component.text(text), closed)

    init {
        super.init(this)
        val itemMata = exit.itemMeta
        itemMata.displayName(
            Component.text("Exit")
                .color(TextColor.color(255, 100, 100))
                .decoration(TextDecoration.ITALIC, false)
        )
        exit.itemMeta = itemMata
        inventory.setItem(40, exit)
    }

    override fun onClick(event: InventoryClickEvent) {
        if (event.currentItem == exit) {
            event.whoClicked.closeInventory()
            closed.run()
        }
    }

    override fun onClose(event: InventoryCloseEvent) {
        closed.run()
    }
}