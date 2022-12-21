package cn.t8s.filter;

import cn.core.strategy.ModeStrategy;
import net.coobird.thumbnailator.filters.ImageFilter;
import java.awt.image.BufferedImage;

/**
 * 模式适配器
 *
 * @author tracy
 * @since 0.2.1
 */
public class ModeAdaptor implements ImageFilter {

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
