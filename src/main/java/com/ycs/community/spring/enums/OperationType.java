package com.ycs.community.spring.enums;

public enum OperationType {
    POST("POST"),
    DELETE("DELETE"),
    PUT("PUT"),
    GET("GET"),
    UNKNOWN("UNKNOWN");

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    OperationType(String type) {
        this.type = type;
    }
}
