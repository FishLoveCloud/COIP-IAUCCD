package edu.seu.async;

/**
 * 处理事件
 */
public interface EventHandler {

    void doHandle(EventModel model);

    EventType belongTo();

}
