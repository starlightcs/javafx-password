package com.example.demo.domain;

/**
 * 分组实体
 *
 * @author allen
 */
public class GroupRecord {
    private int id;
    private String groupName;

    public GroupRecord(int id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
