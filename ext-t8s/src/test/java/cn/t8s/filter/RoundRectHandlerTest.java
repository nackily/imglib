package cn.t8s.filter;

import cn.core.ex.InvalidSettingException;
import cn.t8s.BufferedImageComparer;
import cn.t8s.ReflectionUtils;
import cn.t8s.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class RoundRectHandlerTest {

    @Test
    public void test_apply_correctUsage() throws IOException {
        // given
        RoundRectHandler handler = new RoundRectHandler.Builder()
                .arcHeight(50)
                .arcWidth(25)
                .build();
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");

        // when
        BufferedImage target = handler.apply(source);

        // then
        BufferedImage image = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillRoundRect(0, 0, image.getWidth(), image.getHeight(), 25, 50);
        g.setComposite(AlphaComposite.SrcIn);
        g.drawImage(source, 0, 0, image.getWidth(), image.getHeight(), null);
        g.dispose();

        Assert.assertTrue(BufferedImageComparer.isSame(target, image));
    }

    @Test
    public void testEx_apply_nullImage() {
        RoundRectHandler handler = new RoundRectHandler.Builder()
                .arcHeight(50)
                .arcWidth(25)
                .build();

        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> handler.apply(null));
        Assert.assertEquals("Original image is null.", ex.getMessage());
    }


    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            int arc_w = 7;
            int arc_h = 185;

            // when
            RoundRectHandler handler = new RoundRectHandler.Builder()
                    .arcHeight(arc_h)
                    .arcWidth(arc_w)
                    .build();

            // then
            Assert.assertEquals(arc_w, ReflectionUtils.get("arcWidth", handler));
            Assert.assertEquals(arc_h, ReflectionUtils.get("arcHeight", handler));
        }

        @Test
        public void testEx_build_withNullParam() {
            RoundRectHandler.Builder builder = new RoundRectHandler.Builder();
            InvalidSettingException exception = Assert.assertThrows(InvalidSettingException.class,
                    builder::build);
            Assert.assertEquals("The horizontal diameter and vertical diameter of the arc cannot both be less than or equal to 0.",
                    exception.getMessage());
        }

        @Test
        public void testEx_build_withInvalidParam() {
            // the width of arc less than 0
            RoundRectHandler.Builder builder1 = new RoundRectHandler.Builder()
                    .arcWidth(-80);
            InvalidSettingException e1 = Assert.assertThrows(InvalidSettingException.class,
                    builder1::build);
            Assert.assertEquals("The horizontal diameter of the arc must be greater than 0.",
                    e1.getMessage());

            // the height of arc less than 0
            RoundRectHandler.Builder builder2 = new RoundRectHandler.Builder()
                    .arcHeight(-80);
            InvalidSettingException e2 = Assert.assertThrows(InvalidSettingException.class,
                    builder2::build);
            Assert.assertEquals("The vertical diameter of the arc must be greater than 0.",
                    e2.getMessage());
        }
    }

}