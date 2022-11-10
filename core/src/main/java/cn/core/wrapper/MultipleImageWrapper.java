package cn.core.wrapper;

import cn.extension.ImageWrapper;
import java.awt.image.BufferedImage;

/**
 * 复合的包装器
 *
 * @author tracy
 * @since 1.0.0
 */
public class MultipleImageWrapper extends AbstractThumbnailsImageWrapper<MultipleImageWrapper> {

    public MultipleImageWrapper from(BufferedImage bi) {
        return null;
    }

    public MultipleImageWrapper add(BufferedImage bi) {
        return null;
    }

    public MultipleImageWrapper remove(BufferedImage bi) {
        return null;
    }


    /**
     * 是否为复合包装器
     * @param wrapper 包装器
     * @return boolean
     */
    public static boolean isComposite(ImageWrapper<?, ?> wrapper) {
        if (wrapper == null)
            throw new NullPointerException("empty wrapper");

        return wrapper instanceof MultipleImageWrapper;
    }
}
