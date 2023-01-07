package cn.pipe.in.gif;

import cn.pipe.TestUtils;
import com.madgag.gif.fmsware.GifDecoder;
import org.apache.pdfbox.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayGifSourceTest {

    @Test
    public void test_doLoad() throws IOException {
        // given
        InputStream is = TestUtils.getResourceStream("input/seasons.gif");
        byte[] bytes = IOUtils.toByteArray(is);

        // when
        ByteArrayGifSource source = new ByteArrayGifSource(bytes);
        int status = source.doLoad();

        // then
        Assert.assertEquals(GifDecoder.STATUS_OK, status);
    }

}