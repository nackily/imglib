package cn.t8s.mode.graying;

import cn.core.strategy.mode.AbstractGrayingStrategy;

/**
 * A maximum graying strategy. The gray value of any pixel is equal to the maximum value of
 * the red component, green component and blue component.
 *
 * @author tracy
 * @since 0.2.1
 */
public class MaxGrayingStrategy extends AbstractGrayingStrategy {
    @Override
    public int getGraynessValue(int r, int g, int b) {
        return Math.max(Math.max(r, g), b);
    }
}
