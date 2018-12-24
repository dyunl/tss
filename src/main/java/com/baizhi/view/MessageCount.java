package com.baizhi.view;

import java.util.Map;

public class MessageCount {

    Map<String,String> map;

    int count;

    public MessageCount() {
    }

    @Override
    public String toString() {
        return "MessageCount{" +
                "map=" + map +
                ", count=" + count +
                '}';
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public MessageCount(Map<String, String> map, int count) {
        this.map = map;
        this.count = count;
    }
}

