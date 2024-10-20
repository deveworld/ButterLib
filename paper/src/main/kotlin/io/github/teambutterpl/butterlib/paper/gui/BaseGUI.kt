package io.github.teambutterpl.butterlib.paper.gui

import io.github.teambutterpl.butterlib.api.resourcepack.CharRepo
import io.github.teambutterpl.butterlib.api.resourcepack.ResourcePack
import io.github.teambutterpl.butterlib.paper.ButterLibrary
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

abstract class BaseGUI : InventoryHolder {
    private var gui: Component = Component.text(CharRepo.getNeg(8))
    private var bukkitInventory: Inventory? = null
    open val preventUserChange: Boolean = true

    fun open(player: HumanEntity) {
        player.openInventory(bukkitInventory!!)
    }

    abstract fun onClick(event: InventoryClickEvent)

    abstract fun onClose(event: InventoryCloseEvent)

    protected fun init(gui: InventoryHolder) {
        drawGUI()
        bukkitInventory = Bukkit.getServer().createInventory(
            gui, 54, this.gui
        )
    }

    protected fun addComponent(component: Component) {
        gui = gui.append(component)
    }

    protected fun getImage(image: String): Char {
        return ButterLibrary.getResourcePackManager().getResourcePack().getImage(image)
    }

    protected fun getDefaultImage(image: ResourcePack.DefaultImage): Char {
        return getImage(image.path)
    }

    protected open fun drawGUI() {
        addComponent(Component.text(getDefaultImage(ResourcePack.DefaultImage.MENU_CONTAINER)).color(TextColor.color(255, 255, 255)))
        addComponent(Component.text(CharRepo.getNeg(177)))
    }

    override fun getInventory(): Inventory = bukkitInventory!!
}