package edu.stonybrook.cs.netsys.uiwearlib;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqcao on 10/24/16.
 * <p>
 * Utils related to applications
 */

public class AppUtil {
    // Get a list of installed app
    public static List<String> getInstalledPackages(Context mContext) {
        // Initialize a new Intent which action is main
        Intent intent = new Intent(Intent.ACTION_MAIN, null);

        // Set the newly created intent category to launcher
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // Set the intent flags
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        );

        // Generate a list of ResolveInfo object based on intent filter
        List<ResolveInfo> resolveInfoList = mContext.getPackageManager()
                .queryIntentActivities(intent, 0);

        // Initialize a new ArrayList for holding non system package names
        List<String> packageNames = new ArrayList<>();

        // Loop through the ResolveInfo list
        for (ResolveInfo resolveInfo : resolveInfoList) {
            // Get the ActivityInfo from current ResolveInfo
            ActivityInfo activityInfo = resolveInfo.activityInfo;

            // If this is not a system app package
            if (!isSystemPackage(resolveInfo)) {
                // Add the non system package to the list
                packageNames.add(activityInfo.applicationInfo.packageName);
            }
        }

        return packageNames;

    }

    // Custom method to determine an app is system app
    public static boolean isSystemPackage(ResolveInfo resolveInfo) {
        return ((resolveInfo.activityInfo.applicationInfo.flags
                & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    // Custom method to get application icon by package name
    public static Drawable getAppIconByPackageName(Context mContext, String packageName) {
        Drawable icon = null;
        try {
            icon = mContext.getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // Get a default icon
        }
        return icon;
    }

    // Custom method to get application label by package name
    public static String getApplicationLabelByPackageName(Context mContext, String packageName) {
        PackageManager packageManager = mContext.getPackageManager();
        ApplicationInfo applicationInfo;
        String label = "Unknown";
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                label = (String) packageManager.getApplicationLabel(applicationInfo);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return label;
    }

    public static boolean isActionAvailable(Context context, String action) {
        Intent intent = new Intent(action);
        return context.getPackageManager().resolveActivity(intent, 0) != null;
    }

    public static byte[] marshall(Parcelable parcelable) {
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static Parcel unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0); // This is extremely important!
        return parcel;
    }

    public static <T> T unmarshall(byte[] bytes, Parcelable.Creator<T> creator) {
        Parcel parcel = unmarshall(bytes);
        T result = creator.createFromParcel(parcel);
        parcel.recycle();
        return result;
    }

    // pay attention to permission
    public static void storeBitmapAsync(Bitmap bitmap, String imageName) {
        File sdcard = Environment.getExternalStorageDirectory();
        storeBitmapAsync(bitmap, sdcard.getPath() + File.separator + "UIWear", imageName);
    }

    public static void storeBitmapAsync(final Bitmap bitmap, final String folder,
            final String imageName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File dir = new File(folder);
                    File imageFile = new File(dir.getPath()
                            + File.separator
                            + imageName
                            + ".png");
                    boolean isDirCreated = dir.exists() || dir.mkdirs();

                    if (!isDirCreated) {
                        Logger.e("dir failed to create" + dir.getPath());
                    }

                    FileOutputStream outputStream = new FileOutputStream(imageFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Logger.v("image saved:" + imageFile);
                } catch (Throwable e) {
                    // Several error may come out with file handling or OOM
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static byte[] getBitmapBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static final int BITMAP_DIR = 1;
    public static final int PREFERENCE_DIR = 2;
    public static final int MAPPING_RULE_DIR = 3;

    public static String getResDir(Context context, int dirType, String appPkgName) {
        String dirName;
        switch (dirType) {
            case BITMAP_DIR:
                dirName = "SavedImages";
                break;
            case PREFERENCE_DIR:
                dirName = "Preferences";
                break;
            case MAPPING_RULE_DIR:
                dirName = "MappingRules";
                break;
            default:
                dirName = "temp";
        }
        return context.getObbDir() + File.separator + dirName + File.separator + appPkgName;
    }
}
