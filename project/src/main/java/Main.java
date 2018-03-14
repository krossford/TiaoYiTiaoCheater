
import kross.devtool.Log;
import kross.util.DateTimes;
import util.CloseColor;
import util.Image;
import util.NearPixelSet;
import util.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class Main {

    public static String LOG_TAG = "Main";

    public static void main(String[] args) {
        fuck();
    }

    public static void fuck() {
        ScreenCap.screenCap(new ScreenCap.OnScreenCapFinished() {
            public void onCapFinished(String imagePath) {
                float distance = TiaoYiTiao.start(imagePath);
                Jump.jump(distance);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fuck();
            }
        });
    }
}