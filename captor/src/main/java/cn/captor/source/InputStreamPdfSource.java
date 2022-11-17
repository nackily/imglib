package cn.captor.source;

import java.io.IOException;
import java.io.InputStream;

/**
 * InputStreamPdfSource
 *
 * @author tracy
 * @since 1.0.0
 */
public class InputStreamPdfSource implements PdfSource<InputStream> {

    private final InputStream source;

    public InputStreamPdfSource(InputStream source) {
        this.source = source;
    }

    @Override
    public String read() throws IOException {
        return null;
    }

    @Override
    public InputStream getSource() {
        return null;
    }

    @Override
    public boolean isReadCompleted() {
        return false;
    }
}
