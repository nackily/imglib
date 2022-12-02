package cn.pipe.out;

import cn.core.ex.InvalidSettingException;
import cn.core.utils.ObjectUtils;
import cn.core.utils.StringUtils;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import java.io.File;

/**
 * GifFileEncoder
 *
 * @author tracy
 * @since 1.0.0
 */
public class GifFileEncoder extends AbstractGifEncoder {

    private final String filename;

    public GifFileEncoder(Builder builder) {
        super(builder);
        this.filename = builder.filename;
    }

    @Override
    protected void setOutput() {
        encoder.start(filename);
    }

    public static class Builder extends AbstractBuilder {
        private String filename;

        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder file(File file) {
            ObjectUtils.excNull(file, "file is null");
            this.filename = file.getAbsolutePath();
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
            if (StringUtils.isEmpty(filename)) {
                throw new InvalidSettingException("filename has not been set");
            }
            return new GifFileEncoder(this);
        }
    }

}
