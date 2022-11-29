package cn.t8s.overlay.shape.closed;

import cn.core.exec.InvalidSettingException;
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
        public Rect.Builder rect(Rectangle rect) {
            return (Rect.Builder) super.rect(rect);
        }

        @Override
        public Rect.Builder stroke(Stroke stroke) {
            return (Rect.Builder) super.stroke(stroke);
        }

        @Override
        public Rect.Builder fill(boolean fill) {
            return (Rect.Builder) super.fill(fill);
        }

        @Override
        public Rect.Builder color(Color color) {
            return (Rect.Builder) super.color(color);
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
