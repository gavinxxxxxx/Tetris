package me.gavin.game.tetris.core.shape;

import android.graphics.Point;

/**
 * I | J | L | O | S | T | Z
 *
 * @author gavin.xiong 2017/10/11
 */
public class Shape {

    public static final int TYPE_0_I = 0;
    public static final int TYPE_0_J = 1;
    public static final int TYPE_0_L = 2;
    public static final int TYPE_0_O = 3;
    public static final int TYPE_0_S = 4;
    public static final int TYPE_0_T = 5;
    public static final int TYPE_0_Z = 6;

    public int pointCount;
    public Point[] points;
    public Point[] prePoints;

    public int type;
    public int morphological; // 形态

    public int color;

    public static Shape fromShape(Shape src) {
        Shape shape = null;
        if (src.type == TYPE_0_I) {
            shape = new I(src.morphological);
        } else if (src.type == TYPE_0_J) {
            shape = new J(src.morphological);
        } else if (src.type == TYPE_0_L) {
            shape = new L(src.morphological);
        } else if (src.type == TYPE_0_O) {
            shape = new O();
        } else if (src.type == TYPE_0_S) {
            shape = new S(src.morphological);
        } else if (src.type == TYPE_0_T) {
            shape = new T(src.morphological);
        } else if (src.type == TYPE_0_Z) {
            shape = new Z(src.morphological);
        }
        if (shape != null) {
            shape.pointCount = src.pointCount;
            shape.points = src.points;
            shape.prePoints = src.prePoints;
            shape.type = src.type;
            shape.morphological = src.morphological;
            shape.color = src.color;
        }
        return shape;
    }

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

    public void rotate(Point[] points, boolean isPre) {
        // 重写
    }

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
