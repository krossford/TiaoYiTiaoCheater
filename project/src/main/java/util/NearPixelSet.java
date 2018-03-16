package util;

import java.util.ArrayList;
import java.util.List;

/**
 * 相邻点集合
 * Created by krosshuang on 2018/3/12.
 */
public class NearPixelSet {

    public List<Image.Pixel> mSet = new ArrayList<Image.Pixel>();

    public boolean isNear(Image.Pixel p) {
        if (mSet == null || mSet.isEmpty()) {
            return false;
        } else {
            for (Image.Pixel ourPx : mSet) {
                if (Math.abs(p.x - ourPx.x) <= 4 && Math.abs(p.y - ourPx.y) <= 4) {
                    return true;
                }
            }
            return false;
        }
    }

    private int mModCount = 0;

    public void add(Image.Pixel p) {
        mSet.add(p);
        mModCount++;
    }

    private Rect mRect = null;

    public Rect getBoundRect() {
        if (mRect == null || mModCount > 0) {
            Rect rect = new Rect();
            boolean isFirst = true;
            for (Image.Pixel px : mSet) {
                if (isFirst) {
                    rect.left = px.x;
                    rect.right = px.x;
                    rect.top = px.y;
                    rect.bottom = px.y;
                    isFirst = false;
                } else {
                    rect.left = Math.min(px.x, rect.left);
                    rect.right = Math.max(px.x, rect.right);
                    rect.top = Math.min(px.y, rect.top);
                    rect.bottom = Math.max(px.y, rect.bottom);
                }
            }
            mRect = rect;
            mModCount = 0;
        }
        return mRect;
    }

    private int[][] mBitmap = null;

    public int getPx(int x, int y) {
        return getPx(x, y, false);
    }

    public int getPx(int x, int y, boolean isLocal) {
        Rect rect = getBoundRect();
        if (mBitmap == null || mModCount > 0) {
            mBitmap = new int[rect.width()][rect.height()];

            for (Image.Pixel m : mSet) {
                mBitmap[m.x - rect.left][m.y - rect.top] = m.rgb;
            }
        }

        if (isLocal) {
            return mBitmap[x][y];
        } else {
            return mBitmap[x - rect.left][y - rect.top];
        }

    }
}
