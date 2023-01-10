package cn.usage.builder;

import cn.core.ImageGenerator;
import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.pipe.captor.TransparentImageGenerator;
import cn.usage.BufferedImageComparer;
import cn.usage.ReflectionUtils;
import org.junit.Assert;
import org.junit.Test;
import java.awt.image.BufferedImage;
import java.util.List;

@SuppressWarnings("unchecked")
public class EmptySourceBuilderTest {

    @Test
    public void test_register_single() {
        // given
        ImageGenerator ge = new TransparentImageGenerator.Builder()
                .width(30).height(40)
                .build();

        // when
        EmptySourceBuilder builder = new EmptySourceBuilder()
                .register(ge);

        // then
        List<ImageGenerator> handlers = (List<ImageGenerator>) ReflectionUtils.get("captors", builder);

        Assert.assertEquals(1, handlers.size());
        Assert.assertEquals(ge, handlers.get(0));
    }

    @Test
    public void testEx_register_single_null() {
        EmptySourceBuilder builder = new EmptySourceBuilder();
        NullPointerException ex = Assert.assertThrows(NullPointerException.class,
                () -> builder.register((ImageGenerator) null));
        Assert.assertEquals("ImageGenerator is null.",
                ex.getMessage());
    }

    @Test
    public void test_register_array() {
        // given
        ImageGenerator ge = new TransparentImageGenerator.Builder()
                .width(30).height(40)
                .build();

        // when
        EmptySourceBuilder builder = new EmptySourceBuilder()
                .register(ge, ge, ge);

        // then
        List<ImageGenerator> handlers = (List<ImageGenerator>) ReflectionUtils.get("captors", builder);

        Assert.assertEquals(3, handlers.size());
        Assert.assertEquals(ge, handlers.get(0));
        Assert.assertEquals(ge, handlers.get(1));
        Assert.assertEquals(ge, handlers.get(2));
    }

    @Test
    public void testEx_register_array_nullOrEmpty() {
        EmptySourceBuilder builder = new EmptySourceBuilder();
        // null
        NullPointerException ex1 = Assert.assertThrows(NullPointerException.class,
                () -> builder.register((ImageGenerator[]) null));
        Assert.assertEquals("No ImageGenerator was found.",
                ex1.getMessage());

        // empty
        InvalidSettingException ex2 = Assert.assertThrows(InvalidSettingException.class,
                builder::register);
        Assert.assertEquals("Empty ImageGenerator array.",
                ex2.getMessage());
    }

    @Test
    public void test_obtainSourceImages() {
        // given
        ImageGenerator ge = new TransparentImageGenerator.Builder()
                .width(30).height(40)
                .build();
        BufferedImage standard = ge.generate();

        // when
        EmptySourceBuilder builder = new EmptySourceBuilder()
                .register(ge);

        // then
        List<BufferedImage> target = builder.obtainSourceImages();

        Assert.assertEquals(1, target.size());
        Assert.assertTrue(BufferedImageComparer.isSame(standard, target.get(0)));
    }

    @Test
    public void testEx_obtainSourceImages_withNoCaptor() {
        EmptySourceBuilder builder = new EmptySourceBuilder();
        HandlingException ex = Assert.assertThrows(HandlingException.class,
                builder::obtainSourceImages);
        Assert.assertEquals("No captors are registered.",
                ex.getMessage());
    }

}