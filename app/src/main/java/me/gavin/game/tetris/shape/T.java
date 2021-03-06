package me.gavin.game.tetris.shape;

import android.graphics.Point;

/**
 * T
 *
 * @author gavin.xiong 2017/10/12
 */
public class T extends Shape {

    public T(int morphological) {
        super(morphological);
        this.type = TYPE_4_T;
        this.pointCount = 4;
        this.points = new Point[pointCount];
        if (morphological == 0) {
            points[0] = new Point(4, -2);
            points[1] = new Point(5, -2);
            points[2] = new Point(6, -2);
            points[3] = new Point(5, -1);
        } else if (morphological == 1) {
            points[2] = new Point(4, -1);
            points[1] = new Point(4, -2);
            points[0] = new Point(4, -3);
            points[3] = new Point(5, -2);
        } else if (morphological == 2) {
            points[2] = new Point(6, -1);
            points[1] = new Point(5, -1);
            points[0] = new Point(4, -1);
            points[3] = new Point(5, -2);
        } else if (morphological == 3) {
            points[0] = new Point(5, -3);
            points[1] = new Point(5, -2);
            points[2] = new Point(5, -1);
            points[3] = new Point(4, -2);
        }
    }

    @Override
    public void rotate(Point[] points, boolean isPre) {
        if (morphological == 0) {
            if (!isPre) {
                morphological++;
            }
            points[0].offset(1, 1);
            points[2].offset(-1, -1);
            points[3].offset(1, -1);
        } else if (morphological == 1) {
            if (!isPre) {
                morphological++;
            }
            points[0].offset(1, -1);
            points[2].offset(-1, 1);
            points[3].offset(-1, -1);
        } else if (morphological == 2) {
            if (!isPre) {
                morphological++;
            }
            points[0].offset(-1, -1);
            points[2].offset(1, 1);
            points[3].offset(-1, 1);
        } else if (morphological == 3) {
            if (!isPre) {
                morphological = 0;
            }
            points[0].offset(-1, 1);
            points[2].offset(1, -1);
            points[3].offset(1, 1);
        }
    }

}
