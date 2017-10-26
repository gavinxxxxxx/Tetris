package me.gavin.game.tetris.core.shape;

import android.graphics.Point;

/**
 * 4:   I | J | L | O | S | T | Z
 * 5:   S | T | U | V | X | Z
 *
 * @author gavin.xiong 2017/10/11
 */
public class Shape {

    static final String TYPE_4_I = "I4";
    static final String TYPE_4_J = "J4";
    static final String TYPE_4_L = "L4";
    static final String TYPE_4_O = "O4";
    static final String TYPE_4_S = "S4";
    static final String TYPE_4_T = "T4";
    static final String TYPE_4_Z = "Z4";

    static final String TYPE_5_S = "S5";
    static final String TYPE_5_T = "T5";
    static final String TYPE_5_U = "U5";
    static final String TYPE_5_V = "V5";
    static final String TYPE_5_X = "X5";
    static final String TYPE_5_Z = "Z5";

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

    public static Shape fromShape(Shape src) {
        Shape shape = null;
        switch (src.type) {
            case TYPE_4_I:
                shape = new I(src.morphological);
                break;
            case TYPE_4_J:
                shape = new J(src.morphological);
                break;
            case TYPE_4_L:
                shape = new L(src.morphological);
                break;
            case TYPE_4_O:
                shape = new O(src.morphological);
                break;
            case TYPE_4_S:
                shape = new S(src.morphological);
                break;
            case TYPE_4_T:
                shape = new T(src.morphological);
                break;
            case TYPE_4_Z:
                shape = new Z(src.morphological);
                break;

            case TYPE_5_S:
                shape = new S5(src.morphological);
                break;
            case TYPE_5_T:
                shape = new T5(src.morphological);
                break;
            case TYPE_5_U:
                shape = new U5(src.morphological);
                break;
            case TYPE_5_V:
                shape = new V5(src.morphological);
                break;
            case TYPE_5_X:
                shape = new X5(src.morphological);
                break;
            case TYPE_5_Z:
                shape = new Z5(src.morphological);
                break;

            default:
                // 未知类型
                break;
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

}
