package com.infotec.ftp.client.utils;

import java.util.HashMap;
import java.util.Map;

public enum EMenuOptions {
    LIST(0),
    GET_BY_ID(1),
    ADD(2),
    DELETE_BY_ID(3),
    EXIT(4),
    HELP(5);
    private final int value;

    private static final Map<Integer, EMenuOptions> map = new HashMap<>();
    static {
        for (EMenuOptions menuOptions : EMenuOptions.values()) {
            map.put(menuOptions.value, menuOptions);
        }
    }

    public static EMenuOptions valueOf(int value) {
        return map.get(value);
    }

    EMenuOptions(final int value) {
        this.value = value;
    }
}
