package cn.t8s.mode.bina;

import cn.core.ex.InvalidSettingException;
import cn.core.strategy.mode.AbstractGrayingStrategy;
import cn.core.utils.BufferedImageUtils;
import cn.t8s.BufferedImageComparer;
import cn.t8s.ReflectionUtils;
import cn.t8s.TestUtils;
import cn.t8s.mode.graying.AvgGrayingStrategy;
import cn.t8s.mode.graying.FixedGrayingStrategy;
import org.junit.Assert;
import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SimpleBinaryStrategyTest {

    @Test
    public void test_binaryImage() throws IOException {
        // given
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");
        AbstractGrayingStrategy grayingStrategy = new FixedGrayingStrategy();
        int threshold = 130;
        // do gray first
        BufferedImage target = BufferedImageUtils.copy(source, source.getType());
        SimpleBinaryStrategy binaryStrategy = new SimpleBinaryStrategy.Builder()
                .grayingStrategy(grayingStrategy)
                .threshold(threshold)
                .build();
        grayingStrategy.execute(target);

        // when
        binaryStrategy.binaryImage(target);

        // then
        BufferedImage image = BufferedImageUtils.copy(source, source.getType());
        grayingStrategy.execute(image); // do gray first
        int w = image.getWidth(), h = image.getHeight();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {

                int currentGraynessValue = image.getRGB(x, y) & 0xff;
                int binaryValue = currentGraynessValue > threshold ?
                        SimpleBinaryStrategy.BINARY_MAX :
                        SimpleBinaryStrategy.BINARY_MIN;

                int binaryRgb = (binaryValue << 16) | (binaryValue << 8) | binaryValue;
                image.setRGB(x, y, binaryRgb);
            }
        }

        Assert.assertTrue(BufferedImageComparer.isSame(image, target));
    }

    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            AbstractGrayingStrategy grayingStrategy = new FixedGrayingStrategy();
            int threshold = 130;

            // when
            SimpleBinaryStrategy strategy = new SimpleBinaryStrategy.Builder()
                    .grayingStrategy(grayingStrategy)
                    .threshold(threshold)
                    .build();

            // then
            Assert.assertEquals(grayingStrategy, ReflectionUtils.getFromSuper("grayingStrategy", strategy));
            Assert.assertEquals(threshold, ReflectionUtils.get("threshold", strategy));
        }

        @Test
        public void test_build_defaultParam() {
            // when
            SimpleBinaryStrategy strategy = new SimpleBinaryStrategy.Builder().build();

            // then
            Assert.assertEquals(128, ReflectionUtils.get("threshold", strategy));
            Object grayingStrategy = ReflectionUtils.getFromSuper("grayingStrategy", strategy);
            Assert.assertTrue(grayingStrategy instanceof AvgGrayingStrategy);
        }

        @Test
        public void testEx_build_withInvalidParam() {
            SimpleBinaryStrategy.Builder builder = new SimpleBinaryStrategy.Builder()
                    .threshold(260);
            InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                    builder::build);
            Assert.assertEquals("The threshold out of bounds:[0, 255].",
                    ex.getMessage());
        }
    }

}