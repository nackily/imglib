package cn.pipe.out;

import cn.core.utils.ObjectUtils;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import java.io.OutputStream;

/**
 * GifOutputStreamEncoder
 *
 * @author tracy
 * @since 0.2.1
 */
public class GifOutputStreamEncoder extends AbstractGifEncoder {

    private final OutputStream stream;

    public GifOutputStreamEncoder(Builder builder) {
        super(builder);
        this.stream = builder.stream;
    }

    @Override
    protected void setOutput() {
        encoder.start(stream);
    }

    public static class Builder extends AbstractBuilder {
        private OutputStream stream;

        public Builder outputStream(OutputStream stream) {
            ObjectUtils.excNull(stream, "OutputStream is null");
            this.stream = stream;
            return this;
        }

        @Override
        public Builder encoder(AnimatedGifEncoder encoder) {
            return (Builder) super.encoder(encoder);
        }

        @Override
        public Builder delay(int delay) {
            return (Builder) super.delay(delay);
        }

        @Override
        public Builder repeat(int repeat) {
            return (Builder) super.repeat(repeat);
        }

        @Override
        public Builder reverse() {
            return (Builder) super.reverse();
        }

        @Override
        public AbstractGifEncoder build() {
            ObjectUtils.excNull(stream, "OutputStream has not been set.");
            return new GifOutputStreamEncoder(this);
        }
    }

}
