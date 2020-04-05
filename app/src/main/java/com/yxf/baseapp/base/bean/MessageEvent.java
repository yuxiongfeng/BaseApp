package com.yxf.baseapp.base.bean;

public class MessageEvent {

    private EventType eventType;
    private String msg;
    private String msg2;
    private Object object;

    public Object getObject2() {
        return object2;
    }

    public void setObject2(Object object2) {
        this.object2 = object2;
    }

    private Object object2;

    public MessageEvent(EventType type) {
        this.eventType = type;
    }

    public MessageEvent(EventType type, String msg) {
        this.eventType = type;
        this.msg = msg;
    }

    public MessageEvent(EventType type, String msg, String msg2) {
        this.eventType = type;
        this.msg = msg;
        this.msg2 = msg2;
    }

    public MessageEvent(EventType type, String msg, Object object) {
        this.eventType = type;
        this.msg = msg;
        this.object = object;
    }

    public MessageEvent(EventType type, Object object) {
        this.eventType = type;
        this.object = object;
    }

    public MessageEvent(EventType type, Object object, Object object2) {
        this.eventType = type;
        this.object = object;
        this.object2 = object2;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg2() {
        return msg2;
    }

    public void setMsg2(String msg2) {
        this.msg2 = msg2;
    }

    public enum EventType {
        /**
         * 测试
         */
        test_event_type,

    }
}
