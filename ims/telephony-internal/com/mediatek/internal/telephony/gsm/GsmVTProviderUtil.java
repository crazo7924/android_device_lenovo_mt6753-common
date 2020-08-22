package com.mediatek.internal.telephony.gsm;

import android.telecom.VideoProfile;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class GsmVTProviderUtil {
  public static final int HIDE_ME_TYPE_DISABLE = 1;
  
  public static final int HIDE_ME_TYPE_FREEZE = 2;
  
  public static final int HIDE_ME_TYPE_NONE = 0;
  
  public static final int HIDE_ME_TYPE_PICTURE = 3;
  
  public static final int HIDE_YOU_TYPE_DISABLE = 0;
  
  public static final int HIDE_YOU_TYPE_ENABLE = 1;
  
  private static final String TAG = "GsmVTProviderUtil";
  
  public static final int TURN_OFF_CAMERA = -1;
  
  public static final int UI_MODE_DESTROY = 65536;
  
  private static GsmVTMessagePacker mPacker = new GsmVTMessagePacker();
  
  private static ParameterSet mParamSet;
  
  private static Map<String, Object> mProviderById = new HashMap<String, Object>();
  
  private static Map<String, Object> mSurfaceStatusById = new HashMap<String, Object>();
  
  GsmVTProviderUtil() {
    mParamSet = new ParameterSet();
  }
  
  public static ParameterSet getSetting() {
    return mParamSet;
  }
  
  public static String packFromVdoProfile(VideoProfile paramVideoProfile) {
    return mPacker.packFromVdoProfile(paramVideoProfile);
  }
  
  public static void recordAdd(int paramInt, GsmVTProvider paramGsmVTProvider) {
    Log.d("GsmVTProviderUtil", "recordAdd id = " + paramInt + ", size = " + recordSize());
    mProviderById.put("" + paramInt, paramGsmVTProvider);
  }
  
  public static boolean recordContain(int paramInt) {
    return mProviderById.containsKey(Integer.valueOf(paramInt));
  }
  
  public static GsmVTProvider recordGet(int paramInt) {
    Log.d("GsmVTProviderUtil", "recordGet id = " + paramInt + ", size = " + recordSize());
    return (GsmVTProvider)mProviderById.get("" + paramInt);
  }
  
  public static int recordPopId() {
    if (mProviderById.size() != 0) {
      Iterator<GsmVTProvider> iterator = mProviderById.values().iterator();
      if (iterator.hasNext())
        return ((GsmVTProvider)iterator.next()).getId(); 
    } 
    return -10000;
  }
  
  public static void recordRemove(int paramInt) {
    Log.d("GsmVTProviderUtil", "recordRemove id = " + paramInt + ", size = " + recordSize());
    mProviderById.remove("" + paramInt);
  }
  
  public static int recordSize() {
    return mProviderById.size();
  }
  
  public static int surfaceGet(int paramInt) {
    Integer integer = (Integer)mSurfaceStatusById.get("" + paramInt);
    if (integer != null) {
      Log.d("GsmVTProviderUtil", "[surfaceGet] state = " + integer.intValue() + ", Id = " + paramInt);
      return integer.intValue();
    } 
    Log.d("GsmVTProviderUtil", "[surfaceGet] state = 0, Id = " + paramInt);
    return 0;
  }
  
  public static void surfaceSet(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    byte b;
    Integer integer = (Integer)mSurfaceStatusById.get("" + paramInt);
    Log.d("GsmVTProviderUtil", "[surfaceSet] isLocal = " + paramBoolean1 + ", isSet = " + paramBoolean2);
    if (integer != null) {
      b = integer.intValue();
      Log.d("GsmVTProviderUtil", "[surfaceSet] state (before) = " + b);
      if (paramBoolean1) {
        if (!paramBoolean2) {
          b &= 0xFFFFFFFE;
        } else {
          b |= 0x1;
        } 
      } else if (!paramBoolean2) {
        b &= 0xFFFFFFFD;
      } else {
        b |= 0x2;
      } 
    } else {
      Log.d("GsmVTProviderUtil", "[surfaceSet] state (before) = null");
      if (paramBoolean1) {
        if (!paramBoolean2) {
          b = 0;
        } else {
          b = 1;
        } 
      } else if (!paramBoolean2) {
        b = 0;
      } else {
        b = 2;
      } 
    } 
    Log.d("GsmVTProviderUtil", "[surfaceSet] state (after) = " + b + ", Id = " + paramInt);
    mSurfaceStatusById.put("" + paramInt, new Integer(b));
  }
  
  public static VideoProfile unPackToVdoProfile(String paramString) {
    return mPacker.unPackToVdoProfile(paramString);
  }
  
  public static class GsmVTMessagePacker {
    public String packFromVdoProfile(VideoProfile param1VideoProfile) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("mVideoState");
      stringBuilder.append("=");
      stringBuilder.append("" + param1VideoProfile.getVideoState());
      stringBuilder.append(";");
      stringBuilder.append("mQuality");
      stringBuilder.append("=");
      stringBuilder.append("" + param1VideoProfile.getQuality());
      stringBuilder.append(";");
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      Log.d("GsmVTProviderUtil", "[packFromVdoProfile] profile = " + stringBuilder.toString());
      return stringBuilder.toString();
    }
    
    public VideoProfile unPackToVdoProfile(String param1String) {
      Log.d("GsmVTProviderUtil", "[unPackToVdoProfile] flattened = " + param1String);
      StringTokenizer stringTokenizer = new StringTokenizer(param1String, ";");
      int i = 3;
      int j = 4;
      while (stringTokenizer.hasMoreElements()) {
        String str = stringTokenizer.nextToken();
        int k = str.indexOf('=');
        if (k != -1) {
          String str1 = str.substring(0, k);
          str = str.substring(k + 1);
          Log.d("GsmVTProviderUtil", "[unPackToVdoProfile] k = " + str1 + ", v = " + str);
          if (str1.equals("mVideoState")) {
            i = Integer.valueOf(str).intValue();
            continue;
          } 
          if (str1.equals("mQuality"))
            j = Integer.valueOf(str).intValue(); 
        } 
      } 
      Log.d("GsmVTProviderUtil", "[unPackToVdoProfile] state = " + i + ", qty = " + j);
      return new VideoProfile(i, j);
    }
  }
  
  public class ParameterSet {
    public static final String ANTIBANDING_50HZ = "50hz";
    
    public static final String ANTIBANDING_60HZ = "60hz";
    
    public static final String ANTIBANDING_AUTO = "auto";
    
    public static final String ANTIBANDING_OFF = "off";
    
    public static final String CAPTURE_MODE_BEST_SHOT = "bestshot";
    
    public static final String CAPTURE_MODE_BURST_SHOT = "burstshot";
    
    public static final String CAPTURE_MODE_EV_BRACKET_SHOT = "evbracketshot";
    
    public static final String CAPTURE_MODE_NORMAL = "normal";
    
    public static final String CONTRAST_HIGH = "high";
    
    public static final String CONTRAST_LOW = "low";
    
    public static final String CONTRAST_MIDDLE = "middle";
    
    public static final String EFFECT_AQUA = "aqua";
    
    public static final String EFFECT_BLACKBOARD = "blackboard";
    
    public static final String EFFECT_MONO = "mono";
    
    public static final String EFFECT_NEGATIVE = "negative";
    
    public static final String EFFECT_NONE = "none";
    
    public static final String EFFECT_POSTERIZE = "posterize";
    
    public static final String EFFECT_SEPIA = "sepia";
    
    public static final String EFFECT_SOLARIZE = "solarize";
    
    public static final String EFFECT_WHITEBOARD = "whiteboard";
    
    public static final String FLASH_MODE_AUTO = "auto";
    
    public static final String FLASH_MODE_OFF = "off";
    
    public static final String FLASH_MODE_ON = "on";
    
    public static final String FLASH_MODE_RED_EYE = "red-eye";
    
    public static final String FLASH_MODE_TORCH = "torch";
    
    public static final String FOCUS_MODE_AUTO = "auto";
    
    public static final String FOCUS_MODE_EDOF = "edof";
    
    public static final String FOCUS_MODE_FIXED = "fixed";
    
    public static final String FOCUS_MODE_INFINITY = "infinity";
    
    public static final String FOCUS_MODE_MACRO = "macro";
    
    public static final String KEY_ANTIBANDING = "antibanding";
    
    public static final String KEY_BRIGHTNESS_MODE = "brightness";
    
    public static final String KEY_BURST_SHOT_NUM = "burst-num";
    
    public static final String KEY_CAPTURE_MODE = "cap-mode";
    
    public static final String KEY_CAPTURE_PATH = "capfname";
    
    public static final String KEY_CONTRAST_MODE = "contrast";
    
    public static final String KEY_EDGE_MODE = "edge";
    
    public static final String KEY_EFFECT = "effect";
    
    public static final String KEY_EXPOSURE = "exposure";
    
    public static final String KEY_EXPOSURE_COMPENSATION = "exposure-compensation";
    
    public static final String KEY_EXPOSURE_COMPENSATION_STEP = "exposure-compensation-step";
    
    public static final String KEY_EXPOSURE_METER = "exposure-meter";
    
    public static final String KEY_FD_MODE = "fd-mode";
    
    public static final String KEY_FLASH_MODE = "flash-mode";
    
    public static final String KEY_FOCAL_LENGTH = "focal-length";
    
    public static final String KEY_FOCUS_METER = "focus-meter";
    
    public static final String KEY_FOCUS_MODE = "focus-mode";
    
    public static final String KEY_GPS_ALTITUDE = "gps-altitude";
    
    public static final String KEY_GPS_LATITUDE = "gps-latitude";
    
    public static final String KEY_GPS_LONGITUDE = "gps-longitude";
    
    public static final String KEY_GPS_PROCESSING_METHOD = "gps-processing-method";
    
    public static final String KEY_GPS_TIMESTAMP = "gps-timestamp";
    
    public static final String KEY_HORIZONTAL_VIEW_ANGLE = "horizontal-view-angle";
    
    public static final String KEY_HUE_MODE = "hue";
    
    public static final String KEY_ISOSPEED_MODE = "iso-speed";
    
    public static final String KEY_JPEG_QUALITY = "jpeg-quality";
    
    public static final String KEY_JPEG_THUMBNAIL_HEIGHT = "jpeg-thumbnail-height";
    
    public static final String KEY_JPEG_THUMBNAIL_QUALITY = "jpeg-thumbnail-quality";
    
    public static final String KEY_JPEG_THUMBNAIL_SIZE = "jpeg-thumbnail-size";
    
    public static final String KEY_JPEG_THUMBNAIL_WIDTH = "jpeg-thumbnail-width";
    
    public static final String KEY_MAX_EXPOSURE_COMPENSATION = "max-exposure-compensation";
    
    public static final String KEY_MAX_ZOOM = "max-zoom";
    
    public static final String KEY_MIN_EXPOSURE_COMPENSATION = "min-exposure-compensation";
    
    public static final String KEY_PICTURE_FORMAT = "picture-format";
    
    public static final String KEY_PICTURE_SIZE = "picture-size";
    
    public static final String KEY_PREVIEW_FORMAT = "preview-format";
    
    public static final String KEY_PREVIEW_FRAME_RATE = "preview-frame-rate";
    
    public static final String KEY_PREVIEW_SIZE = "preview-size";
    
    public static final String KEY_ROTATION = "rotation";
    
    public static final String KEY_SATURATION_MODE = "saturation";
    
    public static final String KEY_SCENE_MODE = "scene-mode";
    
    public static final String KEY_SMOOTH_ZOOM_SUPPORTED = "smooth-zoom-supported";
    
    public static final String KEY_VERTICAL_VIEW_ANGLE = "vertical-view-angle";
    
    public static final String KEY_WHITE_BALANCE = "whitebalance";
    
    public static final String KEY_ZOOM = "zoom";
    
    public static final String KEY_ZOOM_RATIOS = "zoom-ratios";
    
    public static final String KEY_ZOOM_SUPPORTED = "zoom-supported";
    
    private static final String PIXEL_FORMAT_JPEG = "jpeg";
    
    private static final String PIXEL_FORMAT_RGB565 = "rgb565";
    
    private static final String PIXEL_FORMAT_YUV420SP = "yuv420sp";
    
    private static final String PIXEL_FORMAT_YUV422I = "yuv422i-yuyv";
    
    private static final String PIXEL_FORMAT_YUV422SP = "yuv422sp";
    
    public static final String SCENE_MODE_ACTION = "action";
    
    public static final String SCENE_MODE_AUTO = "auto";
    
    public static final String SCENE_MODE_BARCODE = "barcode";
    
    public static final String SCENE_MODE_BEACH = "beach";
    
    public static final String SCENE_MODE_CANDLELIGHT = "candlelight";
    
    public static final String SCENE_MODE_FIREWORKS = "fireworks";
    
    public static final String SCENE_MODE_LANDSCAPE = "landscape";
    
    public static final String SCENE_MODE_NIGHT = "night";
    
    public static final String SCENE_MODE_NIGHT_PORTRAIT = "night-portrait";
    
    public static final String SCENE_MODE_PARTY = "party";
    
    public static final String SCENE_MODE_PORTRAIT = "portrait";
    
    public static final String SCENE_MODE_SNOW = "snow";
    
    public static final String SCENE_MODE_SPORTS = "sports";
    
    public static final String SCENE_MODE_STEADYPHOTO = "steadyphoto";
    
    public static final String SCENE_MODE_SUNSET = "sunset";
    
    public static final String SCENE_MODE_THEATRE = "theatre";
    
    public static final String SUPPORTED_VALUES_SUFFIX = "-values";
    
    public static final String WHITE_BALANCE_AUTO = "auto";
    
    public static final String WHITE_BALANCE_CLOUDY_DAYLIGHT = "cloudy-daylight";
    
    public static final String WHITE_BALANCE_DAYLIGHT = "daylight";
    
    public static final String WHITE_BALANCE_FLUORESCENT = "fluorescent";
    
    public static final String WHITE_BALANCE_INCANDESCENT = "incandescent";
    
    public static final String WHITE_BALANCE_SHADE = "shade";
    
    public static final String WHITE_BALANCE_TWILIGHT = "twilight";
    
    public static final String WHITE_BALANCE_WARM_FLUORESCENT = "warm-fluorescent";
    
    private HashMap<String, String> mMap = new HashMap<String, String>();
    
    private ArrayList<String> split(String param1String) {
      if (param1String == null)
        return null; 
      StringTokenizer stringTokenizer = new StringTokenizer(param1String, ",");
      ArrayList<String> arrayList = new ArrayList();
      while (true) {
        ArrayList<String> arrayList1 = arrayList;
        if (stringTokenizer.hasMoreElements()) {
          arrayList.add(stringTokenizer.nextToken());
          continue;
        } 
        return arrayList1;
      } 
    }
    
    private ArrayList<Integer> splitInt(String param1String) {
      if (param1String == null)
        return null; 
      StringTokenizer stringTokenizer = new StringTokenizer(param1String, ",");
      ArrayList<Integer> arrayList2 = new ArrayList();
      while (stringTokenizer.hasMoreElements())
        arrayList2.add(Integer.valueOf(Integer.parseInt(stringTokenizer.nextToken()))); 
      ArrayList<Integer> arrayList1 = arrayList2;
      return (arrayList2.size() == 0) ? null : arrayList1;
    }
    
    private ArrayList<GsmVTProviderUtil.Size> splitSize(String param1String) {
      if (param1String == null)
        return null; 
      StringTokenizer stringTokenizer = new StringTokenizer(param1String, ",");
      ArrayList<GsmVTProviderUtil.Size> arrayList2 = new ArrayList();
      while (stringTokenizer.hasMoreElements()) {
        GsmVTProviderUtil.Size size = strToSize(stringTokenizer.nextToken());
        if (size != null)
          arrayList2.add(size); 
      } 
      ArrayList<GsmVTProviderUtil.Size> arrayList1 = arrayList2;
      return (arrayList2.size() == 0) ? null : arrayList1;
    }
    
    private GsmVTProviderUtil.Size strToSize(String param1String) {
      if (param1String == null)
        return null; 
      int i = param1String.indexOf('x');
      if (i != -1) {
        String str = param1String.substring(0, i);
        param1String = param1String.substring(i + 1);
        return new GsmVTProviderUtil.Size(Integer.parseInt(str), Integer.parseInt(param1String));
      } 
      Log.e("GsmVTProviderUtil", "Invalid size parameter string=" + param1String);
      return null;
    }
    
    public void dump() {
      Log.e("GsmVTProviderUtil", "dump: size=" + this.mMap.size());
      for (String str : this.mMap.keySet())
        Log.e("GsmVTProviderUtil", "dump: " + str + "=" + (String)this.mMap.get(str)); 
    }
    
    public String flatten() {
      StringBuilder stringBuilder = new StringBuilder();
      for (String str : this.mMap.keySet()) {
        stringBuilder.append(str);
        stringBuilder.append("=");
        stringBuilder.append(this.mMap.get(str));
        stringBuilder.append(";");
      } 
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      return stringBuilder.toString();
    }
    
    public String get(String param1String) {
      return this.mMap.get(param1String);
    }
    
    public float getFloat(String param1String, float param1Float) {
      try {
        return Float.parseFloat(this.mMap.get(param1String));
      } catch (NumberFormatException numberFormatException) {
        return param1Float;
      } 
    }
    
    public int getInt(String param1String, int param1Int) {
      try {
        return Integer.parseInt(this.mMap.get(param1String));
      } catch (NumberFormatException numberFormatException) {
        return param1Int;
      } 
    }
    
    public List<Integer> getIntList(String param1String) {
      return splitInt(get(param1String));
    }
    
    public GsmVTProviderUtil.Size getSize(String param1String) {
      return strToSize(get(param1String));
    }
    
    public List<GsmVTProviderUtil.Size> getSizeList(String param1String) {
      return splitSize(get(param1String));
    }
    
    public List<String> getStrList(String param1String) {
      return split(get(param1String));
    }
    
    public void remove(String param1String) {
      this.mMap.remove(param1String);
    }
    
    public void set(String param1String, int param1Int) {
      this.mMap.put(param1String, Integer.toString(param1Int));
    }
    
    public void set(String param1String1, String param1String2) {
      if (param1String1.indexOf('=') != -1 || param1String1.indexOf(';') != -1) {
        Log.e("GsmVTProviderUtil", "Key \"" + param1String1 + "\" contains invalid character (= or ;)");
        return;
      } 
      if (param1String2.indexOf('=') != -1 || param1String2.indexOf(';') != -1) {
        Log.e("GsmVTProviderUtil", "Value \"" + param1String2 + "\" contains invalid character (= or ;)");
        return;
      } 
      this.mMap.put(param1String1, param1String2);
    }
    
    public void unflatten(String param1String) {
      this.mMap.clear();
      StringTokenizer stringTokenizer = new StringTokenizer(param1String, ";");
      while (stringTokenizer.hasMoreElements()) {
        String str = stringTokenizer.nextToken();
        int i = str.indexOf('=');
        if (i != -1) {
          String str1 = str.substring(0, i);
          str = str.substring(i + 1);
          this.mMap.put(str1, str);
        } 
      } 
    }
  }
  
  public static class Size {
    public int height;
    
    public int width;
    
    public Size(int param1Int1, int param1Int2) {
      this.width = param1Int1;
      this.height = param1Int2;
    }
    
    public boolean equals(Object param1Object) {
      if (param1Object instanceof Size) {
        param1Object = param1Object;
        if (this.width == ((Size)param1Object).width && this.height == ((Size)param1Object).height)
          return true; 
      } 
      return false;
    }
    
    public int hashCode() {
      return this.width * 32713 + this.height;
    }
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/gsm/GsmVTProviderUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */