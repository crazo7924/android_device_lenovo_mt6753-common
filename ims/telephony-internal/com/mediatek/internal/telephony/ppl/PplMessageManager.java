package com.mediatek.internal.telephony.ppl;

import android.content.Context;
import java.util.Arrays;
import java.util.regex.Pattern;

public class PplMessageManager {
  private static final String[] SMS_PATTERNS;
  
  public static final String SMS_SENT_ACTION = "com.mediatek.ppl.SMS_SENT";
  
  private static final String[] SMS_TEMPLATES = new String[] { 
      "我的手机可能被盗，请保留发送此短信的号码。", "#suoding#", "已接受到您的锁屏指令，锁屏成功。", "#jiesuo#", "已接受到您的解锁指令，解锁成功。", "#mima#", "您的手机防盗密码为%s。", "#xiaohui#", "远程删除数据已开始。", "远程数据删除已完成，您的隐私得到保护，请放心。", 
      "我开启了手机防盗功能，已将你的手机号码设置为紧急联系人号码，这样手机丢失也能够远程控制啦。\n以下是相关指令：\n远程锁定： #suoding#\n远程销毁数据： #xiaohui#\n找回密码： #mima#" };
  
  private final Context mContext;
  
  private final Pattern[] mMessagePatterns;
  
  private final String[] mMessageTemplates;
  
  static {
    SMS_PATTERNS = new String[] { 
        "我的手机可能被盗，请保留发送此短信的号码。", " *#suoding# *", "已接受到您的锁屏指令，锁屏成功。", " *#jiesuo# *", "已接受到您的解锁指令，解锁成功。", " *#mima# *", "您的手机防盗密码为[0-9]*。", " *#xiaohui# *", "远程删除数据已开始。", "远程数据删除已完成，您的隐私得到保护，请放心。", 
        "我开启了手机防盗功能，已将你的手机号码设置为紧急联系人号码，这样手机丢失也能够远程控制啦。\n以下是相关指令：\n远程锁定： #suoding#\n远程销毁数据： #xiaohui#\n找回密码： #mima#" };
  }
  
  public PplMessageManager(Context paramContext) {
    this.mContext = paramContext;
    this.mContext.getResources();
    this.mMessageTemplates = SMS_TEMPLATES;
    String[] arrayOfString = SMS_PATTERNS;
    this.mMessagePatterns = new Pattern[arrayOfString.length];
    for (int i = 0; i < arrayOfString.length; i++)
      this.mMessagePatterns[i] = Pattern.compile(arrayOfString[i], 2); 
  }
  
  public String buildMessage(byte paramByte, Object... paramVarArgs) {
    return String.format(getMessageTemplate(paramByte), paramVarArgs);
  }
  
  public String getMessageTemplate(byte paramByte) {
    return this.mMessageTemplates[paramByte];
  }
  
  public byte getMessageType(String paramString) {
    byte b2 = -1;
    byte b1 = 0;
    while (true) {
      byte b = b2;
      if (b1 < this.mMessagePatterns.length)
        if (this.mMessagePatterns[b1].matcher(paramString).matches()) {
          b = b1;
        } else {
          b1 = (byte)(b1 + 1);
          continue;
        }  
      b1 = b;
      if (b == 11)
        b1 = 10; 
      return b1;
    } 
  }
  
  public static class PendingMessage {
    public static final int ALL_SIM_ID = -2;
    
    public static final int ANY_SIM_ID = -1;
    
    public static final long INVALID_ID = -1L;
    
    public static final int INVALID_SIM_ID = -3;
    
    public static final String KEY_FIRST_TRIAL = "firstTrial";
    
    public static final String KEY_ID = "id";
    
    public static final String KEY_NUMBER = "number";
    
    public static final String KEY_SEGMENT_INDEX = "segmentIndex";
    
    public static final String KEY_SIM_ID = "simId";
    
    public static final String KEY_TYPE = "type";
    
    public static final int PENDING_MESSAGE_LENGTH = 49;
    
    public String content;
    
    public long id;
    
    public String number;
    
    public int simId;
    
    public byte type;
    
    public PendingMessage() {
      this.id = -1L;
      this.type = -1;
      this.number = null;
      this.simId = -1;
      this.content = null;
    }
    
    public PendingMessage(long param1Long, byte param1Byte, String param1String1, int param1Int, String param1String2) {
      this.id = param1Long;
      this.type = param1Byte;
      this.number = param1String1;
      this.simId = param1Int;
      this.content = param1String2;
    }
    
    public PendingMessage(byte[] param1ArrayOfbyte, int param1Int) {
      decode(param1ArrayOfbyte, param1Int);
    }
    
    private static long bytes2long(byte[] param1ArrayOfbyte, int param1Int) {
      long l = 0L;
      for (int i = 0; i < 8; i++)
        l = l << 8L | (param1ArrayOfbyte[i + param1Int] & 0xFF); 
      return l;
    }
    
    public static long getNextId() {
      return System.currentTimeMillis();
    }
    
    private static byte[] long2bytes(long param1Long) {
      byte[] arrayOfByte = new byte[8];
      for (int i = 0; i < 8; i++)
        arrayOfByte[i] = (byte)(int)(param1Long >>> 56 - i * 8); 
      return arrayOfByte;
    }
    
    public PendingMessage clone() {
      return new PendingMessage(this.id, this.type, this.number, this.simId, this.content);
    }
    
    public void decode(byte[] param1ArrayOfbyte, int param1Int) {
      int i = param1Int + 1;
      this.type = param1ArrayOfbyte[param1Int];
      this.id = bytes2long(param1ArrayOfbyte, i);
      i += 8;
      for (param1Int = i;; param1Int++) {
        if (param1Int >= i + 40 || param1ArrayOfbyte[param1Int] == 0) {
          this.number = new String(param1ArrayOfbyte, i, param1Int - i);
          return;
        } 
      } 
    }
    
    public void encode(byte[] param1ArrayOfbyte, int param1Int) {
      int i = param1Int + 1;
      param1ArrayOfbyte[param1Int] = this.type;
      byte[] arrayOfByte = long2bytes(this.id);
      System.arraycopy(arrayOfByte, 0, param1ArrayOfbyte, i, arrayOfByte.length);
      arrayOfByte = this.number.getBytes();
      if (arrayOfByte.length > 40)
        throw new Error("Destination number is too long"); 
      arrayOfByte = Arrays.copyOf(arrayOfByte, 40);
      System.arraycopy(arrayOfByte, 0, param1ArrayOfbyte, i + 8, arrayOfByte.length);
    }
    
    public String toString() {
      return "PendingMessage " + hashCode() + " {" + this.id + ", " + this.type + ", " + this.number + ", " + this.simId + ", " + this.content + "}";
    }
  }
  
  public static class Type {
    public static final byte INSTRUCTION_DESCRIPTION = 10;
    
    public static final byte INSTRUCTION_DESCRIPTION2 = 11;
    
    public static final byte INVALID = -1;
    
    public static final byte LOCK_REQUEST = 1;
    
    public static final byte LOCK_RESPONSE = 2;
    
    public static final byte RESET_PW_REQUEST = 5;
    
    public static final byte RESET_PW_RESPONSE = 6;
    
    public static final byte SIM_CHANGED = 0;
    
    public static final byte UNLOCK_REQUEST = 3;
    
    public static final byte UNLOCK_RESPONSE = 4;
    
    public static final byte WIPE_COMPLETED = 9;
    
    public static final byte WIPE_REQUEST = 7;
    
    public static final byte WIPE_STARTED = 8;
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/ppl/PplMessageManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */