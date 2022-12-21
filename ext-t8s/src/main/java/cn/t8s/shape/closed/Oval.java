package cn.t8s.shape.closed;

import cn.core.strategy.shape.AbstractClosedShape;
import cn.core.utils.ObjectUtils;

import java.awt.*;

/**
 * A closed shape of oval.
 *
 * @author tracy
 * @since 0.2.1
 */
public class Oval extends AbstractClosedShape {


    public Oval(Builder csb) {
        super(csb);
    }

    @Override
    public void drawBorder(int canvasWidth, int canvasHeight, Graphics2D g2d) {
        g2d.drawOval(rect.x, rect.y, rect.width, rect.height);
    }

    @Override
    public void fillInside(int canvasWidth, int canvasHeight, Graphics2D g2d) {
        g2d.fillOval(rect.x, rect.y, rect.width, rect.height);
    }

    public static class Builder extends AbstractClosedShapeBuilder {

        @Override
        public Oval.Builder rect(Rectangle rect) {
            return (Oval.Builder) super.rect(rect);
        }

        @Override
        public Oval.Builder stroke(Stroke stroke) {
            return (Oval.Builder) super.stroke(stroke);
        }

        @Override
        public Oval.Builder fill(boolean fill) {
            return (Oval.Builder) super.fill(fill);
        }

        @Override
        public Oval.Builder color(Color color) {
            return (Oval.Builder) super.color(color);
        }

        @Override
        public Oval build() {
            ObjectUtils.excNull(rect, "Rectangle for this oval not specified.");
            return new Oval(this);
        }
    }
}
