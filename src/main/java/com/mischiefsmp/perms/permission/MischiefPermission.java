package com.mischiefsmp.perms.permission;

public class MischiefPermission {
    private final MischiefPermissionPart[] parts;
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
        if(!(other instanceof MischiefPermission o))
            return false;

        if(!isAllowed == o.isAllowed)
            return false;

        if(parts.length == 1 && parts[0].equals(WILDCARD_PART))
            return true;

        int length = Math.max(parts.length, o.parts.length);
        boolean ok = true;
        MischiefPermissionPart last = null;
        for(int i = 0; i < length; i++) {
            MischiefPermissionPart p1 = arg(parts, i);
            MischiefPermissionPart p2 = arg(o.parts, i);

            if(p1 == null) {
                if(last != null && !last.equals(WILDCARD_PART))
                    ok = false;
                else
                    break;
            } else {
                if(p2 != null && !p1.equals(p2))
                    ok = false;
            }

            if(p1 != null)
                last = p1;
        }

        return ok;
    }

    private MischiefPermissionPart arg(MischiefPermissionPart[] ps, int index) {
        if(index < ps.length)
            return ps[index];
        return null;
    }
}
