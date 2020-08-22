package com.mediatek.internal.telephony.gsm;

import android.net.Uri;
import android.telecom.VideoProfile;
import android.util.Log;
import android.view.Surface;

public class GsmVTProvider extends GsmVideoCallProvider {
  public static final int SESSION_EVENT_BAD_DATA_BITRATE = 4008;
  
  public static final int SESSION_EVENT_CALL_ABNORMAL_END = 1009;
  
  public static final int SESSION_EVENT_CALL_END = 1008;
  
  public static final int SESSION_EVENT_CAM_CAP_CHANGED = 4007;
  
  public static final int SESSION_EVENT_DATA_BITRATE_RECOVER = 4009;
  
  public static final int SESSION_EVENT_DATA_USAGE_CHANGED = 4006;
  
  public static final int SESSION_EVENT_ERROR_CAMERA = 8003;
  
  public static final int SESSION_EVENT_ERROR_CAMERA_SET_IGNORED = 8006;
  
  public static final int SESSION_EVENT_ERROR_CODEC = 8004;
  
  public static final int SESSION_EVENT_ERROR_REC = 8005;
  
  public static final int SESSION_EVENT_ERROR_SERVER_DIED = 8002;
  
  public static final int SESSION_EVENT_ERROR_SERVICE = 8001;
  
  public static final int SESSION_EVENT_HANDLE_CALL_SESSION_EVT = 4003;
  
  public static final int SESSION_EVENT_LOCAL_SIZE_CHANGED = 4005;
  
  public static final int SESSION_EVENT_PEER_CAMERA_CLOSE = 1012;
  
  public static final int SESSION_EVENT_PEER_CAMERA_OPEN = 1011;
  
  public static final int SESSION_EVENT_PEER_SIZE_CHANGED = 4004;
  
  public static final int SESSION_EVENT_RECEIVE_FIRSTFRAME = 1001;
  
  public static final int SESSION_EVENT_RECORDER_EVENT_INFO_COMPLETE = 1007;
  
  public static final int SESSION_EVENT_RECORDER_EVENT_INFO_NO_I_FRAME = 1006;
  
  public static final int SESSION_EVENT_RECORDER_EVENT_INFO_REACH_MAX_DURATION = 1004;
  
  public static final int SESSION_EVENT_RECORDER_EVENT_INFO_REACH_MAX_FILESIZE = 1005;
  
  public static final int SESSION_EVENT_RECORDER_EVENT_INFO_UNKNOWN = 1003;
  
  public static final int SESSION_EVENT_RECV_SESSION_CONFIG_REQ = 4001;
  
  public static final int SESSION_EVENT_RECV_SESSION_CONFIG_RSP = 4002;
  
  public static final int SESSION_EVENT_SNAPSHOT_DONE = 1002;
  
  public static final int SESSION_EVENT_START_COUNTER = 1010;
  
  public static final int SESSION_EVENT_WARNING_SERVICE_NOT_READY = 9001;
  
  static final String TAG = "GsmVTProvider";
  
  public static final int VT_PROVIDER_INVALIDE_ID = -10000;
  
  private static int mDefaultId = -10000;
  
  private int mId = 1;
  
  private GsmVTProviderUtil mUtil;
  
  public GsmVTProvider() {
    Log.d("GsmVTProvider", "New GsmVTProvider without id");
    this.mId = -10000;
  }
  
