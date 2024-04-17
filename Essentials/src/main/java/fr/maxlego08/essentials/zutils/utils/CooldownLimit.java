package fr.maxlego08.essentials.zutils.utils;

import com.google.common.base.Preconditions;

public class CooldownLimit {

    private long[] samples;
    private int latestSamplePos = 0;

    public void setSamples(int maxSamples){
        this.samples = new long[maxSamples];
    }

    public void add() {
        latestSamplePos = (latestSamplePos + 1) % samples.length;
        samples[latestSamplePos] = System.currentTimeMillis();
    }

    public long limited(long... limits) {
        Preconditions.checkArgument(limits.length > 0, "Limits array cannot be empty.");
        Preconditions.checkArgument(limits.length % 2 == 0, "Limits array must contain pairs of values.");

        long currentTime = System.currentTimeMillis();
        long maxNextInterval = 0;

        for (int i = 0; i < limits.length; i += 2) {
            long interval = limits[i];
            long limit = limits[i + 1];

            for (int j = 0; j < limit && j < samples.length; j++) {
                int pos = (latestSamplePos - j + samples.length) % samples.length;
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
