package cn.t8s.shape.closed;

import cn.core.utils.BufferedImageUtils;
import cn.t8s.BufferedImageComparer;
import cn.t8s.ReflectionUtils;
import cn.t8s.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OvalTest {

    @Test
    public void test_drawBorder_correctUsage() throws IOException {
        // given
        Color color = Color.BLACK;
        Stroke stroke = new BasicStroke();
        Oval oval = new Oval.Builder()
                .rect(new Rectangle(10, 20, 80, 30))
                .color(color)
                .stroke(stroke)
                .build();
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");
        int canvasWidth = source.getWidth();
        int canvasHeight = source.getHeight();

        // when
        BufferedImage target = BufferedImageUtils.copy(source, source.getType());
        Graphics2D g = target.createGraphics();
        g.setColor(color);
        g.setStroke(stroke);
        oval.drawBorder(canvasWidth, canvasHeight, g);
        g.dispose();

        // then
        BufferedImage image = BufferedImageUtils.copy(source, source.getType());
        Graphics2D gh = image.createGraphics();
        gh.setColor(color);
        gh.setStroke(stroke);
        gh.drawOval(10, 20, 80, 30);
        gh.dispose();

        Assert.assertTrue(BufferedImageComparer.isSame(image, target));
    }

    @Test
    public void test_fillInside_correctUsage() throws IOException {
        // given
        Color color = Color.GRAY;
        Stroke stroke = new BasicStroke();
        Oval oval = new Oval.Builder()
                .rect(new Rectangle(10, 20, 80, 30))
                .color(color)
                .stroke(stroke)
                .build();
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");
        int canvasWidth = source.getWidth();
        int canvasHeight = source.getHeight();

        // when
        BufferedImage target = BufferedImageUtils.copy(source, source.getType());
        Graphics2D g = target.createGraphics();
        g.setColor(color);
        g.setStroke(stroke);
        oval.fillInside(canvasWidth, canvasHeight, g);
        g.dispose();

        // then
        BufferedImage image = BufferedImageUtils.copy(source, source.getType());
        Graphics2D gh = image.createGraphics();
        gh.setColor(color);
        gh.setStroke(stroke);
        gh.fillOval(10, 20, 80, 30);
        gh.dispose();

        Assert.assertTrue(BufferedImageComparer.isSame(image, target));
    }


    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            Color color = Color.BLACK;
            Stroke stroke = new BasicStroke();
            Rectangle rectangle = new Rectangle(10, 20, 80, 30);
            Oval.Builder builder = new Oval.Builder()
                    .rect(rectangle)
                    .color(color)
                    .stroke(stroke)
                    .fill(false);

            // when
            Oval o = builder.build();

            // then
            Assert.assertEquals(color, ReflectionUtils.getFromSuper("color", o));
            Assert.assertEquals(stroke, ReflectionUtils.getFromSuper("stroke", o));
            Assert.assertEquals(rectangle, ReflectionUtils.getFromSuper("rect", o));
            Assert.assertFalse((Boolean) ReflectionUtils.getFromSuper("fill", o));
        }

        @Test
        public void testEx_build_nullParam() {
            Oval.Builder builder = new Oval.Builder()
                    .color(Color.GRAY)
                    .stroke(new BasicStroke());
            NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                    builder::build);
            Assert.assertEquals("Rectangle for this oval not specified.", ex.getMessage());
        }
    }
}
