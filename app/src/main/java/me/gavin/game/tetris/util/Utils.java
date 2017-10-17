package me.gavin.game.tetris.util;

import me.gavin.game.tetris.shape.I;
import me.gavin.game.tetris.shape.J;
import me.gavin.game.tetris.shape.L;
import me.gavin.game.tetris.shape.O;
import me.gavin.game.tetris.shape.S;
import me.gavin.game.tetris.shape.Shape;
import me.gavin.game.tetris.shape.T;
import me.gavin.game.tetris.shape.Z;

/**
 * Utils
 *
 * @author gavin.xiong 2017/10/12
 */
public class Utils {

    public static Shape nextShape() {
        int type = (int) (Math.random() * 7);
        switch (type) {
            case 0:
                return new I(0);
            case 1:
                return new J(0);
            case 2:
                return new L(0);
            case 3:
                return new O();
            case 4:
                return new S(0);
            case 5:
                return new T(0);
            case 6:
                return new Z(0);
            default:
                return new O();
        }
    }
}
