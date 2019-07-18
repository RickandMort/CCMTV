
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := libibs-jni
LOCAL_SRC_FILES := libibs.so
LOCAL_LDFLAGS   += -fPIC

include $(PREBUILT_SHARED_LIBRARY)