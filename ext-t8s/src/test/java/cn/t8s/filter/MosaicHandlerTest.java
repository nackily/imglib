package cn.t8s.filter;

import cn.core.ex.InvalidSettingException;
import cn.core.utils.BufferedImageUtils;
import cn.core.utils.ColorUtils;
import cn.t8s.BufferedImageComparer;
import cn.t8s.ReflectionUtils;
import cn.t8s.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MosaicHandlerTest {

    @Test
    public void test_apply_correctUsage() throws IOException {
        // given
        int side = 10, x = 55, y = 35, w = 60, h = 39;
        MosaicHandler handler = new MosaicHandler.Builder()
                .sideLength(side)
                .startX(x)
                .startY(y)
                .width(w)
                .height(h)
                .build();
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");

        // when
        BufferedImage target = handler.apply(source);

        // then
        BufferedImage image = BufferedImageUtils.copy(source, source.getType());
        Graphics g = image.getGraphics();
        int i = x;
        while (i < x + w) {
            int rw = Math.min(side, x + w - i);
            int j = y;
            while (j < y + h) {
                int rh = Math.min(side, y + h - j);
                int centerRgb = ColorUtils.obtainRectCenterRGB(source, i, j, rw, rh);
                g.setColor(ColorUtils.ofRGB(centerRgb));
                g.fillRect(i, j, rw, rh);
                j += side;
            }
            i += side;
        }
        g.dispose();

        Assert.assertTrue(BufferedImageComparer.isSame(image, target));
    }

    @Test
    public void testEx_apply_nullImage() {
        MosaicHandler handler = new MosaicHandler.Builder()
                .sideLength(10)
                .startX(0)
                .startY(0)
                .width(60)
                .height(50)
                .build();

        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> handler.apply(null));
        Assert.assertEquals("Original image is null.", ex.getMessage());
    }


    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            int side = 10, x = 20, y = 30, w = 40, h = 50;

            // when
            MosaicHandler handler = new MosaicHandler.Builder()
                    .sideLength(side)
                    .startX(x)
                    .startY(y)
                    .width(w)
                    .height(h)
                    .build();

            // then
            Assert.assertEquals(10, ReflectionUtils.get("sideLength", handler));
            Assert.assertEquals(20, ReflectionUtils.get("startX", handler));
            Assert.assertEquals(30, ReflectionUtils.get("startY", handler));
            Assert.assertEquals(60, ReflectionUtils.get("endX", handler));
            Assert.assertEquals(80, ReflectionUtils.get("endY", handler));
        }

        @Test
        public void test_build_defaultValue() {
            // given
            int side = 10, x = 20, y = 30;

            // when
            MosaicHandler handler = new MosaicHandler.Builder()
                    .sideLength(side)
                    .startX(x)
                    .startY(y)
                    .build();

            // then
            Assert.assertEquals(10, ReflectionUtils.get("sideLength", handler));
            Assert.assertEquals(20, ReflectionUtils.get("startX", handler));
            Assert.assertEquals(30, ReflectionUtils.get("startY", handler));
            Assert.assertEquals((10 + 20), ReflectionUtils.get("endX", handler));
            Assert.assertEquals((10 + 30), ReflectionUtils.get("endY", handler));
        }

        @Test
        public void testEx_build_nullParam() {
            // null side length
            MosaicHandler.Builder builder1 = new MosaicHandler.Builder()
                    .startX(0).startY(0)
                    .width(60).height(50);
            InvalidSettingException e1 = Assert.assertThrows(InvalidSettingException.class,
                    builder1::build);
            Assert.assertEquals("Side length must be greater than 0.",
                    e1.getMessage());
        }

        @Test
        public void testEx_build_invalidParam() {
            // side length < 0
            MosaicHandler.Builder builder1 = new MosaicHandler.Builder()
                    .sideLength(-1)
                    .startX(0).startY(0)
                    .width(60).height(50);
            InvalidSettingException e1 = Assert.assertThrows(InvalidSettingException.class,
                    builder1::build);
            Assert.assertEquals("Side length must be greater than 0.",
                    e1.getMessage());

            // width < 0
            MosaicHandler.Builder builder2 = new MosaicHandler.Builder()
                    .sideLength(10)
                    .startX(0).startY(0)
                    .height(50).width(-5);
            InvalidSettingException e2 = Assert.assertThrows(InvalidSettingException.class,
                    builder2::build);
            Assert.assertEquals("The rectangle's width must be greater than 0.",
                    e2.getMessage());

            // height < 0
            MosaicHandler.Builder builder3 = new MosaicHandler.Builder()
                    .sideLength(10)
                    .startX(0).startY(0)
                    .width(40).height(-5);
            InvalidSettingException e3 = Assert.assertThrows(InvalidSettingException.class,
                    builder3::build);
            Assert.assertEquals("The rectangle's height must be greater than 0.",
                    e3.getMessage());

            // start_x < 0
            MosaicHandler.Builder builder4 = new MosaicHandler.Builder()
                    .sideLength(10)
                    .startX(-20).startY(0)
                    .width(40).height(50);
            InvalidSettingException e4 = Assert.assertThrows(InvalidSettingException.class,
                    builder4::build);
            Assert.assertEquals("The start point's X must be greater than 0.",
                    e4.getMessage());

            // start_y < 0
            MosaicHandler.Builder builder5 = new MosaicHandler.Builder()
                    .sideLength(10)
                    .startX(20).startY(-30)
                    .width(40).height(50);
            InvalidSettingException e5 = Assert.assertThrows(InvalidSettingException.class,
                    builder5::build);
            Assert.assertEquals("The start point's Y must be greater than 0.",
                    e5.getMessage());
        }
    }

}