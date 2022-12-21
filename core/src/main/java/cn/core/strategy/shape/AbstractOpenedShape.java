package cn.core.strategy.shape;

import cn.core.strategy.Shape;
import cn.core.GenericBuilder;
import java.awt.*;

/**
 * An abstract superclass of all opened shapes.
 *
 * @author tracy
 * @since 0.2.1
 */
public abstract class AbstractOpenedShape implements Shape {

    /**
     * The stroke.
     */
    protected Stroke stroke;

    /**
     * The color.
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
     * Draw this shape.
     *
     * @param canvasWidth The canvas width.
     * @param canvasHeight The canvas height.
     * @param g2d The Graphics2D object of original image.
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
