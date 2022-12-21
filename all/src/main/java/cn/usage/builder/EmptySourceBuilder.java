package cn.usage.builder;

import cn.usage.AbstractSourceBuilder;
import cn.core.ImageGenerator;
import cn.core.ex.HandlingException;
import cn.core.utils.CollectionUtils;
import cn.core.utils.ObjectUtils;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * EmptySourceBuilder
 *
 * @author tracy
 * @since 0.2.1
 */
public class EmptySourceBuilder extends AbstractSourceBuilder<EmptySourceBuilder> {

    public static final String NULL_GENERATOR = "ImageGenerator is null.";
    protected List<ImageGenerator> captors = new ArrayList<>();

    public EmptySourceBuilder register(ImageGenerator ig) {
        ObjectUtils.excNull(ig, NULL_GENERATOR);
        captors.add(ig);
        return this;
    }

    public EmptySourceBuilder register(ImageGenerator... igs) {
        ObjectUtils.excNull(igs, NULL_GENERATOR);
        captors.addAll(Arrays.asList(igs));
        return this;
    }

    protected void checkReadiness() {
        if (CollectionUtils.isNullOrEmpty(captors)) {
            throw new HandlingException("No captors are registered.");
        }
    }

    @Override
    protected List<BufferedImage> obtainSourceImages(){
        checkReadiness();
        List<BufferedImage> images = new ArrayList<>();
        for (ImageGenerator ca : captors) {
            images.add(ca.generate());
        }
        return images;
    }

}
