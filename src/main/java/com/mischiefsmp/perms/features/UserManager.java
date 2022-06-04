package com.mischiefsmp.perms.features;

import com.mischiefsmp.perms.MischiefPerms;
import com.mischiefsmp.perms.permission.MischiefUser;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class UserManager {
    private static final HashMap<UUID, MischiefUser> users = new HashMap<>();

    public static void loadPlayer(UUID uuid) {
        //TODO: Also load from disk
        if(users.containsKey(uuid))
            return;

        MischiefUser user = new MischiefUser(uuid);
        //TODO: add default permissions? Configurable per user?
        users.put(uuid, user);
    }

    public static MischiefUser getUser(UUID userUUID) {
        loadPlayer(userUUID);
        return users.get(userUUID);
    }
}
