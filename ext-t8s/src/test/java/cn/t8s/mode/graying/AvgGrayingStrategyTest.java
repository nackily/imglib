package cn.t8s.mode.graying;

import org.junit.Assert;
import org.junit.Test;

public class AvgGrayingStrategyTest {

    @Test
    public void test_getGraynessValue() {
        // given
        int red = 67, green = 255, blue = 184;

        // when
        AvgGrayingStrategy strategy = new AvgGrayingStrategy();
        int graynessValue = strategy.getGraynessValue(red, green, blue);

        // then
        int avg = (67 + 255 + 184) / 3;
        Assert.assertEquals(avg, graynessValue);
    }
}
