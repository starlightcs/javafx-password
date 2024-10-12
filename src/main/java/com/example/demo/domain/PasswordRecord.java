package com.example.demo.domain;

/**
 * 密码记录实体
 *
 * @author allen
 */
public class PasswordRecord {
    private int id;
    private String site;
    private String siteUrl;
    private String username;
    private String password;
    private String notes;
    private String groupName;  // 新增的分组字段

    // 构造器
    public PasswordRecord(int id, String site, String siteUrl, String username, String password, String notes, String groupName) {
        this.id = id;
        this.site = site;
        this.siteUrl = siteUrl;
        this.username = username;
        this.password = password;
        this.notes = notes;
        this.groupName = groupName;  // 初始化分组
    }

    // Getters 和 Setters
    public int getId() {
        return id;
    }

    public String getSite() {
        return site;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNotes() {
        return notes;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
