package cn.core.wrapper;

import cn.extension.ImageCaptor;
import cn.extension.exec.ParameterException;
import net.coobird.thumbnailator.Thumbnails;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

/**
 * 默认的单个包装器
 *
 * @author tracy
 * @since 1.0.0
 */
public class SingleImageWrapper extends AbstractThumbnailsImageWrapper<SingleImageWrapper> {

    public static SingleImageWrapper from(BufferedImage bi) {
        return from(Thumbnails.of(bi));
    }
    public static SingleImageWrapper from(ImageCaptor captor) {
        return from(Thumbnails.of(captor.capture()));
    }
    public static SingleImageWrapper from(String filename) {
        return from(Thumbnails.of(filename));
    }
    public static SingleImageWrapper from(File file) {
        return from(Thumbnails.of(file));
    }
    public static SingleImageWrapper from(URL url){
        return from(Thumbnails.of(url));
    }
    public static SingleImageWrapper from(InputStream is){
        return from(Thumbnails.of(is));
    }
    public static SingleImageWrapper from(Thumbnails.Builder<?> tb) {
        if (tb == null)
            throw new NullPointerException("empty thumbnails builder");

        // initialize the scale equal 1.0, to keep the original image
        // if not, will get an exception:size is not set.
        Thumbnails.Builder<?> object = tb.scale(1.0);
        Iterator<BufferedImage> iterator = object.iterableBufferedImages().iterator();
        iterator.next();
        if (iterator.hasNext()) {
            // this builder contains multiple images
            throw new ParameterException("single wrapper does not support multipart images");
        }
        SingleImageWrapper wrapper = new SingleImageWrapper();
        wrapper.object = object;
        return wrapper;
    }


    public MultipleImageWrapper toMultiple() throws IOException {
        return new MultipleImageWrapper()
                .from(object.asBufferedImage());
    }


}
