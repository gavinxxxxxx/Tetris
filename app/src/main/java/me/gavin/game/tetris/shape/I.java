package me.gavin.game.tetris.shape;

import android.graphics.Point;

/**
 * I
 *
 * @author gavin.xiong 2017/10/12
 */
public class I extends Shape {

    public I(int morphological) {
        super(morphological);
        this.type = TYPE_4_I;
        this.pointCount = 4;
        this.points = new Point[pointCount];
        if (morphological == 0) {
            points[0] = new Point(5, -3);
            points[1] = new Point(5, -2);
            points[2] = new Point(5, -1);
            points[3] = new Point(5, 0);
        } else if (morphological == 1) {
            points[0] = new Point(3, 0);
            points[1] = new Point(4, 0);
            points[2] = new Point(5, 0);
            points[3] = new Point(6, 0);
        }
    }

    public void rotate(Point[] points, boolean isPre) {
        if (morphological == 0) {
            if (!isPre) {
                morphological++;
            }
            points[0].x -= 2;
            points[0].y += 2;
            points[1].x -= 1;
            points[1].y += 1;
            points[3].x += 1;
            points[3].y -= 1;
        } else if (morphological == 1) {
            if (!isPre) {
                morphological--;
            }
            points[0].x += 2;
            points[0].y -= 2;
            points[1].x += 1;
            points[1].y -= 1;
            points[3].x -= 1;
            points[3].y += 1;
        }
    }

}
