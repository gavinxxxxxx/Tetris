package me.gavin.game.tetris.shape;

import android.graphics.Point;

/**
 * X5
 *
 * @author gavin.xiong 2017/10/12
 */
public class X5 extends Shape {

    public X5(int morphological) {
        super(morphological);
        this.type = TYPE_5_X;
        this.pointCount = 5;
        this.points = new Point[pointCount];
        points[0] = new Point(6, -1);
        points[1] = new Point(5, -1);
        points[2] = new Point(4, -1);
        points[3] = new Point(5, -2);
        points[4] = new Point(5, 0);
    }

    @Override
    public void rotate(Point[] points, boolean isPre) {
        // do nothing
    }

}
