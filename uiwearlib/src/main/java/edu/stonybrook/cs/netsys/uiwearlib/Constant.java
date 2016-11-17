package edu.stonybrook.cs.netsys.uiwearlib;

import android.content.Intent;

/**
 * Created by qqcao on 10/18/16 Tuesday.
 * Constants for UIWear
 */

public class Constant {
    public static final int ACCESSIBILITY_SERVICE_REQUEST_CODE = 1;
    public static final String PREFERENCE_SETTING_KEY = "PREFERENCE_SETTING_KEY";
    public static final int PREFERENCE_SETTING_CODE = 2;
    public static final String PREFERENCE_SETTING_SAVE = "PREFERENCE_SETTING_SAVE";
    public static final String PREFERENCE_NODES_KEY = "PREFERENCE_NODES_KEY";
    public static final String PREFERENCE_SETTING_EXIT = "PREFERENCE_SETTING_EXIT";
    public static final String SYSTEM_UI_PKG = "com.android.systemui";
    public static final String PREFERENCE_SETTING_STARTED = "PREFERENCE_SETTING_STARTED";
    public static final String NODES_AVAILABLE = "NODES_AVAILABLE";
    public static final String AVAILABLE_NODES_PREFERENCE_SETTING_KEY
            = "AVAILABLE_NODES_PREFERENCE_SETTING_KEY";
    public static final String PREFERENCE_STOP_KEY = "PREFERENCE_STOP_KEY";
    public static final int PREFERENCE_STOP_CODE = 3;
    public static final Intent ACCESSIBILITY_SETTING_INTENT
            = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
    public static final String ENABLED_APPS_PREF_NAME = "UIWearEnabledApps";

    public static final float CLICK_SPAN_THRESHOLD = 5.0f;

    public static final String CAPABILITY = "UIWear_Capability";

    public static final int PERSIST_PREFERENCE_NODES_SUCCESS = 5;
    public static final int READ_PREFERENCE_NODES_SUCCESS = 6;

    // at most 20 apps running, each app has at most 50 preferences
    public static final int RUNNING_APP_PREF_CACHE_SIZE = 20 * 50;

    public static final String XML_EXT = ".xml";
    public static final String TIME_FORMAT = "yyyy-MM-dd-hh_mm_ss";

    // 10MB bitmap cache
    public static final int BITMAP_CACHE_SIZE = 10 * 1024 * 1024;

    public static final String DATA_BUNDLE_HASH_PATH = "/DATA_BUNDLE_HASH_PATH";
    public static final String DATA_BUNDLE_REQUIRED_PATH = "/DATA_BUNDLE_REQUIRED_PATH";
    public static final String DATA_BUNDLE_PATH = "/DATA_BUNDLE_PATH";
    public static final String DATA_BUNDLE_KEY = "/DATA_BUNDLE_KEY";
    public static final int DATABUNDLE_CACHE_SIZE = 10 * 1024 * 1024;

    public static final String TRANSFER_APK_REQUEST = "/TRANSFER_APK_REQUEST";
    public static final String TRANSFER_MAPPING_RULES_REQUEST = "/TRANSFER_MAPPING_RULES_REQUEST";

    public static final int PERMISSIONS_REQUEST_CODE = 7;

    public static final String WATCH_RESOLUTION_PATH = "/WATCH_RESOLUTION_PATH";
    public static final String WATCH_PHONE_RESOLUTION_RATIO_PREF_NAME =
            "WATCH_PHONE_RESOLUTION_RATIO_PREF_NAME";
    public static final String WATCH_PHONE_RESOLUTION_RATIO_KEY =
            "WATCH_PHONE_RESOLUTION_RATIO_KEY";
}
