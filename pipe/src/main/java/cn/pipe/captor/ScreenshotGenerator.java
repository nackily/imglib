package cn.pipe.captor;

import cn.core.GenericBuilder;
import cn.core.ImageGenerator;
import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.core.tool.Range;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;

/**
 * An image generator that can capture a screenshot.
 *
 * @author tracy
 * @since 0.2.1
 */
public class ScreenshotGenerator extends Robot implements ImageGenerator {

    /**
     * The x-coordinate of the upper left corner of the screenshot.
     */
    private final int x;

    /**
     * The y-coordinate of the upper left corner of the screenshot.
     */
    private final int y;

    /**
     * The width of the screenshot.
     */
    private final int width;

    /**
     * The height of the screenshot.
     */
    private final int height;

    public ScreenshotGenerator(Builder bu) throws AWTException {
        super();
        this.x = bu.x;
        this.y = bu.y;
        this.width = bu.width;
        this.height = bu.height;
    }

    @Override
    public BufferedImage generate() {
        return createScreenCapture(new Rectangle(x, y, width, height));
    }

    public static class Builder implements GenericBuilder<ScreenshotGenerator> {

        public static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();

        private int x = 0;
        private int y = 0;
        private int width;
        private int height;

        public Builder startPoint(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        @Override
        public ScreenshotGenerator build() {
            if (Range.ofInt(0, SCREEN_DIMENSION.width).notWithin(x)) {
                throw new InvalidSettingException("Start point's x is out of bounds.");
            }
            if (Range.ofInt(0, SCREEN_DIMENSION.height).notWithin(y)) {
                throw new InvalidSettingException("Start point's y is out of bounds.");
            }
            if (Range.ofInt(1, SCREEN_DIMENSION.width).notWithin(width)) {
                throw new InvalidSettingException("The area's width is out of bounds.");
            }
            if (Range.ofInt(1, SCREEN_DIMENSION.height).notWithin(height)) {
                throw new InvalidSettingException("The area's height is out of bounds.");
            }

            if (Range.ofInt(0, SCREEN_DIMENSION.width).notWithin(x + width)) {
                throw new InvalidSettingException(MessageFormat
                        .format("The screenshot area(x={0}, width={1}) is out of screen width bounds[0, {2}].",
                                x,
                                width,
                                SCREEN_DIMENSION.width));
            }
            if (Range.ofInt(0, SCREEN_DIMENSION.height).notWithin(y + height)) {
                throw new InvalidSettingException(MessageFormat
                        .format("The screenshot area(y={0}, height={1}) is out of screen height bounds[0, {2}].",
                                y,
                                height,
                                SCREEN_DIMENSION.height));
            }

            ScreenshotGenerator generator;
            try {
                generator = new ScreenshotGenerator(this);
            } catch (AWTException e){
                throw new HandlingException(e);
            }
            return generator;
        }
    }

}
