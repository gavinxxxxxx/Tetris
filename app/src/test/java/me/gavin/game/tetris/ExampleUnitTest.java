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
}