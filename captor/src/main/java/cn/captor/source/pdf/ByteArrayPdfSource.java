package cn.captor.source.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

/**
 * ByteArrayPdfSource
 *
 * @author tracy
 * @since 1.0.0
 */
public class ByteArrayPdfSource extends AbstractPdfBoxSource<byte[]> {

    public ByteArrayPdfSource(byte[] source) {
        super(source);
    }

    @Override
    protected void loadIfNot() throws IOException {
        if (readCompleted) {
            return;
        }
        pdf = PDDocument.load(source);
        readCompleted = true;
    }
}
