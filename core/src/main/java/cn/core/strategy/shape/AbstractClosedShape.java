package cn.core.strategy.shape;

import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.core.strategy.Shape;
import cn.core.GenericBuilder;
import cn.core.utils.ObjectUtils;
import java.awt.*;

/**
 * An abstract superclass of all closed shapes.
 *
 * @author tracy
 * @since 0.2.1
 */
public abstract class AbstractClosedShape implements Shape {

    /**
     * The rectangle of the shape.
     */
    protected Rectangle rect;

    /**
     * The stroke.
     */
    protected Stroke stroke;

    /**
     * Whether to fill the shape.
     */
    protected boolean fill;

    /**
     * The color.
     */
    protected Color color;


    protected AbstractClosedShape(AbstractClosedShapeBuilder csb) {
        this.rect = csb.rect;
        this.stroke = csb.stroke;
        this.fill = csb.fill;
        this.color = csb.color;
    }

    @Override
    public void paint(int canvasWidth, int canvasHeight, Graphics2D g2d) {
        // check for rect is out of bounds
        if (rect == null) {
            throw new HandlingException("Rectangle for this shape not specified.");
        } else {
            int maxX = rect.x + rect.width;
            int maxY = rect.y + rect.height;
            if (maxX > canvasWidth || maxY > canvasHeight) {
                throw new HandlingException("The Rectangle is out of image.");
            }
        }

        if (fill) {
            // the default fill color is white
            g2d.setColor(color == null ? Color.WHITE : color);
            fillInside(canvasWidth, canvasHeight, g2d);
        } else {
            // the default draw color is black
            g2d.setColor(color == null ? Color.BLACK : color);
            // the default store is basic stroke
            g2d.setStroke(stroke == null ? new BasicStroke() : stroke);
            drawBorder(canvasWidth, canvasHeight, g2d);
        }
    }

    /**
     * Draw a border for this shape.
     *
     * @param canvasWidth The canvas width.
     * @param canvasHeight The canvas height.
     * @param g2d The Graphics2D object of original image.
     */
    public abstract void drawBorder(int canvasWidth, int canvasHeight, Graphics2D g2d);

    /**
     * Fill the rectangle for this shape.
     *
     * @param canvasWidth The canvas width.
     * @param canvasHeight The canvas height.
     * @param g2d The Graphics2D object of original image.
     */
    public abstract void fillInside(int canvasWidth, int canvasHeight, Graphics2D g2d);


    public abstract static class AbstractClosedShapeBuilder implements GenericBuilder<AbstractClosedShape> {
        protected Rectangle rect;
        protected Stroke stroke;
        protected boolean fill;
        protected Color color;

        public AbstractClosedShapeBuilder rect(Rectangle rect) {
            this.rect = rect;
            ObjectUtils.excNull(rect, "Rectangle is null.");
            return this;
        }
        public AbstractClosedShapeBuilder stroke(Stroke stroke){
            this.stroke = stroke;
            ObjectUtils.excNull(stroke, "Stroke is null.");
            return this;
        }
        public AbstractClosedShapeBuilder fill(boolean fill){
            this.fill = fill;
            return this;
        }
        public AbstractClosedShapeBuilder color(Color color){
            this.color = color;
            ObjectUtils.excNull(color, "Color is null.");
            return this;
        }
    }
}
