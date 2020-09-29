package com.example.base.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {
    private static final String TAG = "FileUtils";
    private static final String FILE_NAME = "data.txt";

    /**
     * 写入字符串到指定文件目录
     *
     * @param path    指定文件目录
     * @param content 字符串
     */
    public static void writeString(String path, String content) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (file.isDirectory()) {
                file = new File(path, FILE_NAME);
                if (!file.exists()) {
                    file.createNewFile();
                }
            }
            FileOutputStream out = new FileOutputStream(file);
            StringBuffer sb = new StringBuffer();
            sb.append(content);
            out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
            out.close();
        } catch (IOException ex) {
            YWLogUtil.e(TAG, ex.toString());
        }
    }

    /**
     * 整个文件以String返回
     *
     * @param path 指定文件目录
     * @return 字符串
     */
    public static String readFile(String path) throws FileNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (file.isDirectory()) {
            file = new File(path, FILE_NAME);
        }
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        StringBuffer sb = new StringBuffer();
        String tempstr = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            while ((tempstr = br.readLine()) != null) {
                sb.append(tempstr);
            }
        } catch (IOException ex) {
            YWLogUtil.e(TAG, ex.toString());
        }
        return sb.toString();
    }

    /**
     * 按行读取文件，以list<String>的形式返回
     *
     * @param path
     * @return 字符串列表
     */
    public static List<String> readLineFile(String path) {
        List<String> lines = new ArrayList<String>();
        String tempstr = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            while ((tempstr = br.readLine()) != null) {
                lines.add(tempstr.toString());
            }
        } catch (IOException ex) {
            YWLogUtil.e(TAG, ex.toString());
        }
        return lines;
    }

    /**
     * 创建目录
     *
     * @param dirPath 指定目录
     */
    public static void mkDir(String dirPath) {
        File file = new File(dirPath);
        try {
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            YWLogUtil.e(TAG, "新建目录操作出错");
        }
    }

    /**
     * 创建文件
     *
     * @param filePath 文件目录
     */
    public static void createNewFile(String filePath) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            YWLogUtil.e(TAG, "新建文件操作出错");
        }
    }

    /**
     * 递归删除文件或者目录
     *
     * @param filePath 文件目录
     */
    public static void deleteEveryThing(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            if (file.isFile()) {
                file.delete();
            } else {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    String root = files[i].getAbsolutePath(); //得到子文件或文件夹的绝对路径
                    deleteEveryThing(root);
                }
                file.delete();
            }
        } catch (Exception e) {
            YWLogUtil.e(TAG, "删除文件失败");
        }
    }

    /*
     * 得到一个文件夹下所有文件
     */
    public static List<String> getAllFileNameInFold(String foldPath) {
        List<String> list = new ArrayList<>();

        LinkedList<String> folderList = new LinkedList<>();
        folderList.add(foldPath);
        while (folderList.size() > 0) {
            String last = folderList.peekLast();
            if (last == null) {
                break;
            }
            File file = new File(last);
            folderList.removeLast();
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    folderList.add(files[i].getPath());
                } else {
                    list.add(files[i].getPath());
                }
            }
        }
        return list;
    }


}
