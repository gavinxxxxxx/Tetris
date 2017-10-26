package me.gavin.game.tetris.next;

import java.util.Random;

import me.gavin.game.tetris.core.shape.I;
import me.gavin.game.tetris.core.shape.J;
import me.gavin.game.tetris.core.shape.L;
import me.gavin.game.tetris.core.shape.O;
import me.gavin.game.tetris.core.shape.S;
import me.gavin.game.tetris.core.shape.S5;
import me.gavin.game.tetris.core.shape.Shape;
import me.gavin.game.tetris.core.shape.T;
import me.gavin.game.tetris.core.shape.T5;
import me.gavin.game.tetris.core.shape.U5;
import me.gavin.game.tetris.core.shape.V5;
import me.gavin.game.tetris.core.shape.X5;
import me.gavin.game.tetris.core.shape.Z;
import me.gavin.game.tetris.core.shape.Z5;

/**
 * Utils
 *
 * @author gavin.xiong 2017/10/12
 */
public class Utils {

    private static Random random = new Random(System.nanoTime());

    public static Shape nextShape() {
        switch (random.nextInt(13)) {
            case 0:
                return new I(0);
            case 1:
                return new J(0);
            case 2:
                return new L(0);
            case 3:
                return new O(0);
            case 4:
                return new S(0);
            case 5:
                return new T(0);
            case 6:
                return new Z(0);

            case 7:
                return new S5(0);
            case 8:
                return new T5(0);
            case 9:
                return new U5(0);
            case 10:
                return new V5(0);
            case 11:
                return new X5(0);
            case 12:
                return new Z5(0);

            default:
                return new O(0);
        }
    }
}
