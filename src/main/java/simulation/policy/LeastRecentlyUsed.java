package simulation.policy;

import data.parser.Record;
import java.util.HashMap;
import java.util.Map;


/**
 * Class representing the LRU cache simulation.policy.
 */
public class LeastRecentlyUsed extends Policy {

    /**
     * Class to represent a doubly-linked list.
     */
    private class Node {
        private transient String id;
        private transient long sz;
        private transient Node prev;
        private transient Node next;

        /**
         * Constructor for a node of the doubly-linked list.
         * @param id the identifier of the data.record this node represents
         * @param sz the size of the data.record this node represents
         */
        private Node(String id, long sz) {
            this.id = id;
            this.sz = sz;
        }

        /**
         * Removes this node from the doubly-linked list.
         */
        private void removeFromList() {
            next.prev = prev;
            prev.next = next;
        }

        /**
         * Adds this node after another node in the doubly-linked list.
         * @param after the node to be added after
         */
        private void addAfter(Node after) {
            next = after.next;
            prev = after;
            after.next.prev = this;
            after.next = this;
        }

        public String toString() {
            return id + " " + sz;
        }
    }

    private transient Map<String, Node> cache;
    private transient Node head;
    private transient Node tail;

    /**
     * Constructing a new cache using the LRU simulation.policy.
     * @param cacheSize the size of the cache
     * @param isBytes the cache size parameter
     */
    public LeastRecentlyUsed(long cacheSize, boolean isBytes) {
        super(cacheSize, isBytes);
        cache = new HashMap<>();
        head = new Node("head", 0);
        tail = new Node("tail", 0);
        head.next = tail;
        tail.prev = head;
    }

    /**
     * Checks whether a data.record is present in the cache.
     * @param record the data.record to be checked
     * @return true if the data.record is present in the cache, false otherwise
     */
    @Override
    // DD and DU anomalies for the boolean existing which I don't seem
    // to know how to fix (or if I fix the code looks really ugly and not
    // well structured).
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public boolean isPresentInCache(Record record) {
        this.getStats().recordRequest();
        String id = record.getId();
        this.checkIsBytes(record);
        boolean existing = true;

        if (record.getSize() > this.getCacheSize()) {
            if (cache.containsKey(id)) {
                this.getStats().recordOperation();
                Node toRemove = cache.get(id);
                cache.remove(toRemove.id);
                this.updateCacheSize(toRemove.sz, false);
                toRemove.removeFromList();
            }
            return false;
        }

        if (cache.get(id) == null) {
            this.getStats().recordOperation();
            Node forRecord = new Node(record.getId(), record.getSize());
            cache.put(id, forRecord);
            forRecord.addAfter(head);
            this.updateCacheSize(record.getSize(), true);
            existing = false;
        } else {
            this.getStats().recordOperation();
            Node current = cache.get(id);

            if (current.sz != record.getSize()) {
                this.updateCacheSize(current.sz, false);
                current.sz = record.getSize();
                this.updateCacheSize(current.sz, true);
                existing = false;
            }

            current.removeFromList();
            current.addAfter(head);
        }

        while (this.getRemainingCache() < 0) {
            this.getStats().recordOperation();
            this.getStats().recordEviction();
            Node toRemove = tail.prev;
            toRemove.removeFromList();
            cache.remove(toRemove.id);
            this.updateCacheSize(toRemove.sz, false);
        }

        if (existing) {
            this.getStats().recordHit();
        }
        return existing;
    }

    @Override
    public int numberOfItemsInCache() {
        return cache.size();
    }

    @Override
    public void deleteUntilCacheNotOverloaded() {
        while (this.getRemainingCache() < 0) {
            this.getStats().recordEviction();
            Node toRemove = tail.prev;
            toRemove.removeFromList();
            cache.remove(toRemove.id);
            this.updateCacheSize(toRemove.sz, false);
        }
    }
}
