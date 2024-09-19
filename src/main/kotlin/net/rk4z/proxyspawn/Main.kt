package net.rk4z.proxyspawn

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Main : JavaPlugin() {
    private var spawnLocation: Location? = null
    private val spawnDataFile = File(dataFolder, "spawnLocation.txt")

    override fun onEnable() {
        server.pluginManager.registerEvents(PlayerJoinListener(this), this)

        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }

        if (!spawnDataFile.exists()) {
            spawnDataFile.createNewFile()
        }
    }

    fun saveLocationToFile(location: Location) {
        val dataFile = File(dataFolder, "spawnLocation.txt")
        dataFile.writeText("${location.world.name},${location.x},${location.y},${location.z},${location.yaw},${location.pitch}")
    }

    fun loadLocationFromFile(): Location? {
        val dataFile = File(dataFolder, "spawnLocation.txt")
        if (!dataFile.exists()) return null

        val data = dataFile.readText().split(",")
        if (data.size < 6) return null

        val world = Bukkit.getWorld(data[0]) ?: return null
        val x = data[1].toDoubleOrNull() ?: return null
        val y = data[2].toDoubleOrNull() ?: return null
        val z = data[3].toDoubleOrNull() ?: return null
        val yaw = data[4].toFloatOrNull() ?: return null
        val pitch = data[5].toFloatOrNull() ?: return null

        return Location(world, x, y, z, yaw, pitch)
    }

    fun setSpawn(location: Location) {
        spawnLocation = location
    }
}