  public GsmVTProvider(int paramInt) {
    Log.d("GsmVTProvider", "New GsmVTProvider id = " + paramInt);
    int i = 0;
    Log.d("GsmVTProvider", "New GsmVTProvider check if exist the same id");
    while (true) {
      if (GsmVTProviderUtil.recordGet(paramInt) != null) {
        Log.d("GsmVTProvider", "New GsmVTProvider the same id exist, wait ...");
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException interruptedException) {}
        int j = i + 1;
        i = j;
        if (j > 10) {
          Log.d("GsmVTProvider", "New GsmVTProvider the same id exist, break!");
        } else {
          continue;
        } 
      } 
      this.mId = paramInt;
      this.mUtil = new GsmVTProviderUtil();
      GsmVTProviderUtil.recordAdd(this.mId, this);
      nInitialization(this.mId);
      if (mDefaultId == -10000)
        mDefaultId = this.mId; 
      return;
    } 
  }
  
  public static native int nFinalization(int paramInt);
  
  public static native String nGetCameraParameters(int paramInt);
  
  public static native int nGetCameraSensorCount(int paramInt);
  
  public static native int nInitialization(int paramInt);
  
  public static native int nRequestCallDataUsage(int paramInt);
  
  public static native int nRequestCameraCapabilities(int paramInt);
  
  public static native int nRequestPeerConfig(int paramInt, String paramString);
  
  public static native int nResponseLocalConfig(int paramInt, String paramString);
  
  public static native int nSetCamera(int paramInt1, int paramInt2);
  
  public static native int nSetCameraParameters(int paramInt, String paramString);
  
  public static native int nSetDeviceOrientation(int paramInt1, int paramInt2);
  
  public static native int nSetDisplaySurface(int paramInt, Surface paramSurface);
  
  public static native int nSetPreviewSurface(int paramInt, Surface paramSurface);
  
  public static native int nSetUIMode(int paramInt1, int paramInt2);
  
  public static native int nSnapshot(int paramInt1, int paramInt2, String paramString);
  
  public static native int nStartRecording(int paramInt1, int paramInt2, String paramString, long paramLong);
  
  public static native int nStopRecording(int paramInt);
  
  public static void postEventFromNative(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Object<GsmVTProviderUtil.Size> paramObject1, Object paramObject2, Object paramObject3) {
    boolean bool;
    paramObject3 = GsmVTProviderUtil.recordGet(paramInt2);
    if (paramObject3 == null) {
      Log.e("GsmVTProvider", "Error: post event to Call is already release or has happen error before!");
      return;
    } 
    Log.i("GsmVTProvider", "postEventFromNative [" + paramInt1 + "]");
    switch (paramInt1) {
      default:
        Log.d("GsmVTProvider", "postEventFromNative : msg = UNKNOWB");
        return;
      case 1001:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_RECEIVE_FIRSTFRAME");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 1002:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_SNAPSHOT_DONE");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 1003:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_RECORDER_EVENT_INFO_UNKNOWN");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 1004:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_RECORDER_EVENT_INFO_REACH_MAX_DURATION");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 1005:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_RECORDER_EVENT_INFO_REACH_MAX_FILESIZE");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 1006:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_RECORDER_EVENT_INFO_NO_I_FRAME");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 1007:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_RECORDER_EVENT_INFO_COMPLETE");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 1008:
      case 1009:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_CALL_END / SESSION_EVENT_CALL_ABNORMAL_END");
        GsmVTProviderUtil.recordRemove(paramInt2);
        updateDefaultId();
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 1010:
        Log.d("GsmVTProvider", "postEventFromNative : msg = MSG_START_COUNTER");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 1011:
        Log.d("GsmVTProvider", "postEventFromNative : msg = MSG_PEER_CAMERA_OPEN");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 1012:
        Log.d("GsmVTProvider", "postEventFromNative : msg = MSG_PEER_CAMERA_CLOSE");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 4001:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_RECV_SESSION_CONFIG_REQ");
        paramObject3.receiveSessionModifyRequest(GsmVTProviderUtil.unPackToVdoProfile((String)paramObject1));
        return;
      case 4002:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_RECV_SESSION_CONFIG_RSP");
        paramObject3.receiveSessionModifyResponse(paramInt3, GsmVTProviderUtil.unPackToVdoProfile((String)paramObject1), GsmVTProviderUtil.unPackToVdoProfile((String)paramObject2));
        return;
      case 4003:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_HANDLE_CALL_SESSION_EVT");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 4004:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_PEER_SIZE_CHANGED");
        paramObject3.changePeerDimensionsWithAngle(paramInt3, paramInt4, paramInt5);
        return;
      case 4005:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_LOCAL_SIZE_CHANGED");
        return;
      case 4006:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_DATA_USAGE_CHANGED");
        paramObject3.changeCallDataUsage(paramInt3);
        return;
      case 4007:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_CAM_CAP_CHANGED");
        Log.d("GsmVTProvider", (String)paramObject1);
        GsmVTProviderUtil.getSetting().unflatten((String)paramObject1);
        paramObject1 = (Object<GsmVTProviderUtil.Size>)GsmVTProviderUtil.getSetting();
        paramInt3 = paramObject1.getInt("max-zoom", 0);
        bool = "true".equals(paramObject1.get("zoom-supported"));
        paramObject1 = (Object<GsmVTProviderUtil.Size>)paramObject1.getSizeList("preview-size");
        paramInt2 = 176;
        paramInt1 = 144;
        if (paramObject1 != null) {
          paramInt2 = ((GsmVTProviderUtil.Size)paramObject1.get(0)).width;
          paramInt1 = ((GsmVTProviderUtil.Size)paramObject1.get(0)).height;
        } 
        paramObject3.changeCameraCapabilities(new VideoProfile.CameraCapabilities(paramInt1, paramInt2, bool, paramInt3));
        return;
      case 4008:
        Log.d("GsmVTProvider", "postEventFromNative : msg = SESSION_EVENT_BAD_DATA_BITRATE");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 8001:
        Log.d("GsmVTProvider", "postEventFromNative : msg = MSG_ERROR_SERVICE");
        GsmVTProviderUtil.recordRemove(paramInt2);
        updateDefaultId();
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 8002:
        Log.d("GsmVTProvider", "postEventFromNative : msg = MSG_ERROR_SERVER_DIED");
        GsmVTProviderUtil.recordRemove(paramInt2);
        updateDefaultId();
        if (paramObject3 != null) {
          paramObject3.handleCallSessionEvent(paramInt1);
          return;
        } 
        return;
      case 8003:
        Log.d("GsmVTProvider", "postEventFromNative : msg = MSG_ERROR_CAMERA");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 8004:
        Log.d("GsmVTProvider", "postEventFromNative : msg = MSG_ERROR_CODEC");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 8005:
        Log.d("GsmVTProvider", "postEventFromNative : msg = MSG_ERROR_REC");
        paramObject3.handleCallSessionEvent(paramInt1);
        return;
      case 8006:
        break;
    } 
    Log.d("GsmVTProvider", "postEventFromNative : msg = MSG_ERROR_CAMERA_SET_IGNORED");
    paramObject3.handleCallSessionEvent(paramInt1);
  }
  
  private static void updateDefaultId() {
    if (!GsmVTProviderUtil.recordContain(mDefaultId)) {
      if (GsmVTProviderUtil.recordSize() != 0) {
        mDefaultId = GsmVTProviderUtil.recordPopId();
        return;
      } 
    } else {
      return;
    } 
    mDefaultId = -10000;
  }
  
  public int getId() {
    return this.mId;
  }
  
  public void onRequestCallDataUsage() {
    nRequestCallDataUsage(this.mId);
  }
  
  public void onRequestCameraCapabilities() {
    while (this.mId == -10000) {
      try {
        Thread.sleep(500L);
      } catch (InterruptedException interruptedException) {}
    } 
    nRequestCameraCapabilities(this.mId);
  }
  
  public void onSendSessionModifyRequest(VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2) {
    nRequestPeerConfig(this.mId, GsmVTProviderUtil.packFromVdoProfile(paramVideoProfile2));
  }
  
  public void onSendSessionModifyResponse(VideoProfile paramVideoProfile) {
    nResponseLocalConfig(this.mId, GsmVTProviderUtil.packFromVdoProfile(paramVideoProfile));
  }
  
  public void onSetCamera(String paramString) {
    if (this.mId == -10000) {
      handleCallSessionEvent(9001);
      return;
    } 
    if (paramString != null) {
      nSetCamera(this.mId, Integer.valueOf(paramString).intValue());
      return;
    } 
    nSetCamera(this.mId, -1);
  }
  
  public void onSetDeviceOrientation(int paramInt) {
    nSetDeviceOrientation(this.mId, paramInt);
  }
  
  public void onSetDisplaySurface(Surface paramSurface) {
    nSetDisplaySurface(this.mId, paramSurface);
    if (paramSurface == null) {
      GsmVTProviderUtil.surfaceSet(this.mId, false, false);
    } else {
      GsmVTProviderUtil.surfaceSet(this.mId, false, true);
    } 
    if (GsmVTProviderUtil.surfaceGet(this.mId) == 0) {
      GsmVTProvider gsmVTProvider = GsmVTProviderUtil.recordGet(this.mId);
      if (gsmVTProvider != null)
        gsmVTProvider.handleCallSessionEvent(1008); 
    } 
  }
  
  public void onSetPauseImage(Uri paramUri) {}
  
  public void onSetPreviewSurface(Surface paramSurface) {
    nSetPreviewSurface(this.mId, paramSurface);
    if (paramSurface == null) {
      GsmVTProviderUtil.surfaceSet(this.mId, true, false);
    } else {
      GsmVTProviderUtil.surfaceSet(this.mId, true, true);
    } 
    if (GsmVTProviderUtil.surfaceGet(this.mId) == 0) {
      GsmVTProvider gsmVTProvider = GsmVTProviderUtil.recordGet(this.mId);
      if (gsmVTProvider != null)
        gsmVTProvider.handleCallSessionEvent(1008); 
    } 
  }
  
  public void onSetUIMode(int paramInt) {
    if (paramInt == 65536) {
      nFinalization(this.mId);
      return;
    } 
    nSetUIMode(this.mId, paramInt);
  }
  
  public void onSetZoom(float paramFloat) {
    GsmVTProviderUtil gsmVTProviderUtil = this.mUtil;
    GsmVTProviderUtil.getSetting().set("zoom", (int)paramFloat);
    gsmVTProviderUtil = this.mUtil;
    String str = GsmVTProviderUtil.getSetting().flatten();
    nSetCameraParameters(this.mId, str);
  }
  
  public void setId(int paramInt) {
    Log.d("GsmVTProvider", "setId id = " + paramInt);
    Log.d("GsmVTProvider", "setId mId = " + this.mId);
    if (this.mId == -10000) {
      int i = 0;
      Log.d("GsmVTProvider", "New GsmVTProvider check if exist the same id");
      while (true) {
        if (GsmVTProviderUtil.recordGet(paramInt) != null) {
          Log.d("GsmVTProvider", "New GsmVTProvider the same id exist, wait ...");
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException interruptedException) {}
          int j = i + 1;
          i = j;
          if (j > 10) {
            Log.d("GsmVTProvider", "New GsmVTProvider the same id exist, break!");
          } else {
            continue;
          } 
        } 
        this.mId = paramInt;
        this.mUtil = new GsmVTProviderUtil();
        GsmVTProviderUtil.recordAdd(this.mId, this);
        nInitialization(this.mId);
        if (mDefaultId == -10000)
          mDefaultId = this.mId; 
        return;
      } 
    } 
  }
  
  static {
    System.loadLibrary("mtk_vt_wrapper");
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/gsm/GsmVTProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */