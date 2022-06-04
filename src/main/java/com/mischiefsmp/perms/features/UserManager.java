package com.mischiefsmp.perms.features;

import com.mischiefsmp.perms.MischiefPerms;
import com.mischiefsmp.perms.permission.MischiefUser;

import java.util.HashMap;
import java.util.UUID;

public class UserManager {
    private static final HashMap<UUID, MischiefUser> users = new HashMap<>();

    public static MischiefUser getUser(UUID userUUID) {
        return users.get(userUUID);
    }
}
