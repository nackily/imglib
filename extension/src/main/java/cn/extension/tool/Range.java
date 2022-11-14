package cn.extension.tool;

/**
 * 范围
 *
 * @author tracy
 * @since 1.0.0
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
        if (lessOfMin || greatOfMax) {
            return true;
        }
        return false;
    }

    public static Range<Integer> ofInt(int min, int max) {
        return new Range<>(min, max);
    }

    public static Range<Float> ofFloat(float min, float max) {
        return new Range<>(min, max);
    }

    public static Range<Double> ofDouble(double min, double max) {return new Range<>(min, max); }

}
