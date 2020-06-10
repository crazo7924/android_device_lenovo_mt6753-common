# RIL
PRODUCT_PROPERTY_OVERRIDES += \
    rild.libpath=mtk-ril.so \
    rild.libargs=-d/dev/ttyC0 \
    ro.telephony.sim.count=2 \
    persist.radio.default.sim=0 \
    persist.radio.mobile.data=0,0 \
    persist.radio.multisim.config=dsds \
    bgw.current3gband=0 \
    ril.current.share_modem=2 \
    ril.external.md=0 \
    ril.telephony.mode=0 \
    persist.radio.fd.counter=15 \
    persist.radio.fd.off.counter=5 \
    persist.radio.fd.r8.counter=15 \
    persist.radio.fd.off.r8.counter=5 \
    ro.mtk_gemini_support=1 \
    persist.gemini.sim_num=2 \
    ro.gemini.smart_sim_switch=false

# Maintainer
PRODUCT_PROPERTY_OVERRIDES += \
    ro.havoc.maintainer=DidntRead

# NFC
PRODUCT_PROPERTY_OVERRIDES += \
    ro.nfc.port=I2C

# WIFI
PRODUCT_PROPERTY_OVERRIDES += \
    wifi.interface=wlan0 \
    wifi.direct.interface=p2p0 \
    ro.mediatek.wlan.wsc=1 \
    ro.mediatek.wlan.p2p=1 \
    mediatek.wlan.ctia=0

# OpenGL
PRODUCT_PROPERTY_OVERRIDES += \
    ro.opengles.version=196610 \
    ro.kernel.qemu.gles=0

# Audio
PRODUCT_PROPERTY_OVERRIDES += \
    ro.camera.sound.forced=0 \
    ro.audio.silent=0 \
    persist.af.audio_patch_fm=1

# Mali
PRODUCT_PROPERTY_OVERRIDES += \
    debug.hwui.render_dirty_regions=false

# LCD Density
PRODUCT_PROPERTY_OVERRIDES += \
    ro.sf.lcd_density=420

# MTK
PRODUCT_PROPERTY_OVERRIDES += \
    ro.mtk_gemini_enhancement=1 \
    ro.mtk_eap_sim_aka=1 \
    ro.mtk_enable_md1=1 \
    ro.mtk_flight_mode_power_off_md=1 \
    ril.flightmode.poweroffMD=1 \
    ro.mtk_sim_hot_swap=1 \
    ro.mtk_world_phone=1 \
    ro.mtk_cta_set=1 \
    ro.mtk_lte_support=1 \
    ro.mtk_rild_read_imsi=1 \
    fmradio.driver.enable=0 \
    ro.mtk_bt_support=1 \
    ro.mtk_agps_app=1 \
    ro.mtk_wlan_support=1 \
    ro.mtk_gps_support=1 \
    persist.md.perm.checked=1 \
    ro.mediatek.chip_ver=S01 \
    ro.mediatek.platform=MT6735 \
    persist.mtk.wcn.combo.chipid=0x6630

# Misc
PRODUCT_PROPERTY_OVERRIDES += \
    camera.disable_zsl_mode=1 \
    ro.sf.hwrotation=0 \
    ro.oem_unlock_supported=1

# WFD
PRODUCT_PROPERTY_OVERRIDES += \
    wfd.dummy.enable=1 \
    persist.debug.wfd.enable=1

# DRM
PRODUCT_PROPERTY_OVERRIDES += \
    drm.service.enabled=true

# RescueParty
PRODUCT_PROPERTY_OVERRIDES += \
    persist.sys.disable_rescue=1

# FRP
PRODUCT_PROPERTY_OVERRIDES += \
    ro.frp.pst=/dev/block/platform/mtk-msdc.0/11230000.msdc0/by-name/frp

# Vendor security patch level
PRODUCT_PROPERTY_OVERRIDES += \
    ro.lineage.build.vendor_security_patch=2019-06-05
