package cn.pipe.ypipe.merge;

import cn.core.ex.InvalidSettingException;
import cn.core.utils.BufferedImageUtils;
import cn.pipe.BufferedImageComparer;
import cn.pipe.ReflectionUtils;
import cn.pipe.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class GridMergeHandlerTest {

    @Test
    public void test_merge_correctUsage() throws IOException {
        // given
        int grid_w = 56, grid_h = 36;
        int h_num = 2;
        float alpha = 0.8f;
        Color fc = Color.LIGHT_GRAY;

        java.util.List<BufferedImage> sources = new ArrayList<>();
        sources.add(TestUtils.getImageFromResource("input/slices/1.jpg"));
        sources.add(TestUtils.getImageFromResource("input/slices/2.jpg"));
        sources.add(TestUtils.getImageFromResource("input/slices/3.jpg"));
        sources.add(TestUtils.getImageFromResource("input/slices/4.jpg"));
        sources.add(TestUtils.getImageFromResource("input/slices/5.jpg"));

        // when
        GridMergeHandler handler = new GridMergeHandler.Builder()
                .gridWidth(grid_w).gridHeight(grid_h)
                .horizontalNum(h_num)
                .alpha(alpha).fillColor(fc)
                .alignCenter().autoAdapts()
                .build();
        BufferedImage target = handler.merge(sources);


        /*
         * 1 All original images are 50*30.
         * 2 The images will be merged according to the following layout:
         *      |-----------|-----------|
         *      |   1.jpg   |   2.jpg   |
         *      |-----------|-----------|
         *      |   3.jpg   |   4.jpg   |
         *      |-----------|-----------|
         *      |   5.jpg   |           |
         *      |-----------|-----------|
         */
        int mergedWidth = grid_w * 2;
        int mergedHeight = grid_h * 3;
        BufferedImage merged = BufferedImageUtils.newColoredImage(mergedWidth, mergedHeight, alpha, fc);
        // paint all images
        Graphics2D g = merged.createGraphics();
        int w_offset = (56 - 50) / 2;
        int h_offset = (36 - 30) / 2;
        g.drawImage(sources.get(0), w_offset, h_offset, null);
        g.drawImage(sources.get(1), (w_offset * 3) + 50, h_offset, null);
        g.drawImage(sources.get(2), w_offset, (h_offset * 3) + 30, null);
        g.drawImage(sources.get(3), (w_offset * 3) + 50, (h_offset * 3) + 30, null);
        g.drawImage(sources.get(4), w_offset, (h_offset * 5) + (30 * 2), null);
        g.dispose();

        // then
        Assert.assertTrue(BufferedImageComparer.isSame(merged, target));
    }

    @Test
    public void test_adjustHorizontalNumIfNecessary_withHorizontalNumTooBig() {
        // given
        int grid_w = 56, grid_h = 36;
        float alpha = 0.8f;
        Color fc = Color.LIGHT_GRAY;
        int sourceImageSize = 3, horizontalNum = 4;

        // when
        GridMergeHandler handler = new GridMergeHandler.Builder()
                .gridWidth(grid_w).gridHeight(grid_h)
                .horizontalNum(horizontalNum)
                .alpha(alpha).fillColor(fc)
                .alignCenter()
                .build();

        int before = (int) ReflectionUtils.get("horizontalNum", handler);
        handler.adjustHorizontalNumIfNecessary(sourceImageSize);
        int after = (int) ReflectionUtils.get("horizontalNum", handler);

        // then
        Assert.assertEquals(4, before);
        Assert.assertEquals(3, after);
    }

    @Test
    public void test_adjustHorizontalNumIfNecessary_withHorizontalNumNotSet() {
        // given
        int grid_w = 56, grid_h = 36;
        float alpha = 0.8f;
        Color fc = Color.LIGHT_GRAY;
        int sourceImageSize = 3;

        // when
        GridMergeHandler handler = new GridMergeHandler.Builder()
                .gridWidth(grid_w).gridHeight(grid_h)
                .alpha(alpha).fillColor(fc)
                .alignCenter()
                .build();

        int before = (int) ReflectionUtils.get("horizontalNum", handler);
        handler.adjustHorizontalNumIfNecessary(sourceImageSize);
        int after = (int) ReflectionUtils.get("horizontalNum", handler);

        // then
        Assert.assertEquals(0, before);
        Assert.assertEquals(3, after);
    }

    @Test
    public void test_adjustGridSize() throws IOException {
        // given
        int grid_w = 56, grid_h = 36;
        int h_num = 2;
        float alpha = 0.8f;
        Color fc = Color.LIGHT_GRAY;

        java.util.List<BufferedImage> sources = new ArrayList<>();
        sources.add(TestUtils.getImageFromResource("input/slices/1.jpg"));
        sources.add(TestUtils.getImageFromResource("input/nothing.jpg"));

        // when
        GridMergeHandler handler = new GridMergeHandler.Builder()
                .gridWidth(grid_w).gridHeight(grid_h)
                .horizontalNum(h_num)
                .alpha(alpha).fillColor(fc)
                .alignCenter()
                .autoAdapts()       // enable auto adapt
                .build();

        int w_before = (int) ReflectionUtils.get("gridWidth", handler);
        int h_before = (int) ReflectionUtils.get("gridHeight", handler);
        handler.adjustGridSize(sources);
        int w_after = (int) ReflectionUtils.get("gridWidth", handler);
        int h_after = (int) ReflectionUtils.get("gridHeight", handler);

        // then
        Assert.assertEquals(grid_w, w_before);
        Assert.assertEquals(grid_h, h_before);

        Assert.assertEquals(154, w_after);
        Assert.assertEquals(98, h_after);
    }

    public static class BuilderTest {

        @Test
        public void test_build_correctUsage() {
            // given
            int grid_w = 56, grid_h = 36;
            int h_num = 2;
            float alpha = 0.8f;
            Color fc = Color.LIGHT_GRAY;

            // when
            GridMergeHandler handler = new GridMergeHandler.Builder()
                    .gridWidth(grid_w).gridHeight(grid_h)
                    .horizontalNum(h_num)
                    .alpha(alpha).fillColor(fc)
                    .alignCenter()
                    .autoAdapts()
                    .build();

            // then
            Assert.assertEquals(grid_w, ReflectionUtils.get("gridWidth", handler));
            Assert.assertEquals(grid_h, ReflectionUtils.get("gridHeight", handler));
            Assert.assertEquals(h_num, ReflectionUtils.get("horizontalNum", handler));
            Assert.assertEquals(alpha, ReflectionUtils.get("alpha", handler));
            Assert.assertEquals(fc, ReflectionUtils.get("fillColor", handler));
            Assert.assertTrue((boolean) ReflectionUtils.get("alignCenter", handler));
            Assert.assertTrue((boolean) ReflectionUtils.get("autoAdapts", handler));
        }

        @Test
        public void test_build_defaultParam() {
            // given
            int grid_w = 56, grid_h = 36;

            // when
            GridMergeHandler handler = new GridMergeHandler.Builder()
                    .gridWidth(grid_w).gridHeight(grid_h)
                    .build();

            // then
            Assert.assertEquals(0, ReflectionUtils.get("horizontalNum", handler));
            Assert.assertEquals(1.0f, ReflectionUtils.get("alpha", handler));
            Assert.assertEquals(Color.WHITE, ReflectionUtils.get("fillColor", handler));
            Assert.assertFalse((boolean) ReflectionUtils.get("alignCenter", handler));
            Assert.assertFalse((boolean) ReflectionUtils.get("autoAdapts", handler));
        }

        @Test
        public void testEx_build_invalidParam() {
            // width of grid < 0
            GridMergeHandler.Builder builder1 = new GridMergeHandler.Builder()
                    .gridWidth(-50).gridHeight(100)
                    .horizontalNum(2);
            InvalidSettingException ex1 = Assert.assertThrows(InvalidSettingException.class,
                    builder1::build);
            Assert.assertEquals("The width of the grid must be greater than 0.",
                    ex1.getMessage());

            // height of grid < 0
            GridMergeHandler.Builder builder2 = new GridMergeHandler.Builder()
                    .gridWidth(100).gridHeight(-50)
                    .horizontalNum(2);
            InvalidSettingException ex2 = Assert.assertThrows(InvalidSettingException.class,
                    builder2::build);
            Assert.assertEquals("The height of the grid must be greater than 0.",
                    ex2.getMessage());

            // number of horizontal < 0
            GridMergeHandler.Builder builder3 = new GridMergeHandler.Builder()
                    .gridWidth(100).gridHeight(100)
                    .horizontalNum(-2);
            InvalidSettingException ex3 = Assert.assertThrows(InvalidSettingException.class,
                    builder3::build);
            Assert.assertEquals("The number placed in horizontal must be greater than 0.",
                    ex3.getMessage());

            // alpha out of bound
            GridMergeHandler.Builder builder4 = new GridMergeHandler.Builder()
                    .gridWidth(100).gridHeight(100)
                    .horizontalNum(2)
                    .alpha(2.0f);
            InvalidSettingException ex4 = Assert.assertThrows(InvalidSettingException.class,
                    builder4::build);
            Assert.assertEquals("The alpha out of bounds:[0, 1].",
                    ex4.getMessage());
        }

        @Test
        public void testEx_build_nullParam() {
            // grid width not set
            GridMergeHandler.Builder builder1 = new GridMergeHandler.Builder()
                    .gridHeight(100)
                    .horizontalNum(2);
            InvalidSettingException ex1 = Assert.assertThrows(InvalidSettingException.class,
                    builder1::build);
            Assert.assertEquals("The width of the grid must be determined when autoAdapts is not enabled.",
                    ex1.getMessage());

            // grid height not set
            GridMergeHandler.Builder builder2 = new GridMergeHandler.Builder()
                    .gridWidth(100)
                    .horizontalNum(2);
            InvalidSettingException ex2 = Assert.assertThrows(InvalidSettingException.class,
                    builder2::build);
            Assert.assertEquals("The height of the grid must be determined when autoAdapts is not enabled.",
                    ex2.getMessage());
        }
    }
}