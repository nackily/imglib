package cn.t8s.mode.graying;

import cn.core.strategy.mode.AbstractGrayingStrategy;

/**
 * 平均值灰度化策略
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
