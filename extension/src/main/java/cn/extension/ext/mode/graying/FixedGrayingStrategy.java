package cn.extension.ext.mode.graying;

import cn.extension.ext.mode.AbstractGrayingStrategy;

/**
 * 固定分量的灰度化策略
 *
 * @author tracy
 * @since 1.0.0
 */
public class FixedGrayingStrategy extends AbstractGrayingStrategy {

    private final FixedOption fo;

    public FixedGrayingStrategy() {
        this.fo = FixedOption.R;
    }

    public FixedGrayingStrategy(FixedOption fo) {
        this.fo = fo;
    }

    @Override
    public int getGraynessValue(int r, int g, int b) {
        return fo.chose(r, g, b);
    }


    public interface FixedOption {

        /**
         * 选择一个分量
         * @param r R
         * @param g G
         * @param b B
         * @return 分量值
         */
        int chose(int r, int g, int b);

        FixedOption R = (o1, o2, o3) -> o1;

        FixedOption G = (o1, o2, o3) -> o2;

        FixedOption B = (o1, o2, o3) -> o3;
    }
}
