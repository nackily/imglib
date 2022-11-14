package cn.extension.ext.mode.graying;

import cn.extension.ext.mode.AbstractGrayingStrategy;

/**
 * 最大值灰度化策略
 *
 * @author tracy
 * @since 1.0.0
 */
public class MaxGrayingStrategy extends AbstractGrayingStrategy {
    @Override
    public int getGraynessValue(int r, int g, int b) {
        return Math.max(Math.max(r, g), b);
    }
}
