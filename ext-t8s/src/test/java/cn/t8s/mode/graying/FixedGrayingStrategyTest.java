package cn.t8s.mode.graying;

import org.junit.Assert;
import org.junit.Test;

public class FixedGrayingStrategyTest {

    @Test
    public void test_getGraynessValue_fixedR() {
        // given
        int red = 67, green = 255, blue = 184;

        // when
        FixedGrayingStrategy strategy = new FixedGrayingStrategy(FixedGrayingStrategy.FixedOption.R);
        int graynessValue = strategy.getGraynessValue(red, green, blue);

        // then
        Assert.assertEquals(red, graynessValue);
    }

    @Test
    public void test_getGraynessValue_fixedG() {
        // given
        int red = 67, green = 255, blue = 184;

        // when
        FixedGrayingStrategy strategy = new FixedGrayingStrategy(FixedGrayingStrategy.FixedOption.G);
        int graynessValue = strategy.getGraynessValue(red, green, blue);

        // then
        Assert.assertEquals(green, graynessValue);
    }

    @Test
    public void test_getGraynessValue_fixedB() {
        // given
        int red = 67, green = 255, blue = 184;

        // when
        FixedGrayingStrategy strategy = new FixedGrayingStrategy(FixedGrayingStrategy.FixedOption.B);
        int graynessValue = strategy.getGraynessValue(red, green, blue);

        // then
        Assert.assertEquals(blue, graynessValue);
    }

}