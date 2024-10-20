package io.github.teambutterpl.butterlib.paper.gui

import io.github.teambutterpl.butterlib.paper.gui.BaseGUI
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class EmptyGUI : BaseGUI() {
    override val preventUserChange: Boolean = true

    init {
        super.init(this)
    }

    override fun onClick(event: InventoryClickEvent) {
        return
    }

    override fun onClose(event: InventoryCloseEvent) {
        return
    }

    override fun drawGUI() {
        super.drawGUI()
        return
    }
}