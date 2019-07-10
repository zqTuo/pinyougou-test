package com.pinyougou.common.pojo;

public class MessageInfo<T> {
    public static final int METHOD_ADD=1;  //用于新增操作
    public static final int METHOD_UPDATE=2; //用于更新操作
    public static final int METHOD_DELETE=3;//用于删除操作

//    要发送的内容
    private Object context;
    private String topic;

    private String tags;

    private String keys;
    private int methods;  //要执行的方法

    public MessageInfo() {
    }

    public MessageInfo(Object context, String topic, String tags, String keys, int methods) {
        this.context = context;
        this.topic = topic;
        this.tags = tags;
        this.keys = keys;
        this.methods = methods;
    }

    public MessageInfo(Object context, String topic, String tags, int methods) {
        this.context = context;
        this.topic = topic;
        this.tags = tags;
        this.methods = methods;
    }

    public static int getMethodAdd() {
        return METHOD_ADD;
    }

    public static int getMethodUpdate() {
        return METHOD_UPDATE;
    }

    public static int getMethodDelete() {
        return METHOD_DELETE;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public int getMethods() {
        return methods;
    }

    public void setMethods(int methods) {
        this.methods = methods;
    }
}
