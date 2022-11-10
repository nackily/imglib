package cn.core.wrapper;

import cn.extension.ImageCaptor;
import cn.extension.ImageWrapper;
import cn.extension.ext.EditingSupport;
import cn.extension.filter.BorderHandler;
import cn.extension.filter.HighQualityExpandHandler;
import net.coobird.thumbnailator.Thumbnails;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Thumbnails 包装器
 *
 * @author tracy
 * @since 1.0.0
 */
public class ThumbnailsImageWrapper implements ImageWrapper<Thumbnails.Builder<?>, ThumbnailsImageWrapper>,
        EditingSupport<ThumbnailsImageWrapper> {

    protected Thumbnails.Builder<?> object;

    private ThumbnailsImageWrapper(){}

    public static ThumbnailsImageWrapper from(BufferedImage... images) {
        return from(Thumbnails.of(images));
    }
    public static ThumbnailsImageWrapper from(ImageCaptor... captors) {
        return from(Thumbnails.of(Arrays.stream(captors).map(ImageCaptor::capture).toArray(BufferedImage[]::new)));
    }
    public static ThumbnailsImageWrapper from(String... files) {
        return from(Thumbnails.of(files));
    }
    public static ThumbnailsImageWrapper from(File... files) {
        return from(Thumbnails.of(files));
    }
    public static ThumbnailsImageWrapper from(URL... urls){
        return from(Thumbnails.of(urls));
    }
    public static ThumbnailsImageWrapper from(InputStream... iss){
        return from(Thumbnails.of(iss));
    }
    public static ThumbnailsImageWrapper from(Thumbnails.Builder<?> tb) {
        if (tb == null) {
            throw new NullPointerException("empty thumbnails builder");
        }
        ThumbnailsImageWrapper wrapper = new ThumbnailsImageWrapper();
        wrapper.object = tb;
        return wrapper;
    }

    @Override
    public BufferedImage firstImage() throws IOException {
        List<BufferedImage> list = object.asBufferedImages();
        if (list == null || list.size() == 0) {
            throw new NullPointerException("no image found");
        }
        return list.get(0);
    }

    @Override
    public ThumbnailsImageWrapper border(BorderHandler hdl) {
        object.addFilter(hdl);
        return this;
    }

    @Override
    public ThumbnailsImageWrapper expand(HighQualityExpandHandler hdl) {
        object.addFilter(hdl);
        return this;
    }

    @Override
    public Thumbnails.Builder<?> getWrapper() {
        return object;
    }

    @Override
    public ThumbnailsImageWrapper clear() {
        object = null;
        return this;
    }

}
