package com.mediatek.internal.telephony;

public class SrvccCallContext {
  private int mCallDirection;
  
  private int mCallId;
  
  private int mCallMode;
  
  private int mCallState;
  
  private int mCliValidity;
  
  private int mEccCategory;
  
  private String mName;
  
  private String mNumber;
  
  private int mNumberType;
  
  public SrvccCallContext(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString1, String paramString2, int paramInt7) {
    this.mCallId = paramInt1;
    this.mCallMode = paramInt2;
    this.mCallDirection = paramInt3;
    this.mCallState = paramInt4;
    this.mEccCategory = paramInt5;
    this.mNumberType = paramInt6;
    this.mNumber = paramString1;
    this.mName = paramString2;
    this.mCliValidity = paramInt7;
  }
  
  public int getCallDirection() {
    return this.mCallDirection;
  }
  
  public int getCallId() {
    return this.mCallId;
  }
  
  public int getCallMode() {
    return this.mCallMode;
  }
  
  public int getCallState() {
    return this.mCallState;
  }
  
  public int getCliValidity() {
    return this.mCliValidity;
  }
  
  public int getEccCategory() {
    return this.mEccCategory;
  }
  
  public String getName() {
    return this.mName;
  }
  
  public String getNumber() {
    return this.mNumber;
  }
  
  public int getNumberType() {
    return this.mNumberType;
  }
  
  public void setCallDirection(int paramInt) {
    this.mCallDirection = paramInt;
  }
  
  public void setCallId(int paramInt) {
    this.mCallId = paramInt;
  }
  
  public void setCallMode(int paramInt) {
    this.mCallMode = paramInt;
  }
  
  public void setCallState(int paramInt) {
    this.mCallState = paramInt;
  }
  
  public void setCallState(String paramString) {
    this.mNumber = paramString;
  }
  
  public void setCliValidity(int paramInt) {
    this.mCliValidity = paramInt;
  }
  
  public void setEccCategory(int paramInt) {
    this.mEccCategory = paramInt;
  }
  
  public void setName(String paramString) {
    this.mName = paramString;
  }
  
  public void setNumberType(int paramInt) {
    this.mNumberType = paramInt;
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/SrvccCallContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */