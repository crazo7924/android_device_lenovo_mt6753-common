package com.mediatek.internal.telephony;

import android.telephony.PhoneNumberUtils;

import java.util.Arrays;

/**
 * {@hide}
 */
public class CallForwardInfoEx {
    public int             status;      /*1 = active, 0 = not active */
    public int             reason;      /* from TS 27.007 7.11 "reason" */
    public int             serviceClass; /* Sum of CommandsInterface.SERVICE_CLASS */
    public int             toa;         /* "type" from TS 27.007 7.11 */
    public String          number;      /* "number" from TS 27.007 7.11 */
    public int             timeSeconds; /* for CF no reply only */
    public long[]          timeSlot;    /* Time slot of CF */

    @Override
    public String toString() {
        return super.toString() + (status == 0 ? " not active " : " active ")
            + " reason: " + reason
            + " serviceClass: " + serviceClass
            + " \"" + PhoneNumberUtils.stringFromStringAndTOA(number, toa) + "\" "
            + timeSeconds + " seconds"
            + " timeSlot: " + Arrays.toString(timeSlot);
    }
}
