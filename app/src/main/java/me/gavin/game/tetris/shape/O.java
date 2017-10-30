package me.gavin.game.tetris.shape;

import android.graphics.Point;

/**
 * O
 *
 * @author gavin.xiong 2017/10/12
 */
public class O extends Shape {

    public O(int morphological) {
        super(morphological);
        this.type = TYPE_4_O;
        this.pointCount = 4;
        this.points = new Point[pointCount];
        points[0] = new Point(4, -2);
        points[1] = new Point(5, -2);
        points[2] = new Point(4, -1);
        points[3] = new Point(5, -1);
    }

    @Override
    public void rotate(Point[] points, boolean isPre) {
        // do nothing
    }

}
