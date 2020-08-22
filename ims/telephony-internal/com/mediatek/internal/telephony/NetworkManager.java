package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;

public class NetworkManager extends Handler {
  protected static final int EVENT_GET_AVAILABLE_NW = 1;
  
  static final String LOG_TAG = "GSM";
  
  private static NetworkManager sNetworkManager;
  
  private CommandsInterface[] mCi;
  
  private Context mContext;
  
  private int mPhoneCount;
  
  private NetworkManager(Context paramContext, int paramInt, CommandsInterface[] paramArrayOfCommandsInterface) {
    log("Initialize NetworkManager under airplane mode phoneCount= " + paramInt);
    this.mContext = paramContext;
    this.mCi = paramArrayOfCommandsInterface;
    this.mPhoneCount = paramInt;
  }
  
  public static NetworkManager getInstance() {
    return sNetworkManager;
  }
  
  public static NetworkManager init(Context paramContext, int paramInt, CommandsInterface[] paramArrayOfCommandsInterface) {
    // Byte code:
    //   0: ldc com/mediatek/internal/telephony/NetworkManager
    //   2: monitorenter
    //   3: getstatic com/mediatek/internal/telephony/NetworkManager.sNetworkManager : Lcom/mediatek/internal/telephony/NetworkManager;
    //   6: ifnonnull -> 22
    //   9: new com/mediatek/internal/telephony/NetworkManager
    //   12: dup
    //   13: aload_0
    //   14: iload_1
    //   15: aload_2
    //   16: invokespecial <init> : (Landroid/content/Context;I[Lcom/android/internal/telephony/CommandsInterface;)V
    //   19: putstatic com/mediatek/internal/telephony/NetworkManager.sNetworkManager : Lcom/mediatek/internal/telephony/NetworkManager;
    //   22: getstatic com/mediatek/internal/telephony/NetworkManager.sNetworkManager : Lcom/mediatek/internal/telephony/NetworkManager;
    //   25: astore_0
    //   26: ldc com/mediatek/internal/telephony/NetworkManager
    //   28: monitorexit
    //   29: aload_0
    //   30: areturn
    //   31: astore_0
    //   32: ldc com/mediatek/internal/telephony/NetworkManager
    //   34: monitorexit
    //   35: aload_0
    //   36: athrow
    // Exception table:
    //   from	to	target	type
    //   3	22	31	finally
    //   22	29	31	finally
    //   32	35	31	finally
  }
  
  private static void log(String paramString) {
    Log.d("GSM", "[NetworkManager] " + paramString);
  }
  
  public void getAvailableNetworks(long paramLong, Message paramMessage) {}
  
  public void handleMessage(Message paramMessage) {
    // Byte code:
    //   0: aload_1
    //   1: getfield what : I
    //   4: tableswitch default -> 24, 1 -> 50
    //   24: new java/lang/StringBuilder
    //   27: dup
    //   28: invokespecial <init> : ()V
    //   31: ldc 'Unhandled message with number: '
    //   33: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   36: aload_1
    //   37: getfield what : I
    //   40: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   43: invokevirtual toString : ()Ljava/lang/String;
    //   46: invokestatic log : (Ljava/lang/String;)V
    //   49: return
    //   50: aload_0
    //   51: monitorenter
    //   52: aload_0
    //   53: monitorexit
    //   54: return
    //   55: astore_1
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_1
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   52	54	55	finally
    //   56	58	55	finally
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/NetworkManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */