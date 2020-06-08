package policy;

import java.util.HashMap;
import java.util.Map;

import parser.Record;
import policy.helpers.Entry;
import policy.helpers.QueueNode;
import policy.helpers.Type;

/**
 * AvoidDuplicateLiterals is suppressed, because it cccurs from suppressing multiple times
 * the PMD.DataflowAnomalyAnalysis warning.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class Arc extends Policy {

    public final transient Map<String, QueueNode<Entry>> dataNodes;

    private final transient QueueNode<Entry> t1;
    private final transient QueueNode<Entry> t2;
    private final transient QueueNode<Entry> b1;
    private final transient QueueNode<Entry> b2;

    private transient long adaptiveParameter;
    private transient double hitPerBytesB1;
    private transient double hitPerBytesB2;
    private transient long hitsB1;
    private transient long hitsB2;
    private transient boolean isBytes;

    private transient Entry lastRecordAdded;

    private final transient long maxSize;
    private transient long t1CacheSize;
    private transient long t2CacheSize;
    private transient long b1CacheSize;
    private transient long b2CacheSize;
    private transient int numberOfItems;

    /**
     * Constructing a new cache using the ARC policy.
     * @param cacheSize the size of the cache.
     * @param isBytes the cache size parameter.
     */
    public Arc(int cacheSize, boolean isBytes) {
        super(cacheSize, isBytes);
        dataNodes = new HashMap<String, QueueNode<Entry>>();
        this.t1 = new QueueNode<Entry>();
        this.t2 = new QueueNode<Entry>();
        this.b1 = new QueueNode<Entry>();
        this.b2 = new QueueNode<Entry>();
        this.maxSize = cacheSize;
        this.adaptiveParameter = 0;
        this.hitPerBytesB1 = 0;
        this.hitPerBytesB2 = 0;
        this.hitsB1 = 0;
        this.hitsB2 = 0;
        this.isBytes = isBytes;
        this.numberOfItems = 0;
    }

    /**
     * Checks whether a record is present in the cache.
     * @param record the record to be checked
     * @return true if the record is present in the cache, false otherwise
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    @Override
    public boolean isPresentInCache(Record record) {
        //System.out.println(record);
        this.checkIsBytes(record);
        boolean existing = false;
        String key = record.getId();
        QueueNode<Entry> queueNode = dataNodes.get(key);
        Entry entry = new Entry(record.getId(), record.getSize());
        if ((queueNode != null) && queueNode.getEntry().getSize() != record.getSize()) {
            removeNodeCompletely(queueNode);
            onMissInCache(record);
        } else {
            if (record.getSize() > this.getCacheSize()) {
                return false;
            }
            if (queueNode == null) {
                onMissInCache(record);
            } else if (queueNode.getType() == Type.QueueType.B1) {
                queueNode.setEntry(entry);
                onHitB1(queueNode, this.isBytes);
            } else if (queueNode.getType() == Type.QueueType.B2) {
                queueNode.setEntry(entry);
                onHitB2(queueNode, this.isBytes);
            } else {
                queueNode.setEntry(entry);
                onHitT1orT2(queueNode);
                existing = true;
            }
        }

        if (existing) {
            this.getStats().recordHit();
        }
        return existing;
    }

    /**
     * Method to return the number of items in the cache.
     * @return number of items in the cache
     */
    @Override
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public int numberOfItemsInCache() {
        return numberOfItems;
    }

    /**
     * Add the record to the appropriate list (T1 or T2).
     * @param record the record to add
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void onMissInCache(Record record) {
        Entry entry = new Entry(record.getId(), record.getSize());
        QueueNode<Entry> queueNode = new QueueNode<Entry>(record.getId(), entry);
        queueNode.setQueueType(Type.QueueType.T1);

        long sizeL1 = (t1CacheSize + b1CacheSize);
        long sizeL2 = (t2CacheSize + b2CacheSize);
        //        System.out.println(t1CacheSize + " " + b1CacheSize);
        //        System.out.println(t2CacheSize + " " + b2CacheSize);
        if (sizeL1 == maxSize) {
            if (t1CacheSize < maxSize) {
                QueueNode<Entry> queueNodeToBeRemoved = b1.getNext();
                dataNodes.remove(queueNodeToBeRemoved.getKey());
                queueNodeToBeRemoved.remove();
                b1CacheSize -= queueNodeToBeRemoved.getEntry().getSize();
                if (b1CacheSize == 0) {
                    hitPerBytesB1 = 0;
                } else {
                    hitPerBytesB1 = Math.max(0.0, (((double) hitsB1 / b1CacheSize) * 100));
                }

                replace(queueNode);
            } else {
                QueueNode<Entry> queueNodeToBeRemoved = t1.getNext();
                dataNodes.remove(queueNodeToBeRemoved.getKey());
                queueNodeToBeRemoved.remove();
                t1CacheSize -= queueNodeToBeRemoved.getEntry().getSize();
                this.updateCacheSize(queueNodeToBeRemoved.getEntry().getSize(), false);
                -- numberOfItems;
            }
        } else if ((sizeL1 < maxSize) && ((sizeL1 + sizeL2) >= maxSize)) {
            if ((sizeL1 + sizeL2) == (2 * maxSize) && b2CacheSize > 0) {
                QueueNode<Entry> queueNodeToBeRemoved = b2.getNext();
                dataNodes.remove(queueNodeToBeRemoved.getKey());
                queueNodeToBeRemoved.remove();
                //                System.out.println(sizeL1 + " " + sizeL2 + " " + t2CacheSize);
                //                System.out.println(queueNodeToBeRemoved.getRecord());
                b2CacheSize -= queueNodeToBeRemoved.getEntry().getSize();
                if (b2CacheSize == 0) {
                    hitPerBytesB2 = 0;
                } else {
                    hitPerBytesB2 = Math.max(0.0, (((double) hitsB2 / b2CacheSize) * 100));
                }
            } else if ((sizeL1 + sizeL2) == (2 * maxSize)) {
                QueueNode<Entry> entryQueueNode = t2.getNext();
                dataNodes.remove(entryQueueNode.getKey());
                entryQueueNode.remove();
                t2CacheSize -= entryQueueNode.getEntry().getSize();
                -- numberOfItems;
                this.updateCacheSize(entryQueueNode.getEntry().getSize(), false);
            }
            replace(queueNode);
        }

        t1CacheSize += record.getSize();
        this.updateCacheSize(queueNode.getEntry().getSize(), true);
        ++ numberOfItems;
        dataNodes.put(record.getId(), queueNode);
        queueNode.addToLast(t1);
        lastRecordAdded = queueNode.getEntry();
        if (isBytes) {
            makeSpace();
        }
    }

    /**
     * Add the node to the appropriate list.
     * @param nodeEntry the node to add
     * @param isBytes true if the cache is in number of bytes, false otherwise
     */
    public void onHitB1(QueueNode<Entry> nodeEntry, boolean isBytes) {
        if (isBytes) {
            hitsB1++;
            this.hitPerBytesB1 = Math.max(0.0, (((double) hitsB1 / b1CacheSize) * 100));
        } else {
            adaptiveParameter = Math.min(maxSize, adaptiveParameter
                    + Math.max(b2CacheSize / b1CacheSize, 1));
        }
        this.updateCacheSize(nodeEntry.getEntry().getSize(), true);
        ++ numberOfItems;
        replace(nodeEntry);

        t2CacheSize += nodeEntry.getEntry().getSize();
        b1CacheSize -= nodeEntry.getEntry().getSize();
        if (b1CacheSize == 0) {
            hitPerBytesB1 = 0;
        } else {
            hitPerBytesB1 = Math.max(0.0, (((double) hitsB1 / b1CacheSize) * 100));
        }
        nodeEntry.remove();
        nodeEntry.setQueueType(Type.QueueType.T2);
        nodeEntry.addToLast(t2);
        lastRecordAdded = nodeEntry.getEntry();
        if (isBytes) {
            makeSpace();
        }
    }

    /**
     * Add the node to the appropriate list.
     * @param nodeEntry the node to add
     * @param isBytes true if the cache is in number of bytes, false otherwise
     */
    public void onHitB2(QueueNode<Entry> nodeEntry, boolean isBytes) {
        if (isBytes) {
            hitsB2++;
            this.hitPerBytesB2 = Math.max(0, (((double) hitsB2 / b2CacheSize) * 100));
        } else {
            adaptiveParameter = Math.max(0,
                    adaptiveParameter - Math.max(b1CacheSize / b2CacheSize, 1));
        }
        this.updateCacheSize(nodeEntry.getEntry().getSize(), true);
        ++ numberOfItems;
        replace(nodeEntry);

        t2CacheSize += nodeEntry.getEntry().getSize();
        b2CacheSize -= nodeEntry.getEntry().getSize();
        if (b2CacheSize == 0) {
            hitPerBytesB2 = 0;
        } else {
            hitPerBytesB2 = Math.max(0.0, (((double) hitsB2 / b2CacheSize) * 100));
        }
        nodeEntry.remove();
        nodeEntry.setQueueType(Type.QueueType.T2);
        nodeEntry.addToLast(t2);
        lastRecordAdded = nodeEntry.getEntry();
        if (isBytes) {
            makeSpace();
        }
    }

    /**
     * Add the node to the appropriate list.
     * @param nodeEntry the node to add
     */
    public void onHitT1orT2(QueueNode<Entry> nodeEntry) {
        if (nodeEntry.getType() == Type.QueueType.T1) {
            t1CacheSize -= nodeEntry.getEntry().getSize();
            t2CacheSize += nodeEntry.getEntry().getSize();
        }
        nodeEntry.remove();
        nodeEntry.setQueueType(Type.QueueType.T2);
        nodeEntry.addToLast(t2);
        lastRecordAdded = nodeEntry.getEntry();
    }


    /**
     * Replace QueueNode (Case: L1 (T1 or B1) has less than c pages).
     * @param queueNode queue node
     */
    private void replace(QueueNode<Entry> queueNode) {
        while (this.getRemainingCache() < 0) {
            if ((t1CacheSize >= 1) && ((queueNode.getType() == Type.QueueType.B2)
                    || (t1CacheSize > adaptiveParameter))) {
                QueueNode<Entry> queueNodeToBeRemoved = t1.getNext();
                queueNodeToBeRemoved.remove();
                queueNodeToBeRemoved.setQueueType(Type.QueueType.B1);
                queueNodeToBeRemoved.addToLast(b1);
                this.updateCacheSize(queueNodeToBeRemoved.getEntry().getSize(), false);
                t1CacheSize -= queueNodeToBeRemoved.getEntry().getSize();
                b1CacheSize += queueNodeToBeRemoved.getEntry().getSize();
                hitPerBytesB1 = Math.max(0.0, (((double) hitsB1 / b1CacheSize) * 100));
            } else {
                QueueNode<Entry> queueNodeToBeRemoved = t2.getNext();
                queueNodeToBeRemoved.remove();
                queueNodeToBeRemoved.setQueueType(Type.QueueType.B2);
                queueNodeToBeRemoved.addToLast(b2);
                this.updateCacheSize(queueNodeToBeRemoved.getEntry().getSize(), false);
                t2CacheSize -= queueNodeToBeRemoved.getEntry().getSize();
                b2CacheSize += queueNodeToBeRemoved.getEntry().getSize();
                hitPerBytesB2 = Math.max(0.0, (((double) hitsB2 / b2CacheSize) * 100));

            }
            this.getStats().recordEviction();
            -- numberOfItems;
        }
    }


    /**
     * Maintains that the cache has always capacity withing the specified one.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private void makeSpace() {
        while (this.getRemainingCache() < 0) {
            if (t2CacheSize == 0 || (hitPerBytesB1 >= hitPerBytesB2 && t1CacheSize != 0
                    && !lastRecordAdded.getId().equals(t1.getNext().getKey()))) {
                QueueNode<Entry> queueNodeToBeRemoved = t1.getNext();
                this.updateCacheSize(queueNodeToBeRemoved.getEntry().getSize(), false);
                queueNodeToBeRemoved.remove();
                queueNodeToBeRemoved.setQueueType(Type.QueueType.B1);
                queueNodeToBeRemoved.addToLast(b1);
                t1CacheSize -= queueNodeToBeRemoved.getEntry().getSize();
                b1CacheSize += queueNodeToBeRemoved.getEntry().getSize();
                hitPerBytesB1 = Math.max(0.0, (((double) hitsB1 / b1CacheSize) * 100));
                this.getStats().recordEviction();
                -- numberOfItems;
            } else {
                if (t2CacheSize != 0 && !lastRecordAdded.getId().equals(t2.getNext().getKey())) {
                    QueueNode<Entry> queueNodeToBeRemoved = t2.getNext();
                    this.updateCacheSize(queueNodeToBeRemoved.getEntry().getSize(), false);
                    queueNodeToBeRemoved.remove();
                    queueNodeToBeRemoved.setQueueType(Type.QueueType.B2);
                    queueNodeToBeRemoved.addToLast(b2);
                    t2CacheSize -= queueNodeToBeRemoved.getEntry().getSize();
                    b2CacheSize += queueNodeToBeRemoved.getEntry().getSize();
                    hitPerBytesB2 = Math.max(0.0, (((double) hitsB2 / b2CacheSize) * 100));
                    this.getStats().recordEviction();
                    -- numberOfItems;
                }
            }
        }
        while ((b1CacheSize + b2CacheSize) > maxSize  && (b1CacheSize < maxSize)
                && (b2CacheSize < maxSize)) {
            if (hitPerBytesB1 >= hitPerBytesB2) {
                QueueNode<Entry> b1NodeToBeRemoved = b1.getNext();
                removeNodeCompletely(b1NodeToBeRemoved);
                if (b1CacheSize == 0) {
                    hitPerBytesB1 = 0;
                } else {
                    hitPerBytesB1 = Math.max(0.0, (((double) hitsB1 / b1CacheSize) * 100));
                }
            } else {
                QueueNode<Entry> b2NodeToBeRemoved = b2.getNext();
                removeNodeCompletely(b2NodeToBeRemoved);
                if (b2CacheSize == 0) {
                    hitPerBytesB2 = 0;
                } else {
                    hitPerBytesB2 = Math.max(0.0, (((double) hitsB2 / b2CacheSize) * 100));
                }
            }
        }

    }


    /**
     * Removes a node completely from the dataNodes HashMap.
     * @param queueNodeToBeRemoved the node to be removed
     */
    private void removeNodeCompletely(QueueNode<Entry> queueNodeToBeRemoved) {
        if (queueNodeToBeRemoved.getType() == Type.QueueType.T1) {
            this.updateCacheSize(queueNodeToBeRemoved.getEntry().getSize(), false);
            t1CacheSize = t1CacheSize - queueNodeToBeRemoved.getEntry().getSize();
        } else if (queueNodeToBeRemoved.getType() == Type.QueueType.T2) {
            this.updateCacheSize(queueNodeToBeRemoved.getEntry().getSize(), false);
            t2CacheSize = t2CacheSize - queueNodeToBeRemoved.getEntry().getSize();
        } else if (queueNodeToBeRemoved.getType() == Type.QueueType.B1) {
            b1CacheSize = b1CacheSize - queueNodeToBeRemoved.getEntry().getSize();
        } else {
            b2CacheSize = b2CacheSize - queueNodeToBeRemoved.getEntry().getSize();
        }
        dataNodes.remove(queueNodeToBeRemoved.getKey());
        queueNodeToBeRemoved.remove();
    }

}
