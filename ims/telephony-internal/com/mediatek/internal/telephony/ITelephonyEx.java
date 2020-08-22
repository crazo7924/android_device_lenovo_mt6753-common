package com.mediatek.internal.telephony;

import android.net.LinkProperties;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.NeighboringCellInfo;
import android.telephony.RadioAccessFamily;
import java.util.ArrayList;
import java.util.List;

public interface ITelephonyEx extends IInterface {
   boolean broadcastIccUnlockIntent(int var1) throws RemoteException;

   int btSimapApduRequest(int var1, String var2, BtSimapOperResponse var3) throws RemoteException;

   int btSimapConnectSIM(int var1, BtSimapOperResponse var2) throws RemoteException;

   int btSimapDisconnectSIM() throws RemoteException;

   int btSimapPowerOffSIM() throws RemoteException;

   int btSimapPowerOnSIM(int var1, BtSimapOperResponse var2) throws RemoteException;

   int btSimapResetSIM(int var1, BtSimapOperResponse var2) throws RemoteException;

   boolean canSwitchDefaultSubId() throws RemoteException;

   boolean configSimSwap(boolean var1) throws RemoteException;

   void disablePseudoBSMonitor(int var1) throws RemoteException;

   void enablePseudoBSMonitor(int var1, boolean var2, int var3) throws RemoteException;

   void finalizeService(String var1) throws RemoteException;

   int[] getAdnStorageInfo(int var1) throws RemoteException;

   int getBcsmsCfgFromRuim(int var1, int var2, int var3) throws RemoteException;

   int[] getCallForwardingFc(int var1, int var2) throws RemoteException;

   int[] getCallWaitingFc(int var1) throws RemoteException;

   String[] getCapSwitchManualList() throws RemoteException;

   int getCdmaSubscriptionActStatus(int var1) throws RemoteException;

   Bundle getCellLocationUsingSlotId(int var1) throws RemoteException;

   int[] getDonotDisturbFc(int var1) throws RemoteException;

   String getIccCardType(int var1) throws RemoteException;

   int getLastDataConnectionFailCause(String var1, int var2) throws RemoteException;

   LinkProperties getLinkProperties(String var1, int var2) throws RemoteException;

   String getLocatedPlmn(int var1) throws RemoteException;

   String getLteAccessStratumState() throws RemoteException;

   int getMainCapabilityPhoneId() throws RemoteException;

   MmsConfigInfo getMmsConfigInfo(int var1) throws RemoteException;

   MmsIcpInfo getMmsIcpInfo(int var1) throws RemoteException;

   String getMvnoMatchType(int var1) throws RemoteException;

   String getMvnoPattern(int var1, String var2) throws RemoteException;

   List<NeighboringCellInfo> getNeighboringCellInfoUsingSlotId(int var1) throws RemoteException;

   int getNetworkHideState(int var1) throws RemoteException;

   String getNetworkOperatorGemini(int var1) throws RemoteException;

   String getNetworkOperatorNameGemini(int var1) throws RemoteException;

   String getNetworkOperatorNameUsingSub(int var1) throws RemoteException;

   String getNetworkOperatorUsingSub(int var1) throws RemoteException;

   int getNextMessageId(int var1) throws RemoteException;

   int getPhoneCapability(int var1) throws RemoteException;

   Bundle getScAddressUsingSubId(int var1) throws RemoteException;

   Bundle getServiceState(int var1) throws RemoteException;

   Bundle getUserCustomizedEccList() throws RemoteException;

   int[] getVMRetrieveFc(int var1) throws RemoteException;

   int getWapMsgId(int var1) throws RemoteException;

   void initializeService(String var1) throws RemoteException;

   boolean isAirplanemodeAvailableNow() throws RemoteException;

   boolean isAppTypeSupported(int var1, int var2) throws RemoteException;

   boolean isCapSwitchManualEnabled() throws RemoteException;

   boolean isCapabilitySwitching() throws RemoteException;

   boolean isEccInProgress() throws RemoteException;

   boolean isFdnEnabled(int var1) throws RemoteException;

   boolean isImsRegistered(int var1) throws RemoteException;

   boolean isInHomeNetwork(int var1) throws RemoteException;

   boolean isOmhCard(int var1) throws RemoteException;

   boolean isOmhEnable(int var1) throws RemoteException;

   boolean isPhbReady(int var1) throws RemoteException;

   boolean isRadioOffBySimManagement(int var1) throws RemoteException;

   boolean isSharedDefaultApn() throws RemoteException;

   boolean isSimSwapped() throws RemoteException;

   boolean isTestIccCard(int var1) throws RemoteException;

   boolean isUserCustomizedEcc(String var1) throws RemoteException;

   boolean isVolteEnabled(int var1) throws RemoteException;

   boolean isWifiCallingEnabled(int var1) throws RemoteException;

   Bundle queryNetworkLock(int var1, int var2) throws RemoteException;

   List<PseudoBSRecord> queryPseudoBSRecords(int var1) throws RemoteException;

   void repollIccStateForNetworkLock(int var1, boolean var2) throws RemoteException;

   boolean setDefaultSubIdForAll(int var1, int var2, ISetDefaultSubResultCallback var3) throws RemoteException;

   void setEccInProgress(boolean var1) throws RemoteException;

   int setLine1Number(int var1, String var2, String var3) throws RemoteException;

   boolean setLteAccessStratumReport(boolean var1) throws RemoteException;

   boolean setLteUplinkDataTransfer(boolean var1, int var2) throws RemoteException;

   void setPhoneCapability(int[] var1, int[] var2) throws RemoteException;

   boolean setRadioCapability(RadioAccessFamily[] var1) throws RemoteException;

   boolean setScAddressUsingSubId(int var1, String var2) throws RemoteException;

   void setTelLog(boolean var1) throws RemoteException;

   void setTrmForPhone(int var1, int var2) throws RemoteException;

   byte[] simAkaAuthentication(int var1, int var2, byte[] var3, byte[] var4) throws RemoteException;

   byte[] simGbaAuthBootStrapMode(int var1, int var2, byte[] var3, byte[] var4) throws RemoteException;

   byte[] simGbaAuthNafMode(int var1, int var2, byte[] var3, byte[] var4) throws RemoteException;

   int supplyNetworkDepersonalization(int var1, String var2) throws RemoteException;

   boolean updateUserCustomizedEccList(Bundle var1) throws RemoteException;

