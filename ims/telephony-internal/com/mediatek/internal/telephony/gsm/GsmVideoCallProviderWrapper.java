package com.mediatek.internal.telephony.gsm;

import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telecom.Connection;
import android.telecom.VideoProfile;
import android.util.Log;
import android.view.Surface;
import com.android.internal.os.SomeArgs;

public class GsmVideoCallProviderWrapper extends Connection.VideoProvider {
  private static final int MSG_CHANGE_CALL_DATA_USAGE = 5;
  
  private static final int MSG_CHANGE_CAMERA_CAPABILITIES = 6;
  
  private static final int MSG_CHANGE_PEER_DIMENSIONS = 4;
  
  private static final int MSG_CHANGE_PEER_DIMENSIONS_WITH_ANGLE = 100;
  
  private static final int MSG_CHANGE_VIDEO_QUALITY = 7;
  
  private static final int MSG_HANDLE_CALL_SESSION_EVENT = 3;
  
  private static final int MSG_MTK_BASE = 100;
  
  private static final int MSG_RECEIVE_SESSION_MODIFY_REQUEST = 1;
  
  private static final int MSG_RECEIVE_SESSION_MODIFY_RESPONSE = 2;
  
  static final String TAG = "GsmVideoCallProviderWrapper";
  
  private final GsmVideoCallCallback mBinder;
  
