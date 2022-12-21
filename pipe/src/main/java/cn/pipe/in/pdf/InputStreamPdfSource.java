package cn.pipe.in.pdf;

import cn.pipe.in.AbstractPdfBoxSource;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.io.InputStream;

/**
 * A pdf source that from input stream.
 *
 * @author tracy
 * @since 0.2.1
 */
public class InputStreamPdfSource extends AbstractPdfBoxSource<InputStream> {

    public InputStreamPdfSource(InputStream source) {
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
