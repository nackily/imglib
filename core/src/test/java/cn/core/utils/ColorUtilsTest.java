package cn.core.utils;

import cn.core.TestUtils;
import cn.core.ex.InvalidSettingException;
import cn.core.tool.Range;
import org.junit.Assert;
import org.junit.Test;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class ColorUtilsTest {

    @Test
    public void test_of_correctUsage() {
        // given
        int redComponent = 157;
        int greenComponent = 23;
        int blueComponent = 249;

        // when
        Color color = ColorUtils.of(redComponent, greenComponent, blueComponent);

        // then
        Assert.assertEquals(redComponent, color.getRed());
        Assert.assertEquals(greenComponent, color.getGreen());
        Assert.assertEquals(blueComponent, color.getBlue());
    }

    @Test
    public void testEx_of_redComponentOutOfBound() {

        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class,
                () -> ColorUtils.of(256, 0, 0));

        Assert.assertEquals("Color parameter outside of expected range: Red",
                exception.getMessage());
    }

    @Test
    public void test_ofRGB_correctUsage() {
        // given
        int redComponent = 157;
        int greenComponent = 23;
        int blueComponent = 249;
        int rgb = (redComponent << 16) + (greenComponent << 8) + blueComponent;

        // when
        Color color = ColorUtils.ofRGB(rgb);

        // then
        Assert.assertEquals(redComponent, color.getRed());
        Assert.assertEquals(greenComponent, color.getGreen());
        Assert.assertEquals(blueComponent, color.getBlue());
    }

    @Test
    public void test_random_correctUsage() {
        // given
        Range<Integer> redComponentRange = Range.ofInt(25, 70);
        Range<Integer> greenComponentRange = Range.ofInt(0, 200);
        Range<Integer> blueComponentRange = Range.ofInt(156, 158);

        // when
        Color randomColor = ColorUtils.random(redComponentRange, greenComponentRange, blueComponentRange);

        // then
        Assert.assertTrue(redComponentRange.within(randomColor.getRed()));
        Assert.assertTrue(greenComponentRange.within(randomColor.getGreen()));
        Assert.assertTrue(blueComponentRange.within(randomColor.getBlue()));
    }

    @Test
    public void testEx_random_greenRangeOutOfBound() {
        Range<Integer> redComponentRange = Range.ofInt(25, 70);
        Range<Integer> greenComponentRange = Range.ofInt(-1, 200);
        Range<Integer> blueComponentRange = Range.ofInt(156, 158);

        InvalidSettingException exception = Assert.assertThrows(InvalidSettingException.class,
                () -> ColorUtils.random(redComponentRange, greenComponentRange, blueComponentRange));
        Assert.assertEquals("Color's G-value out of bounds, must limited to [0, 255].",
                exception.getMessage());
    }

    @Test
    public void test_anyOf_correctUsage() {
        // given
        Color one = Color.BLACK;
        Color two = Color.WHITE;
        Color three = Color.RED;
        Color four = Color.GRAY;
        
        // when
        Color selectedItem = ColorUtils.anyOf(one, two, three, four);

        // then
        Assert.assertNotNull(selectedItem);

        boolean withinScope = selectedItem == one || selectedItem == two ||
                selectedItem == three || selectedItem == four;
        Assert.assertTrue(withinScope);
    }

    @Test
    public void testEx_anyOf_emptyOptions() {
        InvalidSettingException exception = Assert.assertThrows(InvalidSettingException.class,
                () -> ColorUtils.anyOf(new Color[0]));
        Assert.assertEquals("No options.",
                exception.getMessage());
    }

    @Test
    public void test_obtainRectCenterRGB_correctUsage() throws IOException {
        // given
        int x = 20;
        int y = 30;
        int width = 5;
        int height = 14;
        BufferedImage img = TestUtils.getImageFromResource("input/nothing.jpg");

        // when
        int rgb = ColorUtils.obtainRectCenterRGB(img, x, y, width, height);

        // then
        int centerX = x + (width / 2);
        int centerY = y + (height / 2);
        Assert.assertEquals(rgb, img.getRGB(centerX, centerY));
    }

    @Test
    public void testEx_obtainRectCenterRGB_nullImage() {
        NullPointerException exception = Assert.assertThrows(NullPointerException.class,
                () -> ColorUtils.obtainRectCenterRGB(null, 0, 0, 2, 2));
        Assert.assertEquals("BufferedImage is null.",
                exception.getMessage());
    }

    @Test
    public void testEx_obtainRectCenterRGB_invalidWidth() throws IOException {
        BufferedImage image = TestUtils.getImageFromResource("input/nothing.jpg");

        InvalidSettingException exception = Assert.assertThrows(InvalidSettingException.class,
                () -> ColorUtils.obtainRectCenterRGB(image, 0, 0, 0, 2));
        Assert.assertEquals("The width of region cannot be less than or equal to 0.",
                exception.getMessage());
    }

    @Test
    public void testEx_obtainRectCenterRGB_xOutOfBound() throws IOException {
        BufferedImage image = TestUtils.getImageFromResource("input/nothing.jpg");

        InvalidSettingException exception = Assert.assertThrows(InvalidSettingException.class,
                () -> ColorUtils.obtainRectCenterRGB(image, 100, 0, 57, 2));
        Assert.assertEquals("The specified region crosses the width of the image.",
                exception.getMessage());
    }

    @Test
    public void testEx_obtainRectCenterRGB_yOutOfBound() throws IOException {
        BufferedImage image = TestUtils.getImageFromResource("input/nothing.jpg");

        InvalidSettingException exception = Assert.assertThrows(InvalidSettingException.class,
                () -> ColorUtils.obtainRectCenterRGB(image, 0, 70, 2, 29));
        Assert.assertEquals("The specified region crosses the height of the image.",
                exception.getMessage());
    }
}
