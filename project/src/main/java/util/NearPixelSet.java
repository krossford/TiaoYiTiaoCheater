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

    public void add(Image.Pixel p) {
        mSet.add(p);
    }

    public Rect getBoundRect() {
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

        return rect;
    }
}
