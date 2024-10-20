package io.github.teambutterpl.butterlib.paper.listeners

import io.github.teambutterpl.butterlib.paper.ButterLib
import io.github.teambutterpl.butterlib.paper.ButterLibrary
import io.github.teambutterpl.butterlib.paper.listeners.BaseListener
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

class TestListener(plugin: ButterLib) : BaseListener(plugin) {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        ButterLibrary.getGUIManager().createChatGUI(
            event.player,
            Component.text("[ 월드 ]").color(TextColor.color(255, 255, 255)),
            Component.text("안녕하세요. \n").color(TextColor.color(255,100,100))
            .append(Component.text("이건 파란색 글씨임. 긴거는 아래로 감. ").color(TextColor.color(100,100,255)))
            .append(Component.text("이건 초록색 글씨임! 똑같이 긴건 아래로 감! 최대 5줄!!").color(TextColor.color(100,255,100)))
        )
    }

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        event.isCancelled = true
        plugin.server.scheduler.scheduleSyncDelayedTask(plugin) {
            ButterLibrary.getGUIManager().createConfirmGUI(
                event.player,
                Component.text("확인").color(TextColor.color(255, 255, 255)),
                Component.textOfChildren(event.message()).color(TextColor.color(255, 255, 255))
                    .append(Component.text(" \n "))
                    .append(Component.text("정말로 이 메시지를 보내시겠습니까?").color(TextColor.color(255, 255, 100))),
                { plugin.server.broadcast(event.player.displayName().append(Component.text(" : ")).append(event.message())) },
                { event.player.sendMessage(Component.text("Canceled")) }
            )
        }
    }
}