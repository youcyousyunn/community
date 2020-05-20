package com.ycs.community.spring.enums;

public enum OperationTypeEnum {
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

    OperationTypeEnum(String type) {
        this.type = type;
    }
}
