package cn.t8s.mode.graying;

import cn.core.ex.InvalidSettingException;
import cn.core.strategy.mode.AbstractGrayingStrategy;
import cn.core.GenericBuilder;
import cn.core.tool.Range;
import java.text.MessageFormat;

/**
 * A weight graying strategy. The gray value of any pixel is equal to the weighted sum of
 * the red component, green component and blue component.
 *
 * @author tracy
 * @since 0.2.1
 */
public class WeightGrayingStrategy extends AbstractGrayingStrategy {

    /**
     * The red component weight.
     */
    private final double redWeight;

    /**
     * The green component weight.
     */
    private final double greenWeight;

    public WeightGrayingStrategy(Builder bu) {
        this.redWeight = bu.redWeight;
        this.greenWeight = bu.greenWeight;
    }

    @Override
    public int getGraynessValue(int r, int g, int b) {
        double blueWeight = 1.0 - redWeight - greenWeight;
        // calculate the graying value
        int target = (int)(redWeight * r + greenWeight * g + blueWeight * b);
        // ensure the graying value are within the range
        target = Math.min(target, 255);
        return target;
    }

    public static class Builder implements GenericBuilder<WeightGrayingStrategy> {
        private static final Range<Double> WEIGHT_RANGE = Range.ofDouble(0f, 1f);

        private double redWeight = 0;
        private double greenWeight = 0;


        public Builder redWeight(double redWeight) {
            this.redWeight = redWeight;
            if (WEIGHT_RANGE.notWithin(redWeight)) {
                throw new InvalidSettingException(MessageFormat.format("Red weight out of bounds:[{0}, {1}].",
                        WEIGHT_RANGE.getMin(), WEIGHT_RANGE.getMax()));
            }
            return this;
        }
        public Builder greenWeight(double greenWeight) {
            this.greenWeight = greenWeight;
            if (WEIGHT_RANGE.notWithin(greenWeight)) {
                throw new InvalidSettingException(MessageFormat.format("Green weight out of bounds:[{0}, {1}].",
                        WEIGHT_RANGE.getMin(), WEIGHT_RANGE.getMax()));
            }
            return this;
        }

        @Override
        public WeightGrayingStrategy build() {
            // the default weight allocate rule is: Red = 0.3, Green = 0.59, Blue = 0.11
            if (redWeight == 0 && greenWeight == 0) {
                redWeight = 0.3f;
                greenWeight = 0.59f;
            }
            // check the setting is completed
            if (redWeight == 0 || greenWeight == 0) {
                throw new InvalidSettingException(MessageFormat.format("{0} weight not set.",
                        redWeight == 0 ? "Red" : "Green"));
            }
            // check the setting is rightful
            if (redWeight + greenWeight > 1.0) {
                throw new InvalidSettingException("The sum of red weight and green weight has exceeded 1.");
            }
            return new WeightGrayingStrategy(this);
        }
    }
}
