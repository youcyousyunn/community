package com.ycs.community.activiti.model;

import lombok.Data;

@Data
public class GraphElement {
    /**
     * 实例id，历史的id.
     */
    private String id;

    /**
     * 节点名称，bpmn图形中的id.
     */
    private String name;
}
