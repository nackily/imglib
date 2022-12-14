package cn.pipe.in;

import cn.core.in.GifSource;
import com.madgag.gif.fmsware.GifDecoder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract superclass of gif source.
 *
 * @param <T> The type of gif source.
 * @author tracy
 * @since 0.2.1
 */
public abstract class AbstractGifSource<T> implements GifSource<T> {

    /**
     * The source.
     */
    protected final T source;

    /**
     * The flag that indicating the completion of reading.
     */
    protected boolean readCompleted = false;

    /**
     * The gif decoder.
     */
    protected final GifDecoder decoder = new GifDecoder();


    protected AbstractGifSource(T source) {
        this.source = source;
    }

    @Override
    public int size() throws IOException {
        loadIfNot();
        return decoder.getFrameCount();
    }

    @Override
    public BufferedImage read(int frameIndex) throws IOException {
        loadIfNot();
        return decoder.getFrame(frameIndex);
    }

    @Override
    public List<BufferedImage> read(Integer[] frameIndexes) throws IOException {
        loadIfNot();
        List<BufferedImage> res = new ArrayList<>();
        for (Integer index : frameIndexes) {
            res.add(decoder.getFrame(index));
        }
        return res;
    }

    @Override
    public List<BufferedImage> readAll() throws IOException {
        loadIfNot();
        List<BufferedImage> res = new ArrayList<>();
        int size = decoder.getFrameCount();
        for (int i = 0; i < size; i++) {
            res.add(decoder.getFrame(i));
        }
        return res;
    }

    @Override
    public T getSource() {
        return source;
    }

    @Override
    public boolean isReadCompleted() {
        return readCompleted;
    }

    /**
     * Load the gif source if the source have not loaded.
     * @throws IOException If some I/O exceptions occurred when loading the gif source.
     */
    protected abstract void loadIfNot() throws IOException;
}
