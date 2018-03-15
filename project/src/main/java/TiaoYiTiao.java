import kross.devtool.Log;
import kross.util.DateTimes;
import util.CloseColor;
import util.Image;
import util.NearPixelSet;
import util.Rect;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by krosshuang on 2018/3/14.
 */
public class TiaoYiTiao {

    public static final String LOG_TAG = "TiaoYiTiao";

    public static Image.Pixel findNextCenter(NearPixelSet maxSet) {
        Image.Pixel theMostTopPx = null;
        Image.Pixel theMostLeftPx = null;

        for (Image.Pixel p : maxSet.mSet) {
            if (theMostTopPx == null) {
                theMostTopPx = p;
            } else {
                if (p.y < theMostTopPx.y) {
                    theMostTopPx = p;
                }
            }

            if (theMostLeftPx == null) {
                theMostLeftPx = p;
            } else {
                if (p.x < theMostLeftPx.x) {
                    theMostLeftPx = p;
                }
            }

            //image.set(p.x, p.y , 0xff000000);
        }

        Image.Pixel theMostLeftTopPx = null;

        for (Image.Pixel p : maxSet.mSet) {
            if (p.x - theMostLeftPx.x <= 2) {
                if (theMostLeftTopPx == null) {
                    theMostLeftTopPx = p;
                } else {
                    if (p.y < theMostLeftTopPx.y) {
                        theMostLeftTopPx = p;
                    }
                }
            }
        }

//        image.set(theMostTopPx.x, theMostTopPx.y, 0xff000000);
//        image.set(theMostLeftTopPx.x, theMostLeftTopPx.y, 0xff000000);
//        image.set(theMostTopPx.x, theMostLeftTopPx.y, 0xff000000);

        Image.Pixel p = new Image.Pixel();
        p.x = theMostTopPx.x;
        p.y = theMostLeftTopPx.y;

        return p;
    }

    public static boolean isFirstBaseShadowColor = true;

    public static float start(String imagePath) {
        Image image = Image.load(imagePath);

        Image.Pixel pixel = null;

        CloseColor backgroundColor = new CloseColor(1);

        CloseColor backgroundShadowColor = new CloseColor(1);
        backgroundShadowColor.base(0xffb29594);

        if (isFirstBaseShadowColor) {
            for (int hx = 0; hx < image.height(); hx++) {
                Image.Pixel px = image.getPixel(0, hx);
                if (hx == 0) {
                    backgroundColor.base(px.rgb);
                } else {
                    if (backgroundColor.isClose(px.rgb)) {

                    } else {
                        backgroundShadowColor.base(px.rgb);
                        Log.i(LOG_TAG, "找到阴影色");
                    }
                }
            }
            isFirstBaseShadowColor = false;
        }

        CloseColor playerColor = new CloseColor(2);
        playerColor.base(0xff393848);
        playerColor.base(0xff8e84ad);

        java.util.List<NearPixelSet> playerSetList = new ArrayList();

        for (int hx = 0; hx < image.height(); hx++) {
            for (int wx = 0; wx < image.width(); wx++) {
                pixel = image.getPixel(wx, hx);

                if (wx == 0 && hx == 0) {
                    // 以左上角的颜色作为背景色的基准色
                    backgroundColor.base(pixel.rgb);
                } else {
                    if (backgroundColor.isClose(pixel.rgb) || backgroundShadowColor.isClose(pixel.rgb)) {
                        // 如果与背景色相近，将所有背景色都设置为红色
                        image.set(wx, hx, 0xffff0000);
                    } else if (playerColor.isClose(pixel.rgb)) {
                        // 找到和棋子一样颜色的点，就放到点集合里面
                        image.set(wx, hx, 0xffffff00);
                        if (playerSetList.isEmpty()) {
                            NearPixelSet set = new NearPixelSet();
                            set.add(pixel);
                            playerSetList.add(set);
                        } else {
                            boolean foundOne = false;
                            for (NearPixelSet set : playerSetList) {
                                if (set.isNear(pixel)) {
                                    set.add(pixel);
                                    foundOne = true;
                                    break;
                                }
                            }

                            if (!foundOne) {
                                NearPixelSet set = new NearPixelSet();
                                set.add(pixel);
                                playerSetList.add(set);
                            }
                        }
                    } else {

                    }
                }

            }
        }

        NearPixelSet playerPxSet = null;
        for (NearPixelSet playerSet : playerSetList) {
            Log.i(LOG_TAG, "player set: " + playerSet.mSet.size() + "");

            if (playerPxSet == null) {
                playerPxSet = playerSet;
            } else {
                if (playerSet.mSet.size() > playerPxSet.mSet.size()) {
                    playerPxSet = playerSet;
                }
            }
        }
        // 棋子的矩形区域
        Rect rect = playerPxSet.getBoundRect();

        int playerX = (rect.left + rect.right) / 2;
        int playerY = rect.bottom;

        boolean isPlayerInLeft = false;
        if (playerX > image.width() / 2) {
            Log.i(LOG_TAG, "在右边");
            isPlayerInLeft = false;
        } else {
            Log.i(LOG_TAG, "在左边");
            isPlayerInLeft = true;
        }

        int wxStart = 0;
        int wxEnd = 0;
        if (isPlayerInLeft) {
            wxStart = image.width() / 2;
            wxEnd = image.width();
        } else {
            wxStart = 0;
            wxEnd = image.width() / 2;
        }

        NearPixelSet maxSet = findMaxSet(image, wxStart, wxEnd, playerY);

        Log.i(LOG_TAG, "max set: " + maxSet.mSet.size());

        Image.Pixel nextCenter = findNextCenter(maxSet);

        image.getImage().getGraphics().setColor(Color.BLACK);
        image.getImage().getGraphics().drawOval(nextCenter.x, nextCenter.y, 10, 10);
        image.getImage().getGraphics().drawLine(playerX, playerY, nextCenter.x, nextCenter.y);

        String time = DateTimes.timestamp2string("_yyyy_MM_dd_HH_mm_ss", System.currentTimeMillis());

        image.saveToFile(time + ".png");

        float distance = (float) Math.sqrt(Math.pow(playerX - nextCenter.x, 2) + Math.pow(rect.bottom - nextCenter.y, 2));

        //UI.showImage(image.getImage());

        return distance;
    }

