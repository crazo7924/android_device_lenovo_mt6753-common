#
# Copyright (C) 2020 The LineageOS Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

BUILD_TOP := $(shell pwd)

# Device path
DEVICE_PATH := device/lenovo/k5fpr

# OTA assert
TARGET_OTA_ASSERT_DEVICE := k5fpr

# Partitions informations
BOARD_BOOTIMAGE_PARTITION_SIZE := 16777216
BOARD_RECOVERYIMAGE_PARTITION_SIZE := 16777216
BOARD_CACHEIMAGE_PARTITION_SIZE := 419430400
BOARD_CACHEIMAGE_FILE_SYSTEM_TYPE := ext4
BOARD_SYSTEMIMAGE_PARTITION_SIZE := 2684354560
BOARD_SYSTEMIMAGE_FILE_SYSTEM_TYPE := ext4
BOARD_USERDATAIMAGE_PARTITION_SIZE := 11733020672 # 11733041152 - 20480
BOARD_USERDATAIMAGE_FILE_SYSTEM_TYPE := ext4
BOARD_VENDORIMAGE_PARTITION_SIZE := 539492352
BOARD_VENDORIMAGE_FILE_SYSTEM_TYPE := ext4
BOARD_FLASH_BLOCK_SIZE := 4096

# Partitions type
TARGET_USERIMAGES_USE_EXT4 := true
TARGET_USERIMAGES_USE_F2FS := true

# DT2W node
POWER_FEATURE_DOUBLE_TAP_TO_WAKE := "/sys/lenovo_tp_gestures/tpd_suspend_status"

# Board
TARGET_BOARD_PLATFORM := mt6735
TARGET_BOARD_SUFFIX := _64
TARGET_SOC := mt6753
TARGET_BOOTLOADER_BOARD_NAME := mt6753
TARGET_BOARD_PLATFORM_GPU := mali-t720

# build/make/core/Makefile
TARGET_NO_BOOTLOADER := true

# CPU Architecture
TARGET_ARCH := arm64
TARGET_ARCH_VARIANT := armv8-a
TARGET_CPU_ABI := arm64-v8a
TARGET_CPU_ABI2 :=
TARGET_CPU_VARIANT := cortex-a53
TARGET_CPU_SMP := true

TARGET_2ND_ARCH := arm
TARGET_2ND_ARCH_VARIANT := armv8-a
TARGET_2ND_CPU_ABI := armeabi-v7a
TARGET_2ND_CPU_ABI2 := armeabi
TARGET_2ND_CPU_VARIANT := cortex-a53

ENABLE_SCHEDBOOST := true

# Architecture Extensions
ARCH_ARM_HAVE_NEON := true
ARCH_ARM_HAVE_VFP := true

# Custom RenderScript
OVERRIDE_RS_DRIVER := libRSDriver_mtk.so

# HW headers
TARGET_SPECIFIC_HEADER_PATH := $(DEVICE_PATH)/hardware/include

# Kernel
TARGET_KERNEL_CONFIG := k5fpr_defconfig
TARGET_KERNEL_SOURCE := kernel/lenovo/k5fpr/
BOARD_KERNEL_BASE := 0x40078000
BOARD_KERNEL_PAGESIZE := 2048
BOARD_KERNEL_IMAGE_NAME := Image.gz-dtb

BOARD_KERNEL_CMDLINE := bootopt=64S3,32N2,64N2 firmware_class.path=/vendor/firmware
BOARD_KERNEL_CMDLINE += androidboot.selinux=permissive

BOARD_MKBOOTIMG_ARGS := --base 0x40078000
BOARD_MKBOOTIMG_ARGS += --pagesize 2048
BOARD_MKBOOTIMG_ARGS += --kernel_offset 0x00008000
BOARD_MKBOOTIMG_ARGS += --ramdisk_offset 0x03f88000
BOARD_MKBOOTIMG_ARGS += --second_offset 0x00e88000
BOARD_MKBOOTIMG_ARGS += --tags_offset 0x0df88000
BOARD_MKBOOTIMG_ARGS += --board A7010

# 64-bit Binder
TARGET_USES_64_BIT_BINDER := true

# System
AB_OTA_UPDATER := false
BLOCK_BASED_OTA := true

# Seperate vendor
TARGET_COPY_OUT_VENDOR := vendor

# Audio policy format
USE_XML_AUDIO_POLICY_CONF := 1

# Disable memcpy opt (for audio libraries)
TARGET_CPU_MEMCPY_OPT_DISABLE := true

# Use dlmalloc instead of jemalloc for mallocs
MALLOC_SVELTE := true

# Charger
WITH_LINEAGE_CHARGER := true
BACKLIGHT_PATH := "/sys/class/leds/lcd-backlight/brightness"

# Boot animation
TARGET_BOOTANIMATION_PRELOAD := true
TARGET_BOOTANIMATION_TEXTURE_CACHE := true

