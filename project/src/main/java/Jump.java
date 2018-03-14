import java.io.IOException;

/**
 * Created by krosshuang on 2018/3/14.
 */
public class Jump {

    public static void jump(float distance) {
        long millis = (long) (distance * 1.34);
        try {
            Runtime.getRuntime().exec("adb shell input touchscreen swipe 170 187 170 187 " + millis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
