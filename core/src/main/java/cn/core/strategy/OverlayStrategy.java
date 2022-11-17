package cn.core.strategy;

import java.awt.*;

/**
 * 叠加策略
 *
 * @author tracy
 * @since 1.0.0
 */
public interface OverlayStrategy {

    /**
     * 绘制
     * @param canvasWidth 画布宽度
     * @param canvasHeight 画布高度
     * @param g2d 画笔
     */
    void paint(int canvasWidth, int canvasHeight, Graphics2D g2d);
}