  private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
      public void binderDied() {
        GsmVideoCallProviderWrapper.this.mVideoCallProvider.asBinder().unlinkToDeath(this, 0);
      }
    };
  
  private final Handler mHandler = new Handler(Looper.getMainLooper()) {
      public void handleMessage(Message param1Message) {
        SomeArgs someArgs;
        switch (param1Message.what) {
          default:
            return;
          case 1:
            GsmVideoCallProviderWrapper.this.receiveSessionModifyRequest((VideoProfile)param1Message.obj);
            return;
          case 2:
            someArgs = (SomeArgs)param1Message.obj;
            try {
              int i = ((Integer)someArgs.arg1).intValue();
              VideoProfile videoProfile1 = (VideoProfile)someArgs.arg2;
              VideoProfile videoProfile2 = (VideoProfile)someArgs.arg3;
              GsmVideoCallProviderWrapper.this.receiveSessionModifyResponse(i, videoProfile1, videoProfile2);
              return;
            } finally {
              someArgs.recycle();
            } 
          case 3:
            GsmVideoCallProviderWrapper.this.handleCallSessionEvent(((Integer)((Message)someArgs).obj).intValue());
            return;
          case 4:
            someArgs = (SomeArgs)((Message)someArgs).obj;
            try {
              int i = ((Integer)someArgs.arg1).intValue();
              int j = ((Integer)someArgs.arg2).intValue();
              GsmVideoCallProviderWrapper.this.changePeerDimensions(i, j);
              return;
            } finally {
              someArgs.recycle();
            } 
          case 100:
            someArgs = (SomeArgs)((Message)someArgs).obj;
            try {
              int i = ((Integer)someArgs.arg1).intValue();
              int j = ((Integer)someArgs.arg2).intValue();
              int k = ((Integer)someArgs.arg3).intValue();
              GsmVideoCallProviderWrapper.this.changePeerDimensionsWithAngle(i, j, k);
              return;
            } finally {
              someArgs.recycle();
            } 
          case 5:
            GsmVideoCallProviderWrapper.this.changeCallDataUsage(((Long)((Message)someArgs).obj).longValue());
            return;
          case 6:
            GsmVideoCallProviderWrapper.this.changeCameraCapabilities((VideoProfile.CameraCapabilities)((Message)someArgs).obj);
            return;
          case 7:
            break;
        } 
        GsmVideoCallProviderWrapper.this.changeVideoQuality(((Message)someArgs).arg1);
      }
    };
  
  private final IGsmVideoCallProvider mVideoCallProvider;
  
  public GsmVideoCallProviderWrapper(IGsmVideoCallProvider paramIGsmVideoCallProvider) throws RemoteException {
    Log.d("GsmVideoCallProviderWrapper", "New GsmVideoCallProviderWrapper");
    this.mVideoCallProvider = paramIGsmVideoCallProvider;
    this.mVideoCallProvider.asBinder().linkToDeath(this.mDeathRecipient, 0);
    this.mBinder = new GsmVideoCallCallback();
    this.mVideoCallProvider.setCallback((IGsmVideoCallCallback)this.mBinder);
  }
  
  public void onRequestCameraCapabilities() {
    try {
      this.mVideoCallProvider.requestCameraCapabilities();
      return;
    } catch (RemoteException remoteException) {
      return;
    } 
  }
  
  public void onRequestConnectionDataUsage() {
    try {
      this.mVideoCallProvider.requestCallDataUsage();
      return;
    } catch (RemoteException remoteException) {
      return;
    } 
  }
  
  public void onSendSessionModifyRequest(VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2) {
    try {
      this.mVideoCallProvider.sendSessionModifyRequest(paramVideoProfile1, paramVideoProfile2);
      return;
    } catch (RemoteException remoteException) {
      return;
    } 
  }
  
  public void onSendSessionModifyResponse(VideoProfile paramVideoProfile) {
    try {
      this.mVideoCallProvider.sendSessionModifyResponse(paramVideoProfile);
      return;
    } catch (RemoteException remoteException) {
      return;
    } 
  }
  
  public void onSetCamera(String paramString) {
    try {
      this.mVideoCallProvider.setCamera(paramString);
      return;
    } catch (RemoteException remoteException) {
      return;
    } 
  }
  
  public void onSetDeviceOrientation(int paramInt) {
    try {
      this.mVideoCallProvider.setDeviceOrientation(paramInt);
      return;
    } catch (RemoteException remoteException) {
      return;
    } 
  }
  
  public void onSetDisplaySurface(Surface paramSurface) {
    try {
      this.mVideoCallProvider.setDisplaySurface(paramSurface);
      return;
    } catch (RemoteException remoteException) {
      return;
    } 
  }
  
  public void onSetPauseImage(Uri paramUri) {
    try {
      this.mVideoCallProvider.setPauseImage(paramUri);
      return;
    } catch (RemoteException remoteException) {
      return;
    } 
  }
  
  public void onSetPreviewSurface(Surface paramSurface) {
    try {
      this.mVideoCallProvider.setPreviewSurface(paramSurface);
      return;
    } catch (RemoteException remoteException) {
      return;
    } 
  }
  
  public void onSetUIMode(int paramInt) {
    try {
      this.mVideoCallProvider.setUIMode(paramInt);
      return;
    } catch (RemoteException remoteException) {
      return;
    } 
  }
  
  public void onSetZoom(float paramFloat) {
    try {
      this.mVideoCallProvider.setZoom(paramFloat);
      return;
    } catch (RemoteException remoteException) {
      return;
    } 
  }
  
  private final class GsmVideoCallCallback extends IGsmVideoCallCallback.Stub {
    private GsmVideoCallCallback() {}
    
    public void changeCallDataUsage(long param1Long) {
      GsmVideoCallProviderWrapper.this.mHandler.obtainMessage(5, Long.valueOf(param1Long)).sendToTarget();
    }
    
    public void changeCameraCapabilities(VideoProfile.CameraCapabilities param1CameraCapabilities) {
      GsmVideoCallProviderWrapper.this.mHandler.obtainMessage(6, param1CameraCapabilities).sendToTarget();
    }
    
    public void changePeerDimensions(int param1Int1, int param1Int2) {
      SomeArgs someArgs = SomeArgs.obtain();
      someArgs.arg1 = Integer.valueOf(param1Int1);
      someArgs.arg2 = Integer.valueOf(param1Int2);
      GsmVideoCallProviderWrapper.this.mHandler.obtainMessage(4, someArgs).sendToTarget();
    }
    
    public void changePeerDimensionsWithAngle(int param1Int1, int param1Int2, int param1Int3) {
      SomeArgs someArgs = SomeArgs.obtain();
      someArgs.arg1 = Integer.valueOf(param1Int1);
      someArgs.arg2 = Integer.valueOf(param1Int2);
      someArgs.arg3 = Integer.valueOf(param1Int3);
      GsmVideoCallProviderWrapper.this.mHandler.obtainMessage(100, someArgs).sendToTarget();
    }
    
    public void changeVideoQuality(int param1Int) {
      GsmVideoCallProviderWrapper.this.mHandler.obtainMessage(7, param1Int, 0).sendToTarget();
    }
    
    public void handleCallSessionEvent(int param1Int) {
      GsmVideoCallProviderWrapper.this.mHandler.obtainMessage(3, Integer.valueOf(param1Int)).sendToTarget();
    }
    
    public void receiveSessionModifyRequest(VideoProfile param1VideoProfile) {
      GsmVideoCallProviderWrapper.this.mHandler.obtainMessage(1, param1VideoProfile).sendToTarget();
    }
    
    public void receiveSessionModifyResponse(int param1Int, VideoProfile param1VideoProfile1, VideoProfile param1VideoProfile2) {
      SomeArgs someArgs = SomeArgs.obtain();
      someArgs.arg1 = Integer.valueOf(param1Int);
      someArgs.arg2 = param1VideoProfile1;
      someArgs.arg3 = param1VideoProfile2;
      GsmVideoCallProviderWrapper.this.mHandler.obtainMessage(2, someArgs).sendToTarget();
    }
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/gsm/GsmVideoCallProviderWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */