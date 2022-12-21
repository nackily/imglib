package cn.t8s.mode.graying;

import cn.core.strategy.mode.AbstractGrayingStrategy;

/**
 * 最小值灰度化策略
 *
 * @author tracy
 * @since 0.2.1
 */
public class MinGrayingStrategy extends AbstractGrayingStrategy {
    @Override
    public int getGraynessValue(int r, int g, int b) {
        return Math.min(Math.min(r, g), b);
    }
}
