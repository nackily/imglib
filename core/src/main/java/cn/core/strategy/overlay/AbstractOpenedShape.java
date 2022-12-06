package cn.core.strategy.overlay;

import cn.core.strategy.Shape;
import cn.core.GenericBuilder;
import java.awt.*;

/**
 * 开口图像
 *
 * @author tracy
 * @since 1.0.0
 */
public abstract class AbstractOpenedShape implements Shape {

    /**
     * 笔画样式，例如 BasicStroke
     */
    protected Stroke stroke;

    /**
     * 颜色
     */
    protected Color color;


    protected AbstractOpenedShape(AbstractOpenedShapeBuilder csb) {
        this.stroke = csb.stroke;
        this.color = csb.color;
    }

    @Override
    public void paint(int canvasWidth, int canvasHeight, Graphics2D g2d) {
        // the default draw color is black
        g2d.setColor(color == null ? Color.BLACK : color);
        // the default store is basic stroke
        g2d.setStroke(stroke == null ? new BasicStroke() : stroke);
        draw(canvasWidth, canvasHeight, g2d);
    }

    /**
     * 绘制
     * @param canvasWidth 画笔宽
     * @param canvasHeight 画笔高
     * @param g2d 画笔
     */
    public abstract void draw(int canvasWidth, int canvasHeight, Graphics2D g2d);


    public abstract static class AbstractOpenedShapeBuilder implements GenericBuilder<AbstractOpenedShape> {
        protected Stroke stroke;
        protected Color color;

        public AbstractOpenedShapeBuilder stroke(Stroke stroke){
            this.stroke = stroke;
            return this;
        }
        public AbstractOpenedShapeBuilder color(Color color){
            this.color = color;
            return this;
        }
    }
}