package me.gavin.game.tetris.shape;

import android.graphics.Point;

/**
 * 4:   I | J | L | O | S | T | Z
 * 5:   S | T | U | V | X | Z
 *
 * @author gavin.xiong 2017/10/11
 */
public class Shape {

    public static final String TYPE_4_I = "I4";
    public static final String TYPE_4_J = "J4";
    public static final String TYPE_4_L = "L4";
    public static final String TYPE_4_O = "O4";
    public static final String TYPE_4_S = "S4";
    public static final String TYPE_4_T = "T4";
    public static final String TYPE_4_Z = "Z4";

    public static final String TYPE_5_S = "S5";
    public static final String TYPE_5_T = "T5";
    public static final String TYPE_5_U = "U5";
    public static final String TYPE_5_V = "V5";
    public static final String TYPE_5_X = "X5";
    public static final String TYPE_5_Z = "Z5";

    public int pointCount;
    public Point[] points;
    public Point[] prePoints;

    public String type;
    public int morphological; // 形态

    public int color;

    public Shape(int morphological) {
        this.morphological = morphological;
    }

    private void initPre() {
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

    public void preMove(int dx, int dy) {
        initPre();
        for (Point point : prePoints) {
            point.offset(dx, dy);
        }
    }

    public void move(int dx, int dy) {
        for (Point point : points) {
            point.offset(dx, dy);
        }
    }

    public void preBack() {
        for (int i = 0; i < pointCount; i++) {
            prePoints[i].set(points[i].x, points[i].y);
        }
    }

}
