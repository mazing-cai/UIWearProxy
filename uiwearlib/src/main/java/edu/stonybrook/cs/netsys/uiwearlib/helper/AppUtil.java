package edu.stonybrook.cs.netsys.uiwearlib.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.stonybrook.cs.netsys.uiwearlib.R;

/**
 * Created by qqcao on 10/24/16.
 * <p>
 * Utils related to applications
 */

public class AppUtil {
    // Get a list of installed app
    public static List<String> getInstalledPackages(final Context mContext) {
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
            if (!isSystemPackage(resolveInfo) && !mContext.getPackageName().equals(
                    activityInfo.applicationInfo.packageName)) {
                // Add the non system package to the list
                if (!packageNames.contains(activityInfo.applicationInfo.packageName)) {
                    packageNames.add(activityInfo.applicationInfo.packageName);
                }
            }
        }

        Collections.sort(packageNames, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return getApplicationLabelByPackageName(mContext, lhs).compareToIgnoreCase(
                        getApplicationLabelByPackageName(mContext, rhs));
            }
        });

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

    public static void dumpAppsInfo(final Context context) {
        StringBuilder sb = new StringBuilder();

        List<String> appPkgList = getInstalledPackages(context);
        for (String pkgName : appPkgList) {
            String appName = getApplicationLabelByPackageName(context, pkgName);
            sb.append(appName).append(", ").append(pkgName).append("\n");
        }

        try {
            String infoFile = getInfoFilePath();
            FileUtils.writeStringToFile(new File(infoFile), sb.toString());
            String successInfo = context.getString(R.string.dump_info_success, infoFile);
            Toast.makeText(context, successInfo, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, R.string.dump_info_failed, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static boolean isActionAvailable(Context context, String action) {
        Intent intent = new Intent(action);
        return context.getPackageManager().resolveActivity(intent, 0) != null;
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

    public static String getImageCacheFolderPath() {
        File sdcard = Environment.getExternalStorageDirectory();
        return sdcard.getPath() + File.separator + "UIWear" + File.separator
                + "ImageCache";
    }

    private static String getInfoFilePath() {
        File sdcard = Environment.getExternalStorageDirectory();
        return sdcard.getPath() + File.separator + "UIWear" + File.separator
                + "apps_info.txt";
    }

    public static byte[] getBitmapBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmap.getByteCount() > 100 * 1024) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
        } else {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }
        return stream.toByteArray();
    }

    public static String hashBitmap(Bitmap bmp) {
        int hash = 1;
        // use step 10 for better calculation performance
        for (int x = 0; x < bmp.getWidth(); x += 10) {
            for (int y = 0; y < bmp.getHeight(); y += 10) {
                hash = 31 * hash + bmp.getPixel(x, y);
            }
        }
        return Integer.toHexString(hash);
    }

    public static void purgeImageCache(final Context context) {
        final Handler mMainThreadHandler = new Handler(context.getMainLooper());
        // delete image folders
        String imageCacheFolder = getImageCacheFolderPath();
        try {
            FileUtils.deleteDirectory(new File(imageCacheFolder));
            mMainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Cache Purged",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            mMainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Cache Purge Failed",
                            Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
    }
}
