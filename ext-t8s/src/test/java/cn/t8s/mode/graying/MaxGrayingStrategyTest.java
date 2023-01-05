package cn.t8s.mode.graying;

import org.junit.Assert;
import org.junit.Test;

public class MaxGrayingStrategyTest {

    @Test
    public void test_getGraynessValue() {
        // given
        int red = 67, green = 255, blue = 184;

        // when
        MaxGrayingStrategy strategy = new MaxGrayingStrategy();
        int graynessValue = strategy.getGraynessValue(red, green, blue);

        // then
        Assert.assertEquals(255, graynessValue);
    }
}