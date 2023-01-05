package cn.t8s.mode.graying;

import org.junit.Assert;
import org.junit.Test;


public class MinGrayingStrategyTest {

    @Test
    public void test_getGraynessValue() {
        // given
        int red = 67, green = 255, blue = 184;

        // when
        MinGrayingStrategy strategy = new MinGrayingStrategy();
        int graynessValue = strategy.getGraynessValue(red, green, blue);

        // then
        Assert.assertEquals(67, graynessValue);
    }

}