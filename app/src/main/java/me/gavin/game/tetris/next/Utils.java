package me.gavin.game.tetris.next;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import me.gavin.game.tetris.shape.I;
import me.gavin.game.tetris.shape.J;
import me.gavin.game.tetris.shape.L;
import me.gavin.game.tetris.shape.O;
import me.gavin.game.tetris.shape.S;
import me.gavin.game.tetris.shape.S5;
import me.gavin.game.tetris.shape.Shape;
import me.gavin.game.tetris.shape.T;
import me.gavin.game.tetris.shape.T5;
import me.gavin.game.tetris.shape.U5;
import me.gavin.game.tetris.shape.V5;
import me.gavin.game.tetris.shape.X5;
import me.gavin.game.tetris.shape.Z;
import me.gavin.game.tetris.shape.Z5;

/**
 * Utils
 *
 * @author gavin.xiong 2017/10/12
 */
public class Utils {

    private static final String[] TYPES = {
            "I4,J4,L4,O4,S4,T4,Z4",
            "S5,T5,U5,V5,X5,Z5",
    };

    private static Random random = new Random(System.nanoTime());

    private static List<String> LIMITS;

    public static void resetLimit(@Nullable Set<String> set) {
        LIMITS = new ArrayList<>();
        if (set == null) {
            LIMITS.addAll(Arrays.asList(TYPES[0].split(",")));
        } else {
            if (set.isEmpty() || set.contains("4.0")) {
                LIMITS.addAll(Arrays.asList(TYPES[0].split(",")));
            }
            if (set.contains("5.0")) {
                LIMITS.addAll(Arrays.asList(TYPES[1].split(",")));
            }
        }
    }

    public static Shape nextShape() {
        String type = LIMITS.get(random.nextInt(LIMITS.size()));
        switch (type) {
            case Shape.TYPE_4_I:
                return new I(0);
            case Shape.TYPE_4_J:
                return new J(0);
            case Shape.TYPE_4_L:
                return new L(0);
            case Shape.TYPE_4_O:
                return new O(0);
            case Shape.TYPE_4_S:
                return new S(0);
            case Shape.TYPE_4_T:
                return new T(0);
            case Shape.TYPE_4_Z:
                return new Z(0);

            case Shape.TYPE_5_S:
                return new S5(0);
            case Shape.TYPE_5_T:
                return new T5(0);
            case Shape.TYPE_5_U:
                return new U5(0);
            case Shape.TYPE_5_V:
                return new V5(0);
            case Shape.TYPE_5_X:
                return new X5(0);
            case Shape.TYPE_5_Z:
                return new Z5(0);

            default:
                return new O(0);
        }
    }

    public static Shape fromShape(Shape src) {
        Shape shape = null;
        switch (src.type) {
            case Shape.TYPE_4_I:
                shape = new I(src.morphological);
                break;
            case Shape.TYPE_4_J:
                shape = new J(src.morphological);
                break;
            case Shape.TYPE_4_L:
                shape = new L(src.morphological);
                break;
            case Shape.TYPE_4_O:
                shape = new O(src.morphological);
                break;
            case Shape.TYPE_4_S:
                shape = new S(src.morphological);
                break;
            case Shape.TYPE_4_T:
                shape = new T(src.morphological);
                break;
            case Shape.TYPE_4_Z:
                shape = new Z(src.morphological);
                break;

            case Shape.TYPE_5_S:
                shape = new S5(src.morphological);
                break;
            case Shape.TYPE_5_T:
                shape = new T5(src.morphological);
                break;
            case Shape.TYPE_5_U:
                shape = new U5(src.morphological);
                break;
            case Shape.TYPE_5_V:
                shape = new V5(src.morphological);
                break;
            case Shape.TYPE_5_X:
                shape = new X5(src.morphological);
                break;
            case Shape.TYPE_5_Z:
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
