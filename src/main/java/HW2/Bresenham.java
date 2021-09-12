package HW2;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Bresenham {

    public static void drawLine(Mat img, int xstart, int ystart, int xend, int yend) {
        int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;

        dx = xend - xstart;
        dy = yend - ystart;
        incx = Integer.compare(dx, 0);
        incy = Integer.compare(dy, 0);
        if (dx < 0) dx = -dx;
        if (dy < 0) dy = -dy;

        if (dx > dy) {
            pdx = incx;
            pdy = 0;
            es = dy;
            el = dx;
        } else {
            pdx = 0;
            pdy = incy;
            es = dx;
            el = dy;
        }

        x = xstart;
        y = ystart;
        err = el / 2;
        img.put(x, y, 255, 255, 255);

        for (int t = 0; t < el; t++) {
            err -= es;
            if (err < 0) {
                err += el;
                x += incx;
                y += incy;
            } else {
                x += pdx;
                y += pdy;
            }
            img.put(x, y, 255, 255, 255);
        }
    }

    public static void simpleLine(Mat img, int xstart, int ystart, int xend, int yend) {
        Imgproc.line(img,
                new Point(ystart, xstart),
                new Point(yend, xend),
                new Scalar(255, 255, 255),
                1
        );
    }

    public static void drawCircle(Mat img, int xstart, int ystart, int radius) {
        int x = 0;
        int y = radius;
        int p = 3 - 2 * radius;
        do {
            if (p < 0)
                p = p + 4 * x + 6;
             else {
                p = p + 4 * (x - y) + 10;
                y = y - 1;
            }
            x = x + 1;
            img.put(xstart + x, ystart + y, 255, 255, 255);
            img.put(xstart + x, ystart - y, 255, 255, 255);
            img.put(xstart - x, ystart + y, 255, 255, 255);
            img.put(xstart - x, ystart - y, 255, 255, 255);
            img.put(xstart + y, ystart + x, 255, 255, 255);
            img.put(xstart + y, ystart - x, 255, 255, 255);
            img.put(xstart - y, ystart + x, 255, 255, 255);
            img.put(xstart - y, ystart - x, 255, 255, 255);
        }
        while (x < y);
    }

    public static void simpleCircle(Mat img, int xstart, int ystart, int radius) {
        Imgproc.circle(img,
                new Point(xstart, ystart), radius,
                new Scalar(255, 255, 255), 1);
    }
}
