package com.mediatek.internal.telephony;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.PhoneBase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class CellBroadcastFwkExt {
  public static final int CB_SET_TYPE_CLOSE_ETWS_CHANNEL = 2;
  
  public static final int CB_SET_TYPE_NORMAL = 0;
  
  public static final int CB_SET_TYPE_OPEN_ETWS_CHANNEL = 1;
  
  private static final Uri CHANNEL_URI = Telephony.SmsCb.CbChannel.CONTENT_URI;
  
  private static final Uri CHANNEL_URI1 = Uri.parse("content://cb/channel1");
  
  private static final int EVENT_CLOSE_ETWS_CHANNEL_DONE = 3;
  
  private static final int EVENT_OPEN_ETWS_CHANNEL_DONE = 2;
  
  private static final int EVENT_QUERY_CB_CONFIG = 1;
  
  private static final int MAX_ETWS_NOTIFICATION = 4;
  
  private static final int NEXT_ACTION_NO_ACTION = 100;
  
  private static final int NEXT_ACTION_ONLY_ADD = 101;
  
  private static final int NEXT_ACTION_ONLY_REMOVE = 101;
  
  private static final int NEXT_ACTION_REMOVE_THEN_ADD = 102;
  
  private static final String TAG = "CellBroadcastFwkExt";
  
  private CommandsInterface mCi = null;
  
  private CellBroadcastConfigInfo mConfigInfo = null;
  
  private Context mContext = null;
  
  private ArrayList<EtwsNotification> mEtwsNotificationList = null;
  
  private Handler mHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        EtwsNotification etwsNotification1;
        int i;
        String str;
        AsyncResult asyncResult2;
        Rlog.d("CellBroadcastFwkExt", "receive message " + CellBroadcastFwkExt.this.idToString(param1Message.what));
        switch (param1Message.what) {
          default:
            Rlog.d("CellBroadcastFwkExt", "unknown CB event " + param1Message.what);
            return;
          case 1:
            Rlog.d("CellBroadcastFwkExt", "handle EVENT_QUERY_CB_CONFIG");
            asyncResult2 = (AsyncResult)param1Message.obj;
            if (asyncResult2.exception != null) {
              Rlog.d("CellBroadcastFwkExt", "fail to query cb config");
              return;
            } 
            str = ((CellBroadcastConfigInfo)asyncResult2.result).channelConfigInfo;
            i = param1Message.arg1;
            etwsNotification1 = (EtwsNotification)asyncResult2.userObj;
            CellBroadcastFwkExt.this.handleQueriedConfig(str, i, etwsNotification1);
            return;
          case 2:
            Rlog.d("CellBroadcastFwkExt", "handle EVENT_OPEN_ETWS_CHANNEL_DONE");
            asyncResult1 = (AsyncResult)((Message)etwsNotification1).obj;
            etwsNotification2 = (EtwsNotification)asyncResult1.userObj;
            if (asyncResult1.exception == null) {
              Rlog.d("CellBroadcastFwkExt", "success to open cb channel " + etwsNotification2.messageId);
              i = ((Message)etwsNotification1).arg1;
              if (i == 101) {
                CellBroadcastFwkExt.this.addEtwsNoti(etwsNotification2);
              } else if (i == 102) {
                CellBroadcastFwkExt.this.removeFirstEtwsNotiThenAdd(etwsNotification2);
              } else {
                Rlog.d("CellBroadcastFwkExt", "invalid next action " + i);
              } 
              CellBroadcastFwkExt.this.updateDatabase(true);
              return;
            } 
            Rlog.d("CellBroadcastFwkExt", "fail to open cb channel");
            return;
          case 3:
            break;
        } 
        Rlog.d("CellBroadcastFwkExt", "handle EVENT_CLOSE_ETWS_CHANNEL_DONE");
        AsyncResult asyncResult1 = (AsyncResult)((Message)etwsNotification1).obj;
        EtwsNotification etwsNotification2 = (EtwsNotification)asyncResult1.userObj;
        if (asyncResult1.exception == null) {
          Rlog.d("CellBroadcastFwkExt", "success to close cb channel " + etwsNotification2.messageId);
          i = ((Message)etwsNotification1).arg1;
          if (i == 101) {
            CellBroadcastFwkExt.this.removeEtwsNoti(etwsNotification2);
          } else {
            Rlog.d("CellBroadcastFwkExt", "invalid next action " + i);
          } 
          CellBroadcastFwkExt.this.updateDatabase(false);
          return;
        } 
        Rlog.d("CellBroadcastFwkExt", "fail to close cb channel");
      }
    };
  
  private Object mLock = null;
  
  private PhoneBase mPhone = null;
  
  private int mPhoneId = 0;
  
  private boolean mSuccess = false;
  
  public CellBroadcastFwkExt(PhoneBase paramPhoneBase) {
    if (paramPhoneBase == null) {
      Rlog.d("CellBroadcastFwkExt", "FAIL! phone is null");
      return;
    } 
    this.mPhone = paramPhoneBase;
    this.mCi = paramPhoneBase.mCi;
    this.mContext = paramPhoneBase.getContext();
    this.mPhoneId = paramPhoneBase.getPhoneId();
    this.mLock = new Object();
    this.mEtwsNotificationList = new ArrayList<EtwsNotification>(4);
  }
  
  private void addEtwsNoti(EtwsNotification paramEtwsNotification) {
    Rlog.d("CellBroadcastFwkExt", "call addEtwsNoti");
    this.mEtwsNotificationList.add(paramEtwsNotification);
  }
  
  private void handleQueriedConfig(String paramString, int paramInt, EtwsNotification paramEtwsNotification) {
    Message<Integer> message;
    String str1;
    String str3;
    Message message1;
    SortedSet<Integer> sortedSet;
    Rlog.d("CellBroadcastFwkExt", "handleQueriedConfig");
    ArrayList<Integer> arrayList1 = parseConfigInfoToList(paramString);
    ArrayList<Integer> arrayList2 = new ArrayList();
    arrayList2.add(Integer.valueOf(4352));
    arrayList2.add(Integer.valueOf(4353));
    arrayList2.add(Integer.valueOf(4354));
    arrayList2.add(Integer.valueOf(4355));
    arrayList2.add(Integer.valueOf(4356));
    if (paramInt == 2) {
      SortedSet<Integer> sortedSet1;
      Message<Integer> message2;
      Rlog.d("CellBroadcastFwkExt", "to open ETWS channel: " + paramEtwsNotification.messageId);
      if (this.mEtwsNotificationList.size() < 4) {
        Rlog.d("CellBroadcastFwkExt", "list is NOT full");
        SortedSet<Integer> sortedSet2 = mergeConfigList(arrayList1, arrayList2);
        message2 = this.mHandler.obtainMessage(paramInt, 101, 0, paramEtwsNotification);
        sortedSet1 = sortedSet2;
        message = message2;
      } else {
        Rlog.d("CellBroadcastFwkExt", "list is full");
        EtwsNotification etwsNotification = this.mEtwsNotificationList.get(0);
        int i = 0;
        while (true) {
          if (i < message.size())
            if (((Integer)message.get(i)).intValue() == etwsNotification.messageId) {
              Rlog.d("CellBroadcastFwkExt", "remove channel from old config: " + etwsNotification.messageId);
              message.remove(i);
            } else {
              i++;
              continue;
            }  
          sortedSet = mergeConfigList((ArrayList<Integer>)message, (ArrayList<Integer>)message2);
          message = this.mHandler.obtainMessage(paramInt, 102, 0, sortedSet1);
          sortedSet1 = sortedSet;
          str3 = parseSortedSetToString(sortedSet1);
          this.mCi.setCellBroadcastChannelConfigInfo(str3, 1, message);
          return;
        } 
      } 
    } else {
      if (paramInt == 3) {
        Rlog.d("CellBroadcastFwkExt", "to close ETWS channel: " + ((EtwsNotification)str3).messageId);
        SortedSet<Integer> sortedSet1 = minusConfigList((ArrayList<Integer>)message, (ArrayList<Integer>)sortedSet);
        message1 = this.mHandler.obtainMessage(paramInt, 101, 0, str3);
        str1 = parseSortedSetToString(sortedSet1);
        this.mCi.setCellBroadcastChannelConfigInfo(str1, 2, message1);
        return;
      } 
      Rlog.d("CellBroadcastFwkExt", "invalid action: " + paramInt);
      return;
    } 
    String str2 = parseSortedSetToString((SortedSet<Integer>)message1);
    this.mCi.setCellBroadcastChannelConfigInfo(str2, 1, (Message)str1);
  }
  
  private String idToString(int paramInt) {
    switch (paramInt) {
      default:
        return "unknown message id: " + paramInt;
      case 1:
        return "EVENT_QUERY_CB_CONFIG";
      case 2:
        return "EVENT_OPEN_ETWS_CHANNEL_DONE";
      case 3:
        break;
    } 
    return "EVENT_CLOSE_ETWS_CHANNEL_DONE";
  }
  
  private SortedSet<Integer> mergeConfigList(ArrayList<Integer> paramArrayList1, ArrayList<Integer> paramArrayList2) {
    Rlog.d("CellBroadcastFwkExt", "call mergeConfigInfoList");
    TreeSet<Integer> treeSet = new TreeSet();
    if (paramArrayList1 != null && paramArrayList1.size() > 0) {
      Iterator<Integer> iterator = paramArrayList1.iterator();
      while (iterator.hasNext())
        treeSet.add(Integer.valueOf(((Integer)iterator.next()).intValue())); 
    } else {
      Rlog.d("CellBroadcastFwkExt", "oldConfigList is null");
    } 
    if (paramArrayList2 != null && paramArrayList2.size() > 0) {
      Iterator<Integer> iterator = paramArrayList2.iterator();
      while (iterator.hasNext())
        treeSet.add(Integer.valueOf(((Integer)iterator.next()).intValue())); 
    } else {
      Rlog.d("CellBroadcastFwkExt", "newConfigList is null");
    } 
    return treeSet;
  }
  
  private SortedSet<Integer> minusConfigList(ArrayList<Integer> paramArrayList1, ArrayList<Integer> paramArrayList2) {
    Rlog.d("CellBroadcastFwkExt", "call minusConfigList");
    TreeSet<Integer> treeSet = new TreeSet();
    if (paramArrayList1 == null || paramArrayList1.size() == 0) {
      Rlog.d("CellBroadcastFwkExt", "oldConfigList, no need to minus");
      return treeSet;
    } 
    if (paramArrayList2 != null && paramArrayList2.size() > 0) {
      Iterator<Integer> iterator1 = paramArrayList2.iterator();
      while (iterator1.hasNext()) {
        int j = ((Integer)iterator1.next()).intValue();
        int i = 0;
        int k = paramArrayList1.size();
        while (i < k) {
          if (j == ((Integer)paramArrayList1.get(i)).intValue()) {
            Rlog.d("CellBroadcastFwkExt", "delete config: " + j);
            paramArrayList1.remove(i);
            break;
          } 
          i++;
        } 
      } 
    } 
    Iterator<Integer> iterator = paramArrayList1.iterator();
    while (true) {
      if (iterator.hasNext()) {
        treeSet.add(Integer.valueOf(((Integer)iterator.next()).intValue()));
        continue;
      } 
      return treeSet;
    } 
  }
  
  private ArrayList<Integer> parseConfigInfoToList(String paramString) {
    Rlog.d("CellBroadcastFwkExt", "call parseConfigInfoToList");
    byte b = 0;
    int i = 0;
    boolean bool = false;
    ArrayList<Integer> arrayList = new ArrayList();
    if (paramString != null && paramString.length() != 0 && (paramString.length() != 1 || paramString.charAt(0) != ',')) {
      paramString = paramString + ",";
      int j = 0;
      int k = paramString.length();
      while (true) {
        if (j < k) {
          int m;
          boolean bool1;
          byte b1;
          char c = paramString.charAt(j);
          if (c >= '0' && c <= '9') {
            m = i * 10 + c - 48;
            bool1 = bool;
            b1 = b;
          } else if (c == '-') {
            bool1 = true;
            m = 0;
            b1 = i;
          } else {
            b1 = b;
            bool1 = bool;
            m = i;
            if (c == ',') {
              if (bool) {
                for (m = b; m <= i; m++)
                  arrayList.add(Integer.valueOf(m)); 
                bool = false;
              } else {
                arrayList.add(Integer.valueOf(i));
              } 
              m = 0;
              b1 = b;
              bool1 = bool;
            } 
          } 
          j++;
          b = b1;
          bool = bool1;
          i = m;
          continue;
        } 
        return arrayList;
      } 
    } 
    return arrayList;
  }
  
  private String parseSortedSetToString(SortedSet<Integer> paramSortedSet) {
    Rlog.d("CellBroadcastFwkExt", "call parseSortedSet");
    if (paramSortedSet == null || paramSortedSet.size() == 0) {
      Rlog.d("CellBroadcastFwkExt", "sortedSet is null");
      return null;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<Integer> iterator = paramSortedSet.iterator();
    while (iterator.hasNext()) {
      stringBuilder.append(((Integer)iterator.next()).intValue());
      stringBuilder.append(',');
    } 
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    return stringBuilder.toString();
  }
  
  private void removeEtwsNoti(EtwsNotification paramEtwsNotification) {
    Rlog.d("CellBroadcastFwkExt", "call removeEtwsNoti");
    int k = 0;
    int i = 0;
    int j = this.mEtwsNotificationList.size();
    while (i < j) {
      if (((EtwsNotification)this.mEtwsNotificationList.get(i)).messageId == paramEtwsNotification.messageId) {
        this.mEtwsNotificationList.remove(i);
        j--;
        k++;
        continue;
      } 
      i++;
    } 
    Rlog.d("CellBroadcastFwkExt", "remove noti " + k);
  }
  
  private void removeFirstEtwsNotiThenAdd(EtwsNotification paramEtwsNotification) {
    Rlog.d("CellBroadcastFwkExt", "call removeFirstEtwsNotiThenAdd");
    if (this.mEtwsNotificationList.size() >= 4)
      this.mEtwsNotificationList.remove(0); 
    this.mEtwsNotificationList.add(paramEtwsNotification);
  }
  
  private void updateDatabase(boolean paramBoolean) {
    // Byte code:
    //   0: ldc 'CellBroadcastFwkExt'
    //   2: new java/lang/StringBuilder
    //   5: dup
    //   6: invokespecial <init> : ()V
    //   9: ldc_w 'updateDatabase '
    //   12: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: iload_1
    //   16: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   19: invokevirtual toString : ()Ljava/lang/String;
    //   22: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   25: pop
    //   26: getstatic com/mediatek/internal/telephony/CellBroadcastFwkExt.CHANNEL_URI : Landroid/net/Uri;
    //   29: astore #4
    //   31: aload_0
    //   32: getfield mPhoneId : I
    //   35: iconst_1
    //   36: if_icmpne -> 44
    //   39: getstatic com/mediatek/internal/telephony/CellBroadcastFwkExt.CHANNEL_URI1 : Landroid/net/Uri;
    //   42: astore #4
    //   44: iconst_5
    //   45: newarray int
    //   47: astore #6
    //   49: aload #6
    //   51: dup
    //   52: iconst_0
    //   53: sipush #4352
    //   56: iastore
    //   57: dup
    //   58: iconst_1
    //   59: sipush #4353
    //   62: iastore
    //   63: dup
    //   64: iconst_2
    //   65: sipush #4354
    //   68: iastore
    //   69: dup
    //   70: iconst_3
    //   71: sipush #4355
    //   74: iastore
    //   75: dup
    //   76: iconst_4
    //   77: sipush #4356
    //   80: iastore
    //   81: pop
    //   82: iconst_5
    //   83: newarray boolean
    //   85: astore #7
    //   87: aload #7
    //   89: dup
    //   90: iconst_0
    //   91: ldc 0
    //   93: bastore
    //   94: dup
    //   95: iconst_1
    //   96: ldc 0
    //   98: bastore
    //   99: dup
    //   100: iconst_2
    //   101: ldc 0
    //   103: bastore
    //   104: dup
    //   105: iconst_3
    //   106: ldc 0
    //   108: bastore
    //   109: dup
    //   110: iconst_4
    //   111: ldc 0
    //   113: bastore
    //   114: pop
    //   115: aload_0
    //   116: getfield mContext : Landroid/content/Context;
    //   119: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   122: aload #4
    //   124: aconst_null
    //   125: aconst_null
    //   126: aconst_null
    //   127: aconst_null
    //   128: invokevirtual query : (Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   131: astore #5
    //   133: aload #5
    //   135: ifnull -> 364
    //   138: aload #5
    //   140: invokeinterface moveToNext : ()Z
    //   145: ifeq -> 364
    //   148: aload #5
    //   150: aload #5
    //   152: ldc_w 'number'
    //   155: invokeinterface getColumnIndexOrThrow : (Ljava/lang/String;)I
    //   160: invokeinterface getInt : (I)I
    //   165: istore_2
    //   166: ldc 'CellBroadcastFwkExt'
    //   168: new java/lang/StringBuilder
    //   171: dup
    //   172: invokespecial <init> : ()V
    //   175: ldc_w 'updateDatabase channel:'
    //   178: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   181: iload_2
    //   182: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   185: invokevirtual toString : ()Ljava/lang/String;
    //   188: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   191: pop
    //   192: iload_2
    //   193: aload #6
    //   195: iconst_0
    //   196: iaload
    //   197: if_icmplt -> 138
    //   200: iload_2
    //   201: aload #6
    //   203: iconst_4
    //   204: iaload
    //   205: if_icmpgt -> 138
    //   208: aload #5
    //   210: aload #5
    //   212: ldc_w 'enable'
    //   215: invokeinterface getColumnIndexOrThrow : (Ljava/lang/String;)I
    //   220: invokeinterface getInt : (I)I
    //   225: istore_3
    //   226: aload #7
    //   228: iload_2
    //   229: aload #6
    //   231: iconst_0
    //   232: iaload
    //   233: isub
    //   234: iconst_1
    //   235: bastore
    //   236: iload_3
    //   237: iconst_1
    //   238: if_icmpne -> 512
    //   241: iload_1
    //   242: ifne -> 138
    //   245: goto -> 512
    //   248: aload #5
    //   250: aload #5
    //   252: ldc_w '_id'
    //   255: invokeinterface getColumnIndexOrThrow : (Ljava/lang/String;)I
    //   260: invokeinterface getInt : (I)I
    //   265: istore_3
    //   266: new android/content/ContentValues
    //   269: dup
    //   270: iconst_1
    //   271: invokespecial <init> : (I)V
    //   274: astore #8
    //   276: iload_1
    //   277: ifeq -> 359
    //   280: iconst_1
    //   281: istore_2
    //   282: aload #8
    //   284: ldc_w 'enable'
    //   287: iload_2
    //   288: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   291: invokevirtual put : (Ljava/lang/String;Ljava/lang/Integer;)V
    //   294: aload_0
    //   295: getfield mContext : Landroid/content/Context;
    //   298: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   301: aload #4
    //   303: aload #8
    //   305: new java/lang/StringBuilder
    //   308: dup
    //   309: invokespecial <init> : ()V
    //   312: ldc_w '_id='
    //   315: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   318: iload_3
    //   319: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   322: invokevirtual toString : ()Ljava/lang/String;
    //   325: aconst_null
    //   326: invokevirtual update : (Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   329: pop
    //   330: goto -> 138
    //   333: astore #4
    //   335: ldc 'CellBroadcastFwkExt'
    //   337: ldc_w 'get channels error:'
    //   340: aload #4
    //   342: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   345: pop
    //   346: aload #5
    //   348: ifnull -> 358
    //   351: aload #5
    //   353: invokeinterface close : ()V
    //   358: return
    //   359: iconst_0
    //   360: istore_2
    //   361: goto -> 282
    //   364: aload #5
    //   366: ifnull -> 376
    //   369: aload #5
    //   371: invokeinterface close : ()V
    //   376: aload #7
    //   378: arraylength
    //   379: istore_2
    //   380: iconst_0
    //   381: istore_2
    //   382: iload_2
    //   383: iconst_5
    //   384: if_icmpge -> 358
    //   387: aload #7
    //   389: iload_2
    //   390: baload
    //   391: ifne -> 483
    //   394: iload_2
    //   395: aload #6
    //   397: iconst_0
    //   398: iaload
    //   399: iadd
    //   400: istore_3
    //   401: new android/content/ContentValues
    //   404: dup
    //   405: invokespecial <init> : ()V
    //   408: astore #5
    //   410: aload #5
    //   412: ldc_w 'name'
    //   415: new java/lang/StringBuilder
    //   418: dup
    //   419: invokespecial <init> : ()V
    //   422: ldc_w ''
    //   425: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   428: iload_3
    //   429: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   432: invokevirtual toString : ()Ljava/lang/String;
    //   435: invokevirtual put : (Ljava/lang/String;Ljava/lang/String;)V
    //   438: aload #5
    //   440: ldc_w 'number'
    //   443: iload_3
    //   444: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   447: invokevirtual put : (Ljava/lang/String;Ljava/lang/Integer;)V
    //   450: iload_1
    //   451: ifeq -> 507
    //   454: iconst_1
    //   455: istore_3
    //   456: aload #5
    //   458: ldc_w 'enable'
    //   461: iload_3
    //   462: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   465: invokevirtual put : (Ljava/lang/String;Ljava/lang/Integer;)V
    //   468: aload_0
    //   469: getfield mContext : Landroid/content/Context;
    //   472: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   475: aload #4
    //   477: aload #5
    //   479: invokevirtual insert : (Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;
    //   482: pop
    //   483: iload_2
    //   484: iconst_1
    //   485: iadd
    //   486: istore_2
    //   487: goto -> 382
    //   490: astore #4
    //   492: aload #5
    //   494: ifnull -> 504
    //   497: aload #5
    //   499: invokeinterface close : ()V
    //   504: aload #4
    //   506: athrow
    //   507: iconst_0
    //   508: istore_3
    //   509: goto -> 456
    //   512: iload_3
    //   513: ifne -> 248
    //   516: iload_1
    //   517: ifeq -> 138
    //   520: goto -> 248
    // Exception table:
    //   from	to	target	type
    //   138	192	333	java/lang/Exception
    //   138	192	490	finally
    //   208	226	333	java/lang/Exception
    //   208	226	490	finally
    //   248	276	333	java/lang/Exception
    //   248	276	490	finally
    //   282	330	333	java/lang/Exception
    //   282	330	490	finally
    //   335	346	490	finally
  }
  
  public void closeEtwsChannel(EtwsNotification paramEtwsNotification) {
    Rlog.d("CellBroadcastFwkExt", "closeEtwsChannel");
    Message message = this.mHandler.obtainMessage(1, 3, 0, paramEtwsNotification);
    this.mCi.queryCellBroadcastConfigInfo(message);
  }
  
  public boolean containDuplicatedEtwsNotification(EtwsNotification paramEtwsNotification) {
    Rlog.d("CellBroadcastFwkExt", "call containDuplicatedEtwsNotification");
    if (paramEtwsNotification == null) {
      Rlog.d("CellBroadcastFwkExt", "null EtwsNotification");
      return false;
    } 
    Iterator<EtwsNotification> iterator = this.mEtwsNotificationList.iterator();
    while (true) {
      if (iterator.hasNext()) {
        if (((EtwsNotification)iterator.next()).isDuplicatedEtws(paramEtwsNotification))
          return true; 
        continue;
      } 
      return false;
    } 
  }
  
  public void openEtwsChannel(EtwsNotification paramEtwsNotification) {
    Rlog.d("CellBroadcastFwkExt", "openEtwsChannel");
    Message message = this.mHandler.obtainMessage(1, 2, 0, paramEtwsNotification);
    this.mCi.queryCellBroadcastConfigInfo(message);
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/CellBroadcastFwkExt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */