package com.android.ocasa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(CustomTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
<<<<<<< HEAD
=======

        
>>>>>>> f2f7dc97e6bf035dd3c8ac160296ff3eb2fffb88
        assertEquals(4, 2 + 2);
    }
}