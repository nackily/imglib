package cn.pipe.in.gif;

import com.madgag.gif.fmsware.GifDecoder;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FileGifSourceTest {

    @Test
    public void test_doLoad() throws IOException {
        // given
        String path = Objects.requireNonNull(
                ClassLoader.getSystemClassLoader().getResource("input/seasons.gif")
        ).getPath();
        File file = new File(path);

        // when
        FileGifSource source = new FileGifSource(file);
        int status = source.doLoad();

        // then
        Assert.assertEquals(GifDecoder.STATUS_OK, status);
    }

}