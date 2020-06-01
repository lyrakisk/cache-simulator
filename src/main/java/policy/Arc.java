package policy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import parser.Record;
import policy.helpers.QueueNode;
import policy.helpers.Type;

/**
 * AvoidDuplicateLiterals is suppressed, because it cccurs from suppressing multiple times
 * the PMD.DataflowAnomalyAnalysis warning.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class Arc extends Policy {

    public final transient Map<String, QueueNode<Record>> dataNodes;

    private final transient QueueNode<Record> t1;
    private final transient QueueNode<Record> t2;
    private final transient QueueNode<Record> b1;
    private final transient QueueNode<Record> b2;

    private transient long adaptiveParameter;
    private transient double hitPerBytesB1;
    private transient double hitPerBytesB2;
    private transient long hitsB1;
    private transient long hitsB2;
    private transient boolean isBytes;

    private transient Record lastRecordAdded;

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
        dataNodes = new HashMap<String, QueueNode<Record>>();
        this.t1 = new QueueNode<Record>();
        this.t2 = new QueueNode<Record>();
        this.b1 = new QueueNode<Record>();
        this.b2 = new QueueNode<Record>();
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
        QueueNode<Record> queueNode = dataNodes.get(key);
        if ((queueNode != null) && queueNode.getRecord().getSize() != record.getSize()) {
            removeNodeCompletely(queueNode);
            onMissInCache(record);
        } else {
            if (record.getSize() > this.getCacheSize()) {
                return false;
            }
            if (queueNode == null) {
                onMissInCache(record);
            } else if (queueNode.getType() == Type.QueueType.B1) {
                queueNode.setRecord(record);
                onHitB1(queueNode, this.isBytes);
            } else if (queueNode.getType() == Type.QueueType.B2) {
                queueNode.setRecord(record);
                onHitB2(queueNode, this.isBytes);
            } else {
                queueNode.setRecord(record);
                onHitT1orT2(queueNode);
                existing = true;
            }
        }
        return existing;
    }

    @Override
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public int numberOfItemsInCache() {
//        int items = 0;
//        for (QueueNode<Record> node: dataNodes.values()) {
//            if (node.getType() == Type.QueueType.T1 || node.getType() == Type.QueueType.T2) {
//                items++;
//            }
//        }
//        return items;
        return numberOfItems;
    }

    /**
     * Add the record to the appropriate list (T1 or T2).
     * @param record The record to add.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void onMissInCache(Record record) {
        QueueNode<Record> queueNode = new QueueNode<Record>(record.getId(), record);
        queueNode.setQueueType(Type.QueueType.T1);

        long sizeL1 = (t1CacheSize + b1CacheSize);
        long sizeL2 = (t2CacheSize + b2CacheSize);
        if (sizeL1 == maxSize) {
            if (t1CacheSize < maxSize) {
                QueueNode<Record> queueNodeToBeRemoved = b1.getNext();
                dataNodes.remove(queueNodeToBeRemoved.getKey());
                queueNodeToBeRemoved.remove();
                b1CacheSize -= queueNodeToBeRemoved.getRecord().getSize();
                if (b1CacheSize == 0) {
                    hitPerBytesB1 = 0;
                } else {
                    hitPerBytesB1 = Math.max(0.0, (((double) hitsB1 / b1CacheSize) * 100));
                }

                replace(queueNode);
            } else {
                QueueNode<Record> queueNodeToBeRemoved = t1.getNext();
                dataNodes.remove(queueNodeToBeRemoved.getKey());
                queueNodeToBeRemoved.remove();
                t1CacheSize -= queueNodeToBeRemoved.getRecord().getSize();
                this.updateCacheSize(queueNodeToBeRemoved.getRecord().getSize(), false);
                -- numberOfItems;
            }
        } else if ((sizeL1 < maxSize) && ((sizeL1 + sizeL2) >= maxSize)) {
            if ((sizeL1 + sizeL2) == (2 * maxSize)) {
                QueueNode<Record> queueNodeToBeRemoved = b2.getNext();
                dataNodes.remove(queueNodeToBeRemoved.getKey());
                queueNodeToBeRemoved.remove();
                b2CacheSize -= queueNodeToBeRemoved.getRecord().getSize();
                if (b2CacheSize == 0) {
                    hitPerBytesB2 = 0;
                } else {
                    hitPerBytesB2 = Math.max(0.0, (((double) hitsB2 / b2CacheSize) * 100));
                }
            }
            replace(queueNode);
        }

        t1CacheSize += record.getSize();
        this.updateCacheSize(queueNode.getRecord().getSize(), true);
        ++ numberOfItems;
        dataNodes.put(record.getId(), queueNode);
        queueNode.addToLast(t1);
        lastRecordAdded = queueNode.getRecord();
        if (isBytes) {
            makeSpace();
        }
    }

    /**
     * Add the node to the appropriate list.
     * @param nodeRecord The node to record.
     */
    public void onHitB1(QueueNode<Record> nodeRecord, boolean isBytes) {
        if (isBytes == true) {
            hitsB1++;
            this.hitPerBytesB1 = Math.max(0.0, (((double) hitsB1 / b1CacheSize) * 100));
        } else {
            adaptiveParameter = Math.min(maxSize, adaptiveParameter
                    + Math.max(b2CacheSize / b1CacheSize, 1));
        }
        this.updateCacheSize(nodeRecord.getRecord().getSize(), true);
        ++ numberOfItems;
        replace(nodeRecord);

        t2CacheSize += nodeRecord.getRecord().getSize();
        b1CacheSize -= nodeRecord.getRecord().getSize();
        if (b1CacheSize == 0) {
            hitPerBytesB1 = 0;
        } else {
            hitPerBytesB1 = Math.max(0.0, (((double) hitsB1 / b1CacheSize) * 100));
        }
        nodeRecord.remove();
        nodeRecord.setQueueType(Type.QueueType.T2);
        nodeRecord.addToLast(t2);
        lastRecordAdded = nodeRecord.getRecord();
        if (isBytes) {
            makeSpace();
        }
    }

    /**
     * Add the node to the appropriate list.
     * @param nodeRecord The node to record.
     */
    public void onHitB2(QueueNode<Record> nodeRecord, boolean isBytes) {
        if (isBytes == true) {
            hitsB2++;
            this.hitPerBytesB2 = Math.max(0, (((double) hitsB2 / b2CacheSize) * 100));
        } else {
            adaptiveParameter = Math.max(0,
                    adaptiveParameter - Math.max(b1CacheSize / b2CacheSize, 1));
        }
        this.updateCacheSize(nodeRecord.getRecord().getSize(), true);
        ++ numberOfItems;
        replace(nodeRecord);

        t2CacheSize += nodeRecord.getRecord().getSize();
        b2CacheSize -= nodeRecord.getRecord().getSize();
        if (b2CacheSize == 0) {
            hitPerBytesB2 = 0;
        } else {
            hitPerBytesB2 = Math.max(0.0, (((double) hitsB2 / b2CacheSize) * 100));
        }
        nodeRecord.remove();
        nodeRecord.setQueueType(Type.QueueType.T2);
        nodeRecord.addToLast(t2);
        lastRecordAdded = nodeRecord.getRecord();
        if (isBytes) {
            makeSpace();
        }
    }

    /**
     * Add the node to the appropriate list.
     * @param nodeRecord The node to record.
     */
    public void onHitT1orT2(QueueNode<Record> nodeRecord) {
        if (nodeRecord.getType() == Type.QueueType.T1) {
            t1CacheSize -= nodeRecord.getRecord().getSize();
            t2CacheSize += nodeRecord.getRecord().getSize();
        }
        nodeRecord.remove();
        nodeRecord.setQueueType(Type.QueueType.T2);
        nodeRecord.addToLast(t2);
        lastRecordAdded = nodeRecord.getRecord();
    }


    /**
     * Replace QueueNode (Case: L1 (T1 or B1) has less than c pages).
     * @param queueNode queue node
     */
    private void replace(QueueNode<Record> queueNode) {
        while (this.getRemainingCache() < 0) {
            if ((t1CacheSize >= 1) && ((queueNode.getType() == Type.QueueType.B2)
                    || (t1CacheSize > adaptiveParameter))) {
                QueueNode<Record> queueNodeToBeRemoved = t1.getNext();
                queueNodeToBeRemoved.remove();
                queueNodeToBeRemoved.setQueueType(Type.QueueType.B1);
                queueNodeToBeRemoved.addToLast(b1);
                this.updateCacheSize(queueNodeToBeRemoved.getRecord().getSize(), false);
                t1CacheSize -= queueNodeToBeRemoved.getRecord().getSize();
                b1CacheSize += queueNodeToBeRemoved.getRecord().getSize();
                hitPerBytesB1 = Math.max(0.0, (((double) hitsB1 / b1CacheSize) * 100));
            } else {
                QueueNode<Record> queueNodeToBeRemoved = t2.getNext();
                queueNodeToBeRemoved.remove();
                queueNodeToBeRemoved.setQueueType(Type.QueueType.B2);
                queueNodeToBeRemoved.addToLast(b2);
                this.updateCacheSize(queueNodeToBeRemoved.getRecord().getSize(), false);
                t2CacheSize -= queueNodeToBeRemoved.getRecord().getSize();
                b2CacheSize += queueNodeToBeRemoved.getRecord().getSize();
                hitPerBytesB2 = Math.max(0.0, (((double) hitsB2 / b2CacheSize) * 100));

            }
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
                QueueNode<Record> queueNodeToBeRemoved = t1.getNext();
                this.updateCacheSize(queueNodeToBeRemoved.getRecord().getSize(), false);
                queueNodeToBeRemoved.remove();
                queueNodeToBeRemoved.setQueueType(Type.QueueType.B1);
                queueNodeToBeRemoved.addToLast(b1);
                t1CacheSize -= queueNodeToBeRemoved.getRecord().getSize();
                b1CacheSize += queueNodeToBeRemoved.getRecord().getSize();
                hitPerBytesB1 = Math.max(0.0, (((double) hitsB1 / b1CacheSize) * 100));
                -- numberOfItems;
            } else {
                if (t2CacheSize != 0 && !lastRecordAdded.getId().equals(t2.getNext().getKey())) {
                    QueueNode<Record> queueNodeToBeRemoved = t2.getNext();
                    this.updateCacheSize(queueNodeToBeRemoved.getRecord().getSize(), false);
                    queueNodeToBeRemoved.remove();
                    queueNodeToBeRemoved.setQueueType(Type.QueueType.B2);
                    queueNodeToBeRemoved.addToLast(b2);
                    t2CacheSize -= queueNodeToBeRemoved.getRecord().getSize();
                    b2CacheSize += queueNodeToBeRemoved.getRecord().getSize();
                    hitPerBytesB2 = Math.max(0.0, (((double) hitsB2 / b2CacheSize) * 100));
                    -- numberOfItems;
                }
            }
        }
        while ((b1CacheSize + b2CacheSize) > maxSize  && (b1CacheSize < maxSize)
                && (b2CacheSize < maxSize)) {
            if (hitPerBytesB1 >= hitPerBytesB2) {
                QueueNode<Record> b1NodeToBeRemoved = b1.getNext();
                removeNodeCompletely(b1NodeToBeRemoved);
                if (b1CacheSize == 0) {
                    hitPerBytesB1 = 0;
                } else {
                    hitPerBytesB1 = Math.max(0.0, (((double) hitsB1 / b1CacheSize) * 100));
                }
            } else {
                QueueNode<Record> b2NodeToBeRemoved = b2.getNext();
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
     * @param queueNodeToBeRemoved The node to be removed.
     */
    private void removeNodeCompletely(QueueNode<Record> queueNodeToBeRemoved) {
        if (queueNodeToBeRemoved.getType() == Type.QueueType.T1) {
            this.updateCacheSize(queueNodeToBeRemoved.getRecord().getSize(), false);
            t1CacheSize = t1CacheSize - queueNodeToBeRemoved.getRecord().getSize();
        } else if (queueNodeToBeRemoved.getType() == Type.QueueType.T2) {
            this.updateCacheSize(queueNodeToBeRemoved.getRecord().getSize(), false);
            t2CacheSize = t2CacheSize - queueNodeToBeRemoved.getRecord().getSize();
        } else if (queueNodeToBeRemoved.getType() == Type.QueueType.B1) {
            b1CacheSize = b1CacheSize - queueNodeToBeRemoved.getRecord().getSize();
        } else {
            b2CacheSize = b2CacheSize - queueNodeToBeRemoved.getRecord().getSize();
        }
        dataNodes.remove(queueNodeToBeRemoved.getKey());
        queueNodeToBeRemoved.remove();
    }

}
