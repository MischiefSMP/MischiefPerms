package com.mischiefsmp.perms.features;

import com.mischiefsmp.perms.permission.MischiefGroup;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupManager {
    private static final HashMap<String, MischiefGroup> groups = new HashMap<>();

    public static boolean hasGroup(String id) {
        return groups.containsKey(id);
    }

    public static MischiefGroup getGroup(String id) {
        return groups.get(id);
    }

    public static ArrayList<MischiefGroup> getGroups() {
        ArrayList<MischiefGroup> list = new ArrayList<>();
        for(String gKey : groups.keySet())
            list.add(groups.get(gKey));
        return list;
    }

    public static void createGroup(String id) {
        if(!hasGroup(id)) {
            groups.put(id, new MischiefGroup(id));
        }
    }

    public static void deleteGroup(MischiefGroup group) {
        deleteGroup(group.getId());
    }

    public static void deleteGroup(String id) {
        if(!hasGroup(id))
            return;

        MischiefGroup g = groups.get(id);
        g.clear();

        //Remove group
        groups.remove(g.getId());
    }
}
