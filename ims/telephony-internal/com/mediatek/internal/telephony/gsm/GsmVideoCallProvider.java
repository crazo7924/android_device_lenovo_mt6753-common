package com.mediatek.internal.telephony.gsm;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telecom.VideoProfile;
import android.view.Surface;
import com.android.internal.os.SomeArgs;

public abstract class GsmVideoCallProvider {
  private static final int MSG_MTK_BASE = 100;
  
  private static final int MSG_REQUEST_CALL_DATA_USAGE = 10;
  
  private static final int MSG_REQUEST_CAMERA_CAPABILITIES = 9;
  
  private static final int MSG_SEND_SESSION_MODIFY_REQUEST = 7;
  
  private static final int MSG_SEND_SESSION_MODIFY_RESPONSE = 8;
  
  private static final int MSG_SET_CALLBACK = 1;
  
  private static final int MSG_SET_CAMERA = 2;
  
  private static final int MSG_SET_DEVICE_ORIENTATION = 5;
  
  private static final int MSG_SET_DISPLAY_SURFACE = 4;
  
  private static final int MSG_SET_PAUSE_IMAGE = 11;
  
  private static final int MSG_SET_PREVIEW_SURFACE = 3;
  
  private static final int MSG_SET_UI_MODE = 100;
  
  private static final int MSG_SET_ZOOM = 6;
  
  private final GsmVideoCallProviderBinder mBinder = new GsmVideoCallProviderBinder();
  
  private IGsmVideoCallCallback mCallback;
  
  private final Handler mProviderHandler = new Handler(Looper.getMainLooper()) {
      public void handleMessage(Message param1Message) {
        SomeArgs someArgs;
        switch (param1Message.what) {
          default:
            return;
          case 1:
            GsmVideoCallProvider.access$002(GsmVideoCallProvider.this, (IGsmVideoCallCallback)param1Message.obj);
            return;
          case 2:
            GsmVideoCallProvider.this.onSetCamera((String)param1Message.obj);
            return;
          case 3:
            GsmVideoCallProvider.this.onSetPreviewSurface((Surface)param1Message.obj);
            return;
          case 4:
            GsmVideoCallProvider.this.onSetDisplaySurface((Surface)param1Message.obj);
            return;
          case 5:
            GsmVideoCallProvider.this.onSetDeviceOrientation(param1Message.arg1);
            return;
          case 6:
            GsmVideoCallProvider.this.onSetZoom(((Float)param1Message.obj).floatValue());
            return;
          case 7:
            someArgs = (SomeArgs)param1Message.obj;
            try {
              VideoProfile videoProfile1 = (VideoProfile)someArgs.arg1;
              VideoProfile videoProfile2 = (VideoProfile)someArgs.arg2;
              GsmVideoCallProvider.this.onSendSessionModifyRequest(videoProfile1, videoProfile2);
              return;
            } finally {
              someArgs.recycle();
            } 
          case 8:
            GsmVideoCallProvider.this.onSendSessionModifyResponse((VideoProfile)((Message)someArgs).obj);
            return;
          case 9:
            GsmVideoCallProvider.this.onRequestCameraCapabilities();
            return;
          case 10:
            GsmVideoCallProvider.this.onRequestCallDataUsage();
            return;
          case 11:
            GsmVideoCallProvider.this.onSetPauseImage((Uri)((Message)someArgs).obj);
            return;
          case 100:
            break;
        } 
        GsmVideoCallProvider.this.onSetUIMode(((Integer)((Message)someArgs).obj).intValue());
      }
    };
  
  public void changeCallDataUsage(long paramLong) {
    if (this.mCallback != null)
      try {
        this.mCallback.changeCallDataUsage(paramLong);
        return;
      } catch (RemoteException remoteException) {
        return;
      }  
  }
  
  public void changeCameraCapabilities(VideoProfile.CameraCapabilities paramCameraCapabilities) {
    if (this.mCallback != null)
      try {
        this.mCallback.changeCameraCapabilities(paramCameraCapabilities);
        return;
      } catch (RemoteException remoteException) {
        return;
      }  
  }
  
  public void changePeerDimensions(int paramInt1, int paramInt2) {
    if (this.mCallback != null)
      try {
        this.mCallback.changePeerDimensions(paramInt1, paramInt2);
        return;
      } catch (RemoteException remoteException) {
        return;
      }  
  }
  
  public void changePeerDimensionsWithAngle(int paramInt1, int paramInt2, int paramInt3) {
    if (this.mCallback != null)
      try {
        this.mCallback.changePeerDimensionsWithAngle(paramInt1, paramInt2, paramInt3);
        return;
      } catch (RemoteException remoteException) {
        return;
      }  
  }
  
