package com.mediatek.internal.telephony.ppl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPplAgent extends IInterface {
  int needLock() throws RemoteException;
  
  byte[] readControlData() throws RemoteException;
  
  int writeControlData(byte[] paramArrayOfbyte) throws RemoteException;
  
  public static abstract class Stub extends Binder implements IPplAgent {
    private static final String DESCRIPTOR = "com.mediatek.internal.telephony.ppl.IPplAgent";
    
    static final int TRANSACTION_needLock = 3;
    
    static final int TRANSACTION_readControlData = 1;
    
    static final int TRANSACTION_writeControlData = 2;
    
    public Stub() {
      attachInterface(this, "com.mediatek.internal.telephony.ppl.IPplAgent");
    }
    
    public static IPplAgent asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface("com.mediatek.internal.telephony.ppl.IPplAgent");
      return (iInterface != null && iInterface instanceof IPplAgent) ? (IPplAgent)iInterface : new Proxy(param1IBinder);
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      byte[] arrayOfByte;
      switch (param1Int1) {
        default:
          return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2);
        case 1598968902:
          param1Parcel2.writeString("com.mediatek.internal.telephony.ppl.IPplAgent");
          return true;
        case 1:
          param1Parcel1.enforceInterface("com.mediatek.internal.telephony.ppl.IPplAgent");
          arrayOfByte = readControlData();
          param1Parcel2.writeNoException();
          param1Parcel2.writeByteArray(arrayOfByte);
          return true;
        case 2:
          arrayOfByte.enforceInterface("com.mediatek.internal.telephony.ppl.IPplAgent");
          param1Int1 = writeControlData(arrayOfByte.createByteArray());
          param1Parcel2.writeNoException();
          param1Parcel2.writeInt(param1Int1);
          return true;
        case 3:
          break;
      } 
      arrayOfByte.enforceInterface("com.mediatek.internal.telephony.ppl.IPplAgent");
      param1Int1 = needLock();
      param1Parcel2.writeNoException();
      param1Parcel2.writeInt(param1Int1);
      return true;
    }
    
    private static class Proxy implements IPplAgent {
      private IBinder mRemote;
      
      Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getInterfaceDescriptor() {
        return "com.mediatek.internal.telephony.ppl.IPplAgent";
      }
      
      public int needLock() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.mediatek.internal.telephony.ppl.IPplAgent");
          this.mRemote.transact(3, parcel1, parcel2, 0);
          parcel2.readException();
          return parcel2.readInt();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public byte[] readControlData() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.mediatek.internal.telephony.ppl.IPplAgent");
          this.mRemote.transact(1, parcel1, parcel2, 0);
          parcel2.readException();
          return parcel2.createByteArray();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public int writeControlData(byte[] param2ArrayOfbyte) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("com.mediatek.internal.telephony.ppl.IPplAgent");
          parcel1.writeByteArray(param2ArrayOfbyte);
          this.mRemote.transact(2, parcel1, parcel2, 0);
          parcel2.readException();
          return parcel2.readInt();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
    }
  }
  
  static class Proxy implements IPplAgent {
    private IBinder mRemote;
    
    Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getInterfaceDescriptor() {
      return "com.mediatek.internal.telephony.ppl.IPplAgent";
    }
    
    public int needLock() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.mediatek.internal.telephony.ppl.IPplAgent");
        this.mRemote.transact(3, parcel1, parcel2, 0);
        parcel2.readException();
        return parcel2.readInt();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public byte[] readControlData() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.mediatek.internal.telephony.ppl.IPplAgent");
        this.mRemote.transact(1, parcel1, parcel2, 0);
        parcel2.readException();
        return parcel2.createByteArray();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public int writeControlData(byte[] param1ArrayOfbyte) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("com.mediatek.internal.telephony.ppl.IPplAgent");
        parcel1.writeByteArray(param1ArrayOfbyte);
        this.mRemote.transact(2, parcel1, parcel2, 0);
        parcel2.readException();
        return parcel2.readInt();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/ppl/IPplAgent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
