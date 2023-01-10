package cn.pipe.ypipe.split;

import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.pipe.BufferedImageComparer;
import cn.pipe.ReflectionUtils;
import cn.pipe.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class GridSplitHandlerTest {

    @Test
    public void test_split_correctUsage() throws IOException {
        // given
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");
        int gw = 50, gh = 60;
        GridSplitHandler handler = new GridSplitHandler.Builder()
                .gridWidth(gw).gridHeight(gh)
                .build();

        // when
        List<BufferedImage> target = handler.split(source);

        /*
         * The original image will be divided into 8 pieces. The dimensions of each
         * piece are as follows:
         *      50*60     50*60     50*60     4*60
         *      50*38     50*38     50*38     4*38
         */
        BufferedImage image1 = source.getSubimage(0, 0, 50, 60);
        BufferedImage image2 = source.getSubimage(50, 0, 50, 60);
        BufferedImage image3 = source.getSubimage(100, 0, 50, 60);
        BufferedImage image4 = source.getSubimage(150, 0, 4, 60);
        BufferedImage image5 = source.getSubimage(0, 60, 50, 38);
        BufferedImage image6 = source.getSubimage(50, 60, 50, 38);
        BufferedImage image7 = source.getSubimage(100, 60, 50, 38);
        BufferedImage image8 = source.getSubimage(150, 60, 4, 38);

        // then
        Assert.assertEquals(8, target.size());

        Assert.assertTrue(BufferedImageComparer.isSame(image1, target.get(0)));
        Assert.assertTrue(BufferedImageComparer.isSame(image2, target.get(1)));
        Assert.assertTrue(BufferedImageComparer.isSame(image3, target.get(2)));
        Assert.assertTrue(BufferedImageComparer.isSame(image4, target.get(3)));
        Assert.assertTrue(BufferedImageComparer.isSame(image5, target.get(4)));
        Assert.assertTrue(BufferedImageComparer.isSame(image6, target.get(5)));
        Assert.assertTrue(BufferedImageComparer.isSame(image7, target.get(6)));
        Assert.assertTrue(BufferedImageComparer.isSame(image8, target.get(7)));
    }

    @Test
    public void test_split_tooLargeGridWidth() throws IOException {
        // given
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");
        int gw = 160, gh = 60;
        GridSplitHandler handler = new GridSplitHandler.Builder()
                .gridWidth(gw).gridHeight(gh)
                .build();

        // when
        int before = (int) ReflectionUtils.get("gridWidth", handler);
        List<BufferedImage> target = handler.split(source);
        int after = (int) ReflectionUtils.get("gridWidth", handler);

        // then
        Assert.assertEquals(160, before);
        Assert.assertEquals(154, after);

        // and then
        /*
         * The original image will be divided into 2 pieces. The dimensions of each
         * piece are as follows:
         *      153*60
         *      153*38
         */
        BufferedImage image1 = source.getSubimage(0, 0, 154, 60);
        BufferedImage image2 = source.getSubimage(0, 60, 154, 38);

        Assert.assertEquals(2, target.size());

        Assert.assertTrue(BufferedImageComparer.isSame(image1, target.get(0)));
        Assert.assertTrue(BufferedImageComparer.isSame(image2, target.get(1)));
    }

    @Test
    public void test_split_tooLargeGridHeight() throws IOException {
        // given
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");
        int gw = 50, gh = 120;
        GridSplitHandler handler = new GridSplitHandler.Builder()
                .gridWidth(gw).gridHeight(gh)
                .build();

        // when
        int before = (int) ReflectionUtils.get("gridHeight", handler);
        List<BufferedImage> target = handler.split(source);
        int after = (int) ReflectionUtils.get("gridHeight", handler);

        // then
        Assert.assertEquals(120, before);
        Assert.assertEquals(98, after);

        // and then
        /*
         * The original image will be divided into 4 pieces. The dimensions of each
         * piece are as follows:
         *      50*98     50*98     50*98     4*98
         */
        BufferedImage image1 = source.getSubimage(0, 0, 50, 98);
        BufferedImage image2 = source.getSubimage(50, 0, 50, 98);
        BufferedImage image3 = source.getSubimage(100, 0, 50, 98);
        BufferedImage image4 = source.getSubimage(150, 0, 4, 98);

        Assert.assertEquals(4, target.size());
        Assert.assertTrue(BufferedImageComparer.isSame(image1, target.get(0)));
        Assert.assertTrue(BufferedImageComparer.isSame(image2, target.get(1)));
        Assert.assertTrue(BufferedImageComparer.isSame(image3, target.get(2)));
        Assert.assertTrue(BufferedImageComparer.isSame(image4, target.get(3)));
    }

    @Test
    public void testEx_split_noNeedToSplit() throws IOException {
        /*
         * Throw an HandlingException
         * when the width of grid greater than the width of image
         * and the height of grid greater than the height of image.
         */
        BufferedImage source = TestUtils.getImageFromResource("input/nothing.jpg");
        GridSplitHandler handler = new GridSplitHandler.Builder()
                .gridWidth(180).gridHeight(120)
                .build();
        HandlingException ex = Assert.assertThrows(HandlingException.class,
                () -> handler.split(source));
        Assert.assertEquals("There is no need to split.", ex.getMessage());
    }

    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            int w = 50, h = 100;
            GridSplitHandler.Builder builder = new GridSplitHandler.Builder()
                    .gridWidth(w).gridHeight(h);

            // when
            GridSplitHandler handler = builder.build();

            // then
            Assert.assertEquals(w, ReflectionUtils.get("gridWidth", handler));
            Assert.assertEquals(h, ReflectionUtils.get("gridHeight", handler));
        }

        @Test
        public void testEx_build_bothNull() {
            GridSplitHandler.Builder builder = new GridSplitHandler.Builder();
            InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                    builder::build);
            Assert.assertEquals("Both of the width and height of the grid not set.",
                    ex.getMessage());
        }

        @Test
        public void testEx_build_invalidParam() {
            // the width of grid less than 0
            GridSplitHandler.Builder builder1 = new GridSplitHandler.Builder()
                    .gridWidth(-50).gridHeight(100);
            InvalidSettingException ex1 = Assert.assertThrows(InvalidSettingException.class,
                    builder1::build);
            Assert.assertEquals("The width of the grid must be greater than 0.",
                    ex1.getMessage());

            // the height of grid less than 0
            GridSplitHandler.Builder builder2 = new GridSplitHandler.Builder()
                    .gridWidth(100).gridHeight(-20);
            InvalidSettingException ex2 = Assert.assertThrows(InvalidSettingException.class,
                    builder2::build);
            Assert.assertEquals("The height of the grid must be greater than 0.",
                    ex2.getMessage());
        }
    }

}