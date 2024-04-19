package fr.maxlego08.essentials.zutils.utils;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DynamicCooldown {

    private long[] samples;
    private final Map<UUID, Integer> latestSamplePos = new HashMap<>();

    public void setSamples(int maxSamples) {
        this.samples = new long[maxSamples];
    }

    public void add(@NotNull UUID uniqueId) {
        int latestSamplePos = (this.latestSamplePos.getOrDefault(uniqueId, 0) + 1) % samples.length;
        samples[latestSamplePos] = System.currentTimeMillis();
        this.latestSamplePos.put(uniqueId, latestSamplePos);
    }

    public long limited(@NotNull UUID uniqueId, long... limits) {
        Preconditions.checkArgument(limits.length > 0, "Limits array cannot be empty.");
        Preconditions.checkArgument(limits.length % 2 == 0, "Limits array must contain pairs of values.");

        long currentTime = System.currentTimeMillis();
        long maxNextInterval = 0;

        for (int i = 0; i < limits.length; i += 2) {
            long interval = limits[i];
            long limit = limits[i + 1];

            for (int j = 0; j < limit && j < samples.length; j++) {
                int pos = (this.latestSamplePos.getOrDefault(uniqueId, 0) - j + samples.length) % samples.length;
                long diff = currentTime - samples[pos];

                if (diff >= interval) break;
                if (j + 1 == limit) {
                    long nextInterval = interval - diff;
                    if (nextInterval > maxNextInterval) maxNextInterval = nextInterval;
                }
            }
        }

        return maxNextInterval;
    }
}
