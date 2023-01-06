package cn.core.utils;

import cn.core.ex.InvalidSettingException;
import cn.core.ex.UnsupportedFormatException;
import cn.core.tool.Range;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;

/**
 * An util class for buffered image.
 *
 * @author tracy
 * @since 0.2.1
 */
public final class BufferedImageUtils {

    private BufferedImageUtils(){}

    public static BufferedImage newBackgroundImage(int width, int height, Color fillColor) {
        return newBackgroundImage(1f, width, height, fillColor);
    }

    /**
     * Create a background image.
     *
     * @param alpha The alpha of image.
     * @param width The width of image.
     * @param height The height of image.
     * @param fillColor The fill color of image.
     * @return The final created image.
     */
    public static BufferedImage newBackgroundImage(float alpha, int width, int height, Color fillColor) {
        if (alpha == 0) {
            return newTransparentImage(width, height);
        } else {
            return newColoredImage(width, height, alpha, fillColor);
        }
    }

    /**
     * Create a transparent image.
     *
     * @param width The width of image.
     * @param height The height of image.
     * @return The final created image.
     */
    public static BufferedImage newTransparentImage(int width, int height) {
        if (width <= 0) {
            throw new InvalidSettingException("The image width must be greater than 0.");
        }
        if (height <= 0) {
            throw new InvalidSettingException("The image height must be greater than 0.");
        }
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        // make the background transparent
        bi = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g.dispose();
        return bi;
    }

    /**
     * Create a colored image.
     *
     * @param width The width of image.
     * @param height The height of image.
     * @param alpha The alpha of image.
     * @param c The color of image.
     * @return The final created image.
     */
    public static BufferedImage newColoredImage(int width, int height, float alpha, Color c) {
        if (Range.ofFloat(0f, 1f).notWithin(alpha)) {
            throw new InvalidSettingException("Alpha out of bounds:[0, 1].");
        }
        ObjectUtils.excNull(c, "The color is null.");
        BufferedImage image = newTransparentImage(width, height);

        Graphics2D g = image.createGraphics();
        g.setColor(c);
        // AlphaComposite.SRC should be added before drawing anything in image with alpha if you want to secure source color.
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, alpha));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.dispose();

        return image;
    }

    /**
     * Returns a {@link BufferedImage} with the specified image type, where the
     * graphical content is a copy of the specified image.
     *
     * @param source the image to copy
     * @param imageType	the image type for the image to return
     * @return target image
     */
    public static BufferedImage copy(BufferedImage source, int imageType) {
        BufferedImage target = new BufferedImage(source.getWidth(), source.getHeight(), imageType);
        Graphics g = target.createGraphics();

        g.drawImage(source, 0, 0, null);

        g.dispose();
        return target;
    }

    public static void write(BufferedImage img, String filename, String formatName) throws IOException {
        if (StringUtils.isEmpty(filename)) {
            throw new InvalidSettingException("Output file name is null.");
        }
        write(img, formatName, new File(filename));
    }

    public static void write(BufferedImage img, String filename) throws IOException {
        if (StringUtils.isEmpty(filename)) {
            throw new InvalidSettingException("Output file name is null.");
        }
        // get the file's extension
        String formatName = StringUtils.getExtensionName(filename);
        write(img, formatName, new File(filename));
    }

    public static void write(BufferedImage img, File f) throws IOException {
        ObjectUtils.excNull(f, "Output file is null.");
        // get the file's extension
        String formatName = StringUtils.getExtensionName(f);
        write(img, formatName, f);
    }

    public static void write(BufferedImage img, String formatName, File f) throws IOException {
        ObjectUtils.excNull(img, "Buffered image is null.");
        ObjectUtils.excNull(f, "Output file is null.");
        if (StringUtils.isEmpty(formatName)) {
            throw new NullPointerException("Output format name is null.");
        }

        // check for available writers for the current output format name
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
        if (!writers.hasNext()) {
            throw new UnsupportedFormatException(MessageFormat.format(
                    "No suitable ImageWriter found for {0}.", formatName));
        }
        ImageWriter writer = writers.next();
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        FileOutputStream fos = new FileOutputStream(f);
        ImageOutputStream ios = ImageIO.createImageOutputStream(fos);

        // fix the pink background for jpeg/bmp format
        boolean jpg = "JPG".equalsIgnoreCase(formatName);
        boolean jpeg = "JPEG".equalsIgnoreCase(formatName);
        boolean bmp = "BMP".equalsIgnoreCase(formatName);
        if (jpg || jpeg || bmp) {
            img = copy(img, BufferedImage.TYPE_INT_RGB);
        }

        // do write file
        writer.setOutput(ios);
        writer.write(null, new IIOImage(img, null, null), iwp);

        // release resources
        writer.dispose();
        ios.close();
        fos.close();
    }

}
