package com.huitong.monitortracker.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * NormalUtils Tester.
 *
 * @author <zhe.qiushui>
 * @version 1.0
 * @since <pre>3,21, 2021</pre>
 */
public class NormalUtilsTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: formatDateToNormal(Date date)
     */
    @Test
    public void testFormatDateToNormal() throws Exception {
        Date date = null;
        String output = NormalUtils.formatDateToNormal(date);
        Assert.assertNotNull(output);
    }

    /**
     * Method: formatNumberToString(Object obj)
     */
    @Test
    public void testFormatNumberToString() throws Exception {
        Long testLong = null;
        String output = NormalUtils.formatNumberToString(testLong);
        Assert.assertNull(output);

        testLong = 100L;
        output = NormalUtils.formatNumberToString(testLong);
        Assert.assertEquals("100", output);
    }


} 
