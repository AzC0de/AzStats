package us.azcode.AzStats;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SerializableAs("us.azcode.AzStats.PlayerStats")
public class PlayerStats implements ConfigurationSerializable {
    private UUID playerUUID;
    private Instant firstLogin;
    private Duration playTime;
    private int kills;
    private int mobKills;
    private int deaths;

    public PlayerStats(Player player) {
        this.playerUUID = player.getUniqueId();
        this.firstLogin = Instant.now();
        this.playTime = Duration.ZERO;
        this.kills = 0;
        this.mobKills = 0;
        this.deaths = 0;
    }

    public static PlayerStats deserialize(Map<String, Object> map) {
        PlayerStats stats = new PlayerStats();
        stats.playerUUID = UUID.fromString((String) map.get("playerUUID"));
        stats.firstLogin = Instant.parse((String) map.get("firstLogin"));
        stats.playTime = Duration.parse((String) map.get("playTime"));
        stats.kills = (Integer) map.get("kills");
        stats.mobKills = (Integer) map.get("mobKills");
        stats.deaths = (Integer) map.get("deaths");
        return stats;
    }

    private PlayerStats() {
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public Instant getFirstLogin() {
        return firstLogin;
    }

    public Duration getPlayTime() {
        return playTime;
    }

    public int getKills() {
        return kills;
    }

    public int getMobKills() {
        return mobKills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void incrementPlayTime() {
        playTime = playTime.plusMinutes(1);
    }

    public void incrementKills() {
        kills++;
    }

    public void incrementMobKills() {
        mobKills++;
    }

    public void incrementDeaths() {
        deaths++;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("playerUUID", playerUUID.toString());
        map.put("firstLogin", firstLogin.toString());
        map.put("playTime", playTime.toString());
        map.put("kills", kills);
        map.put("mobKills", mobKills);
        map.put("deaths", deaths);
        return map;
    }
}