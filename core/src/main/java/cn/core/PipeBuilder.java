package cn.core;

import cn.core.exec.InvalidSettingException;
import cn.core.utils.BufferedImageUtils;
import cn.core.utils.StringUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

/**
 * 管道构造器
 *
 * @author tracy
 * @since 1.0.0
 */
public abstract class PipeBuilder<S> {

    /**
     * 输出格式
     */
    protected String formatName;

    protected void setFormatName(String formatName) {
        if (formatName == null || "".equals(formatName)) {
            throw new InvalidSettingException("format name can not be null");
        }
        this.formatName = formatName;
    }

    /**
     * 获得一张图像
     * @return 图像
     * @throws IOException IO异常
     */
    public abstract BufferedImage obtainBufferedImage() throws IOException;

    /**
     * 获取图像
     * @return 图像集合
     * @throws IOException IO异常
     */
    public abstract List<BufferedImage> obtainBufferedImages() throws IOException;

    public void toFile(File dest) throws IOException {
        BufferedImage image = obtainBufferedImage();
        BufferedImageUtils.write(image, formatName, choseFormat(dest));
    }

    public void toFile(String fn) throws IOException {
        toFile(new File(fn));
    }

    public void toFiles(Iterator<File> fIter) throws IOException {
        if (fIter == null) {
            throw new NullPointerException("File iterator is null");
        }

        List<BufferedImage> images = obtainBufferedImages();
        for (BufferedImage o : images) {
            if (!fIter.hasNext()) {
                throw new IndexOutOfBoundsException("not enough OutputStream provided by iterator");
            }
            BufferedImageUtils.write(o, formatName, choseFormat(fIter.next()));
        }
    }

    public void toOutputStream(OutputStream os) throws IOException {
        BufferedImage image = obtainBufferedImage();
        ImageIO.write(image, formatName, os);
    }

    public void toOutputStreams(Iterator<OutputStream> osIter) throws IOException {
        if (osIter == null) {
            throw new NullPointerException("OutputStream iterator is null");
        }

        List<BufferedImage> images = obtainBufferedImages();
        for (BufferedImage o : images) {
            if (!osIter.hasNext()) {
                throw new IndexOutOfBoundsException("not enough OutputStream provided by iterator");
            }
            ImageIO.write(o, formatName, osIter.next());
        }
    }

    private File choseFormat(File f) {
        if (f == null) {
            throw new NullPointerException("output file is null");
        }
        String extension = StringUtils.getExtensionName(f);

        if (StringUtils.isEmpty(formatName)) {
            if (StringUtils.isEmpty(extension)) {
                throw new InvalidSettingException("no output format was specified");
            }
            // initial the output format name
            formatName = extension;
        } else {
            // adjust the output file's if necessary
            if (!extension.equalsIgnoreCase(formatName)) {
                String newFilename = f.getAbsolutePath() + "." + formatName;
                return new File(newFilename);
            }
        }
        return f;
    }

}
