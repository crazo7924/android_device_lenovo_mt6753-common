# Prop
PRODUCT_DEFAULT_PROPERTY_OVERRIDES += \
    ro.secure=0 \
    ro.adb.secure=0 \
    camera.disable_zsl_mode=1 \
    persist.service.acm.enable=0 \
    persist.sys.usb.config=mtp,adb \
    ro.allow.mock.location=0 \
    ro.debuggable=1 \
    ro.dalvik.vm.native.bridge=0 \
    ro.mount.fs=EXT4 \
    ro.kernel.android.checkjni=0 \
    ro.telephony.ril.config=fakeiccid \
    ro.com.android.mobiledata=false

# Tethering
PRODUCT_PROPERTY_OVERRIDES += \
    net.tethering.noprovisioning=true

# Ims
# PRODUCT_PROPERTY_OVERRIDES += \
    ro.mtk_ims_support=1 \
    ro.mtk_volte_support=1 \
    persist.mtk.volte.enable=1 \
    ro.mtk_vilte_support=1 \
    persist.mtk.ims.video.enable=1 \
    ro.mtk_vilte_ut_support=0 \
    persist.mtk_dynamic_ims_switch=1
