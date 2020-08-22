package com.mediatek.internal.telephony.dataconnection;

import android.os.SystemProperties;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.dataconnection.DcFailCause;
import com.mediatek.common.MPlugin;
import com.mediatek.common.telephony.IGsmDCTExt;
import java.util.EnumSet;
import java.util.HashMap;

public class DcFailCauseManager {
  static {
    if (SystemProperties.getInt("persist.data.cc33.support", 0) == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    MTK_CC33_SUPPORT = bool;
    if (SystemProperties.get("ro.mtk_fallback_retry_support").equals("1")) {
      bool = true;
    } else {
      bool = false;
    } 
    MTK_FALLBACK_RETRY_SUPPORT = bool;
    OP001Ext_FAIL_CAUSE_TABLE = new int[] { 29, 33 };
    OP002Ext_FAIL_CAUSE_TABLE = new int[] { -1000 };
  }
  
  public DcFailCauseManager() {
    log("constructor");
  }
  
  private boolean canIgnoredReason(Operator paramOperator, String paramString) {
    switch (paramOperator) {
      default:
        return false;
      case OP001Ext:
        break;
    } 
    if (TextUtils.equals(paramString, "dataAttached") || TextUtils.equals(paramString, "lostDataConnection") || TextUtils.equals(paramString, DcFailCause.LOST_CONNECTION.toString()))
      return true; 
  }
  
  private void log(String paramString) {
    Rlog.d("DcFailCauseManager", paramString);
  }
  
  private void loge(String paramString) {
    Rlog.e("DcFailCauseManager", paramString);
  }
  
  private void setRetryConfig(Operator paramOperator, Object paramObject) {
    paramObject = paramObject;
    this.mRetryCount = paramObject.getRetryCount();
    log("RetryCount: " + this.mRetryCount);
    switch (paramOperator) {
      default:
        this.mMaxRetryCount = retryConfigForDefault.maxRetryCount.getValue();
        this.mRetryTime = retryConfigForDefault.retryTime.getValue();
        this.mRandomizationTime = retryConfigForDefault.randomizationTime.getValue();
        log("[default] set SmRetry Config:" + this.mMaxRetryCount + "/" + this.mRetryTime + "/" + this.mRandomizationTime);
        paramObject.configure(this.mMaxRetryCount, this.mRetryTime, this.mRandomizationTime);
        paramObject.setRetryCount(this.mRetryCount);
        return;
      case OP001Ext:
        this.mMaxRetryCount = retryConfigForOp001Ext.maxRetryCount.getValue();
        this.mRetryTime = retryConfigForOp001Ext.retryTime.getValue();
        this.mRandomizationTime = retryConfigForOp001Ext.randomizationTime.getValue();
        log("[" + paramOperator + "] set SmRetry Config:" + this.mMaxRetryCount + "/" + this.mRetryTime + "/" + this.mRandomizationTime);
        paramObject.configure(this.mMaxRetryCount, this.mRetryTime, this.mRandomizationTime);
        paramObject.setRetryCount(this.mRetryCount);
        return;
      case OP002Ext:
        break;
    } 
    log("[" + paramOperator + "] set SmRetry Config:" + "max_retries=13, 5000,10000,30000,60000,300000,1800000,3600000,14400000,28800000,28800000,28800000,28800000,28800000");
    paramObject.configure("max_retries=13, 5000,10000,30000,60000,300000,1800000,3600000,14400000,28800000,28800000,28800000,28800000,28800000");
    paramObject.setRetryCount(this.mRetryCount);
  }
  
  public boolean canHandleFailCause(Object paramObject1, Object paramObject2, String paramString) {
    // Byte code:
    //   0: ldc 'persist.dc.fcmgr.enable'
    //   2: iconst_1
    //   3: invokestatic getBoolean : (Ljava/lang/String;Z)Z
    //   6: ifne -> 17
    //   9: aload_0
    //   10: ldc 'dc fail cause handling mechanism is disabled'
    //   12: invokespecial loge : (Ljava/lang/String;)V
    //   15: iconst_0
    //   16: ireturn
    //   17: iconst_0
    //   18: istore #7
    //   20: iconst_0
    //   21: istore #8
    //   23: iconst_0
    //   24: istore #9
    //   26: ldc ''
    //   28: astore #11
    //   30: aload_1
    //   31: checkcast com/android/internal/telephony/dataconnection/DcFailCause
    //   34: astore #13
    //   36: ldc ''
    //   38: astore #12
    //   40: aload #12
    //   42: astore #10
    //   44: aload_0
    //   45: getfield mPhone : Lcom/android/internal/telephony/PhoneBase;
    //   48: invokevirtual getSubId : ()I
    //   51: invokestatic getPhoneId : (I)I
    //   54: istore #4
    //   56: aload #12
    //   58: astore #10
    //   60: invokestatic getDefault : ()Landroid/telephony/TelephonyManager;
    //   63: iload #4
    //   65: invokevirtual getNetworkOperatorForPhone : (I)Ljava/lang/String;
    //   68: astore #12
    //   70: aload #12
    //   72: astore #10
    //   74: aload_0
    //   75: new java/lang/StringBuilder
    //   78: dup
    //   79: invokespecial <init> : ()V
    //   82: ldc_w 'Check PLMN='
    //   85: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   88: aload #12
    //   90: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   93: invokevirtual toString : ()Ljava/lang/String;
    //   96: invokespecial log : (Ljava/lang/String;)V
    //   99: aload_1
    //   100: ifnull -> 108
    //   103: ldc_w 'c1'
    //   106: astore #11
    //   108: aload_1
    //   109: ifnull -> 226
    //   112: aload_2
    //   113: ifnull -> 226
    //   116: ldc_w 'c2'
    //   119: astore #10
    //   121: aload #10
    //   123: astore_1
    //   124: ldc_w 'c2'
    //   127: aload #10
    //   129: invokevirtual equals : (Ljava/lang/Object;)Z
    //   132: ifeq -> 146
    //   135: aload #10
    //   137: astore_1
    //   138: aload_3
    //   139: ifnull -> 146
    //   142: ldc_w 'c4'
    //   145: astore_1
    //   146: iconst_0
    //   147: istore #4
    //   149: iload #4
    //   151: getstatic com/mediatek/internal/telephony/dataconnection/DcFailCauseManager.specificPLMN : [[Ljava/lang/String;
    //   154: arraylength
    //   155: if_icmpge -> 309
    //   158: iconst_0
    //   159: istore #6
    //   161: iconst_0
    //   162: istore #5
    //   164: iload #5
    //   166: getstatic com/mediatek/internal/telephony/dataconnection/DcFailCauseManager.specificPLMN : [[Ljava/lang/String;
    //   169: iload #4
    //   171: aaload
    //   172: arraylength
    //   173: if_icmpge -> 250
    //   176: aload #12
    //   178: getstatic com/mediatek/internal/telephony/dataconnection/DcFailCauseManager.specificPLMN : [[Ljava/lang/String;
    //   181: iload #4
    //   183: aaload
    //   184: iload #5
    //   186: aaload
    //   187: invokevirtual equals : (Ljava/lang/Object;)Z
    //   190: ifeq -> 196
    //   193: iconst_1
    //   194: istore #6
    //   196: iload #5
    //   198: iconst_1
    //   199: iadd
    //   200: istore #5
    //   202: goto -> 164
    //   205: astore #12
    //   207: aload_0
    //   208: ldc_w 'get plmn fail'
    //   211: invokespecial log : (Ljava/lang/String;)V
    //   214: aload #12
    //   216: invokevirtual printStackTrace : ()V
    //   219: aload #10
    //   221: astore #12
    //   223: goto -> 99
    //   226: aload #11
    //   228: astore #10
    //   230: aload_1
    //   231: ifnull -> 121
    //   234: aload #11
    //   236: astore #10
    //   238: aload_3
    //   239: ifnull -> 121
    //   242: ldc_w 'c3'
    //   245: astore #10
    //   247: goto -> 121
    //   250: iload #6
    //   252: iconst_1
    //   253: if_icmpne -> 384
    //   256: aload_0
    //   257: iload #4
    //   259: invokestatic get : (I)Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;
    //   262: putfield mOperator : Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;
    //   265: aload_0
    //   266: new java/lang/StringBuilder
    //   269: dup
    //   270: invokespecial <init> : ()V
    //   273: ldc_w 'Serving in specific op='
    //   276: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   279: aload_0
    //   280: getfield mOperator : Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;
    //   283: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   286: ldc_w '('
    //   289: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   292: iload #4
    //   294: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   297: ldc_w ')'
    //   300: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   303: invokevirtual toString : ()Ljava/lang/String;
    //   306: invokespecial log : (Ljava/lang/String;)V
    //   309: getstatic com/mediatek/internal/telephony/dataconnection/DcFailCauseManager$1.$SwitchMap$com$mediatek$internal$telephony$dataconnection$DcFailCauseManager$Operator : [I
    //   312: aload_0
    //   313: getfield mOperator : Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;
    //   316: invokevirtual ordinal : ()I
    //   319: iaload
    //   320: tableswitch default -> 340, 1 -> 393
    //   340: aload #13
    //   342: sipush #-1000
    //   345: invokestatic fromInt : (I)Lcom/android/internal/telephony/dataconnection/DcFailCause;
    //   348: invokevirtual equals : (Ljava/lang/Object;)Z
    //   351: ifeq -> 357
    //   354: iconst_1
    //   355: istore #8
    //   357: iload #8
    //   359: ifeq -> 538
    //   362: ldc_w 'c2'
    //   365: aload_1
    //   366: invokevirtual equals : (Ljava/lang/Object;)Z
    //   369: ifeq -> 467
    //   372: aload_0
    //   373: aload_0
    //   374: getfield mOperator : Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;
    //   377: aload_2
    //   378: invokespecial setRetryConfig : (Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;Ljava/lang/Object;)V
    //   381: iload #8
    //   383: ireturn
    //   384: iload #4
    //   386: iconst_1
    //   387: iadd
    //   388: istore #4
    //   390: goto -> 149
    //   393: getstatic com/mediatek/internal/telephony/dataconnection/DcFailCauseManager.OP001Ext_FAIL_CAUSE_TABLE : [I
    //   396: astore #10
    //   398: aload #10
    //   400: arraylength
    //   401: istore #5
    //   403: iconst_0
    //   404: istore #4
    //   406: iload #7
    //   408: istore #8
    //   410: iload #4
    //   412: iload #5
    //   414: if_icmpge -> 357
    //   417: aload #10
    //   419: iload #4
    //   421: iaload
    //   422: invokestatic fromInt : (I)Lcom/android/internal/telephony/dataconnection/DcFailCause;
    //   425: astore #11
    //   427: iload #7
    //   429: istore #8
    //   431: getstatic com/mediatek/internal/telephony/dataconnection/DcFailCauseManager.MTK_CC33_SUPPORT : Z
    //   434: ifeq -> 454
    //   437: iload #7
    //   439: istore #8
    //   441: aload #13
    //   443: aload #11
    //   445: invokevirtual equals : (Ljava/lang/Object;)Z
    //   448: ifeq -> 454
    //   451: iconst_1
    //   452: istore #8
    //   454: iload #4
    //   456: iconst_1
    //   457: iadd
    //   458: istore #4
    //   460: iload #8
    //   462: istore #7
    //   464: goto -> 406
    //   467: ldc_w 'c3'
    //   470: aload_1
    //   471: invokevirtual equals : (Ljava/lang/Object;)Z
    //   474: ifeq -> 498
    //   477: aload_0
    //   478: aload_0
    //   479: getfield mOperator : Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;
    //   482: aload_3
    //   483: invokespecial canIgnoredReason : (Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;Ljava/lang/String;)Z
    //   486: ifeq -> 15
    //   489: aload_0
    //   490: ldc_w 'Can ignore this setup conn reason by Plmn!'
    //   493: invokespecial log : (Ljava/lang/String;)V
    //   496: iconst_1
    //   497: ireturn
    //   498: ldc_w 'c4'
    //   501: aload_1
    //   502: invokevirtual equals : (Ljava/lang/Object;)Z
    //   505: ifeq -> 381
    //   508: aload_0
    //   509: aload_0
    //   510: getfield mOperator : Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;
    //   513: aload_2
    //   514: invokespecial setRetryConfig : (Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;Ljava/lang/Object;)V
    //   517: aload_0
    //   518: aload_0
    //   519: getfield mOperator : Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;
    //   522: aload_3
    //   523: invokespecial canIgnoredReason : (Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;Ljava/lang/String;)Z
    //   526: ifeq -> 15
    //   529: aload_0
    //   530: ldc_w 'Can ignore this setup conn reason by Plmn!'
    //   533: invokespecial log : (Ljava/lang/String;)V
    //   536: iconst_1
    //   537: ireturn
    //   538: iload #9
    //   540: istore #7
    //   542: aload_0
    //   543: getfield mIsBsp : Z
    //   546: ifne -> 562
    //   549: aload_0
    //   550: getfield mGsmDCTExt : Lcom/mediatek/common/telephony/IGsmDCTExt;
    //   553: aload #13
    //   555: invokeinterface needSmRetry : (Ljava/lang/Object;)Z
    //   560: istore #7
    //   562: iload #7
    //   564: ifeq -> 707
    //   567: iconst_0
    //   568: istore #9
    //   570: iconst_0
    //   571: istore #8
    //   573: ldc_w 'c2'
    //   576: aload_1
    //   577: invokevirtual equals : (Ljava/lang/Object;)Z
    //   580: ifeq -> 618
    //   583: aload_0
    //   584: getfield mGsmDCTExt : Lcom/mediatek/common/telephony/IGsmDCTExt;
    //   587: aload_2
    //   588: invokeinterface setSmRetryConfig : (Ljava/lang/Object;)Z
    //   593: pop
    //   594: iload #7
    //   596: ireturn
    //   597: astore #10
    //   599: aload_0
    //   600: ldc_w 'check needSmRetry fail!'
    //   603: invokespecial loge : (Ljava/lang/String;)V
    //   606: aload #10
    //   608: invokevirtual printStackTrace : ()V
    //   611: iload #9
    //   613: istore #7
    //   615: goto -> 562
    //   618: ldc_w 'c3'
    //   621: aload_1
    //   622: invokevirtual equals : (Ljava/lang/Object;)Z
    //   625: ifeq -> 657
    //   628: iload #8
    //   630: istore #7
    //   632: aload_0
    //   633: aload_0
    //   634: getfield mOperator : Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;
    //   637: aload_3
    //   638: invokespecial canIgnoredReason : (Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;Ljava/lang/String;)Z
    //   641: ifeq -> 654
    //   644: iconst_1
    //   645: istore #7
    //   647: aload_0
    //   648: ldc_w 'Can not ignore this setup conn reason by OP!'
    //   651: invokespecial log : (Ljava/lang/String;)V
    //   654: iload #7
    //   656: ireturn
    //   657: ldc_w 'c4'
    //   660: aload_1
    //   661: invokevirtual equals : (Ljava/lang/Object;)Z
    //   664: ifeq -> 594
    //   667: aload_0
    //   668: getfield mGsmDCTExt : Lcom/mediatek/common/telephony/IGsmDCTExt;
    //   671: aload_2
    //   672: invokeinterface setSmRetryConfig : (Ljava/lang/Object;)Z
    //   677: pop
    //   678: iload #9
    //   680: istore #7
    //   682: aload_0
    //   683: aload_0
    //   684: getfield mOperator : Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;
    //   687: aload_3
    //   688: invokespecial canIgnoredReason : (Lcom/mediatek/internal/telephony/dataconnection/DcFailCauseManager$Operator;Ljava/lang/String;)Z
    //   691: ifeq -> 704
    //   694: iconst_1
    //   695: istore #7
    //   697: aload_0
    //   698: ldc_w 'Can not ignore this setup conn reason by OP!'
    //   701: invokespecial log : (Ljava/lang/String;)V
    //   704: iload #7
    //   706: ireturn
    //   707: aload_0
    //   708: ldc_w 'Can not handle this fail cause!'
    //   711: invokespecial log : (Ljava/lang/String;)V
    //   714: iconst_0
    //   715: ireturn
    // Exception table:
    //   from	to	target	type
    //   44	56	205	java/lang/Exception
    //   60	70	205	java/lang/Exception
    //   74	99	205	java/lang/Exception
    //   549	562	597	java/lang/Exception
  }
  
  public boolean createGsmDCTExt(PhoneBase paramPhoneBase) {
    this.mPhone = paramPhoneBase;
    boolean bool = false;
    if (!this.mIsBsp)
      try {
        this.mGsmDCTExt = (IGsmDCTExt)MPlugin.createInstance(IGsmDCTExt.class.getName(), this.mPhone.getContext());
        log("mGsmDCTExt init on phone[" + this.mPhone.getPhoneId() + "]");
        return true;
      } catch (Exception exception) {
        loge("mGsmDCTExt init fail");
        exception.printStackTrace();
        return false;
      }  
    return bool;
  }
  
  public String toString() {
    String str = "DcFailCauseManager: { operator=" + this.mOperator + " maxRetry=" + this.mMaxRetryCount + " retryTime=" + this.mRetryTime + " randomizationTime" + this.mRandomizationTime + " retryCount" + this.mRetryCount;
    return str + "}";
  }
  
  static {
    boolean bool;
  }
  
  public static final boolean DBG = true;
  
  private static final String FALLBACK_DATA_RETRY_CONFIG = "max_retries=13, 5000,10000,30000,60000,300000,1800000,3600000,14400000,28800000,28800000,28800000,28800000,28800000";
  
  public static final String LOG_TAG = "DcFailCauseManager";
  
  public static boolean MTK_CC33_SUPPORT = false;
  
  public static final String MTK_DC_FCMGR_ENABLE = "persist.dc.fcmgr.enable";
  
  public static final boolean MTK_FALLBACK_RETRY_SUPPORT;
  
  private static final int[] OP001Ext_FAIL_CAUSE_TABLE;
  
  private static final int[] OP002Ext_FAIL_CAUSE_TABLE;
  
  private static final int PDP_FAIL_FALLBACK_RETRY = -1000;
  
  private static final int SERVICE_OPTION_NOT_SUBSCRIBED = 33;
  
  private static final int USER_AUTHENTICATION = 29;
  
  public static final boolean VDBG = false;
  
  private static final String[][] specificPLMN = new String[][] { { "33402", "334020" }, { "50501" } };
  
  private IGsmDCTExt mGsmDCTExt;
  
  private boolean mIsBsp = SystemProperties.getBoolean("ro.mtk_bsp_package", false);
  
  public int mMaxRetryCount;
  
  public Operator mOperator = Operator.NONE;
  
  private PhoneBase mPhone;
  
  public int mRandomizationTime;
  
  public int mRetryCount;
  
  public int mRetryTime;
  
  public enum Operator {
    NONE(-1),
    OP001Ext(0),
    OP002Ext(1);
    
    private static final HashMap<Integer, Operator> lookup = new HashMap<Integer, Operator>();
    
    private int value;
    
    static {
      for (Operator operator : EnumSet.<Operator>allOf(Operator.class))
        lookup.put(Integer.valueOf(operator.getValue()), operator); 
    }
    
    Operator(int param1Int1) {
      this.value = param1Int1;
    }
    
    public static Operator get(int param1Int) {
      return lookup.get(Integer.valueOf(param1Int));
    }
    
    public int getValue() {
      return this.value;
    }
  }
  
  private enum retryConfigForDefault {
    maxRetryCount(1),
    randomizationTime(1),
    retryTime(0);
    
    private final int value;
    
    static {
      $VALUES = new retryConfigForDefault[] { maxRetryCount, retryTime, randomizationTime };
    }
    
    retryConfigForDefault(int param1Int1) {
      this.value = param1Int1;
    }
    
    public int getValue() {
      return this.value;
    }
  }
  
  private enum retryConfigForOp001Ext {
    maxRetryCount(2),
    randomizationTime(2),
    retryTime(45000);
    
    private final int value;
    
    static {
      $VALUES = new retryConfigForOp001Ext[] { maxRetryCount, retryTime, randomizationTime };
    }
    
    retryConfigForOp001Ext(int param1Int1) {
      this.value = param1Int1;
    }
    
    public int getValue() {
      return this.value;
    }
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/dataconnection/DcFailCauseManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */