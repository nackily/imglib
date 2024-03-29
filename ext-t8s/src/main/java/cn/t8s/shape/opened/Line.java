package cn.t8s.shape.opened;

import cn.core.ex.HandlingException;
import cn.core.strategy.shape.AbstractOpenedShape;
import cn.core.utils.ObjectUtils;
import java.awt.*;

/**
 * An opened shape of line.
 *
 * @author tracy
 * @since 0.2.1
 */
public class Line extends AbstractOpenedShape {

    /**
     * 起点
     */
    private final Point start;

    /**
     * 终点
     */
    private final Point end;

    public Line(Builder csb) {
        super(csb);
        this.start = csb.start;
        this.end = csb.end;
    }

    @Override
    public void draw(int canvasWidth, int canvasHeight, Graphics2D g2d) {
        if (start.x > canvasWidth || start.y > canvasHeight) {
            throw new HandlingException("The starting point is out of bounds of this image.");
        }
        if (end.x > canvasWidth || end.y > canvasHeight) {
            throw new HandlingException("The ending point is out of bounds of this image.");
        }
        g2d.drawLine(start.x, start.y, end.x, end.y);
    }

    public static class Builder extends AbstractOpenedShapeBuilder {
        private Point start;
        private Point end;

        @Override
        public Builder stroke(Stroke stroke) {
            return (Builder) super.stroke(stroke);
        }

        @Override
        public Builder color(Color color) {
            return (Builder) super.color(color);
        }

        public Builder start(Point start) {
            this.start = start;
            return this;
        }

        public Builder end(Point end) {
            this.end = end;
            return this;
        }

        @Override
        public Line build() {
            ObjectUtils.excNull(start, "The starting point is null.");
            ObjectUtils.excNull(end, "The ending point is null.");
            return new Line(this);
        }
    }
}
