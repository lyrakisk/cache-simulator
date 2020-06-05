package policy.helpers;

@SuppressWarnings("checkstyle:ClassTypeParameterName")
public class QueueNode<Record> {
    transient String key;
    transient QueueNode prev;
    transient QueueNode next;
    transient Type.QueueType queueType;
    Record record;

    /**
     * Default constructor.
     */
    public QueueNode() {
        this.key = "";
        this.prev = this;
        this.next = this;
    }

    /**
     * A constructor with a record as a parameter.
     * @param record The record to be added in the Node.
     */
    public QueueNode(String key, Record record) {
        this.key = key;
        this.record = record;
    }

    /**
     * Get record data.
     * @return record
     */
    public Record getRecord() {
        return record;
    }

    /**
     * Getter for the key.
     * @return the key of the node.
     */
    public String getKey() {
        return key;
    }

    /**
     * Getter for the next node.
     * @return the next node.
     */
    public QueueNode getNext() {
        return next;
    }

    /**
     * Set page data.
     * @param record record data
     */
    public void setRecord(Record record) {
        this.record = record;
    }

    /**
     * Get the type of record.
     * @return the queue type.
     */
    public Type.QueueType getType() {
        return queueType;
    }

    /**
     * Setter for the QueueType.
     * @param queueType the type of the node.
     */
    public void setQueueType(Type.QueueType queueType) {
        this.queueType = queueType;
    }

    /**
     * Add node to last (append left).
     * @param head head of queue
     */
    public void addToLast(QueueNode head) {
        QueueNode tail = head.prev;
        head.prev = this;
        tail.next = this;
        next = head;
        prev = tail;
    }

    /**
     * Remove queue node.
     */
    @SuppressWarnings("PMD.NullAssignment")

    public void remove() {
        if (prev != null && next != null) {
            prev.next = next;
            next.prev = prev;
            prev = next = null;
            queueType = null;
        }
    }
}

