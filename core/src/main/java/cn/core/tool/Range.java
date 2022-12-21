package cn.core.tool;

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
        this.min = min;
        this.max = max;
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    public boolean notWithin(T val) {
        boolean lessOfMin = val.compareTo(min) < 0;
        boolean greatOfMax = val.compareTo(max) > 0;
        return lessOfMin || greatOfMax;
    }

    public static Range<Integer> ofInt(int min, int max) {
        return new Range<>(min, max);
    }

    public static Range<Float> ofFloat(float min, float max) {
        return new Range<>(min, max);
    }

    public static Range<Double> ofDouble(double min, double max) {return new Range<>(min, max); }

}
