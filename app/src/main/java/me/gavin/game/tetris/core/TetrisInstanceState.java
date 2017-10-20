package me.gavin.game.tetris.core;

import me.gavin.game.tetris.core.shape.Shape;
import me.gavin.game.tetris.util.L;

/**
 * TetrisInstanceState
 *
 * @author gavin.xiong 2017/10/20
 */
class TetrisInstanceState {

    private Cell[][] cells;
    private Shape[] shapes;

    Cell[][] getCells() {
        return cells;
    }

    void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    Shape[] getShapes() {
        if (shapes != null) {
            for (int i = 0; i < shapes.length; i++) {
                if (shapes[i].getClass() == Shape.class) {
                    shapes[i] = Shape.fromShape(shapes[i]);
                }
            }
        }
        return shapes;
    }

    void setShapes(Shape[] shapes) {
        this.shapes = shapes;
        for (Shape shape : shapes) {
            L.e(shape.getClass());
        }
    }
}
