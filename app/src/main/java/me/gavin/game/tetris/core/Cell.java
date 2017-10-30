package me.gavin.game.tetris.core;

/**
 * 方格最小单位
 *
 * @author gavin.xiong 2017/10/12
 */
class Cell {

    boolean had;

    @Override
    public String toString() {
        return String.valueOf(had);
    }

//    static String toString(Cell[][] cell) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("---- start -----\n");
//        for (Cell[] cells : cell) {
//            for (Cell cell1 : cells) {
//                sb.append(cell1).append(" ");
//            }
//            sb.append("\n");
//        }
//        sb.append("---- end -----");
//        return sb.toString();
//    }
}
