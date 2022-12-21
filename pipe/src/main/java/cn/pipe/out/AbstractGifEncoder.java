package cn.pipe.out;

import cn.core.BufferedImageEncoder;
import cn.core.GenericBuilder;
import cn.core.ex.InvalidSettingException;
import cn.core.utils.CollectionUtils;
import cn.core.utils.ObjectUtils;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * An abstract superclass of encoder for buffered image that can encode image to GIF format.
 *
 * @author tracy
 * @since 0.2.1
 */
public abstract class AbstractGifEncoder implements BufferedImageEncoder {

    /**
     * The gif encoder.
     */
    protected AnimatedGifEncoder encoder;

    /**
     * the delay time between each frame.
     */
    protected int delay;

    /**
     * The number of times the set of GIF frames should be played.
     * Default is 1; 0 means play indefinitely.
     */
    protected int repeat;

    /**
     * Whether to reverse the order of images.
     */
    protected boolean reverse;

    protected AbstractGifEncoder(AbstractBuilder builder) {
        this.encoder = builder.encoder == null
                ? new AnimatedGifEncoder()
                : builder.encoder;
        this.delay = builder.delay;
        this.repeat = builder.repeat;
        this.reverse = builder.reverse;
    }

    @Override
    public final boolean supportMultiple() {
        return true;
    }

    @Override
    public void encode(List<BufferedImage> sources) throws IOException {
        ObjectUtils.excNull(sources, "Images to be encoded is null.");
        CollectionUtils.excEmpty(sources, "Images to be encoded is empty.");

        if (delay >= 0) {
            encoder.setDelay(delay);
        }
        if (repeat >= 0) {
            encoder.setRepeat(repeat);
        }

        // set the output
        setOutput();

        // reverse the images order
        if (reverse) {
            Collections.reverse(sources);
        }

        for (BufferedImage frame : sources) {
            encoder.addFrame(frame);
        }

        encoder.finish();
    }

    /**
     * 设置输出
     */
    protected abstract void setOutput();


    public abstract static class AbstractBuilder implements GenericBuilder<AbstractGifEncoder> {
        protected AnimatedGifEncoder encoder;
        protected int delay;
        protected int repeat;
        protected boolean reverse = false;

        public AbstractBuilder encoder(AnimatedGifEncoder encoder) {
            this.encoder = encoder;
            ObjectUtils.excNull(encoder, "AnimatedGifEncoder is null.");
            return this;
        }

        public AbstractBuilder delay(int delay) {
            this.delay = delay;
            if (delay < 0) {
                throw new InvalidSettingException("Delay time cannot be less than 0.");
            }
            return this;
        }

        public AbstractBuilder repeat(int repeat) {
            this.repeat = repeat;
            if (repeat < 0) {
                throw new InvalidSettingException("The number of times the set of GIF frames cannot be less than 0.");
            }
            return this;
        }

        public AbstractBuilder reverse() {
            this.reverse = true;
            return this;
        }
    }
}
