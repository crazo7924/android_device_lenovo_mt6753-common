package com.mediatek.internal.telephony;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConferenceCallMessageHandler extends DefaultHandler {
  public static final String STATUS_ALERTING = "alerting";
  
  public static final String STATUS_CONNECTED = "connected";
  
  public static final String STATUS_CONNECT_FAIL = "connect-fail";
  
  public static final String STATUS_DIALING_IN = "dialing-in";
  
  public static final String STATUS_DIALING_OUT = "dialing-out";
  
  public static final String STATUS_DISCONNECTED = "disconnected";
  
  public static final String STATUS_DISCONNECTING = "disconnecting";
  
  public static final String STATUS_MUTED_VIA_FOCUS = "muted-via-focus";
  
  public static final String STATUS_ON_HOLD = "on-hold";
  
  public static final String STATUS_PENDING = "pending";
  
  private static final String TAG = "ConferenceCallMessageHandler";
  
  private int mCallId = -1;
  
  private String mHostInfo;
  
  private int mIndex = 0;
  
  private int mMaxUserCount;
  
  private boolean mParsingHostInfo = false;
  
  private String mTag;
  
  private String mTempVal;
  
  private User mUser;
  
  private int mUserCount = -1;
  
  private List<User> mUsers;
  
  private void setMaxUserCount(String paramString) {
    this.mMaxUserCount = Integer.parseInt(paramString);
  }
  
  public void characters(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws SAXException {
    if (this.mTag == null) {
      Log.d("ConferenceCallMessageHandler", "Parse val failed: tag is null");
      return;
    } 
    String str = new String(paramArrayOfchar, paramInt1, paramInt2);
    Log.d("ConferenceCallMessageHandler", "Current tag: " + this.mTag + " val: " + str);
    if (this.mTag.equalsIgnoreCase("maximum-user-count")) {
      setMaxUserCount(str);
      Log.d("ConferenceCallMessageHandler", "MaxUserCount: " + getMaxUserCount());
    } else if (this.mTag.equalsIgnoreCase("user-count")) {
      this.mUserCount = Integer.parseInt(str);
      Log.d("ConferenceCallMessageHandler", "UserCount: " + getUserCount());
    } else if (this.mParsingHostInfo && this.mTag.equalsIgnoreCase("uri")) {
      this.mHostInfo = str;
      Log.d("ConferenceCallMessageHandler", "host-info: " + getHostInfo());
    } 
    if (this.mUser == null) {
      Log.d("ConferenceCallMessageHandler", "Parse val failed: user is null");
      return;
    } 
    if (this.mTag.equalsIgnoreCase("display-text")) {
      this.mUser.setDisplayText(str);
      Log.d("ConferenceCallMessageHandler", "user - DisplayText: " + this.mUser.getDisplayText());
      return;
    } 
    if (this.mTag.equalsIgnoreCase("status")) {
      this.mUser.setStatus(str);
      Log.d("ConferenceCallMessageHandler", "Status: " + this.mUser.getStatus());
      return;
    } 
  }
  
  public void endElement(String paramString1, String paramString2, String paramString3) throws SAXException {
    if (paramString3.equalsIgnoreCase("user") && this.mUsers != null) {
      this.mUsers.add(this.mUser);
      this.mUser = null;
    } else if (paramString3.equalsIgnoreCase("host-info")) {
      this.mParsingHostInfo = false;
      Log.d("ConferenceCallMessageHandler", "end parsing host info");
    } 
    this.mTag = null;
  }
  
  public int getCallId() {
    return this.mCallId;
  }
  
  public String getHostInfo() {
    return this.mHostInfo;
  }
  
  public int getMaxUserCount() {
    return this.mMaxUserCount;
  }
  
  public int getUserCount() {
    return this.mUserCount;
  }
  
  public List<User> getUsers() {
    return this.mUsers;
  }
  
  public void setCallId(int paramInt) {
    this.mCallId = paramInt;
  }
  
  public void startDocument() throws SAXException {
    this.mUsers = new ArrayList<User>();
  }
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes) throws SAXException {
    if (paramString3.equalsIgnoreCase("user")) {
      this.mIndex++;
      this.mUser = new User();
      this.mUser.setIndex(this.mIndex);
      this.mUser.setEntity(paramAttributes.getValue("", "entity"));
      Log.d("ConferenceCallMessageHandler", "user - entity: " + this.mUser.getEntity());
    } else if (paramString3.equalsIgnoreCase("endpoint")) {
      this.mUser.setEndPoint(paramAttributes.getValue("", "entity"));
      Log.d("ConferenceCallMessageHandler", "endpoint - entity: " + this.mUser.getEndPoint());
    } else if (paramString3.equalsIgnoreCase("host-info")) {
      this.mParsingHostInfo = true;
      Log.d("ConferenceCallMessageHandler", "start parsing host info");
    } 
    this.mTag = paramString3;
    Log.d("ConferenceCallMessageHandler", "Conference uri: " + paramString1 + ", localName: " + paramString2);
  }
  
  public class User {
    private int mConnectionIndex = -1;
    
    private String mDisplayText;
    
    private String mEndPoint;
    
    private String mEntity;
    
    private int mIndex;
    
    private String mSipTelUri;
    
    private String mStatus = "disconnected";
    
    public int getConnectionIndex() {
      return this.mConnectionIndex;
    }
    
    public String getDisplayText() {
      return this.mDisplayText;
    }
    
    public String getEndPoint() {
      return this.mEndPoint;
    }
    
    public String getEntity() {
      return this.mEntity;
    }
    
    public int getIndex() {
      return this.mIndex;
    }
    
    public String getSipTelUri() {
      return this.mSipTelUri;
    }
    
    public String getStatus() {
      return this.mStatus;
    }
    
    public void setConnectionIndex(int param1Int) {
      this.mConnectionIndex = param1Int;
    }
    
    void setDisplayText(String param1String) {
      this.mDisplayText = param1String;
    }
    
    void setEndPoint(String param1String) {
      this.mEndPoint = param1String;
    }
    
    void setEntity(String param1String) {
      this.mEntity = param1String;
    }
    
    void setIndex(int param1Int) {
      this.mIndex = param1Int;
    }
    
    void setSipTelUri(String param1String) {
      this.mSipTelUri = param1String;
    }
    
    void setStatus(String param1String) {
      this.mStatus = param1String;
    }
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/ConferenceCallMessageHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */