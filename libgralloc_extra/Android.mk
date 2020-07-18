LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := \
	GraphicBufferExtra.cpp \
	GraphicBufferExtra_hal.cpp
#	ge.c

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/include \
	$(LOCAL_PATH)/include/hardware

LOCAL_SHARED_LIBRARIES := \
    libhardware \
    libcutils \
    libutils \
    liblog \
    libion \
    libged

LOCAL_EXPORT_C_INCLUDE_DIRS := \
	$(LOCAL_PATH)/include \

LOCAL_C_INCLUDES += \
	system/core/libion/include \
	frameworks/native/libs/nativewindow/include \
	frameworks/native/libs/nativebase/include \
	frameworks/native/libs/arect/include

LOCAL_PROPRIETARY_MODULE := true
LOCAL_MODULE := libgralloc_extra
LOCAL_MODULE_TAGS := optional

include $(BUILD_SHARED_LIBRARY)
