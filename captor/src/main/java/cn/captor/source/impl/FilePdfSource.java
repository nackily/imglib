package cn.captor.source.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.File;
import java.io.IOException;

/**
 * FilePdfSource
 *
 * @author tracy
 * @since 1.0.0
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