    public static NearPixelSet findMaxSet(Image image, int wxStart, int wxEnd, int wyEnd) {
        java.util.List<NearPixelSet> nearPixelSetList = new ArrayList<NearPixelSet>();
        NearPixelSet temp = null;

        Image.Pixel pixel;
        for (int hx = 0; hx < wyEnd; hx += 2) {
            for (int wx = wxStart; wx < wxEnd; wx += 2) {
                pixel = image.getPixel(wx, hx);

                if (pixel.rgb == 0xffff0000) {

                } else {
                    if (temp == null) {
                        temp = new NearPixelSet();
                        temp.add(pixel);
                        nearPixelSetList.add(temp);
                    } else {
                        boolean nearSomething = false;
                        for (NearPixelSet set : nearPixelSetList) {
                            if (set.isNear(pixel)) {
                                //Log.i(LOG_TAG, "找到了一个集合，加入它 " + pixel.toString());
                                set.add(pixel);
                                nearSomething = true;
                                break;
                            }
                        }

                        if (!nearSomething) {
                            //Log.i(LOG_TAG, "没有任何相近的集合，新增一个集合");
                            temp = new NearPixelSet();
                            temp.add(pixel);
                            nearPixelSetList.add(temp);
                        }
                    }
                }
            }
        }

        Log.i(LOG_TAG, "找到: " + nearPixelSetList.size() + "");

        int x = 0;

        NearPixelSet maxSet = null;

        for (NearPixelSet set : nearPixelSetList) {
            Log.i(LOG_TAG, set.mSet.size() + "");

            if (maxSet == null) {
                maxSet = set;
            } else {
                if (set.mSet.size() > maxSet.mSet.size()) {
                    maxSet = set;
                }
            }

            for (Image.Pixel p : set.mSet) {
                int color = 0;
                switch (x) {
                    case 0:
                        color = 0xffffff00;
                        break;
                    case 1:
                        color = 0xff00ff00;
                        break;
                    case 2:
                        color = 0xff0000ff;
                        break;
                }
                //image.set(p.x, p.y, color);
            }

            switch (x) {
                case 0:
                    x = 1;
                    break;
                case 1:
                    x = 2;
                    break;
                case 2:
                    x = 0;
                    break;
            }
        }

        return maxSet;
    }
}
