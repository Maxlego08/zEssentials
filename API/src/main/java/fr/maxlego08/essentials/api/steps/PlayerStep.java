package fr.maxlego08.essentials.api.steps;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerStep {

    private final Map<String, Integer> statistics;
    private final Map<String, Map<String, Integer>> blocksStatistics;
    private final Map<String, Map<String, Integer>> itemsStatistics;
    private final Map<String, Map<String, Integer>> entitiesStatistics;
    private final Map<String, Object> additionalDatas = new HashMap<>();

    public PlayerStep(Player player) {
        this.statistics = new HashMap<>();
        this.blocksStatistics = new HashMap<>();
        this.itemsStatistics = new HashMap<>();
        this.entitiesStatistics = new HashMap<>();

        for (Statistic stat : Statistic.values()) {
            try {
                int value = player.getStatistic(stat);
                statistics.put(stat.name(), value);
            } catch (IllegalArgumentException ignored) {
            }
        }

        for (Material block : Material.values()) {
            if (block.isBlock()) {
                int mined = player.getStatistic(Statistic.MINE_BLOCK, block);
                if (mined > 0) {
                    blocksStatistics.computeIfAbsent("MINE_BLOCK", k -> new HashMap<>()).put(block.name(), mined);
                }
            }
        }

        for (Material item : Material.values()) {
            if (item.isItem()) {
                int used = player.getStatistic(Statistic.USE_ITEM, item);
                int crafted = player.getStatistic(Statistic.CRAFT_ITEM, item);
                int broken = player.getStatistic(Statistic.BREAK_ITEM, item);

                if (used > 0) {
                    itemsStatistics.computeIfAbsent("USE_ITEM", k -> new HashMap<>()).put(item.name(), used);
                }
                if (crafted > 0) {
                    itemsStatistics.computeIfAbsent("CRAFT_ITEM", k -> new HashMap<>()).put(item.name(), crafted);
                }
                if (broken > 0) {
                    itemsStatistics.computeIfAbsent("BREAK_ITEM", k -> new HashMap<>()).put(item.name(), broken);
                }
            }
        }

        for (EntityType entity : EntityType.values()) {
            if (entity.isAlive()) {
                int killed = player.getStatistic(Statistic.KILL_ENTITY, entity);
                int killedBy = player.getStatistic(Statistic.ENTITY_KILLED_BY, entity);

                if (killed > 0) {
                    entitiesStatistics.computeIfAbsent("KILL_ENTITY", k -> new HashMap<>()).put(entity.name(), killed);
                }
                if (killedBy > 0) {
                    entitiesStatistics.computeIfAbsent("ENTITY_KILLED_BY", k -> new HashMap<>()).put(entity.name(), killedBy);
                }
            }
        }
    }

    public PlayerStep(Map<String, Integer> statistics, Map<String, Map<String, Integer>> blocksStatistics, Map<String, Map<String, Integer>> itemsStatistics, Map<String, Map<String, Integer>> entitiesStatistics) {
        this.statistics = statistics;
        this.blocksStatistics = blocksStatistics;
        this.itemsStatistics = itemsStatistics;
        this.entitiesStatistics = entitiesStatistics;
    }

    public Map<String, Integer> getStatistics() {
        return statistics;
    }

    public Map<String, Map<String, Integer>> getBlocksStatistics() {
        return blocksStatistics;
    }

    public Map<String, Map<String, Integer>> getItemsStatistics() {
        return itemsStatistics;
    }

    public Map<String, Map<String, Integer>> getEntitiesStatistics() {
        return entitiesStatistics;
    }

    public Map<String, Object> getAdditionalDatas() {
        return additionalDatas;
    }
}