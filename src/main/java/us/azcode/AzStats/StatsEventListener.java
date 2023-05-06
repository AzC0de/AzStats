package us.azcode.AzStats;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StatsEventListener implements Listener {

    private final StatsPlugin plugin;

    public StatsEventListener(StatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.loadPlayerStats(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.savePlayerStats(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerStats stats = plugin.getPlayerStats(player);
        stats.incrementDeaths();

        Player killer = player.getKiller();
        if (killer != null) {
            PlayerStats killerStats = plugin.getPlayerStats(killer);
            killerStats.incrementKills();
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
    	LivingEntity entity = event.getEntity();
        if (entity.getType() != EntityType.PLAYER) {
            Player killer = entity.getKiller();
            if (killer != null) {
                PlayerStats killerStats = plugin.getPlayerStats(killer);
                killerStats.incrementMobKills();
            }
        }
    }
}