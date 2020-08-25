package com.mediatek.ims;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Provides the call forward information for the supplementary service configuration.
 *
 * @hide
 */
public class ImsCallForwardInfoEx implements Parcelable {
    // Refer to ImsUtInterface#CDIV_CF_XXX
    public int mCondition;
    // 0: disabled, 1: enabled
    public int mStatus;
    // Sum of CommandsInterface.SERVICE_CLASS
    public int mServiceClass;
    // 0x91: International, 0x81: Unknown
    public int mToA;
    // Number (it will not include the "sip" or "tel" URI scheme)
    public String mNumber;
    // No reply timer for CF
    public int mTimeSeconds;
    // Time slot for CF
    public long[] mTimeSlot;

    public ImsCallForwardInfoEx() {
    }

    public ImsCallForwardInfoEx(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mCondition);
        out.writeInt(mStatus);
        out.writeInt(mServiceClass);
        out.writeInt(mToA);
        out.writeString(mNumber);
        out.writeInt(mTimeSeconds);
        out.writeLongArray(mTimeSlot);
    }

    @Override
    public String toString() {
        return super.toString() + ", Condition: " + mCondition
            + ", Status: " + ((mStatus == 0) ? "disabled" : "enabled")
            + ", ServiceClass: " + mServiceClass
            + ", ToA: " + mToA + ", Number=" + mNumber
            + ", Time (seconds): " + mTimeSeconds
            + ", timeSlot: " + Arrays.toString(mTimeSlot);
    }

    private void readFromParcel(Parcel in) {
        mCondition = in.readInt();
        mStatus = in.readInt();
        mServiceClass = in.readInt();
        mToA = in.readInt();
        mNumber = in.readString();
        mTimeSeconds = in.readInt();
        mTimeSlot = new long[2];
        try {
            in.readLongArray(mTimeSlot);
        } catch (RuntimeException e) {
            mTimeSlot = null;
        }
    }

    public static final Creator<ImsCallForwardInfoEx> CREATOR =
            new Creator<ImsCallForwardInfoEx>() {
        @Override
        public ImsCallForwardInfoEx createFromParcel(Parcel in) {
            return new ImsCallForwardInfoEx(in);
        }

        @Override
        public ImsCallForwardInfoEx[] newArray(int size) {
            return new ImsCallForwardInfoEx[size];
        }
    };
}
