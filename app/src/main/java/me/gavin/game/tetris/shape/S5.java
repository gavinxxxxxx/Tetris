package me.gavin.game.tetris.shape;

import android.graphics.Point;

/**
 * S5
 *
 * @author gavin.xiong 2017/10/12
 */
public class S5 extends Shape {

    public S5(int morphological) {
        super(morphological);
        this.type = TYPE_5_S;
        this.pointCount = 5;
        this.points = new Point[pointCount];
        if (morphological == 0) {
            points[0] = new Point(6, -3);
            points[1] = new Point(5, -3);
            points[2] = new Point(5, -2);
            points[3] = new Point(5, -1);
            points[4] = new Point(4, -1);
        } else if (morphological == 1) {
            points[0] = new Point(4, -3);
            points[1] = new Point(4, -2);
            points[2] = new Point(5, -2);
            points[3] = new Point(6, -2);
            points[4] = new Point(6, -1);
        }
    }

    @Override
    public void rotate(Point[] points, boolean isPre) {
        if (morphological == 0) {
            if (!isPre) {
                morphological++;
            }
            points[0].offset(-2, 0);
            points[1].offset(-1, 1);
            points[2].offset(0, 0);
            points[3].offset(1, -1);
            points[4].offset(2, 0);
        } else if (morphological == 1) {
            if (!isPre) {
                morphological--;
            }
            points[0].offset(2, 0);
            points[1].offset(1, -1);
            points[2].offset(0, 0);
            points[3].offset(-1, 1);
            points[4].offset(-2, 0);
        }
    }

}
