package com.mediatek.internal.telephony.gsm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telecom.VideoProfile;
import android.telecom.VideoProfile.CameraCapabilities;

public interface IGsmVideoCallCallback extends IInterface {
   void changeCallDataUsage(long var1) throws RemoteException;

   void changeCameraCapabilities(CameraCapabilities var1) throws RemoteException;

   void changePeerDimensions(int var1, int var2) throws RemoteException;

   void changePeerDimensionsWithAngle(int var1, int var2, int var3) throws RemoteException;

   void changeVideoQuality(int var1) throws RemoteException;

   void handleCallSessionEvent(int var1) throws RemoteException;

   void receiveSessionModifyRequest(VideoProfile var1) throws RemoteException;

   void receiveSessionModifyResponse(int var1, VideoProfile var2, VideoProfile var3) throws RemoteException;

   public abstract static class Stub extends Binder implements IGsmVideoCallCallback {
      private static final String DESCRIPTOR = "com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback";
      static final int TRANSACTION_changeCallDataUsage = 6;
      static final int TRANSACTION_changeCameraCapabilities = 7;
      static final int TRANSACTION_changePeerDimensions = 4;
      static final int TRANSACTION_changePeerDimensionsWithAngle = 5;
      static final int TRANSACTION_changeVideoQuality = 8;
      static final int TRANSACTION_handleCallSessionEvent = 3;
      static final int TRANSACTION_receiveSessionModifyRequest = 1;
      static final int TRANSACTION_receiveSessionModifyResponse = 2;

      public Stub() {
         this.attachInterface(this, "com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
      }

      public static IGsmVideoCallCallback asInterface(IBinder var0) {
         if (var0 == null) {
            return null;
         } else {
            IInterface var1 = var0.queryLocalInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
            return (IGsmVideoCallCallback)(var1 != null && var1 instanceof IGsmVideoCallCallback ? (IGsmVideoCallCallback)var1 : new IGsmVideoCallCallback.Stub.Proxy(var0));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         VideoProfile var7;
         switch(var1) {
         case 1:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
            if (var2.readInt() != 0) {
               var7 = (VideoProfile)VideoProfile.CREATOR.createFromParcel(var2);
            } else {
               var7 = null;
            }

            this.receiveSessionModifyRequest(var7);
            return true;
         case 2:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
            var1 = var2.readInt();
            VideoProfile var6;
            if (var2.readInt() != 0) {
               var6 = (VideoProfile)VideoProfile.CREATOR.createFromParcel(var2);
            } else {
               var6 = null;
            }

            if (var2.readInt() != 0) {
               var7 = (VideoProfile)VideoProfile.CREATOR.createFromParcel(var2);
            } else {
               var7 = null;
            }

            this.receiveSessionModifyResponse(var1, var6, var7);
            return true;
         case 3:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
            this.handleCallSessionEvent(var2.readInt());
            return true;
         case 4:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
            this.changePeerDimensions(var2.readInt(), var2.readInt());
            return true;
         case 5:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
            this.changePeerDimensionsWithAngle(var2.readInt(), var2.readInt(), var2.readInt());
            return true;
         case 6:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
            this.changeCallDataUsage(var2.readLong());
            return true;
         case 7:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
            CameraCapabilities var5;
            if (var2.readInt() != 0) {
               var5 = (CameraCapabilities)CameraCapabilities.CREATOR.createFromParcel(var2);
            } else {
               var5 = null;
            }

            this.changeCameraCapabilities(var5);
            return true;
         case 8:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
            this.changeVideoQuality(var2.readInt());
            return true;
         case 1598968902:
            var3.writeString("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
            return true;
         default:
            return super.onTransact(var1, var2, var3, var4);
         }
      }

      private static class Proxy implements IGsmVideoCallCallback {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public void changeCallDataUsage(long var1) throws RemoteException {
            Parcel var3 = Parcel.obtain();

            try {
               var3.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
               var3.writeLong(var1);
               this.mRemote.transact(6, var3, (Parcel)null, 1);
            } finally {
               var3.recycle();
            }

         }

         public void changeCameraCapabilities(CameraCapabilities var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(7, var2, (Parcel)null, 1);
                     break label166;
                  } catch (Throwable var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var23 = var10000;
               var2.recycle();
               throw var23;
            }

            var2.recycle();
         }

         public void changePeerDimensions(int var1, int var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();

            try {
               var3.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
               var3.writeInt(var1);
               var3.writeInt(var2);
               this.mRemote.transact(4, var3, (Parcel)null, 1);
            } finally {
               var3.recycle();
            }

         }

         public void changePeerDimensionsWithAngle(int var1, int var2, int var3) throws RemoteException {
            Parcel var4 = Parcel.obtain();

            try {
               var4.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
               var4.writeInt(var1);
               var4.writeInt(var2);
               var4.writeInt(var3);
               this.mRemote.transact(5, var4, (Parcel)null, 1);
            } finally {
               var4.recycle();
            }

         }

         public void changeVideoQuality(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
               var2.writeInt(var1);
               this.mRemote.transact(8, var2, (Parcel)null, 1);
            } finally {
               var2.recycle();
            }

         }

         public String getInterfaceDescriptor() {
            return "com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback";
         }

         public void handleCallSessionEvent(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
               var2.writeInt(var1);
               this.mRemote.transact(3, var2, (Parcel)null, 1);
            } finally {
               var2.recycle();
            }

         }

         public void receiveSessionModifyRequest(VideoProfile var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(1, var2, (Parcel)null, 1);
                     break label166;
                  } catch (Throwable var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var23 = var10000;
               var2.recycle();
               throw var23;
            }

            var2.recycle();
         }

         public void receiveSessionModifyResponse(int var1, VideoProfile var2, VideoProfile var3) throws RemoteException {
            Parcel var4 = Parcel.obtain();

            label305: {
               Throwable var10000;
               label309: {
                  boolean var10001;
                  try {
                     var4.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallCallback");
                     var4.writeInt(var1);
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label309;
                  }

                  if (var2 != null) {
                     try {
                        var4.writeInt(1);
                        var2.writeToParcel(var4, 0);
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var4.writeInt(0);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label309;
                     }
                  }

                  if (var3 != null) {
                     try {
                        var4.writeInt(1);
                        var3.writeToParcel(var4, 0);
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var4.writeInt(0);
                     } catch (Throwable var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label309;
                     }
                  }

                  label290:
                  try {
                     this.mRemote.transact(2, var4, (Parcel)null, 1);
                     break label305;
                  } catch (Throwable var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label290;
                  }
               }

               Throwable var47 = var10000;
               var4.recycle();
               throw var47;
            }

            var4.recycle();
         }
      }
   }
}
