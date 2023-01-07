package cn.pipe.in.pdf;

import cn.pipe.in.AbstractPdfBoxSource;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

/**
 * A pdf source that from byte array.
 *
 * @author tracy
 * @since 0.2.1
 */
public class ByteArrayPdfSource extends AbstractPdfBoxSource<byte[]> {

    public ByteArrayPdfSource(byte[] source) {
        super(source);
    }

    @Override
    protected PDDocument doLoad() throws IOException {
        return PDDocument.load(source);
    }
}
