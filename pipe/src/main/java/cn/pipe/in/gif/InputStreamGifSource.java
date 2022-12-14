package cn.pipe.in.gif;

import cn.pipe.in.AbstractGifSource;

import java.io.IOException;
import java.io.InputStream;

/**
 * A gif source that from input stream.
 *
 * @author tracy
 * @since 0.2.1
 */
public class InputStreamGifSource extends AbstractGifSource<InputStream> {

    public InputStreamGifSource(InputStream source) {
        super(source);
    }

    @Override
    protected void loadIfNot() throws IOException {
        if (readCompleted) {
            return;
        }
        decoder.read(source);
        readCompleted = true;
    }
}
