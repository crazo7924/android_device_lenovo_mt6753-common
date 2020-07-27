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

BUILD_TOP := $(shell pwd)

PLATFORM_PATH := device/lenovo/mt6753-common

### BOARD
TARGET_BOARD_PLATFORM := mt6753
TARGET_BOARD_SUFFIX := _64
TARGET_SOC := mt6753
TARGET_BOOTLOADER_BOARD_NAME := mt6753
TARGET_BOARD_PLATFORM_GPU := mali-t720

# build/make/core/Makefile
TARGET_NO_BOOTLOADER := true

### PROCESSOR
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

# Architecture Extensions
ARCH_ARM_HAVE_NEON := true
ARCH_ARM_HAVE_VFP := true

### RENDERSCRIPT
OVERRIDE_RS_DRIVER := libRSDriver_mtk.so

### HARDWARE INCLUDE
TARGET_SPECIFIC_HEADER_PATH := $(PLATFORM_PATH)/hardware/include

### KERNEL
BOARD_KERNEL_BASE            := 0x40078000
BOARD_KERNEL_PAGESIZE        := 2048
BOARD_KERNEL_IMAGE_NAME      := Image.gz-dtb

BOARD_KERNEL_CMDLINE := bootopt=64S3,32N2,64N2 firmware_class.path=/vendor/firmware
BOARD_KERNEL_CMDLINE += androidboot.selinux=permissive

BOARD_MKBOOTIMG_ARGS := --base 0x40078000
BOARD_MKBOOTIMG_ARGS += --pagesize 2048
BOARD_MKBOOTIMG_ARGS += --kernel_offset 0x00008000
BOARD_MKBOOTIMG_ARGS += --ramdisk_offset 0x03f88000
BOARD_MKBOOTIMG_ARGS += --second_offset 0x00e88000
BOARD_MKBOOTIMG_ARGS += --tags_offset 0x0df88000
BOARD_MKBOOTIMG_ARGS += --board A7010

### BINDER
# build/make/core/config.mk
TARGET_USES_64_BIT_BINDER := true

### VNDK
# BOARD_VNDK_VERSION := current

### SYSTEM
BOARD_SYSTEMIMAGE_FILE_SYSTEM_TYPE := ext4
# system/core and build/make
AB_OTA_UPDATER := false
BLOCK_BASED_OTA := true

### VENDOR
BOARD_VENDORIMAGE_FILE_SYSTEM_TYPE := ext4
TARGET_COPY_OUT_VENDOR := vendor

### CACHE
BOARD_CACHEIMAGE_FILE_SYSTEM_TYPE := ext4

### AUDIO
USE_XML_AUDIO_POLICY_CONF := 1

# Disable memcpy opt (for audio libraries)
TARGET_CPU_MEMCPY_OPT_DISABLE := true

### CHARGER
BOARD_CHARGER_ENABLE_SUSPEND := true
BOARD_CHARGER_SHOW_PERCENTAGE := true
BACKLIGHT_PATH := /sys/class/leds/lcd-backlight/brightness

### BOOT-ANIMATION
TARGET_BOOTANIMATION_PRELOAD := true
TARGET_BOOTANIMATION_TEXTURE_CACHE := true

### BLUETOOTH
BOARD_BLUETOOTH_BDROID_BUILDCFG_INCLUDE_DIR := $(PLATFORM_PATH)/hardware/bluetooth
BOARD_BLUETOOTH_DOES_NOT_USE_RFKILL := true

### GRAPHICS
# Display
SF_VSYNC_EVENT_PHASE_OFFSET_NS := -8000000
VSYNC_EVENT_PHASE_OFFSET_NS := -8000000
PRESENT_TIME_OFFSET_FROM_VSYNC_NS := 0
TARGET_FORCE_HWC_FOR_VIRTUAL_DISPLAYS := true
MAX_VIRTUAL_DISPLAY_DIMENSION := 1

# Text layout engine
USE_MINIKIN := true

### FS
TARGET_USES_MKE2FS := true

### CAMERA
TARGET_CAMERASERVICE_CLOSES_NATIVE_HANDLES := true
TARGET_USES_NON_TREBLE_CAMERA := true
USE_CAMERA_STUB := true

### HIDL
DEVICE_MANIFEST_FILE := $(PLATFORM_PATH)/manifest.xml
# Framework manifest
DEVICE_FRAMEWORK_MANIFEST_FILE := $(PLATFORM_PATH)/framework_manifest.xml

### SHIMS
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

### SEPOLICY
BOARD_SEPOLICY_DIRS += $(PLATFORM_PATH)/sepolicy

### PROPERTIES
BOARD_PROPERTY_OVERRIDES_SPLIT_ENABLED := true

### INIT
TARGET_INIT_VENDOR_LIB := libinit_k5fpr

# MTK Hardware flags
BOARD_GLOBAL_CFLAGS += -DCOMPAT_SENSORS_M
BOARD_GLOBAL_CFLAGS += -DADD_LEGACY_ACQUIRE_BUFFER_SYMBOL
BOARD_GLOBAL_CFLAGS += -DNO_SECURE_DISCARD

# EGL
BOARD_EGL_WORKAROUND_BUG_10194508 := true

# Legacy blobs
TARGET_NEEDS_PLATFORM_TEXT_RELOCATIONS := true

### WIFI
BOARD_WLAN_DEVICE		 := MediaTek
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
WIFI_DRIVER_STATE_ON		 := 1
WIFI_DRIVER_STATE_OFF		 := 0

### RIL
# Use stock RIL stack
ENABLE_VENDOR_RIL_SERVICE := true
BOARD_PROVIDES_LIBRIL := true

### ALLOW VENDOR FILE OVERRIDE
BUILD_BROKEN_DUP_RULES := true

### RECOVERY
TARGET_RECOVERY_PIXEL_FORMAT := "RGBA_8888"
TARGET_RECOVERY_FSTAB := $(PLATFORM_PATH)/rootdir/recovery.fstab
LZMA_RAMDISK_TARGETS := boot, recovery
