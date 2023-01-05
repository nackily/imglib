package cn.t8s.mode.graying;

import cn.core.ex.InvalidSettingException;
import cn.t8s.ReflectionUtils;
import org.junit.Assert;
import org.junit.Test;

public class WeightGrayingStrategyTest {

    @Test
    public void test_getGraynessValue() {
        // given
        int red = 67, green = 255, blue = 184;
        double redWeight = 0.35, greenWeight = 0.25;

        // when
        WeightGrayingStrategy strategy = new WeightGrayingStrategy.Builder()
                .redWeight(redWeight)
                .greenWeight(greenWeight)
                .build();
        int graynessValue = strategy.getGraynessValue(red, green, blue);

        // then
        double blueWeight = 1.0 - redWeight - greenWeight;
        double val = (red * redWeight) + (green * greenWeight) + (blue * blueWeight);

        Assert.assertEquals((int) val, graynessValue);
    }

    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            double redWeight = 0.35, greenWeight = 0.25;

            // when
            WeightGrayingStrategy strategy = new WeightGrayingStrategy.Builder()
                    .redWeight(redWeight)
                    .greenWeight(greenWeight)
                    .build();

            // then
            Assert.assertEquals(redWeight, ReflectionUtils.get("redWeight", strategy));
            Assert.assertEquals(greenWeight, ReflectionUtils.get("greenWeight", strategy));
        }

        @Test
        public void test_build_defaultParam() {
            // when
            WeightGrayingStrategy strategy = new WeightGrayingStrategy.Builder()
                    .build();

            // then
            Assert.assertEquals(0.3D, ReflectionUtils.get("redWeight", strategy));
            Assert.assertEquals(0.59D, ReflectionUtils.get("greenWeight", strategy));
        }

        @Test
        public void testEx_build_nullParam() {
            // null red weight
            WeightGrayingStrategy.Builder builder1 = new WeightGrayingStrategy.Builder()
                    .greenWeight(0.5);
            InvalidSettingException e1 = Assert.assertThrows(InvalidSettingException.class,
                    builder1::build);
            Assert.assertEquals("Red weight not set.", e1.getMessage());

            // null green weight
            WeightGrayingStrategy.Builder builder2 = new WeightGrayingStrategy.Builder()
                    .redWeight(0.5);
            InvalidSettingException e2 = Assert.assertThrows(InvalidSettingException.class,
                    builder2::build);
            Assert.assertEquals("Green weight not set.", e2.getMessage());
        }

        @Test
        public void testEx_build_invalidParam() {
            // red weight out of bound
            WeightGrayingStrategy.Builder builder1 = new WeightGrayingStrategy.Builder()
                    .redWeight(1.5)
                    .greenWeight(0.2);
            InvalidSettingException e1 = Assert.assertThrows(InvalidSettingException.class,
                    builder1::build);
            Assert.assertEquals("Red weight out of bounds:[0, 1].", e1.getMessage());

            // green weight out of bound
            WeightGrayingStrategy.Builder builder2 = new WeightGrayingStrategy.Builder()
                    .redWeight(0.5)
                    .greenWeight(1.2);
            InvalidSettingException e2 = Assert.assertThrows(InvalidSettingException.class,
                    builder2::build);
            Assert.assertEquals("Green weight out of bounds:[0, 1].", e2.getMessage());

            // the sum of the two weight is greater than 1
            WeightGrayingStrategy.Builder builder3 = new WeightGrayingStrategy.Builder()
                    .redWeight(0.5)
                    .greenWeight(0.51);
            InvalidSettingException e3 = Assert.assertThrows(InvalidSettingException.class,
                    builder3::build);
            Assert.assertEquals("The sum of red weight and green weight has exceeded 1.", e3.getMessage());
        }
    }
}