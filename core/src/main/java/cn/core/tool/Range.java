package cn.core.tool;

import cn.core.ex.InvalidSettingException;
import cn.core.utils.ObjectUtils;

/**
 * Indicates a range whose boundary is [{@link Range#min}, {@link Range#max}].
 *
 * @param <T> The type of range's element.
 * @author tracy
 * @since 0.2.1
 */
public class Range<T extends Comparable<T>> {

    private final T min;

    private final T max;

    public Range(T min, T max) {
        ObjectUtils.excNull(min, "The lower bound of the range is null.");
        ObjectUtils.excNull(max, "The upper bound of the range is null.");
        if (min.compareTo(max) > 0) {
            throw new InvalidSettingException("The lower bound has crossed the upper bound.");
        }
        this.min = min;
        this.max = max;
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    public boolean within(T val) {
        return !notWithin(val);
    }

    public boolean notWithin(T val) {
        boolean lessOfMin = val.compareTo(min) < 0;
        boolean greatOfMax = val.compareTo(max) > 0;
        return lessOfMin || greatOfMax;
    }

    public static Range<Short> ofShort(Short min, Short max) {
        return new Range<>(min, max);
    }

    public static Range<Integer> ofInt(Integer min, Integer max) {
        return new Range<>(min, max);
    }

    public static Range<Long> ofLong(Long min, Long max) {
        return new Range<>(min, max);
    }

    public static Range<Float> ofFloat(Float min, Float max) {
        return new Range<>(min, max);
    }

    public static Range<Double> ofDouble(Double min, Double max) {return new Range<>(min, max); }

}
