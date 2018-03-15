package util;

import kross.util.Files;
import sun.net.www.protocol.file.FileURLConnection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by krosshuang on 2018/3/12.
 */
public class Image {

    public static class Pixel {
        public int x;
        public int y;
        public int rgb;

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    public BufferedImage getImage() {
        return mBufferedImage;
    }

    private BufferedImage mBufferedImage;

    private Image() {

    }

    public static Image load(String filePath) {
        Image image = new Image();
        File file = new File(filePath);

        try {
            image.mBufferedImage = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }

    public Pixel getPixel(int x, int y) {
        Pixel p = new Pixel();
        p.x = x;
        p.y = y;
        p.rgb = mBufferedImage.getRGB(x, y);
        return p;
    }

    public void set(int x, int y, int color) {
        mBufferedImage.setRGB(x, y, color);
    }

    public int width() {
        return mBufferedImage.getWidth();
    }

    public int height() {
        return mBufferedImage.getHeight();
    }

    public void saveToFile(String filePath) {
        File outputfile = new File(filePath);
        try {
            ImageIO.write(mBufferedImage, Files.getSuffixName(filePath), outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
