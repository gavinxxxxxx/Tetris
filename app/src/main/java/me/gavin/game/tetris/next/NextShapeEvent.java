package me.gavin.game.tetris.next;

import me.gavin.game.tetris.core.shape.Shape;

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
