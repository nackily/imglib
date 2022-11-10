package cn.core.wrapper;

import cn.extension.ImageWrapper;
import cn.extension.ext.EditingSupport;
import cn.extension.filter.BorderHandler;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 抽象的 Thumbnails 包装器
 *
 * @author tracy
 * @since 1.0.0
 */
@SuppressWarnings({"unchecked"})
public abstract class AbstractThumbnailsImageWrapper<Children extends AbstractThumbnailsImageWrapper<Children>>
        implements ImageWrapper<Thumbnails.Builder<?>, Children>, EditingSupport<Children> {

    protected final Children implThis = (Children) this;
    protected Thumbnails.Builder<?> object;

    @Override
    public BufferedImage firstImage() throws IOException {
        List<BufferedImage> list = object.asBufferedImages();
        if (list == null || list.size() == 0) {
            throw new NullPointerException("no image found");
        }
        return list.get(0);
    }

    @Override
    public Children border(BorderHandler bh) {
        object.addFilter(bh);
        return this.implThis;
    }

    @Override
    public Thumbnails.Builder<?> getWrapper() {
        return object;
    }

    @Override
    public Children clear() {
        object = null;
        return this.implThis;
    }

}
