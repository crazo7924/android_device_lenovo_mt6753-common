package com.mediatek.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MmsIcpInfo implements Parcelable {
   public static final Creator<MmsIcpInfo> CREATOR = new Creator<MmsIcpInfo>() {
      public MmsIcpInfo createFromParcel(Parcel var1) {
         MmsIcpInfo var2 = new MmsIcpInfo();
         var2.readFrom(var1);
         return var2;
      }

      public MmsIcpInfo[] newArray(int var1) {
         return new MmsIcpInfo[var1];
      }
   };
   public String mAddress;
   public String mAddressType;
   public String mAuthId;
   public String mAuthMechanism;
   public String mAuthPw;
   public String mAuthType;
   public String mDomainName;
   public String mImplementation;
   public int mPort;
   public String mRelayOrServerAddress;
   public String mService;

   static {
      throw new VerifyError("bad dex opcode");
   }

   public int describeContents() {
      return 0;
   }

   public void readFrom(Parcel var1) {
      this.mImplementation = var1.readString();
      this.mRelayOrServerAddress = var1.readString();
      this.mDomainName = var1.readString();
      this.mAddress = var1.readString();
      this.mAddressType = var1.readString();
      this.mPort = var1.readInt();
      this.mService = var1.readString();
      this.mAuthType = var1.readString();
      this.mAuthId = var1.readString();
      this.mAuthPw = var1.readString();
      this.mAuthMechanism = var1.readString();
   }

   public void writeTo(Parcel var1) {
      var1.writeString(this.mImplementation);
      var1.writeString(this.mRelayOrServerAddress);
      var1.writeString(this.mDomainName);
      var1.writeString(this.mAddress);
      var1.writeString(this.mAddressType);
      var1.writeInt(this.mPort);
      var1.writeString(this.mService);
      var1.writeString(this.mAuthType);
      var1.writeString(this.mAuthId);
      var1.writeString(this.mAuthPw);
      var1.writeString(this.mAuthMechanism);
   }

   public void writeToParcel(Parcel var1, int var2) {
      this.writeTo(var1);
   }
}
