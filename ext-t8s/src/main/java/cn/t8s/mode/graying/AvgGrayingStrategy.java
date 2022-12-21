package cn.t8s.mode.graying;

import cn.core.strategy.mode.AbstractGrayingStrategy;

/**
 * An average graying strategy. The gray value of any pixel is equal to the average value
 * of the red component, green component and blue component.
 *
 * @author tracy
 * @since 0.2.1
 */
public class AvgGrayingStrategy extends AbstractGrayingStrategy {
    @Override
    public int getGraynessValue(int r, int g, int b) {
        return (r + g + b) / 3;
    }
}
