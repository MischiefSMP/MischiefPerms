package com.mischiefsmp.perms.permission;

import com.mischiefsmp.perms.MischiefPerms;
import com.mischiefsmp.perms.features.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class MischiefUser {
    private final UUID uuid;
    private final ArrayList<MischiefPermission> permissions = new ArrayList<>();
    private final ArrayList<String> groups = new ArrayList<>(); //We keep a reference to the group's id, not the class
    private String prefix;
    private String suffix;

    public MischiefUser(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean canRun(String permission) {
        return canRun(new MischiefPermission(permission));
    }

    public boolean canRun(MischiefPermission permission) {
        if(!permission.isAllowed())
            MischiefPerms.log("canRun check for permission <%s>. It is denied with the - char. Should this be asked?", Level.WARNING, permission);

        boolean canRun = false;
        MischiefPermission successfulPermission = null;
        //Check groups
        for(String groupID : groups) {
            MischiefGroup g = GroupManager.getGroup(groupID);
            if(g != null) {
                //TODO: Check this in the same shorter way we check it for the user (- checking)
                MischiefPermission ignoreAllowed = g.getPermission(permission, true, false);
                if(!ignoreAllowed.isAllowed()) {
                    canRun = false;
                    break;
                }

                MischiefPermission check = g.getPermission(permission, false, false);
                if(check != null) {
                    canRun = true;
                    successfulPermission = check;
                }
            } else {
                MischiefPerms.log("Group <%s> in MischiefUser <%s> is null! This should not happen!", Level.WARNING, groupID, uuid);
            }
        }

        //TODO: Check user permissions first, since they are more important
        //Check user permission
        for(MischiefPermission p : permissions) {
            if(p.equals(permission, true, false)) {
                if(!p.isAllowed()) {
                    canRun = false;
                    break;
                }
            }
            if(p.equals(permission)) {
                canRun = true;
                successfulPermission = p;
            }
        }

        //World check
        if(successfulPermission != null) {
            String wCheck = successfulPermission.getWorld();
            if (canRun && wCheck != null && !wCheck.isEmpty()) {
                World hasToBeIn = Bukkit.getServer().getWorld(wCheck);
                Player player = Bukkit.getPlayer(uuid);
                if (hasToBeIn != null && player != null) {
                    World inWorld = player.getWorld();
                    //Permission has a world set
                    if (hasToBeIn != inWorld)
                        canRun = false;
                }
            }
        }
        return canRun;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void addPermission(String permission) {
        permissions.add(new MischiefPermission(permission));
    }

    public void removePermission(MischiefPermission permission) {
        permissions.removeIf(toRemove -> toRemove.equals(permission));
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
