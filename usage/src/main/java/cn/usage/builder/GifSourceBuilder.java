package cn.usage.builder;

import cn.captor.source.GifSource;
import cn.core.exec.HandlingException;
import cn.core.exec.InvalidSettingException;
import cn.core.tool.Range;
import cn.core.utils.CollectionUtils;
import cn.core.utils.StringUtils;
import cn.usage.Captors;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * GifSourceBuilder
 *
 * @author tracy
 * @since 1.0.0
 */
public class GifSourceBuilder<S> extends Captors.Builder<S, GifSourceBuilder<S>> {

    protected final GifSource<S> source;
    private boolean containsAll = false;
    private final Set<Integer> frames = new HashSet<>();

    public GifSourceBuilder(GifSource<S> gifSource) {
        this.source = gifSource;
    }

    public GifSourceBuilder<S> allFrame() {
        containsAll = true;
        return this;
    }

    public GifSourceBuilder<S> frame(int frameIndex) {
        frames.add(frameIndex);
        return this;
    }

    public GifSourceBuilder<S> frame(int... frameIndexes) {
        for (int index : frameIndexes) {
            frames.add(index);
        }
        return this;
    }

    public GifSourceBuilder<S> frame(Range<Integer> range) {
        for (int i = range.getMin(); i <= range.getMax(); i++) {
            frames.add(i);
        }
        return this;
    }

    @Override
    public BufferedImage obtainBufferedImage() throws IOException {
        checkReadiness();
        List<BufferedImage> images = obtainBufferedImages();
        if (images.size() > 1) {
            throw new HandlingException("cannot create one image from multiple original gif frames");
        }
        return images.get(0);
    }

    @Override
    public List<BufferedImage> obtainBufferedImages() throws IOException {
        checkReadiness();

        // the max frame size index of the gif
        int maxFrameIndex = source.size() - 1;

        // export all frame
        if (containsAll) {
            return source.readAll();
        }

        // export specified frames
        // check all frame was in bound
        Set<String> invalidPages = frames.stream()
                .filter(p -> maxFrameIndex < p)
                .map(Objects::toString)
                .collect(Collectors.toSet());
        if (!CollectionUtils.isNullOrEmpty(invalidPages)) {
            throw new InvalidSettingException(MessageFormat.format(
                    "the frame indexes:[{0}] has exceeded the max size of the gif document",
                    StringUtils.join(invalidPages)));
        }
        return source.read(frames.toArray(new Integer[0]));
    }

    protected void checkReadiness() {
        if (CollectionUtils.isNullOrEmpty(frames) && !containsAll) {
            throw new HandlingException("no frame to export");
        }
    }
}
