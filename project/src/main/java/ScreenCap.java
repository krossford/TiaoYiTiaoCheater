import kross.devtool.Log;
import kross.util.DateTimes;

import java.io.File;

/**
 * Created by krosshuang on 2018/3/14.
 */
public class ScreenCap {

    public static final String LOG_TAG = "ScreenCap";

    public interface OnScreenCapFinished {
        void onCapFinished(String imagePath);
    }

    public static void screenCap(OnScreenCapFinished callback) {

        String time = DateTimes.timestamp2string("_yyyy_MM_dd_HH_mm_ss", System.currentTimeMillis());

        String filePath = "image" + time +  ".png";

        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("adb shell screencap /sdcard/screen" + time + ".png");
            // todo 这里需要检测到截屏图片是否生成
            Thread.sleep(1000);
            runtime.exec("adb pull /sdcard/screen" + time + ".png " + filePath);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        boolean isCaptured = false;
        File f = new File(filePath);
        while (!isCaptured) {
            if (f.exists()) {
                Log.i(LOG_TAG, "got it");
                if (callback != null) {
                    callback.onCapFinished(f.getAbsolutePath());
                    isCaptured = true;
                }
            } else {
                Log.i(LOG_TAG, "no file");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
