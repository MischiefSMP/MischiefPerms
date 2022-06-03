package com.mischiefsmp.perms.permission;

public class MischiefPermission {
    private final MischiefPermissionPart[] parts;
    private String world = null;
    private boolean isAllowed = true;
    private final static MischiefPermissionPart WILDCARD_PART = new MischiefPermissionPart("*");

    public MischiefPermission(String permission) {
        if(permission.startsWith("-")) {
            permission = permission.replaceFirst("-", "");
            isAllowed = false;
        }

        String[] stringParts = permission.split("\\.");
        parts = new MischiefPermissionPart[stringParts.length];
        for(int i = 0; i < stringParts.length; i++) {
            parts[i] = new MischiefPermissionPart(stringParts[i]);
        }
    }

    public MischiefPermission(String permission, String world) {
        this(permission);
        this.world = world;
    }

    public MischiefPermission(MischiefPermission permission) {
        this.parts = permission.parts;
        this.isAllowed = permission.isAllowed;
        this.world = permission.world;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(isAllowed ? "" : "-");
        for(MischiefPermissionPart part : parts) {
            builder.append(part);
            if(parts[parts.length - 1] != part)
                builder.append(".");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof MischiefPermission))
            return false;

        return equals(other, false, false);
    }

    public boolean equals(Object other, boolean ignoreAllowed, boolean ignoreWorld) {
        if(!(other instanceof MischiefPermission o))
            return false;

        if(!isAllowed == o.isAllowed && !ignoreAllowed)
            return false;


        if(!compareWorlds(world, o.world))
            return false;

        if(parts.length == 1 && parts[0].equals(WILDCARD_PART))
            return true;

        int length = Math.max(parts.length, o.parts.length);
        MischiefPermissionPart last = null;
        for(int i = 0; i < length; i++) {
            MischiefPermissionPart p1 = arg(parts, i);
            MischiefPermissionPart p2 = arg(o.parts, i);

            if(p1 == null) {
                if(last != null && !last.equals(WILDCARD_PART))
                    return false;
            } else {
                if(p2 != null && !p1.equals(p2))
                    return false;
            }

            if(p1 != null)
                last = p1;
        }

        return true;
    }

    private boolean compareWorlds(String w1, String w2) {
        //Both are null, that works out
        if(w1 == null && w2 == null)
            return true;

        //Neither are null, we can check
        if(w1 != null && w2 != null)
            return w1.equals(w2);

        //Something else, wont work out
        return false;
    }

    private MischiefPermissionPart arg(MischiefPermissionPart[] ps, int index) {
        if(index < ps.length)
            return ps[index];
        return null;
    }

    public void setAllowed(boolean allowed) {
        isAllowed = allowed;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }
}
