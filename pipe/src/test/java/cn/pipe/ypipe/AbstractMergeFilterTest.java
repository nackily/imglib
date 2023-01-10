package cn.pipe.ypipe;

import cn.core.ex.InvalidSettingException;
import cn.pipe.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AbstractMergeFilterTest {

    private static class ReturnFirstHandler extends AbstractMergeFilter {
        @Override
        protected BufferedImage merge(List<BufferedImage> images) {
            // Just return the first image of original images.
            return images.get(0);
        }
    }

    @Test
    public void test_execute_correctUsage() throws IOException {
        // given
        BufferedImage first = TestUtils.getImageFromResource("input/nothing.jpg");
        BufferedImage second = new BufferedImage(200, 300, BufferedImage.TYPE_INT_RGB);
        BufferedImage third = new BufferedImage(200, 300, BufferedImage.TYPE_INT_RGB);

        // when
        AbstractMergeFilter handler = new ReturnFirstHandler();
        List<BufferedImage> target = handler.execute(Arrays.asList(first, second, third));

        // then
        Assert.assertEquals(1, target.size());
        Assert.assertEquals(first, target.get(0));
    }

    @Test
    public void testEx_execute_nullImage() {
        AbstractMergeFilter handler = new ReturnFirstHandler();
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> handler.execute(null));
        Assert.assertEquals("Source images is null.",
                ex.getMessage());
    }

    @Test
    public void testEx_execute_emptyImage() {
        AbstractMergeFilter handler = new ReturnFirstHandler();
        List<BufferedImage> images = Collections.emptyList();
        InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                () -> handler.execute(images));
        Assert.assertEquals("Not any source image was found.",
                ex.getMessage());
    }

}