package cn.usage.builder;

import cn.core.utils.ObjectUtils;
import cn.usage.AbstractSourceBuilder;
import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.core.in.GifSource;
import cn.core.tool.Range;
import cn.core.utils.CollectionUtils;
import cn.core.utils.StringUtils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A source builder which form GIF source.
 *
 * @author tracy
 * @since 0.2.1
 */
public class GifSourceBuilder<S> extends AbstractSourceBuilder<GifSourceBuilder<S>> {

    /**
     * The GIF source.
     */
    protected final GifSource<S> source;

    /**
     * Whether to include all frames.
     */
    private boolean containsAll = false;

    /**
     * The frame indexes to be extracted.
     */
    private final Set<Integer> frames = new HashSet<>();

    public GifSourceBuilder(GifSource<S> gifSource) {
        this.source = gifSource;
    }

    public GifSourceBuilder<S> registerAll() {
        containsAll = true;
        return this;
    }

    public GifSourceBuilder<S> register(int frameIndex) {
        checkFrameIndex(frameIndex);
        frames.add(frameIndex);
        return this;
    }

    public GifSourceBuilder<S> register(int... frameIndexes) {
        for (int index : frameIndexes) {
            checkFrameIndex(index);
            frames.add(index);
        }
        return this;
    }

    public GifSourceBuilder<S> register(Range<Integer> range) {
        ObjectUtils.excNull(range, "Range is null.");
        checkFrameIndex(range.getMin());
        for (int i = range.getMin(); i <= range.getMax(); i++) {
            frames.add(i);
        }
        return this;
    }

    @Override
    protected List<BufferedImage> obtainSourceImages() throws IOException {
        checkReadiness();

        // the max frame size index of the gif
        int maxFrameIndex = source.size() - 1;

        // export all frame
        if (containsAll) {
            return source.readAll();
        }

        // export specified frames
        // check all frame was in bound
        List<String> invalidPages = frames.stream()
                .filter(p -> maxFrameIndex < p)
                .sorted()
                .map(Objects::toString)
                .collect(Collectors.toList());
        if (!CollectionUtils.isNullOrEmpty(invalidPages)) {
            throw new HandlingException(MessageFormat.format(
                    "Frame indexes:[{0}] has exceeded the max frame index of the gif document.",
                    StringUtils.join(invalidPages, ",")));
        }

        return source.read(frames.toArray(new Integer[0]));
    }

    protected void checkReadiness() {
        if (CollectionUtils.isNullOrEmpty(frames) && !containsAll) {
            throw new HandlingException("No frame indexes are registered.");
        }
    }

    private static void checkFrameIndex(int frameIndex) {
        if (frameIndex < 0) {
            throw new InvalidSettingException("Frame index must be greater than or equal to 0.");
        }
    }
}
