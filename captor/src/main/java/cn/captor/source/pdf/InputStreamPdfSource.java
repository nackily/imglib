package cn.captor.source.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.io.InputStream;

/**
 * InputStreamPdfSource
 *
 * @author tracy
 * @since 1.0.0
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
