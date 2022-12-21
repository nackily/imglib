package cn.t8s.filter;

import cn.core.strategy.ModeStrategy;
import net.coobird.thumbnailator.filters.ImageFilter;
import java.awt.image.BufferedImage;

/**
 * An image filter that can apply some mode.At the same time, it is also an adapter
 * that maintains a mode strategy internally.
 *
 * @author tracy
 * @since 0.2.1
 */
public class ModeAdaptor implements ImageFilter {

    /**
     * The mode strategy which apply to the image.
     */
    private final ModeStrategy modeStrategy;

    public ModeAdaptor(ModeStrategy strategy) {
        this.modeStrategy = strategy;
    }

    @Override
    public BufferedImage apply(BufferedImage img) {
        modeStrategy.execute(img);
        return img;
    }
}
