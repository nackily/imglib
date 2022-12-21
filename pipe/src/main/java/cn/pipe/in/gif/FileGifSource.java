package cn.pipe.in.gif;

import cn.pipe.in.AbstractGifSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * FileGifSource
 *
 * @author tracy
 * @since 0.2.1
 */
public class FileGifSource extends AbstractGifSource<File> {

    public FileGifSource(File source) {
        super(source);
    }

    @Override
    protected void loadIfNot() throws IOException {
        if (readCompleted) {
            return;
        }
        decoder.read(new FileInputStream(source));
        readCompleted = true;
    }
}
