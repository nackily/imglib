package cn.core.tool;

import cn.core.ex.InvalidSettingException;
import org.junit.Assert;
import org.junit.Test;
import java.math.BigDecimal;

public class RangeTest {

    @Test
    public void test_notWithin_correctUsage() {
        // given
        BigDecimal lower = BigDecimal.ONE;
        BigDecimal upper = BigDecimal.valueOf(20);
        Range<BigDecimal> range = new Range<>(lower, upper);

        // when
        boolean notWithin = range.notWithin(BigDecimal.valueOf(58));

        // then
        Assert.assertTrue(notWithin);
        Assert.assertEquals(lower, range.getMin());
        Assert.assertEquals(upper, range.getMax());
    }

    @Test
    public void testEx_constructor_nullMin() {
        NullPointerException exception = Assert.assertThrows(NullPointerException.class,
                () -> new Range<>(null, 10));
        Assert.assertEquals("The lower bound of the range is null.",
                exception.getMessage());
    }

    @Test
    public void testEx_constructor_nullMax() {
        NullPointerException exception = Assert.assertThrows(NullPointerException.class,
                () -> new Range<>(2, null));
        Assert.assertEquals("The upper bound of the range is null.",
                exception.getMessage());
    }

    @Test
    public void testEx_constructor_minCrossedMax() {
        InvalidSettingException exception = Assert.assertThrows(InvalidSettingException.class,
                () -> new Range<>(20, 17));
        Assert.assertEquals("The lower bound has crossed the upper bound.",
                exception.getMessage());
    }

}