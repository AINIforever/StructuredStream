package Util;

import Tests.Property;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageProcessUtil {
    private static int[] getWatermark() {
        int[] result = new int[Property.HEIGHT * Property.WIDTH];
        try {
            BufferedImage image = ImageIO.read(new File("watermark.png"));

            result = image.getRGB(0, 0, Property.WIDTH, Property.HEIGHT, result, 0, Property.WIDTH);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }

    /*

     */
    public static String reverse(String cstr) {
        try {
            int c = Integer.parseInt(cstr);
            int a = c & 0xFF000000;
            int r = (c >> 16) & 0xFF;
            int g = (c >> 8) & 0xFF;
            int b = c & 0xFF;
            return "" + (a | ((255 - r) << 16) | ((255 - g) << 8) | (255 - b));
        } catch (Exception e) {
            System.out.println("cannot parse " + cstr + " to int color!");
            e.printStackTrace();
            return "" + 0xff000000;
        }
    }

    public static String[] reverse(String[] cstr) {
        for (int i = 0; i < cstr.length; i++) {
            cstr[i] = reverse(cstr[i]);
        }
        return cstr;
    }

    /**
     * 转化为灰度图
     */
    public static String toGray(String cstr) {
        try {
            int c = Integer.parseInt(cstr);
            int a = c & 0xFF000000;
            int r = (c >> 16) & 0xFF;
            int g = (c >> 8) & 0xFF;
            int b = c & 0xFF;
            int gray = (r + g + b) / 3;
            return "" + (a | (gray << 16) | (gray << 8) | gray);
        } catch (Exception e) {
            System.out.println("cannot parse " + cstr + " to int color!");
            e.printStackTrace();
            return "" + 0xff000000;
        }

    }

    public static String[] toGray(String[] cstr) {
        for (int i = 0; i < cstr.length; i++) {
            cstr[i] = toGray(cstr[i]);
        }
        return cstr;
    }

    /**
     * 转化为黑白色图
     */
    public static void toBnW(byte[] image) {
        try {
            for(int i = 0; i < image.length-3; i+=3)
            {
                int r = image[i] & 0xFF000000;
                int g = image[i+1] & 0xFF000000;
                int b = image[i+2] & 0xFF000000;
                if (r + g + b > 300) {
                    image[i] = 0xffffffff;
                    image[i+1] = 0xffffffff;
                    image[i+2] = 0xffffffff;
                } else {
                    image[i] = (byte)0xff000000;
                    image[i+1] = (byte)0xff000000;
                    image[i+2] = (byte)0xff000000;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 降低 R 值
     *
     * @param cstr
     * @return
     */
    private static String whiter(String cstr) {
        try {
            int c = Integer.parseInt(cstr);
            int a = c & 0xFF000000;
            int r = (c >> 16) & 0xFF;
            int g = (c >> 8) & 0xFF;
            int b = c & 0xFF;
            int newR = r > 100 ? r - 100 : r;
            return "" + (a | (newR << 16) | (g << 8) | b);
        } catch (Exception e) {
            System.out.println("cannot parse " + cstr + " to int color!");
            e.printStackTrace();
            return "" + 0xff000000;
        }
    }

    public static String[] whiter(String[] cstr) {
        for (int i = 0; i < cstr.length; i++) {
            cstr[i] = whiter(cstr[i]);
        }
        return cstr;
    }

    /**
     * 添加水印
     */
    public static String[] watermark(String cstr[]) {
        int[] watermark = getWatermark();
        String[] result = cstr;
        try {
            for (int i = 0; i < Property.Pixels; i++) {
                if (watermark[i] != 0xffffff) {
                    result[i] = "" + watermark[i];
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return cstr;
        }
    }

    private static int min(int a, int b, int c) {
        if (a > b)
            a = b;
        if (a > c)
            a = c;
        return a;
    }

    private static int max(int a, int b, int c) {
        if (a < b)
            a = b;
        if (a < c)
            a = c;
        return a;
    }

    private static boolean isSkin(int r, int g, int b) {

        if (r > 95 && g > 40 && b > 20 && r > g && r > b && (max(r, g, b) - min(r, g, b) > 15) && Math.abs(r - g) > 15) {
            return true;
        } else {
            return false;
        }
    }

    public static String[] Brighter(String[] cstr) {
        int[] values = new int[cstr.length];
        int max = 0xff000000;
        int current;
        for (int i = 0; i < cstr.length; i++) {
            current = Integer.parseInt(cstr[i]);
            values[i] = current;
            if (current > max) {
                max = current;
            }
        }
        float rate = 255 / max;
        for (int i = 0; i < values.length; i++) {
            int c = values[i];
            int a = c & 0xFF000000;
            int r = (c >> 16) & 0xFF;
            int g = (c >> 8) & 0xFF;
            int b = c & 0xFF;
            // 做人体检测
//            if (isSkin(r, g, b)) {
//                cstr[i] = ""+0xffffffff;
//                cstr[i] = "" + (a | (r + 100) << 16 | (g + 100) << 8 | (b + 50));
//            }
            // 2个方向进展
//            if (r + g + b > 300) {
//                cstr[i] = "" + (a | (r > 235 ? r : r + 20) << 16 | (g > 235 ? g : g + 20) << 8 | (b > 235 ? b : b + 20));
//            } else {
//                cstr[i] = "" + (a | (r < 20 ? r : r - 20) << 16 | (g < 20 ? g : g - 20) << 8 | (b < 20 ? b : b - 20));
//            }
            cstr[i] = "" + (a | (int)(r * rate) << 16 | (int)(g * rate) << 8 | (int)(b * rate));
        }
        return cstr;
    }
}
