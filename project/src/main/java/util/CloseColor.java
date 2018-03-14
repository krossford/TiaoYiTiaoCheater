package util;

import java.util.ArrayList;
import java.util.List;

/**
 * 相近的颜色
 * Created by krosshuang on 2018/3/12.
 */
public class CloseColor {

    private List<Integer> colorSet;

    private int mDeltaInRGB = 1;

    public CloseColor(int deltaInRGB) {
        mDeltaInRGB = deltaInRGB;
        colorSet = new ArrayList<Integer>();
    }

    public void base(int color) {
        colorSet.add(color);
    }

    public boolean isClose(int color) {
        int[] targetColorRGB = toRGB(color);

        for (int ourColor : colorSet) {
            if (color == ourColor) {
                return true;
            }
        }

        for (int ourColor : colorSet) {

            int[] ourColorRGB = toRGB(ourColor);

            if (Math.abs(ourColorRGB[0] - targetColorRGB[0]) <= mDeltaInRGB
                    && Math.abs(ourColorRGB[1] - targetColorRGB[1]) <= mDeltaInRGB
                    && Math.abs(ourColorRGB[2] - targetColorRGB[2]) <= mDeltaInRGB) {
                colorSet.add(color);
                return true;
            }

        }

        return false;
    }

    public static int[] toRGB(int rgb) {
        int[] rrggbb = new int[3];

        rrggbb[0] = (rgb & 0xff0000) >> 16;
        rrggbb[1] = (rgb & 0xff00) >> 8;
        rrggbb[2] = (rgb & 0xff);

        return rrggbb;
    }
}
