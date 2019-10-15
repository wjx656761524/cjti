package com.honghailt.cjtj.utils;

import com.google.common.collect.ImmutableMap;
import com.honghailt.cjtj.domain.enumeration.Version;

import java.util.Map;

public class VersionUtils {

    private static final Map<String, Version> VERSION_MAP = ImmutableMap.<String, Version>builder()
        .put("1", Version.BASIS)
        .put("2", Version.FLAG_SHIP)
        .put("3", Version.MM)
        .put("4", Version.VIP)
        .put("5", Version.EXPERT)
        .put("6", Version.SINGLE)
        .build();

    private static final Map<Version, String> VERSION_NAME_MAP = ImmutableMap.<Version, String>builder()
        .put(Version.BASIS, "基础标准版")
        .put(Version.FLAG_SHIP, "豪华旗舰版")
        .put(Version.MM, "至尊人机结合版")
        .put(Version.VIP, "VIP专家全托管")
        .put(Version.EXPERT, "资深专家_人工优化")
        .put(Version.SINGLE, "单引擎-经济版")
        .build();

    /**
     * 根据接口返回的版本号转为对应的版本enum
     * @param versionNum
     * @return
     */
    public static Version getVersionByNum(String versionNum) {
        if (VERSION_MAP.containsKey(versionNum)) {
            return VERSION_MAP.get(versionNum);
        }
        throw new IllegalArgumentException("未知的版本号：" + versionNum);
    }

    /**
     * 获取版本的中文名
     * @param version
     * @return
     */
    public static String getVersionName(Version version) {
        if (VERSION_NAME_MAP.containsKey(version)) {
            return VERSION_NAME_MAP.get(version);
        }
        throw new IllegalArgumentException("未知的版本号:" + version);
    }
}
