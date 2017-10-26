package me.gavin.game.tetris.core.shape;

import android.graphics.Point;

/**
 * Z5
 *
 * @author gavin.xiong 2017/10/12
 */
public class Z5 extends Shape {

    public Z5(int morphological) {
        super(morphological);
        this.type = TYPE_5_Z;
        this.pointCount = 5;
        this.points = new Point[pointCount];
        if (morphological == 0) {
            points[0] = new Point(4, -2);
            points[1] = new Point(5, -2);
            points[2] = new Point(5, -1);
            points[3] = new Point(5, 0);
            points[4] = new Point(6, 0);
        } else if (morphological == 1) {
            points[0] = new Point(4, 0);
            points[1] = new Point(4, -1);
            points[2] = new Point(5, -1);
            points[3] = new Point(6, -1);
            points[4] = new Point(6, -2);
        }
    }

    @Override
    public void rotate(Point[] points, boolean isPre) {
        if (morphological == 0) {
            if (!isPre) {
                morphological++;
            }
            points[0].offset(0, 2);
            points[1].offset(-1, 1);
            points[2].offset(0, 0);
            points[3].offset(1, -1);
            points[4].offset(0, -2);
        } else if (morphological == 1) {
            if (!isPre) {
                morphological--;
            }
            points[0].offset(0, -2);
            points[1].offset(1, -1);
            points[2].offset(0, 0);
            points[3].offset(-1, 1);
            points[4].offset(0, 2);
        }
    }

}
