package com.mediatek.internal.telephony.ppl;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.mediatek.common.PluginImpl;
import java.util.Iterator;
import java.util.List;

@PluginImpl(interfaceName = "com.mediatek.internal.telephony.ppl.IPplSmsFilter")
public class PplSmsFilterExtension extends ContextWrapper implements IPplSmsFilter {
  public static final String INSTRUCTION_KEY_FROM = "From";
  
  public static final String INSTRUCTION_KEY_SIM_ID = "SimId";
  
  public static final String INSTRUCTION_KEY_TO = "To";
  
  public static final String INSTRUCTION_KEY_TYPE = "Type";
  
  public static final String INTENT_REMOTE_INSTRUCTION_RECEIVED = "com.mediatek.ppl.REMOTE_INSTRUCTION_RECEIVED";
  
  private static final String TAG = "PPL/PplSmsFilterExtension";
  
  private final IPplAgent mAgent;
  
  private final boolean mEnabled;
  
  private final PplMessageManager mMessageManager;
  
  public PplSmsFilterExtension(Context paramContext) {
    super(paramContext);
    Log.d("PPL/PplSmsFilterExtension", "PplSmsFilterExtension enter");
    if (!"1".equals(SystemProperties.get("ro.mtk_privacy_protection_lock"))) {
      this.mAgent = null;
      this.mMessageManager = null;
      this.mEnabled = false;
      return;
    } 
    IBinder iBinder = ServiceManager.getService("PPLAgent");
    if (iBinder == null) {
      Log.e("PPL/PplSmsFilterExtension", "Failed to get PPLAgent");
      this.mAgent = null;
      this.mMessageManager = null;
      this.mEnabled = false;
      return;
    } 
    this.mAgent = IPplAgent.Stub.asInterface(iBinder);
    if (this.mAgent == null) {
      Log.e("PPL/PplSmsFilterExtension", "mAgent is null!");
      this.mMessageManager = null;
      this.mEnabled = false;
      return;
    } 
    this.mMessageManager = new PplMessageManager(paramContext);
    this.mEnabled = true;
    Log.d("PPL/PplSmsFilterExtension", "PplSmsFilterExtension exit");
  }
  
  private boolean matchNumber(String paramString, List<String> paramList) {
    if (paramString != null && paramList != null) {
      Iterator<String> iterator = paramList.iterator();
      while (iterator.hasNext()) {
        if (PhoneNumberUtils.compare(iterator.next(), paramString))
          return true; 
      } 
    } 
    return false;
  }
  
  public boolean pplFilter(Bundle paramBundle) {
    boolean bool;
    String str2;
    PplControlData pplControlData;
    Log.d("PPL/PplSmsFilterExtension", "pplFilter(" + paramBundle + ")");
    if (!this.mEnabled) {
      Log.d("PPL/PplSmsFilterExtension", "pplFilter returns false: feature not enabled");
      return false;
    } 
    String str1 = paramBundle.getString("format");
    if (paramBundle.getInt("smsType") == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    int i = paramBundle.getInt("subId");
    int j = SubscriptionManager.getSlotId(i);
    Log.d("PPL/PplSmsFilterExtension", "subId = " + i + ". simId = " + j);
    Object[] arrayOfObject = (Object[])paramBundle.getSerializable("pdus");
    if (arrayOfObject == null) {
      str2 = paramBundle.getString("msgContent");
      str1 = paramBundle.getString("srdAddr");
      String str = paramBundle.getString("dstAddr");
      Log.d("PPL/PplSmsFilterExtension", "pplFilter: Read msg directly and content is " + str2);
    } else {
      byte[][] arrayOfByte = new byte[str2.length][];
      for (i = 0; i < str2.length; i++)
        arrayOfByte[i] = (byte[])str2[i]; 
      int k = arrayOfByte.length;
      SmsMessage[] arrayOfSmsMessage = new SmsMessage[k];
      for (i = 0; i < k; i++)
        arrayOfSmsMessage[i] = SmsMessage.createFromPdu(arrayOfByte[i], str1); 
      Log.d("PPL/PplSmsFilterExtension", "pplFilter: pdus is " + arrayOfByte + " with length " + arrayOfByte.length);
      Log.d("PPL/PplSmsFilterExtension", "pplFilter: pdus[0] is " + arrayOfByte[0]);
      str2 = arrayOfSmsMessage[0].getMessageBody();
      Log.d("PPL/PplSmsFilterExtension", "pplFilter: message content is " + str2);
      str1 = arrayOfSmsMessage[0].getOriginatingAddress();
      String str = arrayOfSmsMessage[0].getDestinationAddress();
    } 
    if (str2 == null) {
      Log.d("PPL/PplSmsFilterExtension", "pplFilter returns false: content is null");
      return false;
    } 
    try {
      pplControlData = PplControlData.buildControlData(this.mAgent.readControlData());
      if (pplControlData == null || !pplControlData.isEnabled()) {
        Log.d("PPL/PplSmsFilterExtension", "pplFilter returns false: control data is null or ppl is not enabled");
        return false;
      } 
    } catch (RemoteException remoteException) {
      remoteException.printStackTrace();
      Log.d("PPL/PplSmsFilterExtension", "pplFilter returns false: failed to build control data");
      return false;
    } 
    if (bool) {
      Log.d("PPL/PplSmsFilterExtension", "pplFilter: dst is " + remoteException);
      if (!matchNumber((String)remoteException, pplControlData.TrustedNumberList)) {
        Log.d("PPL/PplSmsFilterExtension", "pplFilter returns false: MO number does not match");
        return false;
      } 
    } else {
      Log.d("PPL/PplSmsFilterExtension", "pplFilter: src is " + str1);
      if (!matchNumber(str1, pplControlData.TrustedNumberList)) {
        Log.d("PPL/PplSmsFilterExtension", "pplFilter returns false: MT number does not match");
        return false;
      } 
    } 
    byte b = this.mMessageManager.getMessageType(str2);
    if (b == -1) {
      Log.d("PPL/PplSmsFilterExtension", "pplFilter returns false: message is not matched");
      return false;
    } 
    Intent intent = new Intent("com.mediatek.ppl.REMOTE_INSTRUCTION_RECEIVED");
    intent.setClassName("com.mediatek.ppl", "com.mediatek.ppl.PplService");
    intent.putExtra("Type", b);
    intent.putExtra("SimId", j);
    if (bool) {
      intent.putExtra("To", (String)remoteException);
      Log.d("PPL/PplSmsFilterExtension", "start PPL Service");
      startService(intent);
      return true;
    } 
    intent.putExtra("From", str1);
    Log.d("PPL/PplSmsFilterExtension", "start PPL Service");
    startService(intent);
    return true;
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/ppl/PplSmsFilterExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */