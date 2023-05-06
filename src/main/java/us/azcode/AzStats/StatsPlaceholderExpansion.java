package us.azcode.AzStats;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatsPlaceholderExpansion extends PlaceholderExpansion {

    private StatsPlugin plugin;

    public StatsPlaceholderExpansion(StatsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "playerstats";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        String[] split = identifier.split("_");
        if (split.length != 3) {
            return null;
        }

        String stat = split[0];
        int rank;
        try {
            rank = Integer.parseInt(split[1]) - 1;
        } catch (NumberFormatException e) {
            return null;
        }
        String timeRange = split[2];

        Instant currentCycleStart;
        switch (timeRange) {
            case "daily":
                currentCycleStart = getCurrentDailyCycleStart();
                break;
            case "weekly":
                currentCycleStart = getCurrentWeeklyCycleStart();
                break;
            case "monthly":
                currentCycleStart = getCurrentMonthlyCycleStart();
                break;
            default:
                return null;
        }

        List<PlayerStats> filteredPlayerStats = plugin.getPlayerStatsMap().values().stream()
            .filter(stats -> {
                Instant lastUpdateTime = stats.getFirstLogin().plus(stats.getPlayTime());
                return lastUpdateTime.isAfter(currentCycleStart);
            })
            .collect(Collectors.toList());

        List<PlayerStats> topPlayerStats = getTopPlayerStats(stat, filteredPlayerStats);
        if (rank >= topPlayerStats.size()) {
            String noPlayerMessage = plugin.getConfig().getString("formats.no_player", "N/A");
            return noPlayerMessage;
        }

        PlayerStats stats = topPlayerStats.get(rank);
        Player topPlayer = plugin.getServer().getPlayer(stats.getPlayerUUID());
        if (topPlayer == null) {
            return null;
        }

        String format = plugin.getConfig().getString("formats." + stat);
        if (format == null) {
            return null;
        }

        int statValue;
        switch (stat) {
            case "kills":
                statValue = stats.getKills();
                break;
            case "mobkills":
                statValue = stats.getMobKills();
                break;
            case "deaths":
                statValue = stats.getDeaths();
                break;
            case "playtime":
                statValue = (int) stats.getPlayTime().toHours();
                break;
            default:
                return null;
        }

        return String.format(format, topPlayer.getName(), statValue, rank + 1);
    }

    private List<PlayerStats> getTopPlayerStats(String stat, List<PlayerStats> filteredPlayerStats) {
        Comparator<PlayerStats> comparator;
        switch (stat) {
            case "kills":
                comparator = Comparator.comparing(PlayerStats::getKills).reversed();
                break;
            case "mobkills":
                comparator = Comparator.comparing(PlayerStats::getMobKills).reversed();
                break;
            case "deaths":
                comparator = Comparator.comparing(PlayerStats::getDeaths).reversed();
                break;
            case "playtime":
            comparator = Comparator.comparing(PlayerStats::getPlayTime).reversed();
            break;
            default:
            return Collections.emptyList();
            }
            return filteredPlayerStats.stream()
            .sorted(comparator)
            .collect(Collectors.toList());
            }
    private Instant getCurrentDailyCycleStart() {
        LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
        LocalDate cycleStart = localDate;
        return cycleStart.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    private Instant getCurrentWeeklyCycleStart() {
        LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
        LocalDate cycleStart = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY));
        return cycleStart.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    private Instant getCurrentMonthlyCycleStart() {
        LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
        LocalDate cycleStart = localDate.with(TemporalAdjusters.firstDayOfMonth());
        return cycleStart.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }
}
