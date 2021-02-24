package com.ycs.community.activiti.model;

import lombok.Data;

@Data
public class Edge extends GraphElement {
    /**
     * 起点.
     */
    private Node src;

    /**
     * 终点.
     */
    private Node dest;

    /**
     * 循环.
     */
    private boolean cycle;
}
