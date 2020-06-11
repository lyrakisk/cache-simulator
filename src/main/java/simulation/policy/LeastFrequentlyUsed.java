package simulation.policy;

import data.parser.Record;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import simulation.policy.helpers.Entry;

public class LeastFrequentlyUsed extends Policy {
    private transient Map<String, Entry> items;
    private transient Map<String, Integer> counts;
    private transient Map<Integer, Set<String>> frequencies;
    private transient int minCount;

    /**
     * Constructing a new cache following the LFU simulation.policy.
     * @param size the size of the cache
     * @param isBytes the cache size parameter
     */
    public LeastFrequentlyUsed(long size, boolean isBytes) {
        super(size, isBytes);
        items = new HashMap<>();
        counts = new HashMap<>();
        frequencies = new HashMap<>();
        minCount = -1;
    }

    /**
     * Removes items from the cache until the current item cannot be added.
     */
    private void updateCache() {
        while (this.getRemainingCache() < 0) {
            Set<String> currentMin = frequencies.get(minCount);
            while (!currentMin.isEmpty() && this.getRemainingCache() < 0) {
                this.getStats().recordOperation();
                this.getStats().recordEviction();
                String toRemove = currentMin.iterator().next();
                currentMin.remove(toRemove);
                this.updateCacheSize(items.remove(toRemove).getSize(), false);
                counts.remove(toRemove);
            }
            if (currentMin.isEmpty()) {
                if (items.isEmpty()) {
                    minCount = -1;
                } else {
                    while (frequencies.get(minCount).size() == 0) {
                        ++ minCount;
                    }
                }
            }
        }
    }

    /**
     * Checks whether a data.record is in the cache.
     * @param record the data.record to be checked
     * @return true if the data.record was present in the cache, false otherwise
     */
    @Override
    public boolean isPresentInCache(Record record) {
        String id = record.getId();
        this.checkIsBytes(record);
        this.getStats().recordOperation();
        if (record.getSize() > this.getCacheSize()) {
            if (items.containsKey(id)) {
                Entry toRemove = items.remove(id);
                int occurrences = counts.remove(toRemove.getId());
                frequencies.get(occurrences).remove(toRemove.getId());
                this.updateCacheSize(toRemove.getSize(), false);
            }

            return false;
        }

        boolean found = items.containsKey(id);
        if (found) {
            if (items.get(id).getSize() != record.getSize()) {
                found = false;
            }

            this.updateCacheSize(items.get(id).getSize(), false);
            this.updateCacheSize(record.getSize(), true);
            int currCount = counts.get(id);
            counts.put(id, currCount + 1);
            frequencies.get(currCount).remove(id);
            items.remove(id);

            if (minCount == currCount && frequencies.get(currCount).isEmpty()) {
                ++ minCount;
            }

            if (!frequencies.containsKey(currCount + 1)) {
                frequencies.put(currCount + 1, new LinkedHashSet<>());
            }
        } else {
            this.updateCacheSize(record.getSize(), true);
            counts.put(id, 1);
            minCount = 1;

            if (!frequencies.containsKey(1)) {
                frequencies.put(1, new LinkedHashSet<>());
            }
        }

        this.updateCache();
        int pos = counts.get(id);
        items.put(id, new Entry(record.getId(), record.getSize()));
        frequencies.get(pos).add(id);
        if (pos < minCount || minCount == -1) {
            minCount = pos;
        }

        if (found) {
            this.getStats().recordHit();
        }
        return found;
    }

    @Override
    public int numberOfItemsInCache() {
        return items.size();
    }

    @Override
    public void deleteUntilCacheNotOverloaded() {
        updateCache();
    }
}
