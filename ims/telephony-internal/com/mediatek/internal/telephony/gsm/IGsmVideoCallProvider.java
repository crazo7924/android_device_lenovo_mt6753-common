package com.mediatek.internal.telephony.gsm;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telecom.VideoProfile;
import android.view.Surface;

public interface IGsmVideoCallProvider extends IInterface {
   void requestCallDataUsage() throws RemoteException;

   void requestCameraCapabilities() throws RemoteException;

   void sendSessionModifyRequest(VideoProfile var1, VideoProfile var2) throws RemoteException;

   void sendSessionModifyResponse(VideoProfile var1) throws RemoteException;

   void setCallback(IGsmVideoCallCallback var1) throws RemoteException;

   void setCamera(String var1) throws RemoteException;

   void setDeviceOrientation(int var1) throws RemoteException;

   void setDisplaySurface(Surface var1) throws RemoteException;

   void setPauseImage(Uri var1) throws RemoteException;

   void setPreviewSurface(Surface var1) throws RemoteException;

   void setUIMode(int var1) throws RemoteException;

   void setZoom(float var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IGsmVideoCallProvider {
      private static final String DESCRIPTOR = "com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider";
      static final int TRANSACTION_requestCallDataUsage = 10;
      static final int TRANSACTION_requestCameraCapabilities = 9;
      static final int TRANSACTION_sendSessionModifyRequest = 7;
      static final int TRANSACTION_sendSessionModifyResponse = 8;
      static final int TRANSACTION_setCallback = 1;
      static final int TRANSACTION_setCamera = 2;
      static final int TRANSACTION_setDeviceOrientation = 5;
      static final int TRANSACTION_setDisplaySurface = 4;
      static final int TRANSACTION_setPauseImage = 11;
      static final int TRANSACTION_setPreviewSurface = 3;
      static final int TRANSACTION_setUIMode = 12;
      static final int TRANSACTION_setZoom = 6;

      public Stub() {
         this.attachInterface(this, "com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
      }

      public static IGsmVideoCallProvider asInterface(IBinder var0) {
         if (var0 == null) {
            return null;
         } else {
            IInterface var1 = var0.queryLocalInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
            return (IGsmVideoCallProvider)(var1 != null && var1 instanceof IGsmVideoCallProvider ? (IGsmVideoCallProvider)var1 : new IGsmVideoCallProvider.Stub.Proxy(var0));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         VideoProfile var7;
         Surface var8;
         switch(var1) {
         case 1:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
            this.setCallback(IGsmVideoCallCallback.Stub.asInterface(var2.readStrongBinder()));
            return true;
         case 2:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
            this.setCamera(var2.readString());
            return true;
         case 3:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
            if (var2.readInt() != 0) {
               var8 = (Surface)Surface.CREATOR.createFromParcel(var2);
            } else {
               var8 = null;
            }

            this.setPreviewSurface(var8);
            return true;
         case 4:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
            if (var2.readInt() != 0) {
               var8 = (Surface)Surface.CREATOR.createFromParcel(var2);
            } else {
               var8 = null;
            }

            this.setDisplaySurface(var8);
            return true;
         case 5:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
            this.setDeviceOrientation(var2.readInt());
            return true;
         case 6:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
            this.setZoom(var2.readFloat());
            return true;
         case 7:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
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

            this.sendSessionModifyRequest(var6, var7);
            return true;
         case 8:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
            if (var2.readInt() != 0) {
               var7 = (VideoProfile)VideoProfile.CREATOR.createFromParcel(var2);
            } else {
               var7 = null;
            }

            this.sendSessionModifyResponse(var7);
            return true;
         case 9:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
            this.requestCameraCapabilities();
            return true;
         case 10:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
            this.requestCallDataUsage();
            return true;
         case 11:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
            Uri var5;
            if (var2.readInt() != 0) {
               var5 = (Uri)Uri.CREATOR.createFromParcel(var2);
            } else {
               var5 = null;
            }

            this.setPauseImage(var5);
            return true;
         case 12:
            var2.enforceInterface("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
            this.setUIMode(var2.readInt());
            return true;
         case 1598968902:
            var3.writeString("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
            return true;
         default:
            return super.onTransact(var1, var2, var3, var4);
         }
      }

      private static class Proxy implements IGsmVideoCallProvider {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider";
         }

         public void requestCallDataUsage() throws RemoteException {
            Parcel var1 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
               this.mRemote.transact(10, var1, (Parcel)null, 1);
            } finally {
               var1.recycle();
            }

         }

         public void requestCameraCapabilities() throws RemoteException {
            Parcel var1 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
               this.mRemote.transact(9, var1, (Parcel)null, 1);
            } finally {
               var1.recycle();
            }

         }

         public void sendSessionModifyRequest(VideoProfile var1, VideoProfile var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();

            label305: {
               Throwable var10000;
               label309: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
                  } catch (Throwable var45) {
                     var10000 = var45;
                     var10001 = false;
                     break label309;
                  }

                  if (var1 != null) {
                     try {
                        var3.writeInt(1);
                        var1.writeToParcel(var3, 0);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label309;
                     }
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var41) {
                        var10000 = var41;
                        var10001 = false;
                        break label309;
                     }
                  }

                  label290:
                  try {
                     this.mRemote.transact(7, var3, (Parcel)null, 1);
                     break label305;
                  } catch (Throwable var40) {
                     var10000 = var40;
                     var10001 = false;
                     break label290;
                  }
               }

               Throwable var46 = var10000;
               var3.recycle();
               throw var46;
            }

            var3.recycle();
         }

         public void sendSessionModifyResponse(VideoProfile var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
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
                     this.mRemote.transact(8, var2, (Parcel)null, 1);
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

         public void setCallback(IGsmVideoCallCallback var1) throws RemoteException {
            IBinder var2 = null;
            Parcel var3 = Parcel.obtain();

            label112: {
               Throwable var10000;
               label116: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label116;
                  }

                  if (var1 != null) {
                     try {
                        var2 = var1.asBinder();
                     } catch (Throwable var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label116;
                     }
                  }

                  label104:
                  try {
                     var3.writeStrongBinder(var2);
                     this.mRemote.transact(1, var3, (Parcel)null, 1);
                     break label112;
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label104;
                  }
               }

               Throwable var16 = var10000;
               var3.recycle();
               throw var16;
            }

            var3.recycle();
         }

         public void setCamera(String var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
               var2.writeString(var1);
               this.mRemote.transact(2, var2, (Parcel)null, 1);
            } finally {
               var2.recycle();
            }

         }

         public void setDeviceOrientation(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
               var2.writeInt(var1);
               this.mRemote.transact(5, var2, (Parcel)null, 1);
            } finally {
               var2.recycle();
            }

         }

         public void setDisplaySurface(Surface var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
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
                     this.mRemote.transact(4, var2, (Parcel)null, 1);
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

         public void setPauseImage(Uri var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
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
                     this.mRemote.transact(11, var2, (Parcel)null, 1);
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

         public void setPreviewSurface(Surface var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
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
                     this.mRemote.transact(3, var2, (Parcel)null, 1);
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

         public void setUIMode(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
               var2.writeInt(var1);
               this.mRemote.transact(12, var2, (Parcel)null, 1);
            } finally {
               var2.recycle();
            }

         }

         public void setZoom(float var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("com.mediatek.internal.telephony.gsm.IGsmVideoCallProvider");
               var2.writeFloat(var1);
               this.mRemote.transact(6, var2, (Parcel)null, 1);
            } finally {
               var2.recycle();
            }

         }
      }
   }
}
