package edu.seu.async;

/**
 * 每一个事件类型都需要实现对应的EventHandler
 */
public enum EventType {

    EMAIL(0);

    private int value;
    EventType(int value) { this.value = value; }
    public int getValue() { return value; }
}
