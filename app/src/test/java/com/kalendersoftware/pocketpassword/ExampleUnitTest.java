package com.kalendersoftware.pocketpassword;

import android.content.Context;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Mock
    Context context;

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}