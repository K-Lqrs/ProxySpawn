package net.rk4z.proxyspawn

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

@Suppress("unused")
class ProxySpawn : JavaPlugin(), Listener {
    private var spawnLocation: Location? = null
    private val gson = Gson()
    private val spawnDataFile = File(dataFolder, "spawnLocation.json")

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)

        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }

        spawnLocation = if (spawnDataFile.exists()) {
            loadLocationFromFile()
        } else {
            null
        }

        logger.info("Spawn location loaded: $spawnLocation")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (command.name.equals("pspawn", ignoreCase = true)) {
            if (sender is Player) {
                setSpawn(sender.location)
                sender.sendMessage("Spawn location set and saved!")
                return true
            }
            sender.sendMessage("This command can only be run by a player.")
            return false
        }
        return false
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        spawnLocation?.let {
            event.player.teleport(it)
        } ?: run {
            event.player.sendMessage("No spawn location is set. Please set the spawn using /pspawn.")
        }
    }

    private fun saveLocationToFile(location: Location) {
        val json = JsonObject().apply {
            addProperty("world", location.world.name)
            addProperty("x", location.x)
            addProperty("y", location.y)
            addProperty("z", location.z)
            addProperty("yaw", location.yaw)
            addProperty("pitch", location.pitch)
        }

        spawnDataFile.writeText(gson.toJson(json))
        spawnLocation = location // スポーン位置を更新
    }

    private fun loadLocationFromFile(): Location? {
        if (!spawnDataFile.exists()) return null

        val json = gson.fromJson(spawnDataFile.readText(), JsonObject::class.java)
        val worldName = json.get("world")?.asString ?: return null
        val world = Bukkit.getWorld(worldName) ?: return null

        val x = json.get("x")?.asDouble ?: return null
        val y = json.get("y")?.asDouble ?: return null
        val z = json.get("z")?.asDouble ?: return null
        val yaw = json.get("yaw")?.asFloat ?: return null
        val pitch = json.get("pitch")?.asFloat ?: return null

        return Location(world, x, y, z, yaw, pitch)
    }

    private fun setSpawn(location: Location) {
        spawnLocation = location
        saveLocationToFile(location)
    }
}
