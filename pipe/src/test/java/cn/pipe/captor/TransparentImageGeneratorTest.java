package cn.pipe.captor;

import cn.core.ex.InvalidSettingException;
import cn.pipe.BufferedImageComparer;
import cn.pipe.ReflectionUtils;
import org.junit.Assert;
import org.junit.Test;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TransparentImageGeneratorTest {

    @Test
    public void test_generate_correctUsage() {
        // given
        int w = 50, h = 100;

        // when
        TransparentImageGenerator generator = new TransparentImageGenerator.Builder()
                .width(w)
                .height(h)
                .build();
        BufferedImage target = generator.generate();

        // then
        Assert.assertEquals(w, target.getWidth());
        Assert.assertEquals(h, target.getHeight());

        // and then
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        g.dispose();

        Assert.assertTrue(BufferedImageComparer.isSame(image, target));
    }

    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            int w = 60, h = 110;

            // when
            TransparentImageGenerator generator = new TransparentImageGenerator.Builder()
                    .width(w)
                    .height(h)
                    .build();

            // then
            Assert.assertEquals(w, ReflectionUtils.get("width", generator));
            Assert.assertEquals(h, ReflectionUtils.get("height", generator));
        }

        @Test
        public void testEx_build_invalidParam() {
            // invalid width
            TransparentImageGenerator.Builder builder1 = new TransparentImageGenerator.Builder()
                    .width(-10)
                    .height(50);
            InvalidSettingException ex1 = Assert.assertThrows(InvalidSettingException.class,
                    builder1::build);
            Assert.assertEquals("Invalid size[-10, 50].", ex1.getMessage());

            // invalid height
            TransparentImageGenerator.Builder builder2 = new TransparentImageGenerator.Builder()
                    .width(10)
                    .height(-50);
            InvalidSettingException ex2 = Assert.assertThrows(InvalidSettingException.class,
                    builder2::build);
            Assert.assertEquals("Invalid size[10, -50].", ex2.getMessage());
        }
    }

}