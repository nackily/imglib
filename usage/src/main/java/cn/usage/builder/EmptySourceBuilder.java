package cn.usage.builder;

import cn.core.ImageGenerator;
import cn.core.exec.HandlingException;
import cn.core.utils.CollectionUtils;
import cn.usage.Captors;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * EmptySourceBuilder
 *
 * @author tracy
 * @since 1.0.0
 */
public class EmptySourceBuilder extends Captors.Builder<EmptySourceBuilder> {

    public static final String NULL_GENERATOR = "ImageGenerator is null";
    protected List<ImageGenerator> captors = new ArrayList<>();

    public EmptySourceBuilder addLast(ImageGenerator ig) {
        CollectionUtils.excNull(ig, NULL_GENERATOR);
        captors.add(ig);
        return this;
    }

    public EmptySourceBuilder addLast(ImageGenerator... igs) {
        CollectionUtils.excNull(igs, NULL_GENERATOR);
        captors.addAll(Arrays.asList(igs));
        return this;
    }

    public EmptySourceBuilder remove(ImageGenerator ig) {
        CollectionUtils.excNull(ig, NULL_GENERATOR);
        captors.remove(ig);
        return this;
    }

    protected void checkReadiness() {
        if (CollectionUtils.isNullOrEmpty(captors)) {
            throw new HandlingException("not put any captor");
        }
    }

    protected void checkSingleOutput() {
        if (CollectionUtils.isNullOrEmpty(captors)) {
            throw new HandlingException("not put any captor");
        }
        if (captors.size() > 1) {
            throw new HandlingException("cannot create one image from multiple original captors");
        }
    }

    @Override
    public BufferedImage obtainBufferedImage() {
        checkReadiness();
        checkSingleOutput();
        return captors.get(0).generate();
    }

    @Override
    public List<BufferedImage> obtainBufferedImages(){
        checkReadiness();
        List<BufferedImage> images = new ArrayList<>();
        for (ImageGenerator ca : captors) {
            images.add(ca.generate());
        }
        return images;
    }

}
