package cn.pipe.ypipe;

import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.core.utils.BufferedImageUtils;
import cn.pipe.BufferedImageComparer;
import cn.pipe.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AbstractSplitFilterTest {

    private static class CopyHandler extends AbstractSplitFilter {
        @Override
        protected List<BufferedImage> split(BufferedImage image) {
            // Just copy two images from original image.
            List<BufferedImage> target = new ArrayList<>();
            target.add(BufferedImageUtils.copy(image, image.getType()));
            target.add(BufferedImageUtils.copy(image, image.getType()));
            return target;
        }
    }

    @Test
    public void test_execute_correctUsage() throws IOException {
        // given
        BufferedImage img = TestUtils.getImageFromResource("input/nothing.jpg");

        // when
        AbstractSplitFilter handler = new CopyHandler();
        List<BufferedImage> target = handler.execute(Collections.singletonList(img));

        // then
        Assert.assertEquals(2, target.size());
        Assert.assertTrue(BufferedImageComparer.isSame(img, target.get(0)));
        Assert.assertTrue(BufferedImageComparer.isSame(img, target.get(1)));
    }

    @Test
    public void testEx_execute_nullImage() {
        AbstractSplitFilter handler = new CopyHandler();
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> handler.execute(null));
        Assert.assertEquals("Source images is null.",
                ex.getMessage());
    }

    @Test
    public void testEx_execute_emptyImage() {
        AbstractSplitFilter handler = new CopyHandler();
        List<BufferedImage> images = Collections.emptyList();
        InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                () -> handler.execute(images));
        Assert.assertEquals("Not any source image was found.",
                ex.getMessage());
    }

    @Test
    public void testEx_execute_multipleImages() {
        BufferedImage image1 = new BufferedImage(200, 300, BufferedImage.TYPE_INT_RGB);
        BufferedImage image2 = new BufferedImage(100, 300, BufferedImage.TYPE_INT_RGB);

        AbstractSplitFilter handler = new CopyHandler();
        List<BufferedImage> images = Arrays.asList(image1, image2);
        HandlingException ex = Assert.assertThrows(HandlingException.class,
                () -> handler.execute(images));
        Assert.assertEquals("Splitting of multiple images is not supported.",
                ex.getMessage());
    }

}