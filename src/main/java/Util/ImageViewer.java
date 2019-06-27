package Util;

import Tests.Property;
import org.bytedeco.javacv.CanvasFrame;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class ImageViewer implements Serializable {
    static transient private CanvasFrame canvas;
    static transient private String title;
    public ImageViewer(String title) {
        this.title = title;
        canvas = new CanvasFrame(title);//新建一个窗口
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void showImage(int[] data) {
        BufferedImage image = new BufferedImage(Property.WIDTH, Property.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, Property.WIDTH, Property.HEIGHT, data, 0, Property.WIDTH);

        canvas.showImage(image);
    }

    public void showImage(String[] data) {
        int[] ints = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            ints[i] = Integer.parseInt(data[i]);
        }
        showImage(ints);
    }

    public void updateInfo(int delay) {
        canvas.setTitle(title + "\tping:" + delay + "ms");
    }

}
