package cn.t8s.overlay.shape.closed;

import cn.core.ex.InvalidSettingException;
import cn.core.strategy.overlay.AbstractClosedShapeStrategy;
import java.awt.*;

/**
 * 椭圆形
 *
 * @author tracy
 * @since 1.0.0
 */
public class Oval extends AbstractClosedShapeStrategy {


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
            if (rect == null) {
                throw new InvalidSettingException("not specified any rect for this shape");
            }
            return new Oval(this);
        }
    }
}
