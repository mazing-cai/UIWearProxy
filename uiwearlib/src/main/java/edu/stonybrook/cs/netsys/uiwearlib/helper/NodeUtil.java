package edu.stonybrook.cs.netsys.uiwearlib.helper;

import static edu.stonybrook.cs.netsys.uiwearlib.Constant.SYSTEM_UI_PKG;

import android.content.Context;
import android.graphics.Rect;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.orhanobut.logger.Logger;

import edu.stonybrook.cs.netsys.uiwearlib.dataProtocol.AccNode;

/**
 * Created by qqcao on 10/22/16.
 * <p>
 * AccessibilityNodeInfo utils
 */

public class NodeUtil {
    public static void printNodeTree(AccessibilityNodeInfo node) {
        if (node == null) {
            Logger.v("printing null");
            return;
        }

        int count = node.getChildCount();
        if (count == 0) {
            Logger.v("printing " + node.toString());
        } else {
            for (int i = 0; i < count; i++) {
                AccessibilityNodeInfo child = node.getChild(i);
                printNodeTree(child);
            }
        }
    }

    public static void printNodeTreeD(String tag, AccessibilityNodeInfo node) {
        if (node == null) {
            Logger.t(tag).d("node null");
            return;
        }

        int count = node.getChildCount();
        if (count == 0) {
            Logger.t(tag).d(node.toString());
        } else {
            for (int i = 0; i < count; i++) {
                AccessibilityNodeInfo child = node.getChild(i);
                printNodeTreeD(tag, child);
            }
        }
    }

    public static void printAccNodeTreeD(String tag, AccNode node) {
        if (node == null) {
            Logger.t(tag).d("node null");
            return;
        }

        int count = node.getChildCount();
        if (count == 0) {
            Logger.t(tag).d(node.toString());
        } else {
            for (int i = 0; i < count; i++) {
                AccNode child = node.getChild(i);
                printAccNodeTreeD(tag, child);
            }
        }
    }

    public static String getBriefNodeInfo(AccessibilityNodeInfo source) {
        if (source == null) {
            return "null";
        }
        Rect rectScreen = new Rect();
        source.getBoundsInScreen(rectScreen);

        return "rect: " + rectScreen.toString() + "; "
                + "size: " + source.getChildCount() + "; "
                + "packageName: " + source.getPackageName() + ";"
                + "viewID: " + source.getViewIdResourceName() + "; "
                + "class: " + source.getClassName() + "; "
                + "id: " + Integer.toHexString(source.hashCode()) + "; "
                + "text: " + source.getText() + "; "
                + "contentDesc: " + source.getContentDescription() + "; "
                + "click: " + source.isClickable();
    }

    public static String getNodeIdText(AccessibilityNodeInfo source) {
        if (source == null) {
            return "null";
        }
        return "viewID: " + source.getViewIdResourceName() + "; "
                + "text: " + source.getText();
    }

    public static String getNodeIdText(AccNode source) {
        if (source == null) {
            return "null";
        }
        return "viewID: " + source.getViewId() + "; "
                + "text: " + source.getText();
    }

    public static String getNodePkgName(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null)
                return "";
        CharSequence pkg = nodeInfo.getPackageName();
        if (pkg != null) {
            return pkg.toString();
        }
        return "";
    }

    public static boolean isAppRootNode(Context context, AccessibilityNodeInfo rootNode) {
        if (context == null) {
            Logger.e("context shouldn't be null");
            return false;
        }

        if (rootNode == null) {
            Logger.i("root node null");
            return false;
        }

        CharSequence nodePkgName = rootNode.getPackageName();
        return !(nodePkgName == null || nodePkgName.length() == 0) && !SYSTEM_UI_PKG.equals(
                nodePkgName) && !context.getPackageName().equals(nodePkgName);
    }

    public static String getEvenInfo(AccessibilityEvent source) {
        if (source == null) {
            return "null";
        }

        return "text: " + source.getText() + ","
                + "packageName: " + source.getPackageName() + ","
                + "className: " + source.getClassName() + ","
                + "action: " + source.getAction() + ","
                + "evenType: " + AccessibilityEvent.eventTypeToString(source.getEventType()) + ","
                + "contentDescription: " + source.getContentDescription();
    }
}
