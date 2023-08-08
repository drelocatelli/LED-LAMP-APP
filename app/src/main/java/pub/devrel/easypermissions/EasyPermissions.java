package pub.devrel.easypermissions;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* loaded from: classes.dex */
public class EasyPermissions {
    private static final String DIALOG_TAG = "RationaleDialogFragmentCompat";
    private static final String TAG = "EasyPermissions";

    /* loaded from: classes.dex */
    public interface PermissionCallbacks extends ActivityCompat.OnRequestPermissionsResultCallback {
        void onPermissionsDenied(int i, List<String> list);

        void onPermissionsGranted(int i, List<String> list);
    }

    public static boolean hasPermissions(Context context, String... strArr) {
        if (Build.VERSION.SDK_INT < 23) {
            Log.w(TAG, "hasPermissions: API version < M, returning true by default");
            return true;
        }
        for (String str : strArr) {
            if (!(ContextCompat.checkSelfPermission(context, str) == 0)) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermissions(Object obj, String str, int i, String... strArr) {
        requestPermissions(obj, str, 17039370, 17039360, i, strArr);
    }

    public static void requestPermissions(Object obj, String str, int i, int i2, int i3, String... strArr) {
        checkCallingObjectSuitability(obj);
        boolean z = false;
        for (String str2 : strArr) {
            z = z || shouldShowRequestPermissionRationale(obj, str2);
        }
        if (z) {
            if (getSupportFragmentManager(obj) != null) {
                showRationaleDialogFragmentCompat(getSupportFragmentManager(obj), str, i, i2, i3, strArr);
                return;
            } else if (getFragmentManager(obj) != null) {
                showRationaleDialogFragment(getFragmentManager(obj), str, i, i2, i3, strArr);
                return;
            } else {
                showRationaleAlertDialog(obj, str, i, i2, i3, strArr);
                return;
            }
        }
        executePermissionsRequest(obj, strArr, i3);
    }

    private static void showRationaleDialogFragmentCompat(FragmentManager fragmentManager, String str, int i, int i2, int i3, String... strArr) {
        RationaleDialogFragmentCompat.newInstance(i, i2, str, i3, strArr).show(fragmentManager, DIALOG_TAG);
    }

    private static void showRationaleDialogFragment(android.app.FragmentManager fragmentManager, String str, int i, int i2, int i3, String... strArr) {
        RationaleDialogFragment.newInstance(i, i2, str, i3, strArr).show(fragmentManager, DIALOG_TAG);
    }

    private static void showRationaleAlertDialog(final Object obj, String str, int i, int i2, final int i3, final String... strArr) {
        Activity activity = getActivity(obj);
        if (activity == null) {
            throw new IllegalStateException("Can't show rationale dialog for null Activity");
        }
        new AlertDialog.Builder(activity).setCancelable(false).setMessage(str).setPositiveButton(i, new DialogInterface.OnClickListener() { // from class: pub.devrel.easypermissions.EasyPermissions.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i4) {
                EasyPermissions.executePermissionsRequest(obj, strArr, i3);
            }
        }).setNegativeButton(i2, new DialogInterface.OnClickListener() { // from class: pub.devrel.easypermissions.EasyPermissions.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i4) {
                Object obj2 = obj;
                if (obj2 instanceof PermissionCallbacks) {
                    ((PermissionCallbacks) obj2).onPermissionsDenied(i3, Arrays.asList(strArr));
                }
            }
        }).create().show();
    }

