package hao.bk.com.vdmvsi;

import org.junit.Test;

import hao.bk.com.utils.TextUtils;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(TextUtils.equalTime("2016-06-12 02:37:04", "2016-06-12 02:37:04"),true);
    }
}