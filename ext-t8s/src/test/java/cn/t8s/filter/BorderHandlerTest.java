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

public class BorderHandlerTest {

    @Test
    public void test_apply_correctUsage() throws IOException {
        // given
        BorderHandler handler = new BorderHandler.Builder()
                .vMargins(3)
                .hMargins(7)
                .alpha(0.5f)
                .fillColor(Color.BLUE)
                .build();
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");

        // when
        BufferedImage target = handler.apply(source);

        // then
        Assert.assertEquals(source.getWidth() + 14, target.getWidth());
        Assert.assertEquals(source.getHeight() + 6, target.getHeight());

        // and then
        BufferedImage image = new BufferedImage(source.getWidth() + 14, source.getHeight() + 6, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.BLUE);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.dispose();
        Graphics g = image.getGraphics();
        g.drawImage(source, 7, 3, null);
        g.dispose();

        Assert.assertTrue(BufferedImageComparer.isSame(target, image));
    }

    @Test
    public void testEx_apply_nullImage() {
        BorderHandler handler = new BorderHandler.Builder()
                .vMargins(3)
                .hMargins(7)
                .alpha(0.5f)
                .fillColor(Color.BLUE)
                .build();

        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> handler.apply(null));
        Assert.assertEquals("Original image is null.", ex.getMessage());
    }

    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            int horizontalMargins = 8;
            int verticalMargins = 12;
            float alpha = 0.5f;
            Color color = Color.BLACK;

            // when
            BorderHandler handler = new BorderHandler.Builder()
                    .vMargins(verticalMargins)
                    .hMargins(horizontalMargins)
                    .alpha(alpha)
                    .fillColor(color)
                    .build();

            // then
            Assert.assertEquals(horizontalMargins, ReflectionUtils.get("hMargins", handler));
            Assert.assertEquals(verticalMargins, ReflectionUtils.get("vMargins", handler));
            Assert.assertEquals(alpha, ReflectionUtils.get("alpha", handler));
            Assert.assertEquals(color, ReflectionUtils.get("fillColor", handler));
        }

        @Test
        public void testEx_build_withNullParam() {
            // null horizontal margins
            BorderHandler.Builder nullHMargin = new BorderHandler.Builder()
                    .vMargins(10)
                    .alpha(0f)
                    .fillColor(Color.BLACK);
            InvalidSettingException exNullHMargin = Assert.assertThrows(InvalidSettingException.class,
                    nullHMargin::build);
            Assert.assertEquals("The horizontal margins of border must be greater than 0.",
                    exNullHMargin.getMessage());

            // null vertical margins
            BorderHandler.Builder nullVMargin = new BorderHandler.Builder()
                    .hMargins(10)
                    .alpha(0f)
                    .fillColor(Color.BLACK);
            InvalidSettingException exNullVMargin = Assert.assertThrows(InvalidSettingException.class,
                    nullVMargin::build);
            Assert.assertEquals("The vertical margins of border must be greater than 0.",
                    exNullVMargin.getMessage());

            // null fill color
            BorderHandler.Builder nullColor = new BorderHandler.Builder()
                    .vMargins(5)
                    .hMargins(10)
                    .alpha(0f)
                    .fillColor(null);
            NullPointerException exNullColor = Assert.assertThrows(NullPointerException.class,
                    nullColor::build);
            Assert.assertEquals("Fill color is null.",
                    exNullColor.getMessage());
        }

        @Test
        public void testEx_build_withInvalidParam() {
            // alpha out of range
            BorderHandler.Builder invalidAlpha = new BorderHandler.Builder()
                    .vMargins(5)
                    .hMargins(10)
                    .alpha(2f)
                    .fillColor(Color.BLACK);
            InvalidSettingException exInvalidAlpha = Assert.assertThrows(InvalidSettingException.class,
                    invalidAlpha::build);
            Assert.assertEquals("Alpha out of bounds:[0, 1].",
                    exInvalidAlpha.getMessage());

            // horizontal margins less than 0
            BorderHandler.Builder invalidVMargin = new BorderHandler.Builder()
                    .vMargins(-5)
                    .hMargins(10)
                    .alpha(0.5f)
                    .fillColor(Color.BLACK);
            InvalidSettingException exInvalidVMargin = Assert.assertThrows(InvalidSettingException.class,
                    invalidVMargin::build);
            Assert.assertEquals("The vertical margins of border must be greater than 0.",
                    exInvalidVMargin.getMessage());

            // vertical margins less than 0
            BorderHandler.Builder invalidHMargin = new BorderHandler.Builder()
                    .vMargins(5)
                    .hMargins(-10)
                    .alpha(0.5f)
                    .fillColor(Color.BLACK);
            InvalidSettingException exInvalidHMargin = Assert.assertThrows(InvalidSettingException.class,
                    invalidHMargin::build);
            Assert.assertEquals("The horizontal margins of border must be greater than 0.",
                    exInvalidHMargin.getMessage());
        }
    }
}
