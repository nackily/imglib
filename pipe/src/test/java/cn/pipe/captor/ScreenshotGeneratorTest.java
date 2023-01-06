package cn.pipe.captor;

import cn.core.ex.InvalidSettingException;
import cn.pipe.ReflectionUtils;
import org.junit.Assert;
import org.junit.Test;
import java.awt.image.BufferedImage;

public class ScreenshotGeneratorTest {

    @Test
    public void test_generate_correctUsage() {
        // given
        int x = 100, y = 200, w = 650, h = 450;

        // when
        ScreenshotGenerator generator = new ScreenshotGenerator.Builder()
                .startPoint(x, y)
                .width(w)
                .height(h)
                .build();
        BufferedImage target = generator.generate();

        // then
        Assert.assertEquals(w, target.getWidth());
        Assert.assertEquals(h, target.getHeight());
    }

    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            int x = 100, y = 200, w = 650, h = 450;

            // when
            ScreenshotGenerator generator = new ScreenshotGenerator.Builder()
                    .startPoint(x, y)
                    .width(w)
                    .height(h)
                    .build();

            // then
            Assert.assertEquals(x, ReflectionUtils.get("x", generator));
            Assert.assertEquals(y, ReflectionUtils.get("y", generator));
            Assert.assertEquals(w, ReflectionUtils.get("width", generator));
            Assert.assertEquals(h, ReflectionUtils.get("height", generator));
        }

        @Test
        public void test_build_correctUsage_withDefaultParam() {
            // given
            int w = 650, h = 450;

            // when
            ScreenshotGenerator generator = new ScreenshotGenerator.Builder()
                    .width(w)
                    .height(h)
                    .build();

            // then
            Assert.assertEquals(0, ReflectionUtils.get("x", generator));
            Assert.assertEquals(0, ReflectionUtils.get("y", generator));
            Assert.assertEquals(w, ReflectionUtils.get("width", generator));
            Assert.assertEquals(h, ReflectionUtils.get("height", generator));
        }

        @Test
        public void testEx_build_outOfBound() {
            // x out of bound
            ScreenshotGenerator.Builder builder1 = new ScreenshotGenerator.Builder()
                    .startPoint(-50, 0)
                    .width(100).height(100);
            InvalidSettingException ex1 = Assert.assertThrows(InvalidSettingException.class,
                    builder1::build);
            Assert.assertEquals("Start point's x is out of bounds.", ex1.getMessage());

            // y out of bound
            ScreenshotGenerator.Builder builder2 = new ScreenshotGenerator.Builder()
                    .startPoint(0, -50)
                    .width(100).height(100);
            InvalidSettingException ex2 = Assert.assertThrows(InvalidSettingException.class,
                    builder2::build);
            Assert.assertEquals("Start point's y is out of bounds.", ex2.getMessage());

            // width out of bound
            ScreenshotGenerator.Builder builder3 = new ScreenshotGenerator.Builder()
                    .startPoint(0, 0)
                    .width(-50).height(100);
            InvalidSettingException ex3 = Assert.assertThrows(InvalidSettingException.class,
                    builder3::build);
            Assert.assertEquals("The area's width is out of bounds.", ex3.getMessage());

            // height out of bound
            ScreenshotGenerator.Builder builder4 = new ScreenshotGenerator.Builder()
                    .startPoint(0, 0)
                    .width(50).height(-100);
            InvalidSettingException ex4 = Assert.assertThrows(InvalidSettingException.class,
                    builder4::build);
            Assert.assertEquals("The area's height is out of bounds.", ex4.getMessage());

            // Tips: the screen size of author is 1920 * 1080.

            // x-and-width out of bound
            ScreenshotGenerator.Builder builder5 = new ScreenshotGenerator.Builder()
                    .startPoint(1000, 0)
                    .width(1100).height(100);
            InvalidSettingException ex5 = Assert.assertThrows(InvalidSettingException.class,
                    builder5::build);
            Assert.assertEquals("The screenshot area(x=1,000, width=1,100) is out of screen width bounds[0, 1,920].",
                    ex5.getMessage());

            // y-and-height out of bound
            ScreenshotGenerator.Builder builder6 = new ScreenshotGenerator.Builder()
                    .startPoint(100, 600)
                    .width(110).height(650);
            InvalidSettingException ex6 = Assert.assertThrows(InvalidSettingException.class,
                    builder6::build);
            Assert.assertEquals("The screenshot area(y=600, height=650) is out of screen height bounds[0, 1,080].",
                    ex6.getMessage());
        }
    }

}