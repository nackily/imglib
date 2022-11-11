package cn.core.wrapper;

import cn.core.support.TransitioningSupport;
import cn.extension.ImageCaptor;
import cn.extension.ImageWrapper;
import cn.core.support.EditingSupport;
import cn.core.filter.BorderHandler;
import cn.core.filter.HighQualityExpandHandler;
import cn.core.filter.MosaicHandler;
import cn.core.filter.RoundRectHandler;
import cn.extension.MergeStrategy;
import cn.extension.SplitStrategy;
import net.coobird.thumbnailator.Thumbnails;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * 默认包装器
 *
 * @author tracy
 * @since 1.0.0
 */
public class DefaultWrapper implements ImageWrapper<Thumbnails.Builder<?>, DefaultWrapper>,
        EditingSupport<DefaultWrapper>, TransitioningSupport<DefaultWrapper> {

    protected Thumbnails.Builder<?> object;

    private DefaultWrapper(){}

    public static DefaultWrapper from(BufferedImage... images) {
        return from(Thumbnails.of(images));
    }
    public static DefaultWrapper from(ImageCaptor... captors) {
        return from(Thumbnails.of(Arrays.stream(captors).map(ImageCaptor::capture).toArray(BufferedImage[]::new)));
    }
    public static DefaultWrapper from(String... files) {
        return from(Thumbnails.of(files));
    }
    public static DefaultWrapper from(File... files) {
        return from(Thumbnails.of(files));
    }
    public static DefaultWrapper from(URL... urls){
        return from(Thumbnails.of(urls));
    }
    public static DefaultWrapper from(InputStream... iss){
        return from(Thumbnails.of(iss));
    }
    public static DefaultWrapper from(Thumbnails.Builder<?> tb) {
        if (tb == null) {
            throw new NullPointerException("empty thumbnails builder");
        }
        DefaultWrapper wrapper = new DefaultWrapper();
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
    public DefaultWrapper border(BorderHandler handler) {
        object.addFilter(handler);
        return this;
    }

    @Override
    public DefaultWrapper expand(HighQualityExpandHandler handler) {
        object.addFilter(handler);
        return this;
    }

    @Override
    public DefaultWrapper mosaic(MosaicHandler handler) {
        object.addFilter(handler);
        return this;
    }

    @Override
    public DefaultWrapper roundRect(RoundRectHandler handler) {
        object.addFilter(handler);
        return this;
    }

    @Override
    public DefaultWrapper merge(MergeStrategy strategy) throws IOException {
        List<BufferedImage> images = object.asBufferedImages();
        object = Thumbnails.of(strategy.execute(images));
        return this;
    }

    @Override
    public DefaultWrapper split(SplitStrategy strategy) throws IOException {
        return this;
    }

    @Override
    public Thumbnails.Builder<?> getWrapper() {
        return object;
    }

    @Override
    public DefaultWrapper clear() {
        object = null;
        return this;
    }

}
