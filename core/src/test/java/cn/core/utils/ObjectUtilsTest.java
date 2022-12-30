package cn.core.utils;

import org.junit.Assert;
import org.junit.Test;

public class ObjectUtilsTest {

    @Test
    public void testEx_nullObject() {
        String exceptionMsg = "Object is null.";
        NullPointerException exception = Assert.assertThrows(NullPointerException.class,
                () -> ObjectUtils.excNull(null, exceptionMsg));
        Assert.assertEquals(exceptionMsg,
                exception.getMessage());
    }
}