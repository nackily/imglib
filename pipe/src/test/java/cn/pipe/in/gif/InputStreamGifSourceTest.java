package cn.pipe.in.gif;

import cn.pipe.TestUtils;
import com.madgag.gif.fmsware.GifDecoder;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamGifSourceTest {

    @Test
    public void test_doLoad() throws IOException {
        // given
        InputStream is = TestUtils.getResourceStream("input/seasons.gif");

        // when
        InputStreamGifSource source = new InputStreamGifSource(is);
        int status = source.doLoad();

        // then
        Assert.assertEquals(GifDecoder.STATUS_OK, status);
    }
}