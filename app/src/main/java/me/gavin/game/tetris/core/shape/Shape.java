package me.gavin.game.tetris.core.shape;

import android.graphics.Point;

/**
 * I | J | L | O | S | T | Z
 *
 * @author gavin.xiong 2017/10/11
 */
public abstract class Shape {

    public int pointCount;
    public Point[] points;
    public Point[] prePoints;

    public int morphological; // 形态

    public int color;

    public Shape(int morphological) {
        this.morphological = morphological;
    }

    void initPre() {
        if (prePoints == null) {
            prePoints = new Point[pointCount];
            for (int i = 0; i < pointCount; i++) {
                prePoints[i] = new Point(points[i]);
            }
        } else {
            preBack();
        }
    }

    public void preRotate() {
        initPre();
        rotate(prePoints, true);
    }

    public void rotate() {
        rotate(points, false);
    }

    public abstract void rotate(Point[] points, boolean isPre);

    public void preMove(boolean isHorizontal, int diff) {
        initPre();
        for (Point point : prePoints) {
            if (isHorizontal) {
                point.x += diff;
            } else {
                point.y += diff;
            }
        }
    }

    public void move(boolean isHorizontal, int diff) {
        for (Point point : points) {
            if (isHorizontal) {
                point.x += diff;
            } else {
                point.y += diff;
            }
        }
    }

    public void preBack() {
        for (int i = 0; i < pointCount; i++) {
            prePoints[i].x = points[i].x;
            prePoints[i].y = points[i].y;
        }
    }

}
