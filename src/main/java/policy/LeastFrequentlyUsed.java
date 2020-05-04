package policy;

import parser.Record;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class LeastFrequentlyUsed extends Policy {
    private transient Map<Integer, Record> items;
    private transient Map<Integer, Integer> counts;
    private transient Map<Integer, Set<Integer>> frequencies;
    private transient int minCount;

    public LeastFrequentlyUsed(int size, boolean isBytes) {
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
        if (minCount == -1) {
            return;
        }

        while (this.getRemainingCache() < 0) {
            Set<Integer> currentMin = frequencies.get(minCount);
            while (!currentMin.isEmpty() && this.getRemainingCache() < 0) {
                int toRemove = currentMin.iterator().next();
                currentMin.remove(toRemove);
                this.removeFromCache(items.remove(toRemove));
                counts.remove(toRemove);
            }

            if (currentMin.isEmpty()) {
                while (frequencies.get(minCount) == null || frequencies.get(minCount).size() == 0) {
                    ++ minCount;
                }
            }
        }
    }

    @Override
    public boolean isPresentInCache(Record record) {
        int id = record.getId();
        if (record.getSize() > this.getCacheSize()) {
            if (items.containsKey(id)) {
                Record toRemove = items.remove(id);
                int occurrences = counts.remove(toRemove.getId());
                frequencies.get(occurrences).remove(toRemove.getId());
            }

            return false;
        }

        this.checkIsBytes(record);
        boolean found = items.containsKey(id);

        if (found) {
            this.removeFromCache(items.get(id));
            this.addToCache(record);
            int currCount = counts.get(id);
            counts.put(id, currCount + 1);
            frequencies.get(currCount).remove(id);

            if (minCount == currCount && frequencies.get(currCount).isEmpty()) {
                ++ minCount;
            }

            if (!frequencies.containsKey(currCount + 1)) {
                frequencies.put(currCount + 1, new LinkedHashSet<>());
            }
        } else {
            this.addToCache(record);
            counts.put(id, 1);
            minCount = 1;

            if (!frequencies.containsKey(1)) {
                frequencies.put(1, new LinkedHashSet<>());
            }
        }

        this.updateCache();
        int pos = counts.get(id);
        frequencies.get(pos).add(id);
        return found;
    }
}
