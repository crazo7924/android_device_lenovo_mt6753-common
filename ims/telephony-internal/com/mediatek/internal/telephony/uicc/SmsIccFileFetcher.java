package com.mediatek.internal.telephony.uicc;

import android.content.Context;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class SmsIccFileFetcher extends IccFileFetcherBase {
  private static final String SMSP = "ef_smsp";
  
  private static final String SMSS = "ef_smss";
  
  private static final String TAG = "SmsIccFileFetcher";
  
  ArrayList<String> mFileList = new ArrayList<String>();
  
  public SmsIccFileFetcher(Context paramContext, Phone paramPhone) {
    super(paramContext, paramPhone);
    this.mFileList.add("ef_smss");
    this.mFileList.add("ef_smsp");
  }
  
  public int getNextMessageId() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc 'SmsIccFileFetcher'
    //   4: ldc 'getNextMessageId'
    //   6: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   9: pop
    //   10: aload_0
    //   11: getfield mData : Ljava/util/HashMap;
    //   14: ldc 'ef_smss'
    //   16: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   19: checkcast [B
    //   22: checkcast [B
    //   25: astore #4
    //   27: iconst_m1
    //   28: istore_2
    //   29: iload_2
    //   30: istore_1
    //   31: aload #4
    //   33: ifnull -> 174
    //   36: iload_2
    //   37: istore_1
    //   38: aload #4
    //   40: arraylength
    //   41: iconst_4
    //   42: if_icmple -> 174
    //   45: new java/io/DataInputStream
    //   48: dup
    //   49: new java/io/ByteArrayInputStream
    //   52: dup
    //   53: aload #4
    //   55: invokespecial <init> : ([B)V
    //   58: invokespecial <init> : (Ljava/io/InputStream;)V
    //   61: astore #5
    //   63: iload_2
    //   64: istore_1
    //   65: aload #5
    //   67: invokevirtual readUnsignedShort : ()I
    //   70: istore_3
    //   71: iload_2
    //   72: istore_1
    //   73: aload #5
    //   75: invokevirtual close : ()V
    //   78: iload_3
    //   79: ldc 65535
    //   81: irem
    //   82: iconst_1
    //   83: iadd
    //   84: istore_2
    //   85: iload_2
    //   86: istore_1
    //   87: ldc 'persist.radio.cdma.msgid'
    //   89: iload_2
    //   90: invokestatic toString : (I)Ljava/lang/String;
    //   93: invokestatic set : (Ljava/lang/String;Ljava/lang/String;)V
    //   96: iload_2
    //   97: istore_1
    //   98: ldc 'SmsIccFileFetcher'
    //   100: new java/lang/StringBuilder
    //   103: dup
    //   104: invokespecial <init> : ()V
    //   107: ldc 'getmWapMsgId nextMsgId = '
    //   109: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   112: iload_1
    //   113: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   116: invokevirtual toString : ()Ljava/lang/String;
    //   119: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   122: pop
    //   123: iconst_4
    //   124: invokestatic allocate : (I)Ljava/nio/ByteBuffer;
    //   127: iload_1
    //   128: invokevirtual putInt : (I)Ljava/nio/ByteBuffer;
    //   131: invokevirtual array : ()[B
    //   134: astore #5
    //   136: aload #4
    //   138: iconst_0
    //   139: aload #5
    //   141: iconst_2
    //   142: baload
    //   143: bastore
    //   144: aload #4
    //   146: iconst_1
    //   147: aload #5
    //   149: iconst_3
    //   150: baload
    //   151: bastore
    //   152: aload_0
    //   153: new com/mediatek/internal/telephony/uicc/IccFileRequest
    //   156: dup
    //   157: sipush #28478
    //   160: iconst_1
    //   161: iconst_2
    //   162: ldc '3F007F25'
    //   164: aload #4
    //   166: iconst_m1
    //   167: aconst_null
    //   168: invokespecial <init> : (IIILjava/lang/String;[BILjava/lang/String;)V
    //   171: invokevirtual updateSimInfo : (Lcom/mediatek/internal/telephony/uicc/IccFileRequest;)V
    //   174: aload_0
    //   175: monitorexit
    //   176: iload_1
    //   177: ireturn
    //   178: astore #5
    //   180: ldc 'SmsIccFileFetcher'
    //   182: ldc 'getNextMessageId IOException'
    //   184: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   187: pop
    //   188: goto -> 98
    //   191: astore #4
    //   193: aload_0
    //   194: monitorexit
    //   195: aload #4
    //   197: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	191	finally
    //   38	63	191	finally
    //   65	71	178	java/io/IOException
    //   65	71	191	finally
    //   73	78	178	java/io/IOException
    //   73	78	191	finally
    //   87	96	178	java/io/IOException
    //   87	96	191	finally
    //   98	136	191	finally
    //   152	174	191	finally
    //   180	188	191	finally
  }
  
  public List<byte[]> getSmsPara() {
    Rlog.d("SmsIccFileFetcher", "getSmsPara");
    return (ArrayList)this.mData.get("ef_smsp");
  }
  
  public int getWapMsgId() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc 'SmsIccFileFetcher'
    //   4: ldc 'getmWapMsgId'
    //   6: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   9: pop
    //   10: aload_0
    //   11: getfield mData : Ljava/util/HashMap;
    //   14: ldc 'ef_smss'
    //   16: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   19: checkcast [B
    //   22: checkcast [B
    //   25: astore_3
    //   26: iconst_m1
    //   27: istore_1
    //   28: iload_1
    //   29: istore_2
    //   30: aload_3
    //   31: ifnull -> 164
    //   34: iload_1
    //   35: istore_2
    //   36: aload_3
    //   37: arraylength
    //   38: iconst_4
    //   39: if_icmple -> 164
    //   42: iload_1
    //   43: istore_2
    //   44: iconst_m1
    //   45: ifle -> 164
    //   48: new java/io/DataInputStream
    //   51: dup
    //   52: new java/io/ByteArrayInputStream
    //   55: dup
    //   56: aload_3
    //   57: invokespecial <init> : ([B)V
    //   60: invokespecial <init> : (Ljava/io/InputStream;)V
    //   63: astore #4
    //   65: aload #4
    //   67: invokevirtual readUnsignedShort : ()I
    //   70: pop
    //   71: aload #4
    //   73: invokevirtual readUnsignedShort : ()I
    //   76: istore_2
    //   77: aload #4
    //   79: invokevirtual close : ()V
    //   82: iload_2
    //   83: ldc 65535
    //   85: irem
    //   86: iconst_1
    //   87: iadd
    //   88: istore_1
    //   89: ldc 'SmsIccFileFetcher'
    //   91: new java/lang/StringBuilder
    //   94: dup
    //   95: invokespecial <init> : ()V
    //   98: ldc 'getmWapMsgId mWapMsgId = '
    //   100: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: iload_1
    //   104: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   107: invokevirtual toString : ()Ljava/lang/String;
    //   110: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   113: pop
    //   114: iconst_4
    //   115: invokestatic allocate : (I)Ljava/nio/ByteBuffer;
    //   118: iload_1
    //   119: invokevirtual putInt : (I)Ljava/nio/ByteBuffer;
    //   122: invokevirtual array : ()[B
    //   125: astore #4
    //   127: aload_3
    //   128: iconst_2
    //   129: aload #4
    //   131: iconst_2
    //   132: baload
    //   133: bastore
    //   134: aload_3
    //   135: iconst_3
    //   136: aload #4
    //   138: iconst_3
    //   139: baload
    //   140: bastore
    //   141: aload_0
    //   142: new com/mediatek/internal/telephony/uicc/IccFileRequest
    //   145: dup
    //   146: sipush #28478
    //   149: iconst_1
    //   150: iconst_2
    //   151: ldc '3F007F25'
    //   153: aload_3
    //   154: iconst_m1
    //   155: aconst_null
    //   156: invokespecial <init> : (IIILjava/lang/String;[BILjava/lang/String;)V
    //   159: invokevirtual updateSimInfo : (Lcom/mediatek/internal/telephony/uicc/IccFileRequest;)V
    //   162: iload_1
    //   163: istore_2
    //   164: aload_0
    //   165: monitorexit
    //   166: iload_2
    //   167: ireturn
    //   168: astore #4
    //   170: ldc 'SmsIccFileFetcher'
    //   172: ldc 'getmWapMsgId IOException'
    //   174: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   177: pop
    //   178: goto -> 89
    //   181: astore_3
    //   182: aload_0
    //   183: monitorexit
    //   184: aload_3
    //   185: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	181	finally
    //   36	42	181	finally
    //   48	65	181	finally
    //   65	82	168	java/io/IOException
    //   65	82	181	finally
    //   89	127	181	finally
    //   141	162	181	finally
    //   170	178	181	finally
  }
  
  public IccFileRequest onGetFilePara(String paramString) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual hashCode : ()I
    //   4: tableswitch default -> 36, -1839966443 -> 76, -1839966442 -> 36, -1839966441 -> 36, -1839966440 -> 62
    //   36: iconst_m1
    //   37: istore_2
    //   38: iload_2
    //   39: tableswitch default -> 60, 0 -> 90, 1 -> 108
    //   60: aconst_null
    //   61: areturn
    //   62: aload_1
    //   63: ldc 'ef_smss'
    //   65: invokevirtual equals : (Ljava/lang/Object;)Z
    //   68: ifeq -> 36
    //   71: iconst_0
    //   72: istore_2
    //   73: goto -> 38
    //   76: aload_1
    //   77: ldc 'ef_smsp'
    //   79: invokevirtual equals : (Ljava/lang/Object;)Z
    //   82: ifeq -> 36
    //   85: iconst_1
    //   86: istore_2
    //   87: goto -> 38
    //   90: new com/mediatek/internal/telephony/uicc/IccFileRequest
    //   93: dup
    //   94: sipush #28477
    //   97: iconst_1
    //   98: iconst_2
    //   99: ldc '3F007F25'
    //   101: aconst_null
    //   102: iconst_m1
    //   103: aconst_null
    //   104: invokespecial <init> : (IIILjava/lang/String;[BILjava/lang/String;)V
    //   107: areturn
    //   108: new com/mediatek/internal/telephony/uicc/IccFileRequest
    //   111: dup
    //   112: sipush #28478
    //   115: iconst_0
    //   116: iconst_2
    //   117: ldc '3F007F25'
    //   119: aconst_null
    //   120: iconst_m1
    //   121: aconst_null
    //   122: invokespecial <init> : (IIILjava/lang/String;[BILjava/lang/String;)V
    //   125: areturn
  }
  
  public ArrayList<String> onGetKeys() {
    return this.mFileList;
  }
  
  public void onParseResult(String paramString, byte[] paramArrayOfbyte, ArrayList<byte[]> paramArrayList) {
    if ("ef_smss".equals(paramString)) {
      Rlog.d("SmsIccFileFetcher", "SMSS = " + Arrays.toString(paramArrayOfbyte));
      this.mData.put("ef_smss", paramArrayOfbyte);
      return;
    } 
    if ("ef_smsp".equals(paramString)) {
      Rlog.d("SmsIccFileFetcher", "SMSP = " + paramArrayList);
      this.mData.put("ef_smsp", paramArrayList);
      if (paramArrayList != null && paramArrayList.size() > 0) {
        Iterator<byte> iterator = paramArrayList.iterator();
        while (true) {
          if (iterator.hasNext()) {
            paramArrayOfbyte = (byte[])iterator.next();
            Rlog.d("SmsIccFileFetcher", "SMSP = " + Arrays.toString(paramArrayOfbyte));
            continue;
          } 
          return;
        } 
      } 
    } 
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/uicc/SmsIccFileFetcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */