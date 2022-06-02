package com.mischiefsmp.perms.permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MischiefGroup {
    private String id;
    private int index = -1;
    private final HashMap<String, MischiefPermission> permissions = new HashMap<>();
    private ArrayList<UUID> members = new ArrayList<>();
    private String prefix;
    private String suffix;

    public MischiefGroup(String id) {
        this.id = id;
    }

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

    public MischiefPermission getPermission(String permission, boolean ignoreAllowed) {
        for(String pKey : permissions.keySet()) {
            MischiefPermission p = permissions.get(pKey);
            if(p.equals(new MischiefPermission(permission), true)) {
                return p;
            }
        }
        return null;
    }

    public void clear() {
        int index = -1;
        permissions.clear();
        prefix = null;
        suffix = null;
    }

    public void addPermission(String permission) {
        MischiefPermission newPermission = new MischiefPermission(permission);
        permissions.put(newPermission.toString(), newPermission);
    }

    public void removePermission(String permission) {
        permissions.remove(new MischiefPermission(permission).toString());
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
