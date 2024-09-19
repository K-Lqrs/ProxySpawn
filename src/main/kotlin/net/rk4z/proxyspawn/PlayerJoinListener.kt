package net.rk4z.proxyspawn

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(private val plugin: Main) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val spawnLocation = plugin.loadLocationFromFile() ?: return
        event.player.teleport(spawnLocation)
    }
}
