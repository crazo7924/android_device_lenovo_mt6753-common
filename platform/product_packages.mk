# Filesystem management tools
PRODUCT_PACKAGES += \
    e2fsck \
    fibmap.f2fs \
    fsck.f2fs \
    mkfs.f2fs \
    make_ext4fs \
    resize2fs \
    setup_fs \
    mount.exfat \
    fsck.exfat \
    mkfs.exfat \
    fsck.ntfs \
    mkfs.ntfs \
    mount.ntfs

# Ramdisk
PRODUCT_PACKAGES += \
    ueventd.mt6735.rc

# Keymaster HAL
PRODUCT_PACKAGES += \
    android.hardware.keymaster@3.0-impl

# Health HAL
PRODUCT_PACKAGES += \
    android.hardware.health@2.0-service

# Neural
PRODUCT_PACKAGES += \
    android.hardware.neuralnetworks@1.1-service-armnn

# Configstore
PRODUCT_PACKAGES += \
    android.hardware.configstore@1.1-service

# GNSS HAL
PRODUCT_PACKAGES += \
    android.hardware.gnss@1.0-impl \
    android.hardware.gnss@1.0-service

# GPS library
PRODUCT_PACKAGES += \
    libcurl

# AGPS
PRODUCT_PACKAGES += \
    libandroid_net

# Trusted face
PRODUCT_PACKAGES += \
    libprotobuf-cpp-full

# NVRAM
PRODUCT_PACKAGES += \
    libnvram

# Display
PRODUCT_PACKAGES += \
    libion \
    WallpaperPicker

# Camera
PRODUCT_PACKAGES += \
    libcamera_parameters_mtk \
    libcam.client \
    libmmsdkservice.feature \
    android.hardware.camera.provider@2.4-service \
    android.hardware.camera.provider@2.4-impl-legacy \
    camera.device@1.0-impl-legacy \
    Snap

# Managers
PRODUCT_PACKAGES += \
    hwservicemanager \
    vndservicemanager \
    servicemanager

# Fingerprint
PRODUCT_PACKAGES += \
    android.hardware.biometrics.fingerprint@2.0-service

# Vibrator
PRODUCT_PACKAGES += \
    android.hardware.vibrator@1.0-service.mtk

# Lights
PRODUCT_PACKAGES += \
    android.hardware.light@2.0-service.mtk

# Bluetooth
PRODUCT_PACKAGES += \
    android.hardware.bluetooth@1.0-service.mtk

# Audio
PRODUCT_PACKAGES += \
    android.hardware.audio@2.0-impl.mtk \
    android.hardware.audio@2.0-service.mtk \
    android.hardware.audio.effect@2.0-impl \
    audio.a2dp.default \
    audio.usb.default \
    audio.r_submix.default \
    audio_policy.default \
    libaudiopolicymanagerdefault \
    libaudio-resampler \
    libtinyalsa \
    libtinycompress \
    libtinymix \
    libtinyxml \
    libfs_mgr

# Lineage charger
PRODUCT_PACKAGES += \
    charger_res_images \
    libhealthd.lineage \
    charger \
    font_log.png

# USB Hal
PRODUCT_PACKAGES += \
    android.hardware.usb@1.0-service.basic

# Wifi
PRODUCT_PACKAGES += \
   android.hardware.wifi@1.0-service \
   lib_driver_cmd_mt66xx \
   libwifi-hal-mt66xx \
   hostapd \
   wificond \
   wifilogd \
   wpa_supplicant \
   wpa_supplicant.conf \
   muxreport \
   terservice

# Mediatek
PRODUCT_PACKAGES += \
    libstlport

# NFC packages
PRODUCT_PACKAGES += \
    com.android.nfc_extras \
    libnfc \
    MtkNfc \
    Tag

# NFC stack
PRODUCT_PACKAGES += \
    nfcstackp \

# DRM
PRODUCT_PACKAGES += \
     android.hardware.drm@1.1-service.clearkey \
     android.hardware.drm@1.0-impl \
     android.hardware.drm@1.0-service

# RenderScript
PRODUCT_PACKAGES += \
    android.hardware.renderscript@1.0-impl

# Graphics
PRODUCT_PACKAGES += \
    android.hardware.graphics.allocator@2.0-impl \
    android.hardware.graphics.allocator@2.0-service \
    android.hardware.graphics.composer@2.1-impl \
    android.hardware.memtrack@1.0-impl \
    android.hardware.graphics.mapper@2.0-impl

PRODUCT_PACKAGES += \
    libui_ext \
    libgralloc_extra \
    libgui_ext

# Lineage services
PRODUCT_PACKAGES += \
    lineage.livedisplay@2.0-service-mediatek \
    lineage.touch@1.0-service.lenovo \
    vendor.lineage.livedisplay@2.0-service-mediatek \
    vendor.lineage.trust@1.0-service

PRODUCT_PACKAGES += \
    Stk \
    libem_sensor_jni

# Combo
PRODUCT_PACKAGES += \
    wmt_loader

# Thermal
PRODUCT_PACKAGES += \
    android.hardware.thermal@1.0-service.mtk \
    thermal_manager

# RIL
PRODUCT_PACKAGES += \
   libccci_util

# Shims
PRODUCT_PACKAGES += \
    libmtkshim_ui \
    libmtkshim_gui \
    libmtkshim_audio \
    libmtkshim_camera \
    libshim_xlog \
    libshim_egl

# Power
PRODUCT_PACKAGES += \
    android.hardware.power@1.1-service.mtk

# Sensors
PRODUCT_PACKAGES += \
    android.hardware.sensors@1.0-impl.mtk \
    android.hardware.sensors@1.0-service.mtk

# IMS
# PRODUCT_PACKAGES += \
    okhttp \
    ims-config \
    ImsService \
    Simservs \
    xcap \
    wfo-common \
    libwfo_jni \
    WfoService

