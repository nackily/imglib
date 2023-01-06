package cn.pipe.captor;

import cn.core.ex.InvalidSettingException;
import cn.pipe.BufferedImageComparer;
import cn.pipe.ReflectionUtils;
import org.junit.Assert;
import org.junit.Test;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MonoColorImageGeneratorTest {

    @Test
    public void test_generate_correctUsage() {
        // given
        int w = 50, h = 100;
        Color c = Color.GRAY;
        float a = 0.8f;

        // when
        MonoColorImageGenerator generator = new MonoColorImageGenerator.Builder()
                .width(w).height(h)
                .color(c)
                .alpha(a)
                .build();
        BufferedImage target = generator.generate();

        // then
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(c);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, a));
        g.fillRect(0, 0, w, h);
        g.dispose();

        Assert.assertTrue(BufferedImageComparer.isSame(image, target));
    }

    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            int w = 50, h = 100;
            Color c = Color.GRAY;
            float a = 0.8f;

            // when
            MonoColorImageGenerator generator = new MonoColorImageGenerator.Builder()
                    .width(w).height(h)
                    .color(c)
                    .alpha(a)
                    .build();

            // then
            Assert.assertEquals(w, ReflectionUtils.getFromSuper("width", generator));
            Assert.assertEquals(h, ReflectionUtils.getFromSuper("height", generator));

            Assert.assertEquals(c, ReflectionUtils.get("color", generator));
            Assert.assertEquals(a, ReflectionUtils.get("alpha", generator));
        }

        @Test
        public void test_build_defaultParam() {
            // default alpha is 1.0
            MonoColorImageGenerator generator = new MonoColorImageGenerator.Builder()
                    .width(100).height(100)
                    .color(Color.GRAY)
                    .build();
            Assert.assertEquals(1.0f, ReflectionUtils.get("alpha", generator));
        }

        @Test
        public void testEx_build_withNullParam() {
            // null color
            MonoColorImageGenerator.Builder builder = new MonoColorImageGenerator.Builder()
                    .width(100).height(100)
                    .color(null);
            NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                    builder::build);
            Assert.assertEquals("No color specified.", ex.getMessage());
        }

        @Test
        public void testEx_build_withInvalidParam() {
            // alpha out of bound
            MonoColorImageGenerator.Builder builder = new MonoColorImageGenerator.Builder()
                    .width(100).height(100)
                    .color(Color.BLACK)
                    .alpha(1.2f);
            InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                    builder::build);
            Assert.assertEquals("Alpha out of bounds:[0, 1].",
                    ex.getMessage());
        }
    }

}