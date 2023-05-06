package us.azcode.AzStats;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatsPlugin extends JavaPlugin {

    private Map<UUID, PlayerStats> playerStatsMap;
    private StatsPlaceholderExpansion placeholderExpansion;

    @Override
    public void onEnable() {
        // Registrar serialización de PlayerStats
    	ConfigurationSerialization.registerClass(PlayerStats.class, "us.azcode.AzStats.PlayerStats");

        // Configuración
        saveDefaultConfig();

        // Mapa de estadísticas de los jugadores
        playerStatsMap = new HashMap<>();

        // Registra el listener de eventos
        getServer().getPluginManager().registerEvents(new StatsEventListener(this), this);

        // Registra la expansión de PlaceholderAPI
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderExpansion = new StatsPlaceholderExpansion(this);
            placeholderExpansion.register();
        }

        // Tarea para actualizar el tiempo de juego cada minuto
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : getServer().getOnlinePlayers()) {
                    PlayerStats stats = getPlayerStats(player);
                    stats.incrementPlayTime();
                }
            }
        }.runTaskTimer(this, 0, 20 * 60);
    }

    @Override
    public void onDisable() {
        // Guardar las estadísticas de los jugadores en línea
        for (Player player : getServer().getOnlinePlayers()) {
            savePlayerStats(player);
        }

        // Desregistrar la expansión de PlaceholderAPI
        if (placeholderExpansion != null) {
            placeholderExpansion.unregister();
        }
    }
    public Map<UUID, PlayerStats> getPlayerStatsMap() {
        return playerStatsMap;
    }

    public PlayerStats getPlayerStats(Player player) {
        return playerStatsMap.get(player.getUniqueId());
    }

    public void loadPlayerStats(Player player) {
        if (getConfig().contains("players." + player.getUniqueId())) {
            PlayerStats stats = (PlayerStats) getConfig().get("players." + player.getUniqueId());
            playerStatsMap.put(player.getUniqueId(), stats);
        } else {
            playerStatsMap.put(player.getUniqueId(), new PlayerStats(player));
        }
    }

    public void savePlayerStats(Player player) {
        PlayerStats stats = playerStatsMap.get(player.getUniqueId());
        if (stats != null) {
            stats.incrementPlayTime(); // Actualizar tiempo de juego antes de guardar
            getConfig().set("players." + player.getUniqueId(), stats);
            saveConfig();
        }
    }
}