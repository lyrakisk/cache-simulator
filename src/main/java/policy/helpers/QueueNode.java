package policy.helpers;

@SuppressWarnings("checkstyle:ClassTypeParameterName")
public class QueueNode<T> {
    transient String key;
    transient QueueNode prev;
    transient QueueNode next;
    transient Type.QueueType queueType;
    T entry;

    /**
     * Default constructor.
     */
    public QueueNode() {
        this.key = "";
        this.prev = this;
        this.next = this;
    }

    /**
     * A constructor with an entry as a parameter.
     * @param key the key of the entry
     * @param entry the entry to be added in the node
     */
    public QueueNode(String key, T entry) {
        this.key = key;
        this.entry = entry;
    }

    /**
     * Get entry data.
     * @return the entry of the node
     */
    public T getEntry() {
        return entry;
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
     * Set entry data.
     * @param entry the entry to be set
     */
    public void setEntry(T entry) {
        this.entry = entry;
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

