package com.example.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    //保存密码和账号到data.txt文件中
    public static boolean saveUserInfo(Context context, String account, String password){
        FileOutputStream fos = null;
        try{
            //获取文件输出流对象fos
            fos = context.openFileOutput("data.txt",Context.MODE_PRIVATE);
            fos.write((account + ":" + password).getBytes());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            try {
                if (fos != null){
                    fos.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //从文件中获取储存的账号和密码
    public static Map<String, String> getUserInfor(Context context){
        String content = "";
        FileInputStream fis = null;
        try{
            //获取文件的输入流对象
            fis = context.openFileInput("data.txt");
            //将数据流对象中的数据转换为字节码的形式
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);//通过read()方法读取字节码中的数据
            content = new String(buffer);//将获取的字节码转换为字符串
            Map<String, String> userMap =new HashMap<String, String>();
            //将数字以":"分割后形成一个数组的形式
            String[] infos = content.split(":");
            //将数组中的第一个数据放入userMap集合中
            userMap.put("account",infos[0]);
            //将数组中的第二个数据放入userMap集合中
            userMap.put("password",infos[1]);
            return userMap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            try {
                if(fis != null){
                    fis.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
