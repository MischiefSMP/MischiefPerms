package com.mischiefsmp.perms.features;

import com.mischiefsmp.perms.MischiefPerms;
import com.mischiefsmp.perms.permission.MischiefGroup;
import com.mischiefsmp.perms.permission.MischiefUser;

import java.util.HashMap;
import java.util.UUID;

public class PermissionManager {
    private static MischiefPerms plugin;
    private static final HashMap<String, MischiefGroup> groups = new HashMap<>();
    private static final HashMap<UUID, MischiefUser> users = new HashMap<>();

    public static void init(MischiefPerms plugin) {
        PermissionManager.plugin = plugin;
    }

    public static boolean hasGroup(String id) {
        return groups.containsKey(id);
    }

    public static MischiefGroup getGroup(String id) {
        return groups.get(id);
    }

    public static void createGroup(String id) {
        if(!hasGroup(id)) {
            groups.put(id, new MischiefGroup());
        }
    }

    public static void deleteGroup(String id) {
        if(!hasGroup(id))
            return;

        //Remove users from this group
        MischiefGroup g = groups.get(id);
        for(UUID member : g.getMembers()) {
            MischiefUser user = users.get(member);
            if(user != null) {
                user.removeGroup(g.getId());
            }
        }

        //Remove group
        groups.remove(g.getId());
    }


}
