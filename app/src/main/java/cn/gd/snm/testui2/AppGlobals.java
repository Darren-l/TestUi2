package cn.gd.snm.testui2;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * @ClassName: AppGlobals
 * @author: Shimmer
 * @ceateDate: 2021/7/13 10:26
 * @description: 获取资源类
 */
public class AppGlobals {

    private static Application sApplication;

    volatile private static AppGlobals instance = null;

    private AppGlobals() {
        if (null != instance) {
            throw new RuntimeException("dont construct more AppGlobals!");
        }
    }


    public static AppGlobals getInstance() {
        if (instance == null) {
            synchronized (AppGlobals.class) {
                if (instance == null) {
                    instance = new AppGlobals();
                }
            }
        }
        return instance;
    }

    public void applicationConfig(Application application) {
        if (application == null) {
            Log.e("---->", "SNMSDK init application is null.");
            return;
        }
        if (sApplication == null) {
            sApplication = application;
        }
        if (sApplication.equals(application)) return;
        sApplication = application;

    }


    /**
     * 组件间通信获取上下文
     *
     * @return
     */
    /*@SuppressLint("PrivateApi")
    public static Application getApplication(){
        if(sApplication == null){
            try {
                Class<?> aClass = Class.forName("android.app.ActivityThread");
                Method currentApplication = aClass.getMethod("currentApplication", new Class[0]);

                // 得到当前的ActivityThread对象
                Object localObject = currentApplication.invoke(null, (Object[]) null);

                final Method method = aClass.getMethod("getApplication");

                sApplication = (Application) method.invoke(localObject, (Object[]) null);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return sApplication;
    }*/
    public Application getApplication() {
        return sApplication;
    }

    /**
     * 获取版本名称，比如1.1.1，2.1 等,字符窜
     *
     * @param context 上下文
     * @return 当前应用的版本名。获取失败则返回 null
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 内部升级用，1,2,3,4 之类的整数，随版本的发布逐步增大
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {

        }
        return 0;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context Context
     * @return 屏幕宽度（px）
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    /**
     * 获取屏幕高度
     *
     * @param context Context
     * @return 屏幕高度（px）
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }


    /**
     * 获取设备密度与宽高值
     * @param context
     * @return
     */
    public static String getDisplayMetrics(Context context){
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        // 屏幕宽度（像素）
        int width = dm.widthPixels;
        // 屏幕高度（像素）
        int height = dm.heightPixels;
        int densityDpi = dm.densityDpi;
        float density = dm.density;
        StringBuffer sb = new StringBuffer();
        sb.append("\n屏幕密度DPI(densityDpi): 【" + densityDpi + "dpi】");
        sb.append("\n屏幕密度(density): 【" + density+"】");
        sb.append("\n宽/高英寸(dpi): "+xdpi+"/"+ydpi);
        sb.append("\n宽/高像素(px): "+width+"/"+height);
        return sb.toString();
    }


    /**
     * dp值转换成px值
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px值转换成dp值
     *
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(Context context, final float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp值转换成px值
     *
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(Context context, final float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px值转换成sp值
     *
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(Context context, final float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
