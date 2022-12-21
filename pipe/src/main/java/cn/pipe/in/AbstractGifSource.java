package cn.pipe.in;

import cn.core.in.GifSource;
import com.madgag.gif.fmsware.GifDecoder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AbstractGifSource
 *
 * @author tracy
 * @since 0.2.1
 */
public abstract class AbstractGifSource<T> implements GifSource<T> {

    protected final T source;
    protected boolean readCompleted = false;
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
     * 如果未加载 PDF，则进行加载
     * @throws IOException IO异常
     */
    protected abstract void loadIfNot() throws IOException;
}
