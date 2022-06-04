package com.mischiefsmp.perms.permission;

import com.mischiefsmp.perms.features.UserManager;

import java.util.ArrayList;
import java.util.UUID;

public class MischiefGroup {
    private String id;
    private int index = -1;
    private final ArrayList<MischiefPermission> permissions = new ArrayList<>();
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

    public MischiefPermission getPermission(MischiefPermission permission, boolean ignoreAllowed, boolean ignoreWorld) {
        for(MischiefPermission p : permissions) {
            if(p.equals(permission, ignoreAllowed, ignoreWorld)) {
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

        //Remove users from this group
        for(UUID userUUID : members) {
            MischiefUser user = UserManager.getUser(userUUID);
            if(user != null) {
                user.removeGroup(id);
            }
        }
        members.clear();
    }

    public void addPermission(MischiefPermission permission) {
        permissions.add(permission);
    }

    public void removePermission(MischiefPermission permission) {
        permissions.removeIf(toRemove -> toRemove.equals(permission));
    }

    public ArrayList<MischiefPermission> getPermissions() {
        return permissions;
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
