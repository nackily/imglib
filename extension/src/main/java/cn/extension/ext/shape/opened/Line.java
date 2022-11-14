package cn.extension.ext.shape.opened;

import cn.extension.exec.HandlingException;
import cn.extension.exec.InvalidSettingException;
import cn.extension.ext.shape.AbstractOpenedShape;

import java.awt.*;

/**
 * 直线
 *
 * @author tracy
 * @since 1.0.0
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
            throw new HandlingException("the starting point is out of bounds of this image");
        }
        if (end.x > canvasWidth || end.y > canvasHeight) {
            throw new HandlingException("the ending point is out of bounds of this image");
        }
        g2d.drawLine(start.x, start.y, end.x, end.y);
    }

    public static class Builder extends OpenedShapeBuilder {
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
            if (start == null) {
                throw new InvalidSettingException("the starting point cannot be null");
            }
            this.start = start;
            return this;
        }

        public Builder end(Point end) {
            if (end == null) {
                throw new InvalidSettingException("the ending point cannot be null");
            }
            this.end = end;
            return this;
        }

        @Override
        public AbstractOpenedShape build() {
            if (start == null || end == null) {
                throw new InvalidSettingException("both start and end points are not specified");
            }
            return new Line(this);
        }
    }
}
