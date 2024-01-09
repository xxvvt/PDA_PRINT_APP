package com.example.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUtils {
    public static String getFileName(Context context,String fileName) {
        // 从文件读取数据
        FileInputStream inputStream;
        String jsonStringFromDisk = "";
        try {
            inputStream = context.openFileInput(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonStringFromDisk = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStringFromDisk;
    }

    public static void setFileName(Context context,String fileName,String content){
        // 保存数据到文件
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
