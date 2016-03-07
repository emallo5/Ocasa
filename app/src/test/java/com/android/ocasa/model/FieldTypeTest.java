package com.android.ocasa.model;

import com.android.ocasa.BuildConfig;
import com.android.ocasa.CustomTestRunner;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by ignacio on 07/03/16.
 */
@RunWith(CustomTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class FieldTypeTest {

    @Test
    public void testInvalidMonthCheckDateValue(){
        String date = "2015/13/04";

        assertFalse(FieldType.DATE.isValidValue(date));
    }

    @Test
    public void testInvalidDayCheckDateValue(){
        String date = "2015/12/34";

        assertFalse(FieldType.DATE.isValidValue(date));
    }

    @Test
    public void testCheckDateValue(){
        String date = "2015/01/07";

        assertTrue(FieldType.DATE.isValidValue(date));
    }

    @Test
    public void testInavildMinuteCheckTimeValue(){
        String date = "12:65";

        assertFalse(FieldType.TIME.isValidValue(date));
    }

    @Test
    public void testInavildHourCheckTimeValue(){
        String date = "24:56";

        assertFalse(FieldType.TIME.isValidValue(date));
    }

    @Test
    public void testCheckTimeValue(){
        String date = "23:56";

        assertTrue(FieldType.TIME.isValidValue(date));
    }

    @Test
    public void testFormatMap(){
        LatLng location = new LatLng(-38.6453, 24.5732);

        assertEquals("-38.6453,24.5732", FieldType.MAP.format(location));
    }

    @Test
    public void testParseMap(){
        String value = "-38.6453,24.5732";

        assertEquals(new LatLng(-38.6453, 24.5732), FieldType.MAP.parse(value));
    }
}
