package cn.t8s.filter;

import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.t8s.BufferedImageComparer;
import cn.t8s.ReflectionUtils;
import cn.t8s.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HighQualityExpandHandlerTest {

    @Test
    public void test_apply_correctUsage() throws IOException {
        // given
        int finalWidth = 500;
        int finalHeight = 318; // (500 / 154) * 98
        HighQualityExpandHandler handler = new HighQualityExpandHandler.Builder()
                .keepAspectRatio(true)
                .finalWidth(finalWidth)
                .build();
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");

        // when
        BufferedImage target = handler.apply(source);

        // then
        Assert.assertEquals(finalWidth, target.getWidth());
        Assert.assertEquals(finalHeight, target.getHeight());

        // and then
        Image tmp = source.getScaledInstance(finalWidth, finalHeight, Image.SCALE_FAST);
        BufferedImage image = new BufferedImage(tmp.getWidth(null), tmp.getHeight(null), source.getType());
        Graphics g = image.getGraphics();
        g.drawImage(tmp, 0, 0, null);
        g.dispose();
        Assert.assertTrue(BufferedImageComparer.isSame(image, target));
    }

    @Test
    public void testEx_apply_nullImage() {
        HighQualityExpandHandler handler = new HighQualityExpandHandler.Builder()
                .keepAspectRatio(true)
                .finalWidth(500)
                .build();

        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> handler.apply(null));
        Assert.assertEquals("Original image is null.", ex.getMessage());
    }

    @Test
    public void testEx_apply_invalidFinalSize() throws IOException {
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");

        // invalid final width
        HighQualityExpandHandler invalidWidthHandler = new HighQualityExpandHandler.Builder()
                .keepAspectRatio(false)
                .finalWidth(85)
                .finalHeight(160)
                .build();

        HandlingException ex1 = Assert.assertThrows(HandlingException.class,
                () -> invalidWidthHandler.apply(source));
        Assert.assertEquals("The final width(85) is less than the original width(154).",
                ex1.getMessage());

        // when not keep aspect ratio
        HighQualityExpandHandler invalidHeightHandler = new HighQualityExpandHandler.Builder()
                .keepAspectRatio(false)
                .finalWidth(200)
                .finalHeight(45)
                .build();

        HandlingException ex2 = Assert.assertThrows(HandlingException.class,
                () -> invalidHeightHandler.apply(source));
        Assert.assertEquals("The final height(45) is less than the original height(98).",
                ex2.getMessage());
    }

    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            int fw = 200;
            int fh = 150;

            // when
            HighQualityExpandHandler handler = new HighQualityExpandHandler.Builder()
                    .keepAspectRatio(false)
                    .finalWidth(fw)
                    .finalHeight(fh)
                    .build();

            // then
            Assert.assertFalse((Boolean) ReflectionUtils.get("keepAspectRatio", handler));
            Assert.assertEquals(fw, ReflectionUtils.get("finalWidth", handler));
            Assert.assertEquals(fh, ReflectionUtils.get("finalHeight", handler));
        }

        @Test
        public void testEx_build_invalidParam() {
            // final width less than 0
            HighQualityExpandHandler.Builder builder1 = new HighQualityExpandHandler.Builder()
                    .keepAspectRatio(false)
                    .finalWidth(-100)
                    .finalHeight(50);
            InvalidSettingException e1 = Assert.assertThrows(InvalidSettingException.class,
                    builder1::build);
            Assert.assertEquals("The final width after expanded must greater than 0.", e1.getMessage());

            // final width less than 0
            HighQualityExpandHandler.Builder builder2 = new HighQualityExpandHandler.Builder()
                    .keepAspectRatio(false)
                    .finalWidth(100)
                    .finalHeight(-50);
            InvalidSettingException e2 = Assert.assertThrows(InvalidSettingException.class,
                    builder2::build);
            Assert.assertEquals("The final height after expanded must greater than 0.", e2.getMessage());
        }

        @Test
        public void testEx_build_missingParam() {
            // missing final width
            HighQualityExpandHandler.Builder builder1 = new HighQualityExpandHandler.Builder()
                    .keepAspectRatio(false)
                    .finalWidth(50);
            InvalidSettingException e1 = Assert.assertThrows(InvalidSettingException.class,
                    builder1::build);
            Assert.assertEquals("Both of dimensions should be set when expected not to keep the aspect ratio.", e1.getMessage());

            // missing final height
            HighQualityExpandHandler.Builder builder2 = new HighQualityExpandHandler.Builder()
                    .keepAspectRatio(false)
                    .finalHeight(50);
            InvalidSettingException e2 = Assert.assertThrows(InvalidSettingException.class,
                    builder2::build);
            Assert.assertEquals("Both of dimensions should be set when expected not to keep the aspect ratio.", e2.getMessage());

            // missing all
            HighQualityExpandHandler.Builder builder3 = new HighQualityExpandHandler.Builder();
            InvalidSettingException e3 = Assert.assertThrows(InvalidSettingException.class,
                    builder3::build);
            Assert.assertEquals("At least one dimension should be set when expected not to keep the aspect ratio.", e3.getMessage());

        }
    }

}