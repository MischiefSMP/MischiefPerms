package com.mischiefsmp.perms.features;

import com.mischiefsmp.perms.MischiefPerms;
import com.mischiefsmp.perms.permission.MischiefGroup;
import com.mischiefsmp.perms.permission.MischiefUser;

import java.util.HashMap;
import java.util.UUID;

public class PermissionManager {
    private static MischiefPerms plugin;
    private static HashMap<String, MischiefGroup> groups = new HashMap<>();
    private static HashMap<UUID, MischiefUser> users = new HashMap<>();

    public static void init(MischiefPerms plugin) {
        PermissionManager.plugin = plugin;
    }

    public static MischiefGroup getGroup(String id) {
        return groups.get(id);
    }


}
