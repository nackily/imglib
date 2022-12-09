package cn.usage.builder;

import cn.usage.AbstractSourceBuilder;
import cn.core.in.BufferedImageSource;
import cn.core.ex.HandlingException;
import cn.core.utils.StringUtils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * ImageSourceBuilder
 *
 * @author tracy
 * @since 1.0.0
 */
public class ImageSourceBuilder<S> extends AbstractSourceBuilder<ImageSourceBuilder<S>> {

    protected boolean useOriginalFormat;
    protected final Iterable<BufferedImageSource<S>> sources;

    public ImageSourceBuilder(Iterable<BufferedImageSource<S>> sources) {
        this.sources = sources;
    }


    @Override
    protected List<BufferedImage> obtainSourceImages() throws IOException {
        // obtain the source images
        List<BufferedImage> originalImages = new ArrayList<>();
        List<String> formatNames = new ArrayList<>();
        for (BufferedImageSource<S> o : sources) {
            originalImages.add(o.read());
            formatNames.add(o.getOriginalFormatName());
        }
        // setting the format name
        if (useOriginalFormat) {
            String[] formats = formatNames.stream().filter(StringUtils::isNotEmpty).distinct().toArray(String[]::new);
            if (formats.length == 0) {
                throw new HandlingException("No available original format.");
            } else if (formats.length > 1) {
                throw new HandlingException(MessageFormat.format("Multiple available original formats found:[{0}].",
                        StringUtils.join(formats)));
            } else {
                super.formatName(formats[0]);
            }
        }

        return originalImages;
    }

    public ImageSourceBuilder<S> useOriginalFormat() {
        useOriginalFormat = true;
        return this;
    }
}
