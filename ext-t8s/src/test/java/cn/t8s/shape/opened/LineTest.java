package cn.t8s.shape.opened;

import cn.core.ex.HandlingException;
import cn.core.utils.BufferedImageUtils;
import cn.t8s.BufferedImageComparer;
import cn.t8s.ReflectionUtils;
import cn.t8s.TestUtils;
import cn.t8s.shape.closed.Oval;
import org.junit.Assert;
import org.junit.Test;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LineTest {

    @Test
    public void test_draw_correctUsage() throws IOException {
        // given
        Color color = Color.BLACK;
        Stroke stroke = new BasicStroke();
        Line line = new Line.Builder()
                .color(color)
                .stroke(stroke)
                .start(new Point(10, 10))
                .end(new Point(60, 70))
                .build();
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");
        int canvasWidth = source.getWidth();
        int canvasHeight = source.getHeight();

        // when
        BufferedImage target = BufferedImageUtils.copy(source, source.getType());
        Graphics2D g = target.createGraphics();
        g.setColor(color);
        g.setStroke(stroke);
        line.draw(canvasWidth, canvasHeight, g);
        g.dispose();

        // then
        BufferedImage image = BufferedImageUtils.copy(source, source.getType());
        Graphics2D gh = image.createGraphics();
        gh.setColor(color);
        gh.setStroke(stroke);
        gh.drawLine(10, 10, 60, 70);
        gh.dispose();

        Assert.assertTrue(BufferedImageComparer.isSame(image, target));
    }

    @Test
    public void testEx_draw_startPointOutOfBound() throws IOException {
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");
        int canvasWidth = source.getWidth();
        int canvasHeight = source.getHeight();
        // starting point out of bound
        Line line = new Line.Builder()
                .end(new Point(60, 70))
                .start(new Point(200, 300))
                .build();

        Graphics2D g = source.createGraphics();

        HandlingException ex = Assert.assertThrows(HandlingException.class,
                () -> line.draw(canvasWidth, canvasHeight, g));
        Assert.assertEquals("The starting point is out of bounds of this image.",
                ex.getMessage());

        g.dispose();
    }

    @Test
    public void testEx_draw_endPointOutOfBound() throws IOException {
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");
        int canvasWidth = source.getWidth();
        int canvasHeight = source.getHeight();
        // starting point out of bound
        Line line = new Line.Builder()
                .end(new Point(200, 300))
                .start(new Point(60, 70))
                .build();

        Graphics2D g = source.createGraphics();

        HandlingException ex = Assert.assertThrows(HandlingException.class,
                () -> line.draw(canvasWidth, canvasHeight, g));
        Assert.assertEquals("The ending point is out of bounds of this image.",
                ex.getMessage());

        g.dispose();
    }

    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            Color color = Color.BLACK;
            Stroke stroke = new BasicStroke();
            Point start = new Point(10, 10);
            Point end = new Point(60, 70);

            // when
            Line line = new Line.Builder()
                    .color(color)
                    .stroke(stroke)
                    .start(start).end(end)
                    .build();

            // then
            Assert.assertEquals(start, ReflectionUtils.get("start", line));
            Assert.assertEquals(end, ReflectionUtils.get("end", line));
            Assert.assertEquals(color, ReflectionUtils.getFromSuper("color", line));
            Assert.assertEquals(stroke, ReflectionUtils.getFromSuper("stroke", line));
        }

        @Test
        public void testEx_build_nullParam() {
            // null starting point
            Line.Builder builder1 = new Line.Builder()
                    .end(new Point(60, 70));
            NullPointerException ex1 = Assert.assertThrows(NullPointerException.class,
                    builder1::build);
            Assert.assertEquals("The starting point is null.", ex1.getMessage());

            // null ending point
            Line.Builder builder2 = new Line.Builder()
                    .start(new Point(60, 70));
            NullPointerException ex2 = Assert.assertThrows(NullPointerException.class,
                    builder2::build);
            Assert.assertEquals("The ending point is null.", ex2.getMessage());
        }
    }

}
