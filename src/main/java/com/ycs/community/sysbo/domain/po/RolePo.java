package com.ycs.community.sysbo.domain.po;

import com.ycs.community.basebo.domain.po.BasePo;
import lombok.Data;

import java.util.List;
import java.util.Objects;

//@Data
public class RolePo extends BasePo {
    private Long id;
    private String code;
    private String name;
    private String desc;
    private int level;
    private String dataScope;
    private List<DeptPo> depts;
    private List<MenuPo> menus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public List<DeptPo> getDepts() {
        return depts;
    }

    public void setDepts(List<DeptPo> depts) {
        this.depts = depts;
    }

    public List<MenuPo> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuPo> menus) {
        this.menus = menus;
    }
}