    public static boolean somePermissionPermanentlyDenied(Object obj, List<String> list) {
        for (String str : list) {
            if (permissionPermanentlyDenied(obj, str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean permissionPermanentlyDenied(Object obj, String str) {
        return !shouldShowRequestPermissionRationale(obj, str);
    }

    public static void onRequestPermissionsResult(int i, String[] strArr, int[] iArr, Object... objArr) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2];
            if (iArr[i2] == 0) {
                arrayList.add(str);
            } else {
                arrayList2.add(str);
            }
        }
        for (Object obj : objArr) {
            if (!arrayList.isEmpty() && (obj instanceof PermissionCallbacks)) {
                ((PermissionCallbacks) obj).onPermissionsGranted(i, arrayList);
            }
            if (!arrayList2.isEmpty() && (obj instanceof PermissionCallbacks)) {
                ((PermissionCallbacks) obj).onPermissionsDenied(i, arrayList2);
            }
            if (!arrayList.isEmpty() && arrayList2.isEmpty()) {
                runAnnotatedMethods(obj, i);
            }
        }
    }

    private static boolean shouldShowRequestPermissionRationale(Object obj, String str) {
        if (obj instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) obj, str);
        }
        if (obj instanceof Fragment) {
            return ((Fragment) obj).shouldShowRequestPermissionRationale(str);
        }
        if (obj instanceof android.app.Fragment) {
            return ((android.app.Fragment) obj).shouldShowRequestPermissionRationale(str);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void executePermissionsRequest(Object obj, String[] strArr, int i) {
        checkCallingObjectSuitability(obj);
        if (obj instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) obj, strArr, i);
        } else if (obj instanceof Fragment) {
            ((Fragment) obj).requestPermissions(strArr, i);
        } else if (obj instanceof android.app.Fragment) {
            ((android.app.Fragment) obj).requestPermissions(strArr, i);
        }
    }

    private static Activity getActivity(Object obj) {
        if (obj instanceof Activity) {
            return (Activity) obj;
        }
        if (obj instanceof Fragment) {
            return ((Fragment) obj).getActivity();
        }
        if (obj instanceof android.app.Fragment) {
            return ((android.app.Fragment) obj).getActivity();
        }
        return null;
    }

    private static FragmentManager getSupportFragmentManager(Object obj) {
        if (obj instanceof FragmentActivity) {
            return ((FragmentActivity) obj).getSupportFragmentManager();
        }
        if (obj instanceof Fragment) {
            return ((Fragment) obj).getChildFragmentManager();
        }
        return null;
    }

    private static android.app.FragmentManager getFragmentManager(Object obj) {
        if (obj instanceof Activity) {
            if (Build.VERSION.SDK_INT >= 11) {
                return ((Activity) obj).getFragmentManager();
            }
            return null;
        } else if (obj instanceof android.app.Fragment) {
            if (Build.VERSION.SDK_INT >= 17) {
                return ((android.app.Fragment) obj).getChildFragmentManager();
            }
            return ((android.app.Fragment) obj).getFragmentManager();
        } else {
            return null;
        }
    }

    private static void runAnnotatedMethods(Object obj, int i) {
        Method[] declaredMethods;
        Class<?> cls = obj.getClass();
        if (isUsingAndroidAnnotations(obj)) {
            cls = cls.getSuperclass();
        }
        for (Method method : cls.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AfterPermissionGranted.class) && ((AfterPermissionGranted) method.getAnnotation(AfterPermissionGranted.class)).value() == i) {
                if (method.getParameterTypes().length > 0) {
                    throw new RuntimeException("Cannot execute method " + method.getName() + " because it is non-void method and/or has input parameters.");
                }
                try {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    method.invoke(obj, new Object[0]);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "runDefaultMethod:IllegalAccessException", e);
                } catch (InvocationTargetException e2) {
                    Log.e(TAG, "runDefaultMethod:InvocationTargetException", e2);
                }
            }
        }
    }

    private static void checkCallingObjectSuitability(Object obj) {
        Objects.requireNonNull(obj, "Activity or Fragment should not be null");
        boolean z = obj instanceof Activity;
        boolean z2 = obj instanceof Fragment;
        boolean z3 = obj instanceof android.app.Fragment;
        boolean z4 = Build.VERSION.SDK_INT >= 23;
        if (z2 || z) {
            return;
        }
        if (z3 && z4) {
            return;
        }
        if (z3) {
            throw new IllegalArgumentException("Target SDK needs to be greater than 23 if caller is android.app.Fragment");
        }
        throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
    }

    private static boolean isUsingAndroidAnnotations(Object obj) {
        if (obj.getClass().getSimpleName().endsWith("_")) {
            try {
                return Class.forName("org.androidannotations.api.view.HasViews").isInstance(obj);
            } catch (ClassNotFoundException unused) {
                return false;
            }
        }
        return false;
    }
}
