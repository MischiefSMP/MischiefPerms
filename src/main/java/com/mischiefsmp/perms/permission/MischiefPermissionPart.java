package com.mischiefsmp.perms.permission;

import com.mischiefsmp.perms.utils.MathUtils;

public class MischiefPermissionPart {
    private final String stringValue;
    private final int intValue;
    private final TYPE type;

    enum TYPE {STRING, INT, WILDCARD}

    private final static String WILDCARD_CHAR = "*";

    public MischiefPermissionPart(String part) {
        if(MathUtils.isInteger(part)) {
            stringValue = null;
            intValue = Integer.parseInt(part);
            type = TYPE.INT;
        } else if(part.equals(WILDCARD_CHAR)) {
            stringValue = null;
            intValue = -1;
            type = TYPE.WILDCARD;
        } else {
            stringValue = part;
            intValue = -1;
            type = TYPE.STRING;
        }
    }

    @Override
    public String toString() {
        switch(type) {
            case INT -> { return Integer.toString(intValue); }
            case STRING -> { return stringValue; }
            case WILDCARD -> { return WILDCARD_CHAR; }
        }
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof MischiefPermissionPart o))
            return false;

        switch(type) {
            case INT -> { return intValue == o.intValue; }
            case STRING -> { return stringValue.equals(o.stringValue); }
            case WILDCARD -> { return true; }
        }
        return false;
    }
}