# Bluetooth
BOARD_BLUETOOTH_BDROID_BUILDCFG_INCLUDE_DIR := $(DEVICE_PATH)/hardware/include/
BOARD_BLUETOOTH_DOES_NOT_USE_RFKILL := true

# Display
SF_VSYNC_EVENT_PHASE_OFFSET_NS := -8000000
VSYNC_EVENT_PHASE_OFFSET_NS := -8000000
PRESENT_TIME_OFFSET_FROM_VSYNC_NS := 0
TARGET_FORCE_HWC_FOR_VIRTUAL_DISPLAYS := true
MAX_VIRTUAL_DISPLAY_DIMENSION := 1

# Text layout engine
USE_MINIKIN := true

# Camera
TARGET_HAS_LEGACY_CAMERA_HAL1 := true
TARGET_USES_NON_TREBLE_CAMERA := true
USE_CAMERA_STUB := true
TARGET_SPECIFIC_CAMERA_PARAMETER_LIBRARY := libcamera_parameters_mtk

# Manifests
DEVICE_MANIFEST_FILE := $(DEVICE_PATH)/manifest.xml
DEVICE_FRAMEWORK_MANIFEST_FILE := $(DEVICE_PATH)/framework_manifest.xml

# Shims
TARGET_LD_SHIM_LIBS := \
	/system/lib/liblog.so|/vendor/lib/libshim_xlog.so \
	/system/lib64/liblog.so|/vendor/lib64/libshim_xlog.so \
	/system/lib/libgui.so|/vendor/lib/libmtkshim_gui.so \
	/system/lib64/libgui.so|/vendor/lib64/libmtkshim_gui.so \
	/vendor/lib/hw/audio.primary.mt6753.so|/vendor/lib/libmtkshim_audio.so \
	/vendor/lib64/hw/audio.primary.mt6753.so|/vendor/lib64/libmtkshim_audio.so \
	/vendor/lib/libcam.camadapter.so|/vendor/lib/libmtkshim_camera.so \
	/vendor/lib64/libcam.camadapter.so|/vendor/lib64/libmtkshim_camera.so \
	/system/lib/libui.so|/vendor/lib/libmtkshim_ui.so \
	/system/lib64/libui.so|/vendor/lib64/libmtkshim_ui.so \
	/vendor/lib/libcam.camnode.so|/vendor/lib/libmtkshim_camera.so \
	/vendor/lib64/libcam.camnode.so|/vendor/lib64/libmtkshim_camera.so

# sepolicy
BOARD_SEPOLICY_DIRS += $(DEVICE_PATH)/sepolicy

# split properties
BOARD_PROPERTY_OVERRIDES_SPLIT_ENABLED := true

# custom libinit
TARGET_INIT_VENDOR_LIB := libinit_k5fpr

# MTK Hardware flags
BOARD_GLOBAL_CFLAGS += -DCOMPAT_SENSORS_M
BOARD_GLOBAL_CFLAGS += -DADD_LEGACY_ACQUIRE_BUFFER_SYMBOL
BOARD_GLOBAL_CFLAGS += -DNO_SECURE_DISCARD

# EGL
BOARD_EGL_WORKAROUND_BUG_10194508 := true

# Legacy blobs
TARGET_NEEDS_PLATFORM_TEXT_RELOCATIONS := true

# WiFi
BOARD_WLAN_DEVICE                := MediaTek
BOARD_CONNECTIVITY_VENDOR        := MediaTek
WPA_SUPPLICANT_VERSION           := VER_0_8_X
BOARD_HOSTAPD_DRIVER             := NL80211
BOARD_HOSTAPD_PRIVATE_LIB        := lib_driver_cmd_mt66xx
BOARD_WPA_SUPPLICANT_DRIVER      := NL80211
BOARD_WPA_SUPPLICANT_PRIVATE_LIB := lib_driver_cmd_mt66xx
WIFI_DRIVER_FW_PATH_PARAM        := "/dev/wmtWifi"
WIFI_DRIVER_FW_PATH_STA          := STA
WIFI_DRIVER_FW_PATH_AP           := AP
WIFI_DRIVER_FW_PATH_P2P          := P2P
WIFI_DRIVER_STATE_CTRL_PARAM	 := "/dev/wmtWifi"
WIFI_DRIVER_STATE_ON		     := 1
WIFI_DRIVER_STATE_OFF		     := 0

# Use stock RIL stack
ENABLE_VENDOR_RIL_SERVICE := true
BOARD_PROVIDES_LIBRIL := true

# Allow duplicate rules
BUILD_BROKEN_DUP_RULES := true

# Recovery
TARGET_RECOVERY_PIXEL_FORMAT := "RGBA_8888"
TARGET_RECOVERY_FSTAB := $(DEVICE_PATH)/rootdir/recovery.fstab
TARGET_USE_CUSTOM_LUN_FILE_PATH := "/sys/devices/platform/mt_usb/musb-hdrc.0.auto/gadget/lun%d/file"

# lzma ramdisk
LZMA_RAMDISK_TARGETS := boot, recovery
