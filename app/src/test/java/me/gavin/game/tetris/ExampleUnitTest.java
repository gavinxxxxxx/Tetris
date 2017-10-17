package me.gavin.game.tetris;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void aaa() {
        int[] ds = {1, -3, -2, 3, 5, 100, 23, 2324, 43, 5654, 213, 432, 5435, 643, 211, 432, 643, 643};
        int maxValue = Integer.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < ds.length - 1; i++) {
            int temp = ds[i] + ds[i + 1];
            if (temp > maxValue) {
                maxValue = temp;
                index = i;
            }
        }
        System.out.print(ds[index] + " " + ds[index + 1]);
    }

    @Test
    public void bbb() {
        int[] is = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        System.arraycopy(is, 1, is, 0, 8);
        for (int i : is) {
            System.out.println(i);
        }
    }

    private void a() {
        if (flag) {
            b();
        }
    }

    boolean flag;

    private synchronized void b() {
        flag = true;

        flag = false;
    }

    @Test
    public void arrayCopy() {
        int[][] a = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},{9, 10, 11},{12, 13, 14}};

        System.out.println(a.length);

        System.arraycopy(a, 1, a, 0, a.length - 1);

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
    }

}