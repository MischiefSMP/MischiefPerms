package com.mischiefsmp.perms.permission;

import java.util.ArrayList;
import java.util.UUID;

public class MischiefGroup {
    private String id;
    private int index;
    private ArrayList<MischiefPermission> permissions;
    private ArrayList<UUID> members = new ArrayList<UUID>();
    private String prefix;
    private String suffix;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArrayList<MischiefPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<MischiefPermission> permissions) {
        this.permissions = permissions;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<UUID> members) {
        this.members = members;
    }
}