   public abstract static class Stub extends Binder implements ITelephonyEx {
      private static final String DESCRIPTOR = "com.mediatek.internal.telephony.ITelephonyEx";
      static final int TRANSACTION_broadcastIccUnlockIntent = 24;
      static final int TRANSACTION_btSimapApduRequest = 17;
      static final int TRANSACTION_btSimapConnectSIM = 15;
      static final int TRANSACTION_btSimapDisconnectSIM = 16;
      static final int TRANSACTION_btSimapPowerOffSIM = 20;
      static final int TRANSACTION_btSimapPowerOnSIM = 19;
      static final int TRANSACTION_btSimapResetSIM = 18;
      static final int TRANSACTION_canSwitchDefaultSubId = 70;
      static final int TRANSACTION_configSimSwap = 28;
      static final int TRANSACTION_disablePseudoBSMonitor = 78;
      static final int TRANSACTION_enablePseudoBSMonitor = 77;
      static final int TRANSACTION_finalizeService = 47;
      static final int TRANSACTION_getAdnStorageInfo = 35;
      static final int TRANSACTION_getBcsmsCfgFromRuim = 61;
      static final int TRANSACTION_getCallForwardingFc = 57;
      static final int TRANSACTION_getCallWaitingFc = 58;
      static final int TRANSACTION_getCapSwitchManualList = 31;
      static final int TRANSACTION_getCdmaSubscriptionActStatus = 69;
      static final int TRANSACTION_getCellLocationUsingSlotId = 67;
      static final int TRANSACTION_getDonotDisturbFc = 59;
      static final int TRANSACTION_getIccCardType = 6;
      static final int TRANSACTION_getLastDataConnectionFailCause = 40;
      static final int TRANSACTION_getLinkProperties = 41;
      static final int TRANSACTION_getLocatedPlmn = 32;
      static final int TRANSACTION_getLteAccessStratumState = 51;
      static final int TRANSACTION_getMainCapabilityPhoneId = 45;
      static final int TRANSACTION_getMmsConfigInfo = 54;
      static final int TRANSACTION_getMmsIcpInfo = 53;
      static final int TRANSACTION_getMvnoMatchType = 9;
      static final int TRANSACTION_getMvnoPattern = 10;
      static final int TRANSACTION_getNeighboringCellInfoUsingSlotId = 68;
      static final int TRANSACTION_getNetworkHideState = 33;
      static final int TRANSACTION_getNetworkOperatorGemini = 13;
      static final int TRANSACTION_getNetworkOperatorNameGemini = 11;
      static final int TRANSACTION_getNetworkOperatorNameUsingSub = 12;
      static final int TRANSACTION_getNetworkOperatorUsingSub = 14;
      static final int TRANSACTION_getNextMessageId = 62;
      static final int TRANSACTION_getPhoneCapability = 26;
      static final int TRANSACTION_getScAddressUsingSubId = 37;
      static final int TRANSACTION_getServiceState = 34;
      static final int TRANSACTION_getUserCustomizedEccList = 64;
      static final int TRANSACTION_getVMRetrieveFc = 60;
      static final int TRANSACTION_getWapMsgId = 63;
      static final int TRANSACTION_initializeService = 46;
      static final int TRANSACTION_isAirplanemodeAvailableNow = 39;
      static final int TRANSACTION_isAppTypeSupported = 7;
      static final int TRANSACTION_isCapSwitchManualEnabled = 30;
      static final int TRANSACTION_isCapabilitySwitching = 43;
      static final int TRANSACTION_isEccInProgress = 73;
      static final int TRANSACTION_isFdnEnabled = 5;
      static final int TRANSACTION_isImsRegistered = 74;
      static final int TRANSACTION_isInHomeNetwork = 48;
      static final int TRANSACTION_isOmhCard = 56;
      static final int TRANSACTION_isOmhEnable = 55;
      static final int TRANSACTION_isPhbReady = 36;
      static final int TRANSACTION_isRadioOffBySimManagement = 25;
      static final int TRANSACTION_isSharedDefaultApn = 52;
      static final int TRANSACTION_isSimSwapped = 29;
      static final int TRANSACTION_isTestIccCard = 8;
      static final int TRANSACTION_isUserCustomizedEcc = 66;
      static final int TRANSACTION_isVolteEnabled = 75;
      static final int TRANSACTION_isWifiCallingEnabled = 76;
      static final int TRANSACTION_queryNetworkLock = 1;
      static final int TRANSACTION_queryPseudoBSRecords = 79;
      static final int TRANSACTION_repollIccStateForNetworkLock = 3;
      static final int TRANSACTION_setDefaultSubIdForAll = 71;
      static final int TRANSACTION_setEccInProgress = 72;
      static final int TRANSACTION_setLine1Number = 4;
      static final int TRANSACTION_setLteAccessStratumReport = 49;
      static final int TRANSACTION_setLteUplinkDataTransfer = 50;
      static final int TRANSACTION_setPhoneCapability = 27;
      static final int TRANSACTION_setRadioCapability = 42;
      static final int TRANSACTION_setScAddressUsingSubId = 38;
      static final int TRANSACTION_setTelLog = 80;
      static final int TRANSACTION_setTrmForPhone = 44;
      static final int TRANSACTION_simAkaAuthentication = 21;
      static final int TRANSACTION_simGbaAuthBootStrapMode = 22;
      static final int TRANSACTION_simGbaAuthNafMode = 23;
      static final int TRANSACTION_supplyNetworkDepersonalization = 2;
      static final int TRANSACTION_updateUserCustomizedEccList = 65;

      public Stub() {
         this.attachInterface(this, "com.mediatek.internal.telephony.ITelephonyEx");
      }