  public void changeVideoQuality(int paramInt) {
    if (this.mCallback != null)
      try {
        this.mCallback.changeVideoQuality(paramInt);
        return;
      } catch (RemoteException remoteException) {
        return;
      }  
  }
  
  public final IGsmVideoCallProvider getInterface() {
    return (IGsmVideoCallProvider)this.mBinder;
  }
  
  public void handleCallSessionEvent(int paramInt) {
    if (this.mCallback != null)
      try {
        this.mCallback.handleCallSessionEvent(paramInt);
        return;
      } catch (RemoteException remoteException) {
        return;
      }  
  }
  
  public abstract void onRequestCallDataUsage();
  
  public abstract void onRequestCameraCapabilities();
  
  public abstract void onSendSessionModifyRequest(VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2);
  
  public abstract void onSendSessionModifyResponse(VideoProfile paramVideoProfile);
  
  public abstract void onSetCamera(String paramString);
  
  public abstract void onSetDeviceOrientation(int paramInt);
  
  public abstract void onSetDisplaySurface(Surface paramSurface);
  
  public abstract void onSetPauseImage(Uri paramUri);
  
  public abstract void onSetPreviewSurface(Surface paramSurface);
  
  public abstract void onSetUIMode(int paramInt);
  
  public abstract void onSetZoom(float paramFloat);
  
  public void receiveSessionModifyRequest(VideoProfile paramVideoProfile) {
    if (this.mCallback != null)
      try {
        this.mCallback.receiveSessionModifyRequest(paramVideoProfile);
        return;
      } catch (RemoteException remoteException) {
        return;
      }  
  }
  
  public void receiveSessionModifyResponse(int paramInt, VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2) {
    if (this.mCallback != null)
      try {
        this.mCallback.receiveSessionModifyResponse(paramInt, paramVideoProfile1, paramVideoProfile2);
        return;
      } catch (RemoteException remoteException) {
        return;
      }  
  }
  
  private final class GsmVideoCallProviderBinder extends IGsmVideoCallProvider.Stub {
    private GsmVideoCallProviderBinder() {}
    
    public void requestCallDataUsage() {
      GsmVideoCallProvider.this.mProviderHandler.obtainMessage(10).sendToTarget();
    }
    
    public void requestCameraCapabilities() {
      GsmVideoCallProvider.this.mProviderHandler.obtainMessage(9).sendToTarget();
    }
    
    public void sendSessionModifyRequest(VideoProfile param1VideoProfile1, VideoProfile param1VideoProfile2) {
      SomeArgs someArgs = SomeArgs.obtain();
      someArgs.arg1 = param1VideoProfile1;
      someArgs.arg2 = param1VideoProfile2;
      GsmVideoCallProvider.this.mProviderHandler.obtainMessage(7, someArgs).sendToTarget();
    }
    
    public void sendSessionModifyResponse(VideoProfile param1VideoProfile) {
      GsmVideoCallProvider.this.mProviderHandler.obtainMessage(8, param1VideoProfile).sendToTarget();
    }
    
    public void setCallback(IGsmVideoCallCallback param1IGsmVideoCallCallback) {
      GsmVideoCallProvider.this.mProviderHandler.obtainMessage(1, param1IGsmVideoCallCallback).sendToTarget();
    }
    
    public void setCamera(String param1String) {
      GsmVideoCallProvider.this.mProviderHandler.obtainMessage(2, param1String).sendToTarget();
    }
    
    public void setDeviceOrientation(int param1Int) {
      GsmVideoCallProvider.this.mProviderHandler.obtainMessage(5, param1Int, 0).sendToTarget();
    }
    
    public void setDisplaySurface(Surface param1Surface) {
      GsmVideoCallProvider.this.mProviderHandler.obtainMessage(4, param1Surface).sendToTarget();
    }
    
    public void setPauseImage(Uri param1Uri) {
      GsmVideoCallProvider.this.mProviderHandler.obtainMessage(11, param1Uri).sendToTarget();
    }
    
    public void setPreviewSurface(Surface param1Surface) {
      GsmVideoCallProvider.this.mProviderHandler.obtainMessage(3, param1Surface).sendToTarget();
    }
    
    public void setUIMode(int param1Int) {
      GsmVideoCallProvider.this.mProviderHandler.obtainMessage(100, Integer.valueOf(param1Int)).sendToTarget();
    }
    
    public void setZoom(float param1Float) {
      GsmVideoCallProvider.this.mProviderHandler.obtainMessage(6, Float.valueOf(param1Float)).sendToTarget();
    }
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/gsm/GsmVideoCallProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */