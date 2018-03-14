import kross.devtool.Log;

import java.io.IOException;

/**
 * Created by krosshuang on 2018/3/14.
 */
public class Jump {

    public static final String LOG_TAG = "Jump";

    public static void jump(float distance) {

        long millis = (long) (distance * 1.34);
        int randomX = (int) (Math.random() * 500 + 100);
        int randomY = (int) (Math.random() * 600 + 200);

        Log.i(LOG_TAG, "距离: " + distance + " 长按: " + millis + "ms " + "按下坐标: " + randomX + ", " + randomY);
        try {
            Runtime.getRuntime().exec(String.format("adb shell input touchscreen swipe %d %d %d %d %d", randomX, randomY, randomX, randomY, millis));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
