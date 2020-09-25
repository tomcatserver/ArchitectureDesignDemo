package com.example.network.utils;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class ResponseBodyToDiskUtil {
    private static final int MAX_LENGTH = 4096;
    public static final String SD_HOME_DIR = Environment.getExternalStorageDirectory().getPath();

    /**
     * 下载到本地
     *
     * @param body 内容
     * @return 成功或者失败
     */
    public static String writeResponseBodyToDisk(ResponseBody body) {
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                Environment.getStorageDirectory().getPath()
//            } else {
//                Environment.getExternalStorageDirectory().getPath();
//
//            }
            //判断文件夹是否存在
            File files = new File(SD_HOME_DIR); //跟目录一个文件夹
            if (!files.exists()) {
                //不存在就创建出来
                files.mkdirs();
            }
            //创建一个文件
            File futureStudioIconFile = new File(SD_HOME_DIR + File.separator + "download.jpg");
            Log.e("tag", "writeResponseBodyToDisk: ---path=" + futureStudioIconFile.getPath());
            //初始化输入流
            InputStream inputStream = null;
            //初始化输出流
            OutputStream outputStream = null;
            try {
                //设置每次读写的字节
                byte[] fileReader = new byte[MAX_LENGTH];
//                long fileSize = body.contentLength();
//                long fileSizeDownloaded = 0;
                //请求返回的字节流
                inputStream = body.byteStream();
                //创建输出流
                outputStream = new FileOutputStream(futureStudioIconFile);
                //进行读取操作
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    //进行写入操作
                    outputStream.write(fileReader, 0, read);
//                    fileSizeDownloaded += read;
                }

                //刷新
                outputStream.flush();
                return futureStudioIconFile.getPath();
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    //关闭输入流
                    inputStream.close();
                }
                if (outputStream != null) {
                    //关闭输出流
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }
}
