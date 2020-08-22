package com.mediatek.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISetDefaultSubResultCallback extends IInterface {
   void onComplete(boolean var1) throws RemoteException;

   public abstract static class Stub extends Binder implements ISetDefaultSubResultCallback {
      private static final String DESCRIPTOR = "com.mediatek.internal.telephony.ISetDefaultSubResultCallback";
      static final int TRANSACTION_onComplete = 1;

      public Stub() {
         this.attachInterface(this, "com.mediatek.internal.telephony.ISetDefaultSubResultCallback");
      }

      public static ISetDefaultSubResultCallback asInterface(IBinder var0) {
         if (var0 == null) {
            return null;
         } else {
            IInterface var1 = var0.queryLocalInterface("com.mediatek.internal.telephony.ISetDefaultSubResultCallback");
            return (ISetDefaultSubResultCallback)(var1 != null && var1 instanceof ISetDefaultSubResultCallback ? (ISetDefaultSubResultCallback)var1 : new ISetDefaultSubResultCallback.Stub.Proxy(var0));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         switch(var1) {
         case 1:
            var2.enforceInterface("com.mediatek.internal.telephony.ISetDefaultSubResultCallback");
            boolean var5;
            if (var2.readInt() != 0) {
               var5 = true;
            } else {
               var5 = false;
            }

            this.onComplete(var5);
            return true;
         case 1598968902:
            var3.writeString("com.mediatek.internal.telephony.ISetDefaultSubResultCallback");
            return true;
         default:
            return super.onTransact(var1, var2, var3, var4);
         }
      }

      private static class Proxy implements ISetDefaultSubResultCallback {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "com.mediatek.internal.telephony.ISetDefaultSubResultCallback";
         }

         public void onComplete(boolean var1) throws RemoteException {
            byte var2 = 1;
            Parcel var3 = Parcel.obtain();

            label80: {
               Throwable var10000;
               label85: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("com.mediatek.internal.telephony.ISetDefaultSubResultCallback");
                  } catch (Throwable var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label85;
                  }

                  if (!var1) {
                     var2 = 0;
                  }

                  label75:
                  try {
                     var3.writeInt(var2);
                     this.mRemote.transact(1, var3, (Parcel)null, 1);
                     break label80;
                  } catch (Throwable var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label75;
                  }
               }

               Throwable var4 = var10000;
               var3.recycle();
               throw var4;
            }

            var3.recycle();
         }
      }
   }
}
