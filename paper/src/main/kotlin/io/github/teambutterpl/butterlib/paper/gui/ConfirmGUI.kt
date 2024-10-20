package io.github.teambutterpl.butterlib.paper.gui

import io.github.teambutterpl.butterlib.api.resourcepack.CharRepo
import io.github.teambutterpl.butterlib.api.resourcepack.ResourcePack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class ConfirmGUI(
    titleComponent: TextComponent,
    textComponent: TextComponent,
    private val confirmed: Runnable,
    private val canceled: Runnable,
) : TextGUI(titleComponent, textComponent) {
    override val preventUserChange: Boolean = true
    private var done: Boolean = false

    constructor(title: String, text: String, confirmed: Runnable, canceled: Runnable)
            : this(Component.text(title), Component.text(text), confirmed, canceled)

    init {
        super.init(this)
    }

    override fun onClick(event: InventoryClickEvent) {
        if (event.slot == 42) {
            confirmed.run()
            done = true
            event.whoClicked.closeInventory()
        } else if (event.slot == 38) {
            canceled.run()
            done = true
            event.whoClicked.closeInventory()
        }
    }

    override fun onClose(event: InventoryCloseEvent) {
        if (!done) canceled.run()
    }

    override fun drawGUI() {
        super.drawGUI()
        addComponent(Component.text(getDefaultImage(ResourcePack.DefaultImage.CONFIRM)).color(TextColor.color(255, 255, 255)))
        addComponent(Component.text(CharRepo.getNeg(177)))
    }
}