package com.framgia.soundcloud.util;

/**
 * Created by quangnv on 10/08/2018
 */

public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty() || str.trim().length() == 0;
    }
}
