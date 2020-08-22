package com.mediatek.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;

public class FemtoCellInfo implements Parcelable {
  public static final Parcelable.Creator<FemtoCellInfo> CREATOR = new Parcelable.Creator<FemtoCellInfo>() {
      public FemtoCellInfo createFromParcel(Parcel param1Parcel) {
        return new FemtoCellInfo(param1Parcel.readInt(), param1Parcel.readInt(), param1Parcel.readString(), param1Parcel.readString(), param1Parcel.readString(), param1Parcel.readInt());
      }
      
      public FemtoCellInfo[] newArray(int param1Int) {
        return new FemtoCellInfo[param1Int];
      }
    };
  
  public static final int CSG_ICON_TYPE_ALLOWED = 1;
  
  public static final int CSG_ICON_TYPE_NOT_ALLOWED = 0;
  
  public static final int CSG_ICON_TYPE_OPERATOR = 2;
  
  public static final int CSG_ICON_TYPE_OPERATOR_UNAUTHORIZED = 3;
  
  private int csgIconType;
  
  private int csgId;
  
  private String homeNodeBName;
  
  private String operatorAlphaLong;
  
  private String operatorNumeric;
  
  private int rat = 0;
  
  public FemtoCellInfo(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, int paramInt3) {
    this.csgId = paramInt1;
    this.csgIconType = paramInt2;
    this.homeNodeBName = paramString1;
    this.operatorNumeric = paramString2;
    this.operatorAlphaLong = paramString3;
    this.rat = paramInt3;
  }
  
  public int describeContents() {
    return 0;
  }
  
  public int getCsgIconType() {
    return this.csgIconType;
  }
  
  public int getCsgId() {
    return this.csgId;
  }
  
  public int getCsgRat() {
    return this.rat;
  }
  
  public String getHomeNodeBName() {
    return this.homeNodeBName;
  }
  
  public String getOperatorAlphaLong() {
    return this.operatorAlphaLong;
  }
  
  public String getOperatorNumeric() {
    return this.operatorNumeric;
  }
  
  public String toString() {
    return "FemtoCellInfo " + this.csgId + "/" + this.csgIconType + "/" + this.homeNodeBName + "/" + this.operatorNumeric + "/" + this.operatorAlphaLong + "/" + this.rat;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeInt(this.csgId);
    paramParcel.writeInt(this.csgIconType);
    paramParcel.writeString(this.homeNodeBName);
    paramParcel.writeString(this.operatorNumeric);
    paramParcel.writeString(this.operatorAlphaLong);
    paramParcel.writeInt(this.rat);
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/FemtoCellInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */