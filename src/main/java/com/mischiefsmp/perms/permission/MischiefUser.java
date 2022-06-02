package com.mischiefsmp.perms.permission;

import com.mischiefsmp.perms.MischiefPerms;
import com.mischiefsmp.perms.features.PermissionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class MischiefUser {
    private UUID uuid;
    private HashMap<String, MischiefPermission> permissions = new HashMap<>();
    private ArrayList<String> groups = new ArrayList<>(); //We keep a reference to the group's id, not the class
    private String prefix;
    private String suffix;

    public boolean canRun(String permission) {
        return canRun(new MischiefPermission(permission));
    }

    public boolean canRun(MischiefPermission permission) {
        if(!permission.isAllowed())
            MischiefPerms.log("canRun check for permission <%s>. It is denied with the - char. Should this be asked?", Level.WARNING, permission);

        boolean canRun = false;
        //Check groups
        for(String groupID : groups) {
            MischiefGroup g = PermissionManager.getGroup(groupID);
            if(g != null) {
                for (MischiefPermission p : g.getPermissions()) {
                    if(p.equals(permission, true)) {
                        if(!p.isAllowed()) {
                            canRun = false;
                            break;
                        }
                    }
                    if (p.equals(permission)) {
                        canRun = true;
                    }
                }
            } else {
                MischiefPerms.log("Group <%s> in MischiefUser <%s> is null! This should not happen!", Level.WARNING, groupID, uuid);
            }
        }

        //Check user permission
        for(String pKey : permissions.keySet()) {
            MischiefPermission p = permissions.get(pKey);
            if(p.equals(permission, true)) {
                if(!p.isAllowed()) {
                    canRun = false;
                    break;
                }
            }
            if(p.equals(permission))
                canRun = true;
        }

        return canRun;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void addPermission(String permission) {
        MischiefPermission newPermission = new MischiefPermission(permission);
        permissions.put(newPermission.toString(), newPermission);
    }

    public void removePermission(String permission) {
        permissions.remove(new MischiefPermission(permission).toString());
    }

    public void addGroup(MischiefGroup group) {
        addGroup(group.getId());
    }

    public void addGroup(String groupID) {
        if(!groups.contains(groupID))
            groups.add(groupID);
    }

    public void removeGroup(MischiefGroup group) {
        removeGroup(group.getId());
    }

    public void removeGroup(String groupID) {
        groups.remove(groupID);
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
}
