package cn.t8s.mode.graying;

import cn.core.strategy.mode.AbstractGrayingStrategy;

/**
 * A fixed graying strategy. The gray value of any pixel is equal to the selected component.
 * View {@link FixedOption} for all options, and the default option is {@link FixedOption#R}.
 *
 * @author tracy
 * @since 0.2.1
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
         * Chose a component.
         *
         * @param r The red component.
         * @param g The green component.
         * @param b The blue component.
         * @return The final gray value.
         */
        int chose(int r, int g, int b);

        FixedOption R = (o1, o2, o3) -> o1;

        FixedOption G = (o1, o2, o3) -> o2;

        FixedOption B = (o1, o2, o3) -> o3;
    }
}