      public static ITelephonyEx asInterface(IBinder var0) {
         if (var0 == null) {
            return null;
         } else {
            IInterface var1 = var0.queryLocalInterface("com.mediatek.internal.telephony.ITelephonyEx");
            return (ITelephonyEx)(var1 != null && var1 instanceof ITelephonyEx ? (ITelephonyEx)var1 : new ITelephonyEx.Stub.Proxy(var0));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         byte var6 = 0;
         byte var7 = 0;
         byte var8 = 0;
         byte var9 = 0;
         byte var10 = 0;
         byte var11 = 0;
         byte var12 = 0;
         byte var13 = 0;
         byte var14 = 0;
         byte var15 = 0;
         byte var16 = 0;
         byte var17 = 0;
         byte var18 = 0;
         byte var19 = 0;
         byte var20 = 0;
         byte var21 = 0;
         byte var22 = 0;
         byte var23 = 0;
         byte var24 = 0;
         byte var25 = 0;
         byte var26 = 0;
         byte var27 = 0;
         byte var28 = 0;
         byte var29 = 0;
         byte var30 = 0;
         byte var31 = 0;
         byte var5 = 0;
         boolean var32;
         byte var34;
         List var35;
         Bundle var36;
         int[] var37;
         String var40;
         byte[] var43;
         BtSimapOperResponse var44;
         switch(var1) {
         case 1:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var36 = this.queryNetworkLock(var2.readInt(), var2.readInt());
            var3.writeNoException();
            if (var36 != null) {
               var3.writeInt(1);
               var36.writeToParcel(var3, 1);
               return true;
            }

            var3.writeInt(0);
            return true;
         case 2:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = this.supplyNetworkDepersonalization(var2.readInt(), var2.readString());
            var3.writeNoException();
            var3.writeInt(var1);
            return true;
         case 3:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = var2.readInt();
            if (var2.readInt() != 0) {
               var32 = true;
            } else {
               var32 = false;
            }

            this.repollIccStateForNetworkLock(var1, var32);
            var3.writeNoException();
            return true;
         case 4:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = this.setLine1Number(var2.readInt(), var2.readString(), var2.readString());
            var3.writeNoException();
            var3.writeInt(var1);
            return true;
         case 5:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isFdnEnabled(var2.readInt());
            var3.writeNoException();
            var34 = var5;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 6:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var40 = this.getIccCardType(var2.readInt());
            var3.writeNoException();
            var3.writeString(var40);
            return true;
         case 7:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isAppTypeSupported(var2.readInt(), var2.readInt());
            var3.writeNoException();
            var34 = var6;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 8:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isTestIccCard(var2.readInt());
            var3.writeNoException();
            var34 = var7;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 9:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var40 = this.getMvnoMatchType(var2.readInt());
            var3.writeNoException();
            var3.writeString(var40);
            return true;
         case 10:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var40 = this.getMvnoPattern(var2.readInt(), var2.readString());
            var3.writeNoException();
            var3.writeString(var40);
            return true;
         case 11:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var40 = this.getNetworkOperatorNameGemini(var2.readInt());
            var3.writeNoException();
            var3.writeString(var40);
            return true;
         case 12:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var40 = this.getNetworkOperatorNameUsingSub(var2.readInt());
            var3.writeNoException();
            var3.writeString(var40);
            return true;
         case 13:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var40 = this.getNetworkOperatorGemini(var2.readInt());
            var3.writeNoException();
            var3.writeString(var40);
            return true;
         case 14:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var40 = this.getNetworkOperatorUsingSub(var2.readInt());
            var3.writeNoException();
            var3.writeString(var40);
            return true;
         case 15:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = var2.readInt();
            var44 = new BtSimapOperResponse();
            var1 = this.btSimapConnectSIM(var1, var44);
            var3.writeNoException();
            var3.writeInt(var1);
            if (var44 != null) {
               var3.writeInt(1);
               var44.writeToParcel(var3, 1);
               return true;
            }

            var3.writeInt(0);
            return true;
         case 16:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = this.btSimapDisconnectSIM();
            var3.writeNoException();
            var3.writeInt(var1);
            return true;
         case 17:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = var2.readInt();
            var40 = var2.readString();
            BtSimapOperResponse var33 = new BtSimapOperResponse();
            var1 = this.btSimapApduRequest(var1, var40, var33);
            var3.writeNoException();
            var3.writeInt(var1);
            if (var33 != null) {
               var3.writeInt(1);
               var33.writeToParcel(var3, 1);
               return true;
            }

            var3.writeInt(0);
            return true;
         case 18:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = var2.readInt();
            var44 = new BtSimapOperResponse();
            var1 = this.btSimapResetSIM(var1, var44);
            var3.writeNoException();
            var3.writeInt(var1);
            if (var44 != null) {
               var3.writeInt(1);
               var44.writeToParcel(var3, 1);
               return true;
            }

            var3.writeInt(0);
            return true;
         case 19:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = var2.readInt();
            var44 = new BtSimapOperResponse();
            var1 = this.btSimapPowerOnSIM(var1, var44);
            var3.writeNoException();
            var3.writeInt(var1);
            if (var44 != null) {
               var3.writeInt(1);
               var44.writeToParcel(var3, 1);
               return true;
            }

            var3.writeInt(0);
            return true;
         case 20:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = this.btSimapPowerOffSIM();
            var3.writeNoException();
            var3.writeInt(var1);
            return true;
         case 21:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var43 = this.simAkaAuthentication(var2.readInt(), var2.readInt(), var2.createByteArray(), var2.createByteArray());
            var3.writeNoException();
            var3.writeByteArray(var43);
            return true;
         case 22:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var43 = this.simGbaAuthBootStrapMode(var2.readInt(), var2.readInt(), var2.createByteArray(), var2.createByteArray());
            var3.writeNoException();
            var3.writeByteArray(var43);
            return true;
         case 23:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var43 = this.simGbaAuthNafMode(var2.readInt(), var2.readInt(), var2.createByteArray(), var2.createByteArray());
            var3.writeNoException();
            var3.writeByteArray(var43);
            return true;
         case 24:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.broadcastIccUnlockIntent(var2.readInt());
            var3.writeNoException();
            var34 = var8;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 25:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isRadioOffBySimManagement(var2.readInt());
            var3.writeNoException();
            var34 = var9;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 26:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = this.getPhoneCapability(var2.readInt());
            var3.writeNoException();
            var3.writeInt(var1);
            return true;
         case 27:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            this.setPhoneCapability(var2.createIntArray(), var2.createIntArray());
            var3.writeNoException();
            return true;
         case 28:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            if (var2.readInt() != 0) {
               var32 = true;
            } else {
               var32 = false;
            }

            var32 = this.configSimSwap(var32);
            var3.writeNoException();
            var34 = var10;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 29:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isSimSwapped();
            var3.writeNoException();
            var34 = var11;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 30:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isCapSwitchManualEnabled();
            var3.writeNoException();
            var34 = var12;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 31:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            String[] var42 = this.getCapSwitchManualList();
            var3.writeNoException();
            var3.writeStringArray(var42);
            return true;
         case 32:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var40 = this.getLocatedPlmn(var2.readInt());
            var3.writeNoException();
            var3.writeString(var40);
            return true;
         case 33:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = this.getNetworkHideState(var2.readInt());
            var3.writeNoException();
            var3.writeInt(var1);
            return true;
         case 34:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var36 = this.getServiceState(var2.readInt());
            var3.writeNoException();
            if (var36 != null) {
               var3.writeInt(1);
               var36.writeToParcel(var3, 1);
               return true;
            }

            var3.writeInt(0);
            return true;
         case 35:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var37 = this.getAdnStorageInfo(var2.readInt());
            var3.writeNoException();
            var3.writeIntArray(var37);
            return true;
         case 36:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isPhbReady(var2.readInt());
            var3.writeNoException();
            var34 = var13;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 37:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var36 = this.getScAddressUsingSubId(var2.readInt());
            var3.writeNoException();
            if (var36 != null) {
               var3.writeInt(1);
               var36.writeToParcel(var3, 1);
               return true;
            }

            var3.writeInt(0);
            return true;
         case 38:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.setScAddressUsingSubId(var2.readInt(), var2.readString());
            var3.writeNoException();
            var34 = var14;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 39:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isAirplanemodeAvailableNow();
            var3.writeNoException();
            var34 = var15;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 40:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = this.getLastDataConnectionFailCause(var2.readString(), var2.readInt());
            var3.writeNoException();
            var3.writeInt(var1);
            return true;
         case 41:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            LinkProperties var41 = this.getLinkProperties(var2.readString(), var2.readInt());
            var3.writeNoException();
            if (var41 != null) {
               var3.writeInt(1);
               var41.writeToParcel(var3, 1);
               return true;
            }

            var3.writeInt(0);
            return true;
         case 42:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.setRadioCapability((RadioAccessFamily[])var2.createTypedArray(RadioAccessFamily.CREATOR));
            var3.writeNoException();
            var34 = var16;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 43:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isCapabilitySwitching();
            var3.writeNoException();
            var34 = var17;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 44:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            this.setTrmForPhone(var2.readInt(), var2.readInt());
            var3.writeNoException();
            return true;
         case 45:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = this.getMainCapabilityPhoneId();
            var3.writeNoException();
            var3.writeInt(var1);
            return true;
         case 46:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            this.initializeService(var2.readString());
            var3.writeNoException();
            return true;
         case 47:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            this.finalizeService(var2.readString());
            var3.writeNoException();
            return true;
         case 48:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isInHomeNetwork(var2.readInt());
            var3.writeNoException();
            var34 = var18;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 49:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            if (var2.readInt() != 0) {
               var32 = true;
            } else {
               var32 = false;
            }

            var32 = this.setLteAccessStratumReport(var32);
            var3.writeNoException();
            var34 = var19;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 50:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            if (var2.readInt() != 0) {
               var32 = true;
            } else {
               var32 = false;
            }

            var32 = this.setLteUplinkDataTransfer(var32, var2.readInt());
            var3.writeNoException();
            var34 = var20;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 51:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var40 = this.getLteAccessStratumState();
            var3.writeNoException();
            var3.writeString(var40);
            return true;
         case 52:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isSharedDefaultApn();
            var3.writeNoException();
            var34 = var21;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 53:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            MmsIcpInfo var39 = this.getMmsIcpInfo(var2.readInt());
            var3.writeNoException();
            if (var39 != null) {
               var3.writeInt(1);
               var39.writeToParcel(var3, 1);
               return true;
            }

            var3.writeInt(0);
            return true;
         case 54:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            MmsConfigInfo var38 = this.getMmsConfigInfo(var2.readInt());
            var3.writeNoException();
            if (var38 != null) {
               var3.writeInt(1);
               var38.writeToParcel(var3, 1);
               return true;
            }

            var3.writeInt(0);
            return true;
         case 55:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isOmhEnable(var2.readInt());
            var3.writeNoException();
            var34 = var22;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 56:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isOmhCard(var2.readInt());
            var3.writeNoException();
            var34 = var23;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 57:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var37 = this.getCallForwardingFc(var2.readInt(), var2.readInt());
            var3.writeNoException();
            var3.writeIntArray(var37);
            return true;
         case 58:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var37 = this.getCallWaitingFc(var2.readInt());
            var3.writeNoException();
            var3.writeIntArray(var37);
            return true;
         case 59:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var37 = this.getDonotDisturbFc(var2.readInt());
            var3.writeNoException();
            var3.writeIntArray(var37);
            return true;
         case 60:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var37 = this.getVMRetrieveFc(var2.readInt());
            var3.writeNoException();
            var3.writeIntArray(var37);
            return true;
         case 61:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = this.getBcsmsCfgFromRuim(var2.readInt(), var2.readInt(), var2.readInt());
            var3.writeNoException();
            var3.writeInt(var1);
            return true;
         case 62:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = this.getNextMessageId(var2.readInt());
            var3.writeNoException();
            var3.writeInt(var1);
            return true;
         case 63:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = this.getWapMsgId(var2.readInt());
            var3.writeNoException();
            var3.writeInt(var1);
            return true;
         case 64:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var36 = this.getUserCustomizedEccList();
            var3.writeNoException();
            if (var36 != null) {
               var3.writeInt(1);
               var36.writeToParcel(var3, 1);
               return true;
            }

            var3.writeInt(0);
            return true;
         case 65:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            if (var2.readInt() != 0) {
               var36 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var36 = null;
            }

            var32 = this.updateUserCustomizedEccList(var36);
            var3.writeNoException();
            var34 = var24;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 66:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isUserCustomizedEcc(var2.readString());
            var3.writeNoException();
            var34 = var25;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 67:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var36 = this.getCellLocationUsingSlotId(var2.readInt());
            var3.writeNoException();
            if (var36 != null) {
               var3.writeInt(1);
               var36.writeToParcel(var3, 1);
               return true;
            }

            var3.writeInt(0);
            return true;
         case 68:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var35 = this.getNeighboringCellInfoUsingSlotId(var2.readInt());
            var3.writeNoException();
            var3.writeTypedList(var35);
            return true;
         case 69:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = this.getCdmaSubscriptionActStatus(var2.readInt());
            var3.writeNoException();
            var3.writeInt(var1);
            return true;
         case 70:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.canSwitchDefaultSubId();
            var3.writeNoException();
            var34 = var26;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 71:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.setDefaultSubIdForAll(var2.readInt(), var2.readInt(), ISetDefaultSubResultCallback.Stub.asInterface(var2.readStrongBinder()));
            var3.writeNoException();
            var34 = var27;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 72:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            if (var2.readInt() != 0) {
               var32 = true;
            } else {
               var32 = false;
            }

            this.setEccInProgress(var32);
            var3.writeNoException();
            return true;
         case 73:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isEccInProgress();
            var3.writeNoException();
            var34 = var28;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 74:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isImsRegistered(var2.readInt());
            var3.writeNoException();
            var34 = var29;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 75:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isVolteEnabled(var2.readInt());
            var3.writeNoException();
            var34 = var30;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 76:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var32 = this.isWifiCallingEnabled(var2.readInt());
            var3.writeNoException();
            var34 = var31;
            if (var32) {
               var34 = 1;
            }

            var3.writeInt(var34);
            return true;
         case 77:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var1 = var2.readInt();
            if (var2.readInt() != 0) {
               var32 = true;
            } else {
               var32 = false;
            }

            this.enablePseudoBSMonitor(var1, var32, var2.readInt());
            var3.writeNoException();
            return true;
         case 78:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            this.disablePseudoBSMonitor(var2.readInt());
            var3.writeNoException();
            return true;
         case 79:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            var35 = this.queryPseudoBSRecords(var2.readInt());
            var3.writeNoException();
            var3.writeTypedList(var35);
            return true;
         case 80:
            var2.enforceInterface("com.mediatek.internal.telephony.ITelephonyEx");
            if (var2.readInt() != 0) {
               var32 = true;
            } else {
               var32 = false;
            }

            this.setTelLog(var32);
            var3.writeNoException();
            return true;
         case 1598968902:
            var3.writeString("com.mediatek.internal.telephony.ITelephonyEx");
            return true;
         default:
            return super.onTransact(var1, var2, var3, var4);
         }
      }

      private static class Proxy implements ITelephonyEx {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public boolean broadcastIccUnlockIntent(int var1) throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               this.mRemote.transact(24, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public int btSimapApduRequest(int var1, String var2, BtSimapOperResponse var3) throws RemoteException {
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            try {
               var4.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var4.writeInt(var1);
               var4.writeString(var2);
               this.mRemote.transact(17, var4, var5, 0);
               var5.readException();
               var1 = var5.readInt();
               if (var5.readInt() != 0) {
                  var3.readFromParcel(var5);
               }
            } finally {
               var5.recycle();
               var4.recycle();
            }

            return var1;
         }

         public int btSimapConnectSIM(int var1, BtSimapOperResponse var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            try {
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               this.mRemote.transact(15, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               if (var4.readInt() != 0) {
                  var2.readFromParcel(var4);
               }
            } finally {
               var4.recycle();
               var3.recycle();
            }

            return var1;
         }

         public int btSimapDisconnectSIM() throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            int var1;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               this.mRemote.transact(16, var2, var3, 0);
               var3.readException();
               var1 = var3.readInt();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var1;
         }

         public int btSimapPowerOffSIM() throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            int var1;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               this.mRemote.transact(20, var2, var3, 0);
               var3.readException();
               var1 = var3.readInt();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var1;
         }

         public int btSimapPowerOnSIM(int var1, BtSimapOperResponse var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            try {
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               this.mRemote.transact(19, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               if (var4.readInt() != 0) {
                  var2.readFromParcel(var4);
               }
            } finally {
               var4.recycle();
               var3.recycle();
            }

            return var1;
         }

         public int btSimapResetSIM(int var1, BtSimapOperResponse var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            try {
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               this.mRemote.transact(18, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               if (var4.readInt() != 0) {
                  var2.readFromParcel(var4);
               }
            } finally {
               var4.recycle();
               var3.recycle();
            }

            return var1;
         }

         public boolean canSwitchDefaultSubId() throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            int var1;
            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               this.mRemote.transact(70, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean configSimSwap(boolean var1) throws RemoteException {
            boolean var3 = true;
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            int var13;
            label92: {
               Throwable var10000;
               label96: {
                  boolean var10001;
                  try {
                     var4.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label96;
                  }

                  byte var2;
                  if (var1) {
                     var2 = 1;
                  } else {
                     var2 = 0;
                  }

                  label87:
                  try {
                     var4.writeInt(var2);
                     this.mRemote.transact(28, var4, var5, 0);
                     var5.readException();
                     var13 = var5.readInt();
                     break label92;
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label87;
                  }
               }

               Throwable var6 = var10000;
               var5.recycle();
               var4.recycle();
               throw var6;
            }

            if (var13 != 0) {
               var1 = var3;
            } else {
               var1 = false;
            }

            var5.recycle();
            var4.recycle();
            return var1;
         }

         public void disablePseudoBSMonitor(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(78, var2, var3, 0);
               var3.readException();
            } finally {
               var3.recycle();
               var2.recycle();
            }

         }

         public void enablePseudoBSMonitor(int var1, boolean var2, int var3) throws RemoteException {
            byte var4 = 0;
            Parcel var5 = Parcel.obtain();
            Parcel var6 = Parcel.obtain();

            label80: {
               Throwable var10000;
               label85: {
                  boolean var10001;
                  try {
                     var5.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                     var5.writeInt(var1);
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label85;
                  }

                  byte var14 = var4;
                  if (var2) {
                     var14 = 1;
                  }

                  label75:
                  try {
                     var5.writeInt(var14);
                     var5.writeInt(var3);
                     this.mRemote.transact(77, var5, var6, 0);
                     var6.readException();
                     break label80;
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label75;
                  }
               }

               Throwable var7 = var10000;
               var6.recycle();
               var5.recycle();
               throw var7;
            }

            var6.recycle();
            var5.recycle();
         }

         public void finalizeService(String var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeString(var1);
               this.mRemote.transact(47, var2, var3, 0);
               var3.readException();
            } finally {
               var3.recycle();
               var2.recycle();
            }

         }

         public int[] getAdnStorageInfo(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            int[] var4;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(35, var2, var3, 0);
               var3.readException();
               var4 = var3.createIntArray();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var4;
         }

         public int getBcsmsCfgFromRuim(int var1, int var2, int var3) throws RemoteException {
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            try {
               var4.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var4.writeInt(var1);
               var4.writeInt(var2);
               var4.writeInt(var3);
               this.mRemote.transact(61, var4, var5, 0);
               var5.readException();
               var1 = var5.readInt();
            } finally {
               var5.recycle();
               var4.recycle();
            }

            return var1;
         }

         public int[] getCallForwardingFc(int var1, int var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            int[] var5;
            try {
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               var3.writeInt(var2);
               this.mRemote.transact(57, var3, var4, 0);
               var4.readException();
               var5 = var4.createIntArray();
            } finally {
               var4.recycle();
               var3.recycle();
            }

            return var5;
         }

         public int[] getCallWaitingFc(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            int[] var4;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(58, var2, var3, 0);
               var3.readException();
               var4 = var3.createIntArray();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var4;
         }

         public String[] getCapSwitchManualList() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            String[] var3;
            try {
               var1.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               this.mRemote.transact(31, var1, var2, 0);
               var2.readException();
               var3 = var2.createStringArray();
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }

         public int getCdmaSubscriptionActStatus(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(69, var2, var3, 0);
               var3.readException();
               var1 = var3.readInt();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var1;
         }

         public Bundle getCellLocationUsingSlotId(int var1) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var6 = false;

            Bundle var2;
            label36: {
               try {
                  var6 = true;
                  var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                  var3.writeInt(var1);
                  this.mRemote.transact(67, var3, var4, 0);
                  var4.readException();
                  if (var4.readInt() != 0) {
                     var2 = (Bundle)Bundle.CREATOR.createFromParcel(var4);
                     var6 = false;
                     break label36;
                  }

                  var6 = false;
               } finally {
                  if (var6) {
                     var4.recycle();
                     var3.recycle();
                  }
               }

               var2 = null;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public int[] getDonotDisturbFc(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            int[] var4;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(59, var2, var3, 0);
               var3.readException();
               var4 = var3.createIntArray();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var4;
         }

         public String getIccCardType(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            String var4;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(6, var2, var3, 0);
               var3.readException();
               var4 = var3.readString();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var4;
         }

         public String getInterfaceDescriptor() {
            return "com.mediatek.internal.telephony.ITelephonyEx";
         }

         public int getLastDataConnectionFailCause(String var1, int var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            try {
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeString(var1);
               var3.writeInt(var2);
               this.mRemote.transact(40, var3, var4, 0);
               var4.readException();
               var2 = var4.readInt();
            } finally {
               var4.recycle();
               var3.recycle();
            }

            return var2;
         }

         public LinkProperties getLinkProperties(String var1, int var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var6 = false;

            LinkProperties var8;
            label36: {
               try {
                  var6 = true;
                  var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                  var3.writeString(var1);
                  var3.writeInt(var2);
                  this.mRemote.transact(41, var3, var4, 0);
                  var4.readException();
                  if (var4.readInt() != 0) {
                     var8 = (LinkProperties)LinkProperties.CREATOR.createFromParcel(var4);
                     var6 = false;
                     break label36;
                  }

                  var6 = false;
               } finally {
                  if (var6) {
                     var4.recycle();
                     var3.recycle();
                  }
               }

               var8 = null;
            }

            var4.recycle();
            var3.recycle();
            return var8;
         }

         public String getLocatedPlmn(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            String var4;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(32, var2, var3, 0);
               var3.readException();
               var4 = var3.readString();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var4;
         }

         public String getLteAccessStratumState() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            String var3;
            try {
               var1.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               this.mRemote.transact(51, var1, var2, 0);
               var2.readException();
               var3 = var2.readString();
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }

         public int getMainCapabilityPhoneId() throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            int var1;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               this.mRemote.transact(45, var2, var3, 0);
               var3.readException();
               var1 = var3.readInt();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var1;
         }

         public MmsConfigInfo getMmsConfigInfo(int var1) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var6 = false;

            MmsConfigInfo var2;
            label36: {
               try {
                  var6 = true;
                  var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                  var3.writeInt(var1);
                  this.mRemote.transact(54, var3, var4, 0);
                  var4.readException();
                  if (var4.readInt() != 0) {
                     var2 = (MmsConfigInfo)MmsConfigInfo.CREATOR.createFromParcel(var4);
                     var6 = false;
                     break label36;
                  }

                  var6 = false;
               } finally {
                  if (var6) {
                     var4.recycle();
                     var3.recycle();
                  }
               }

               var2 = null;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public MmsIcpInfo getMmsIcpInfo(int var1) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var6 = false;

            MmsIcpInfo var2;
            label36: {
               try {
                  var6 = true;
                  var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                  var3.writeInt(var1);
                  this.mRemote.transact(53, var3, var4, 0);
                  var4.readException();
                  if (var4.readInt() != 0) {
                     var2 = (MmsIcpInfo)MmsIcpInfo.CREATOR.createFromParcel(var4);
                     var6 = false;
                     break label36;
                  }

                  var6 = false;
               } finally {
                  if (var6) {
                     var4.recycle();
                     var3.recycle();
                  }
               }

               var2 = null;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public String getMvnoMatchType(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            String var4;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(9, var2, var3, 0);
               var3.readException();
               var4 = var3.readString();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var4;
         }

         public String getMvnoPattern(int var1, String var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            try {
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               var3.writeString(var2);
               this.mRemote.transact(10, var3, var4, 0);
               var4.readException();
               var2 = var4.readString();
            } finally {
               var4.recycle();
               var3.recycle();
            }

            return var2;
         }

         public List<NeighboringCellInfo> getNeighboringCellInfoUsingSlotId(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            ArrayList var4;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(68, var2, var3, 0);
               var3.readException();
               var4 = var3.createTypedArrayList(NeighboringCellInfo.CREATOR);
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var4;
         }

         public int getNetworkHideState(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(33, var2, var3, 0);
               var3.readException();
               var1 = var3.readInt();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var1;
         }

         public String getNetworkOperatorGemini(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            String var4;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(13, var2, var3, 0);
               var3.readException();
               var4 = var3.readString();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var4;
         }

         public String getNetworkOperatorNameGemini(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            String var4;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(11, var2, var3, 0);
               var3.readException();
               var4 = var3.readString();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var4;
         }

         public String getNetworkOperatorNameUsingSub(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            String var4;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(12, var2, var3, 0);
               var3.readException();
               var4 = var3.readString();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var4;
         }

         public String getNetworkOperatorUsingSub(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            String var4;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(14, var2, var3, 0);
               var3.readException();
               var4 = var3.readString();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var4;
         }

         public int getNextMessageId(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(62, var2, var3, 0);
               var3.readException();
               var1 = var3.readInt();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var1;
         }

         public int getPhoneCapability(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(26, var2, var3, 0);
               var3.readException();
               var1 = var3.readInt();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var1;
         }

         public Bundle getScAddressUsingSubId(int var1) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var6 = false;

            Bundle var2;
            label36: {
               try {
                  var6 = true;
                  var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                  var3.writeInt(var1);
                  this.mRemote.transact(37, var3, var4, 0);
                  var4.readException();
                  if (var4.readInt() != 0) {
                     var2 = (Bundle)Bundle.CREATOR.createFromParcel(var4);
                     var6 = false;
                     break label36;
                  }

                  var6 = false;
               } finally {
                  if (var6) {
                     var4.recycle();
                     var3.recycle();
                  }
               }

               var2 = null;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public Bundle getServiceState(int var1) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var6 = false;

            Bundle var2;
            label36: {
               try {
                  var6 = true;
                  var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                  var3.writeInt(var1);
                  this.mRemote.transact(34, var3, var4, 0);
                  var4.readException();
                  if (var4.readInt() != 0) {
                     var2 = (Bundle)Bundle.CREATOR.createFromParcel(var4);
                     var6 = false;
                     break label36;
                  }

                  var6 = false;
               } finally {
                  if (var6) {
                     var4.recycle();
                     var3.recycle();
                  }
               }

               var2 = null;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public Bundle getUserCustomizedEccList() throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();
            boolean var5 = false;

            Bundle var1;
            label36: {
               try {
                  var5 = true;
                  var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                  this.mRemote.transact(64, var2, var3, 0);
                  var3.readException();
                  if (var3.readInt() != 0) {
                     var1 = (Bundle)Bundle.CREATOR.createFromParcel(var3);
                     var5 = false;
                     break label36;
                  }

                  var5 = false;
               } finally {
                  if (var5) {
                     var3.recycle();
                     var2.recycle();
                  }
               }

               var1 = null;
            }

            var3.recycle();
            var2.recycle();
            return var1;
         }

         public int[] getVMRetrieveFc(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            int[] var4;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(60, var2, var3, 0);
               var3.readException();
               var4 = var3.createIntArray();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var4;
         }

         public int getWapMsgId(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(63, var2, var3, 0);
               var3.readException();
               var1 = var3.readInt();
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var1;
         }

         public void initializeService(String var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeString(var1);
               this.mRemote.transact(46, var2, var3, 0);
               var3.readException();
            } finally {
               var3.recycle();
               var2.recycle();
            }

         }

         public boolean isAirplanemodeAvailableNow() throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            int var1;
            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               this.mRemote.transact(39, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isAppTypeSupported(int var1, int var2) throws RemoteException {
            boolean var3 = false;
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();
            boolean var8 = false;

            try {
               var8 = true;
               var4.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var4.writeInt(var1);
               var4.writeInt(var2);
               this.mRemote.transact(7, var4, var5, 0);
               var5.readException();
               var1 = var5.readInt();
               var8 = false;
            } finally {
               if (var8) {
                  var5.recycle();
                  var4.recycle();
               }
            }

            if (var1 != 0) {
               var3 = true;
            }

            var5.recycle();
            var4.recycle();
            return var3;
         }

         public boolean isCapSwitchManualEnabled() throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            int var1;
            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               this.mRemote.transact(30, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isCapabilitySwitching() throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            int var1;
            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               this.mRemote.transact(43, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isEccInProgress() throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            int var1;
            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               this.mRemote.transact(73, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isFdnEnabled(int var1) throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               this.mRemote.transact(5, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isImsRegistered(int var1) throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               this.mRemote.transact(74, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isInHomeNetwork(int var1) throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               this.mRemote.transact(48, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isOmhCard(int var1) throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               this.mRemote.transact(56, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isOmhEnable(int var1) throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               this.mRemote.transact(55, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isPhbReady(int var1) throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               this.mRemote.transact(36, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isRadioOffBySimManagement(int var1) throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               this.mRemote.transact(25, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isSharedDefaultApn() throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            int var1;
            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               this.mRemote.transact(52, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isSimSwapped() throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            int var1;
            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               this.mRemote.transact(29, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isTestIccCard(int var1) throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               this.mRemote.transact(8, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isUserCustomizedEcc(String var1) throws RemoteException {
            boolean var3 = false;
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();
            boolean var7 = false;

            int var2;
            try {
               var7 = true;
               var4.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var4.writeString(var1);
               this.mRemote.transact(66, var4, var5, 0);
               var5.readException();
               var2 = var5.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var5.recycle();
                  var4.recycle();
               }
            }

            if (var2 != 0) {
               var3 = true;
            }

            var5.recycle();
            var4.recycle();
            return var3;
         }

         public boolean isVolteEnabled(int var1) throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               this.mRemote.transact(75, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public boolean isWifiCallingEnabled(int var1) throws RemoteException {
            boolean var2 = false;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();
            boolean var7 = false;

            try {
               var7 = true;
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               this.mRemote.transact(76, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var4.recycle();
                  var3.recycle();
               }
            }

            if (var1 != 0) {
               var2 = true;
            }

            var4.recycle();
            var3.recycle();
            return var2;
         }

         public Bundle queryNetworkLock(int var1, int var2) throws RemoteException {
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();
            boolean var7 = false;

            Bundle var3;
            label36: {
               try {
                  var7 = true;
                  var4.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                  var4.writeInt(var1);
                  var4.writeInt(var2);
                  this.mRemote.transact(1, var4, var5, 0);
                  var5.readException();
                  if (var5.readInt() != 0) {
                     var3 = (Bundle)Bundle.CREATOR.createFromParcel(var5);
                     var7 = false;
                     break label36;
                  }

                  var7 = false;
               } finally {
                  if (var7) {
                     var5.recycle();
                     var4.recycle();
                  }
               }

               var3 = null;
            }

            var5.recycle();
            var4.recycle();
            return var3;
         }

         public List<PseudoBSRecord> queryPseudoBSRecords(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            ArrayList var4;
            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var2.writeInt(var1);
               this.mRemote.transact(79, var2, var3, 0);
               var3.readException();
               var4 = var3.createTypedArrayList(PseudoBSRecord.CREATOR);
            } finally {
               var3.recycle();
               var2.recycle();
            }

            return var4;
         }

         public void repollIccStateForNetworkLock(int var1, boolean var2) throws RemoteException {
            byte var3 = 0;
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            label80: {
               Throwable var10000;
               label85: {
                  boolean var10001;
                  try {
                     var4.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                     var4.writeInt(var1);
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label85;
                  }

                  byte var13 = var3;
                  if (var2) {
                     var13 = 1;
                  }

                  label75:
                  try {
                     var4.writeInt(var13);
                     this.mRemote.transact(3, var4, var5, 0);
                     var5.readException();
                     break label80;
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label75;
                  }
               }

               Throwable var6 = var10000;
               var5.recycle();
               var4.recycle();
               throw var6;
            }

            var5.recycle();
            var4.recycle();
         }

         public boolean setDefaultSubIdForAll(int var1, int var2, ISetDefaultSubResultCallback var3) throws RemoteException {
            boolean var4 = false;
            Parcel var5 = Parcel.obtain();
            Parcel var6 = Parcel.obtain();

            label132: {
               Throwable var10000;
               label136: {
                  boolean var10001;
                  try {
                     var5.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                     var5.writeInt(var1);
                     var5.writeInt(var2);
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label136;
                  }

                  IBinder var19;
                  if (var3 != null) {
                     try {
                        var19 = var3.asBinder();
                     } catch (Throwable var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label136;
                     }
                  } else {
                     var19 = null;
                  }

                  label124:
                  try {
                     var5.writeStrongBinder(var19);
                     this.mRemote.transact(71, var5, var6, 0);
                     var6.readException();
                     var1 = var6.readInt();
                     break label132;
                  } catch (Throwable var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label124;
                  }
               }

               Throwable var20 = var10000;
               var6.recycle();
               var5.recycle();
               throw var20;
            }

            if (var1 != 0) {
               var4 = true;
            }

            var6.recycle();
            var5.recycle();
            return var4;
         }

         public void setEccInProgress(boolean var1) throws RemoteException {
            byte var2 = 0;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label80: {
               Throwable var10000;
               label85: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label85;
                  }

                  if (var1) {
                     var2 = 1;
                  }

                  label75:
                  try {
                     var3.writeInt(var2);
                     this.mRemote.transact(72, var3, var4, 0);
                     var4.readException();
                     break label80;
                  } catch (Throwable var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label75;
                  }
               }

               Throwable var5 = var10000;
               var4.recycle();
               var3.recycle();
               throw var5;
            }

            var4.recycle();
            var3.recycle();
         }

         public int setLine1Number(int var1, String var2, String var3) throws RemoteException {
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            try {
               var4.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var4.writeInt(var1);
               var4.writeString(var2);
               var4.writeString(var3);
               this.mRemote.transact(4, var4, var5, 0);
               var5.readException();
               var1 = var5.readInt();
            } finally {
               var5.recycle();
               var4.recycle();
            }

            return var1;
         }

         public boolean setLteAccessStratumReport(boolean var1) throws RemoteException {
            boolean var3 = true;
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            int var13;
            label92: {
               Throwable var10000;
               label96: {
                  boolean var10001;
                  try {
                     var4.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label96;
                  }

                  byte var2;
                  if (var1) {
                     var2 = 1;
                  } else {
                     var2 = 0;
                  }

                  label87:
                  try {
                     var4.writeInt(var2);
                     this.mRemote.transact(49, var4, var5, 0);
                     var5.readException();
                     var13 = var5.readInt();
                     break label92;
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label87;
                  }
               }

               Throwable var6 = var10000;
               var5.recycle();
               var4.recycle();
               throw var6;
            }

            if (var13 != 0) {
               var1 = var3;
            } else {
               var1 = false;
            }

            var5.recycle();
            var4.recycle();
            return var1;
         }

         public boolean setLteUplinkDataTransfer(boolean var1, int var2) throws RemoteException {
            boolean var4 = true;
            Parcel var5 = Parcel.obtain();
            Parcel var6 = Parcel.obtain();

            label92: {
               Throwable var10000;
               label96: {
                  boolean var10001;
                  try {
                     var5.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label96;
                  }

                  byte var3;
                  if (var1) {
                     var3 = 1;
                  } else {
                     var3 = 0;
                  }

                  label87:
                  try {
                     var5.writeInt(var3);
                     var5.writeInt(var2);
                     this.mRemote.transact(50, var5, var6, 0);
                     var6.readException();
                     var2 = var6.readInt();
                     break label92;
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label87;
                  }
               }

               Throwable var7 = var10000;
               var6.recycle();
               var5.recycle();
               throw var7;
            }

            if (var2 != 0) {
               var1 = var4;
            } else {
               var1 = false;
            }

            var6.recycle();
            var5.recycle();
            return var1;
         }

         public void setPhoneCapability(int[] var1, int[] var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            try {
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeIntArray(var1);
               var3.writeIntArray(var2);
               this.mRemote.transact(27, var3, var4, 0);
               var4.readException();
            } finally {
               var4.recycle();
               var3.recycle();
            }

         }

         public boolean setRadioCapability(RadioAccessFamily[] var1) throws RemoteException {
            boolean var3 = false;
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();
            boolean var7 = false;

            int var2;
            try {
               var7 = true;
               var4.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var4.writeTypedArray(var1, 0);
               this.mRemote.transact(42, var4, var5, 0);
               var5.readException();
               var2 = var5.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var5.recycle();
                  var4.recycle();
               }
            }

            if (var2 != 0) {
               var3 = true;
            }

            var5.recycle();
            var4.recycle();
            return var3;
         }

         public boolean setScAddressUsingSubId(int var1, String var2) throws RemoteException {
            boolean var3 = false;
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();
            boolean var7 = false;

            try {
               var7 = true;
               var4.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var4.writeInt(var1);
               var4.writeString(var2);
               this.mRemote.transact(38, var4, var5, 0);
               var5.readException();
               var1 = var5.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var5.recycle();
                  var4.recycle();
               }
            }

            if (var1 != 0) {
               var3 = true;
            }

            var5.recycle();
            var4.recycle();
            return var3;
         }

         public void setTelLog(boolean var1) throws RemoteException {
            byte var2 = 0;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label80: {
               Throwable var10000;
               label85: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label85;
                  }

                  if (var1) {
                     var2 = 1;
                  }

                  label75:
                  try {
                     var3.writeInt(var2);
                     this.mRemote.transact(80, var3, var4, 0);
                     var4.readException();
                     break label80;
                  } catch (Throwable var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label75;
                  }
               }

               Throwable var5 = var10000;
               var4.recycle();
               var3.recycle();
               throw var5;
            }

            var4.recycle();
            var3.recycle();
         }

         public void setTrmForPhone(int var1, int var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            try {
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               var3.writeInt(var2);
               this.mRemote.transact(44, var3, var4, 0);
               var4.readException();
            } finally {
               var4.recycle();
               var3.recycle();
            }

         }

         public byte[] simAkaAuthentication(int var1, int var2, byte[] var3, byte[] var4) throws RemoteException {
            Parcel var5 = Parcel.obtain();
            Parcel var6 = Parcel.obtain();

            try {
               var5.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var5.writeInt(var1);
               var5.writeInt(var2);
               var5.writeByteArray(var3);
               var5.writeByteArray(var4);
               this.mRemote.transact(21, var5, var6, 0);
               var6.readException();
               var3 = var6.createByteArray();
            } finally {
               var6.recycle();
               var5.recycle();
            }

            return var3;
         }

         public byte[] simGbaAuthBootStrapMode(int var1, int var2, byte[] var3, byte[] var4) throws RemoteException {
            Parcel var5 = Parcel.obtain();
            Parcel var6 = Parcel.obtain();

            try {
               var5.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var5.writeInt(var1);
               var5.writeInt(var2);
               var5.writeByteArray(var3);
               var5.writeByteArray(var4);
               this.mRemote.transact(22, var5, var6, 0);
               var6.readException();
               var3 = var6.createByteArray();
            } finally {
               var6.recycle();
               var5.recycle();
            }

            return var3;
         }

         public byte[] simGbaAuthNafMode(int var1, int var2, byte[] var3, byte[] var4) throws RemoteException {
            Parcel var5 = Parcel.obtain();
            Parcel var6 = Parcel.obtain();

            try {
               var5.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var5.writeInt(var1);
               var5.writeInt(var2);
               var5.writeByteArray(var3);
               var5.writeByteArray(var4);
               this.mRemote.transact(23, var5, var6, 0);
               var6.readException();
               var3 = var6.createByteArray();
            } finally {
               var6.recycle();
               var5.recycle();
            }

            return var3;
         }

         public int supplyNetworkDepersonalization(int var1, String var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            try {
               var3.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
               var3.writeInt(var1);
               var3.writeString(var2);
               this.mRemote.transact(2, var3, var4, 0);
               var4.readException();
               var1 = var4.readInt();
            } finally {
               var4.recycle();
               var3.recycle();
            }

            return var1;
         }

         public boolean updateUserCustomizedEccList(Bundle var1) throws RemoteException {
            boolean var3 = true;
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            int var2;
            label186: {
               Throwable var10000;
               label190: {
                  boolean var10001;
                  try {
                     var4.writeInterfaceToken("com.mediatek.internal.telephony.ITelephonyEx");
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label190;
                  }

                  if (var1 != null) {
                     try {
                        var4.writeInt(1);
                        var1.writeToParcel(var4, 0);
                     } catch (Throwable var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label190;
                     }
                  } else {
                     try {
                        var4.writeInt(0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label190;
                     }
                  }

                  label176:
                  try {
                     this.mRemote.transact(65, var4, var5, 0);
                     var5.readException();
                     var2 = var5.readInt();
                     break label186;
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label176;
                  }
               }

               Throwable var26 = var10000;
               var5.recycle();
               var4.recycle();
               throw var26;
            }

            if (var2 == 0) {
               var3 = false;
            }

            var5.recycle();
            var4.recycle();
            return var3;
         }
      }
   }
}
