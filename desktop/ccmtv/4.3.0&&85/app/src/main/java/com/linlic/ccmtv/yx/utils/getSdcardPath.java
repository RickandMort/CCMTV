package com.linlic.ccmtv.yx.utils;

import android.content.Context;
import android.os.storage.StorageManager;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * name：获取SD卡路径
 * author：Larry
 * data：2016/6/17.
 * http://blog.fidroid.com/post/android/ru-he-zheng-que-huo-de-androidnei-wai-sdqia-lu-jing
 * 通过反射的方式使用在sdk中被 隐藏 的类 StroageVolume 中的方法getVolumeList()，获取所有的存储空间（Stroage Volume），然后通过参数is_removable控制，来获取内部存储和外部存储（内外sd卡）的路径，参数 is_removable为false时得到的是内置sd卡路径，为true则为外置sd卡路径。
 */
public class getSdcardPath {

    public static String getStoragePath(Context mContext, boolean is_removale) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}

