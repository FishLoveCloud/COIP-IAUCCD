package edu.seu.async;

import java.util.Map;

/**
 * 事件模型
 */
public class EventModel {

    private EventType eventType;
    // 事件执行需要的参数
    private Map<String, Object> map;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public EventModel() {
    }

    public EventModel(EventType eventType, Map<String, Object> map) {
        this.eventType = eventType;
        this.map = map;
    }
}
