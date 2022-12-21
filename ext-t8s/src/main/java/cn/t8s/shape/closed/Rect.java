package cn.t8s.shape.closed;

import cn.core.strategy.shape.AbstractClosedShape;
import cn.core.utils.ObjectUtils;

import java.awt.*;

/**
 * A closed shape of rectangle.
 *
 * @author tracy
 * @since 0.2.1
 */
public class Rect extends AbstractClosedShape {

    public Rect(Builder bu) {
        super(bu);
    }

    @Override
    public void drawBorder(int canvasWidth, int canvasHeight, Graphics2D g2d) {
        g2d.drawRect(rect.x, rect.y, rect.width, rect.height);
    }

    @Override
    public void fillInside(int canvasWidth, int canvasHeight, Graphics2D g2d) {
        g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
    }


    public static class Builder extends AbstractClosedShapeBuilder {

        @Override
        public Builder rect(Rectangle rect) {
            return (Builder) super.rect(rect);
        }

        @Override
        public Builder stroke(Stroke stroke) {
            return (Builder) super.stroke(stroke);
        }

        @Override
        public Builder fill(boolean fill) {
            return (Builder) super.fill(fill);
        }

        @Override
        public Builder color(Color color) {
            return (Builder) super.color(color);
        }

        @Override
        public Rect build() {
            ObjectUtils.excNull(rect, "Rectangle not specified.");
            return new Rect(this);
        }
    }
}
