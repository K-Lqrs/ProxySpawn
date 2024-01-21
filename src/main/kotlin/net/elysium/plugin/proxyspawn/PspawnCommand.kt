package net.elysium.plugin.proxyspawn

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PspawnCommand(private val plugin: Main) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            plugin.setSpawn(sender.location)
            plugin.saveLocationToFile(sender.location)
            sender.sendMessage("Spawn location set and saved!")
            return true
        }
        sender.sendMessage("This command can only be run by a player.")
        return false
    }
}