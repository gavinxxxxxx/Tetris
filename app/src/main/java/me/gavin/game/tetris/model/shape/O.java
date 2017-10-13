package me.gavin.game.tetris.model.shape;

import android.graphics.Point;

/**
 * O
 *
 * @author gavin.xiong 2017/10/12
 */
public class O extends Shape {

    public O() {
        super(0);
        this.pointCount = 4;
        this.points = new Point[pointCount];
        points[0] = new Point(4, -1);
        points[1] = new Point(5, -1);
        points[2] = new Point(4, 0);
        points[3] = new Point(5, 0);
    }

    @Override
    public void rotate(Point[] points, boolean isPre) {
        // do nothing
    }

}
