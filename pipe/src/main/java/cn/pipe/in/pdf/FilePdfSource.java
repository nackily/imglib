package cn.pipe.in.pdf;

import cn.pipe.in.AbstractPdfBoxSource;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.File;
import java.io.IOException;

/**
 * A pdf source that from file.
 *
 * @author tracy
 * @since 0.2.1
 */
public class FilePdfSource extends AbstractPdfBoxSource<File> {

    public FilePdfSource(File source) {
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
