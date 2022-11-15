package cn.captor.gen;

import cn.core.ImageGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 屏幕快照构建器
 *
 * @author tracy
 * @since 1.0.0
 */
public class ScreenshotGenerator extends Robot implements ImageGenerator {

    public ScreenshotGenerator() throws AWTException {
        super();
    }
    public ScreenshotGenerator(GraphicsDevice screen) throws AWTException {
        super(screen);
    }

    @Override
    public BufferedImage generate() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return createScreenCapture(new Rectangle(screenSize));
    }

}
