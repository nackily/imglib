package cn.captor.impl;

import cn.captor.ImageCaptor;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 屏幕图像构建器
 *
 * @author tracy
 * @since 1.0.0
 */
public class ScreenImageCaptor extends Robot implements ImageCaptor {

    public ScreenImageCaptor() throws AWTException {
        super();
    }
    public ScreenImageCaptor(GraphicsDevice screen) throws AWTException {
        super(screen);
    }

    @Override
    public BufferedImage obtain() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return createScreenCapture(new Rectangle(screenSize));
    }

}
