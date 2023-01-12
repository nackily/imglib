package cn.usage;

import cn.core.PipeFilter;
import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.core.utils.BufferedImageUtils;
import cn.pipe.ypipe.merge.GridMergeHandler;
import cn.pipe.ypipe.split.GridSplitHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class AbstractSourceBuilderTest {

    /**
     * A source builder that can batch create images.
     * <br>
     * For testing only.
     */
    public static final class BatchSourceBuilder extends AbstractSourceBuilder<BatchSourceBuilder> {
        private final int count;
        public BatchSourceBuilder() {
            this(1);
        }
        public BatchSourceBuilder(int count) {
            this.count = count;
        }
        @Override
        protected List<BufferedImage> obtainSourceImages() throws IOException {
            List<BufferedImage> images = new ArrayList<>();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    images.add(BufferedImageUtils.newColoredImage(30, 45, 1.0f, Color.WHITE));
                }
            }
            return images;
        }
    }


    private AbstractSourceBuilder<BatchSourceBuilder> builder;
    @Before
    public void init() {
        builder = new BatchSourceBuilder();
    }


    @Test
    public void test_formatName() {
        // given
        String format = "png";

        // when
        builder.formatName(format);

        // then
        Assert.assertEquals(format, ReflectionUtils.getFromSuper("formatName", builder));
    }

    @Test
    public void testEx_formatName_empty() {
        InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                () -> builder.formatName(""));
        Assert.assertEquals("Format name can not be null.",
                ex.getMessage());
    }

    @Test
    public void test_addFilter_single() {
        // given
        PipeFilter filter = new GridSplitHandler.Builder()
                .gridWidth(20).gridHeight(20)
                .build();

        // when
        builder.addFilter(filter);

        // then
        List<PipeFilter> filters = (List<PipeFilter>) ReflectionUtils.getFromSuper("filters", builder);
        Assert.assertEquals(1, filters.size());
        Assert.assertEquals(filter, filters.get(0));
    }

    @Test
    public void testEx_addFilter_single_null() {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> builder.addFilter((PipeFilter) null));
        Assert.assertEquals("PipeFilter is null.",
                ex.getMessage());
    }

    @Test
    public void test_addFilter_array() {
        // given
        PipeFilter filter = new GridSplitHandler.Builder()
                .gridWidth(20).gridHeight(20)
                .build();

        // when
        builder.addFilter(filter, filter);

        // then
        List<PipeFilter> filters = (List<PipeFilter>) ReflectionUtils.getFromSuper("filters", builder);

        Assert.assertEquals(2, filters.size());
        Assert.assertEquals(filter, filters.get(0));
        Assert.assertEquals(filter, filters.get(1));
    }

    @Test
    public void testEx_addFilter_array_empty() {
        InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                () -> builder.addFilter());
        Assert.assertEquals("No PipeFilter need to be added.",
                ex.getMessage());
    }

    @Test
    public void testEx_removeFilter() {
        // given
        PipeFilter filter = new GridSplitHandler.Builder()
                .gridWidth(20).gridHeight(20)
                .build();
        builder.addFilter(filter);

        // when
        List<PipeFilter> filters = (List<PipeFilter>) ReflectionUtils.getFromSuper("filters", builder);
        int beforeSize = filters.size();

        builder.removeFilter(filter);
        int afterSize = filters.size();

        // then
        Assert.assertEquals(1, beforeSize);
        Assert.assertEquals(0, afterSize);
    }

    @Test
    public void testEx_removeFilter_null() {
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> builder.removeFilter((PipeFilter) null));
        Assert.assertEquals("PipeFilter is null.",
                ex.getMessage());
    }

    @Test
    public void test_obtainBufferedImage() throws IOException {
        // when
        BufferedImage target = builder.obtainBufferedImage();

        // then
        BufferedImage standard = BufferedImageUtils.newColoredImage(30, 45, 1.0f, Color.WHITE);
        Assert.assertTrue(BufferedImageComparer.isSame(standard, target));
    }

    @Test
    public void testEx_obtainBufferedImage_noImage() throws IOException {
        /*
         * The next BatchSourceBuilder will not get any images.
         */
        BatchSourceBuilder bsb = new BatchSourceBuilder(0);
        HandlingException ex = Assert.assertThrows(HandlingException.class,
                bsb::obtainBufferedImage);
        Assert.assertEquals("No images was found.",
                ex.getMessage());
    }

    @Test
    public void testEx_obtainBufferedImage_multipleImages() throws IOException {
        /*
         * The next BatchSourceBuilder will not get multiple images.
         */
        BatchSourceBuilder bsb = new BatchSourceBuilder(2);
        HandlingException ex = Assert.assertThrows(HandlingException.class,
                bsb::obtainBufferedImage);
        Assert.assertEquals("Cannot create an image from multiple original image sources.",
                ex.getMessage());
    }

    @Test
    public void test_obtainBufferedImages() throws IOException {
        // given
        AbstractSourceBuilder<BatchSourceBuilder> asb = new BatchSourceBuilder(3);

        // when
        List<BufferedImage> target = asb.obtainBufferedImages();

        // then
        Assert.assertEquals(3, target.size());

        BufferedImage standard = BufferedImageUtils.newColoredImage(30, 45, 1.0f, Color.WHITE);
        Assert.assertTrue(BufferedImageComparer.isSame(standard, target.get(0)));
        Assert.assertTrue(BufferedImageComparer.isSame(standard, target.get(1)));
        Assert.assertTrue(BufferedImageComparer.isSame(standard, target.get(2)));
    }

    @Test
    public void testEx_obtainBufferedImages_noImage() throws IOException {
        /*
         * The next BatchSourceBuilder will not get any images.
         */
        BatchSourceBuilder bsb = new BatchSourceBuilder(0);
        HandlingException ex = Assert.assertThrows(HandlingException.class,
                bsb::obtainBufferedImages);
        Assert.assertEquals("No images was found.",
                ex.getMessage());
    }

    @Test
    public void test_obtainBufferedImage_withMergeFilter() throws IOException {
        // given
        int grid_w = 50, grid_h = 60, h_num = 2;
        GridMergeHandler handler = new GridMergeHandler.Builder()
                .gridWidth(grid_w).gridHeight(grid_h)
                .horizontalNum(h_num)
                .fillColor(Color.BLUE)
                .build();
        BatchSourceBuilder bsb = new BatchSourceBuilder(3);

        // when
        BufferedImage target = bsb.addFilter(handler)
                .obtainBufferedImage();

        // then
        BufferedImage standard = BufferedImageUtils.newColoredImage(30, 45, 1.0f, Color.WHITE);

        // width = 50 * 2
        // height = 60 * 2
        BufferedImage image = BufferedImageUtils.newColoredImage(
                grid_w * h_num, grid_h * h_num, 1.0f, Color.BLUE);
        Graphics g = image.getGraphics();
        g.drawImage(standard, 0, 0, null);
        g.drawImage(standard, grid_w, 0, null);
        g.drawImage(standard, 0, grid_h, null);
        g.dispose();

        Assert.assertTrue(BufferedImageComparer.isSame(image, target));
    }

    @Test
    public void test_obtainBufferedImages_withSplitFilter() throws IOException {
        // given
        int grid_w = 20, grid_h = 30;
        GridSplitHandler handler = new GridSplitHandler.Builder()
                .gridWidth(grid_w).gridHeight(grid_h)
                .build();
        // when
        List<BufferedImage> target = builder.addFilter(handler)
                .obtainBufferedImages();

        // then
        /*
         * |-----------|-----------|
         * |   20*30   |   10*30   |
         * |-----------|-----------|
         * |   20*15   |   10*15   |
         * |-----------|-----------|
         */
        Assert.assertEquals(4, target.size());

        BufferedImage standard = BufferedImageUtils.newColoredImage(30, 45, 1.0f, Color.WHITE);
        BufferedImage image0 = standard.getSubimage(0, 0, 20, 30);
        BufferedImage image1 = standard.getSubimage(20, 0, 10, 30);
        BufferedImage image2 = standard.getSubimage(0, 30, 20, 15);
        BufferedImage image3 = standard.getSubimage(20, 30, 10, 15);

        Assert.assertTrue(BufferedImageComparer.isSame(image0, target.get(0)));
        Assert.assertTrue(BufferedImageComparer.isSame(image1, target.get(1)));
        Assert.assertTrue(BufferedImageComparer.isSame(image2, target.get(2)));
        Assert.assertTrue(BufferedImageComparer.isSame(image3, target.get(3)));
    }


}