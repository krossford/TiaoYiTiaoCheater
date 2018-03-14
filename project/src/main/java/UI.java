import kross.devtool.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Created by krosshuang on 2018/3/12.
 */
public class UI extends JFrame {

    public static final String LOG_TAG = "UI";

    private ImageIcon mImageIcon;
    private ImageIcon mDisplayImageIcon;

    public UI(BufferedImage bi) {
        JPanel panel = new JPanel(new BorderLayout());

        mImageIcon = new ImageIcon(bi);

        System.out.println(mImageIcon.getIconWidth());
        System.out.println(mImageIcon.getIconHeight());

        mDisplayImageIcon = new ImageIcon(bi);

        final JLabel label = new JLabel(mDisplayImageIcon);

        panel.add(label, BorderLayout.CENTER);

        label.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getX() + ", " + e.getY());

                System.out.println(label.getWidth() + ": " + label.getHeight());
            }

            public void mousePressed(MouseEvent e) {

            }

            public void mouseReleased(MouseEvent e) {

            }

            public void mouseEntered(MouseEvent e) {

            }

            public void mouseExited(MouseEvent e) {

            }
        });

        label.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                Log.i(LOG_TAG, e.getComponent().getWidth() + " " + e.getComponent().getHeight());

                float controlWH = e.getComponent().getWidth() / e.getComponent().getHeight();
                float imageWH = 1f * mImageIcon.getIconWidth() / mImageIcon.getIconHeight();

                int targetImageWidth = 0;
                int targetImageHeight = 0;
                if (controlWH >= imageWH) {
                    targetImageHeight = e.getComponent().getHeight();
                    targetImageWidth = (int) (1f * targetImageHeight * imageWH);
                } else {
                    targetImageWidth = e.getComponent().getWidth();
                    targetImageHeight = (int) (targetImageWidth / imageWH);
                }

                java.awt.Image tempImage = mImageIcon.getImage().getScaledInstance(targetImageWidth, targetImageHeight, java.awt.Image.SCALE_FAST);
                mDisplayImageIcon.setImage(tempImage);
            }

            public void componentMoved(ComponentEvent e) {

            }

            public void componentShown(ComponentEvent e) {

            }

            public void componentHidden(ComponentEvent e) {

            }
        });

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(panel, BorderLayout.CENTER);

        this.setSize(400, 300);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("显示图像");
        this.setVisible(true);
    }

    public static void showImage(BufferedImage image) {
        UI ui = new UI(image);
    }
}
