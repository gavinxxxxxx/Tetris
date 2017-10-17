package me.gavin.game.tetris.region;

import me.gavin.game.tetris.shape.Shape;

/**
 * 下一个
 *
 * @author gavin.xiong 2017/10/17
 */
public class NextShapeEvent {

    public Shape shape;

    public NextShapeEvent(Shape shape) {
        this.shape = shape;
    }
}
