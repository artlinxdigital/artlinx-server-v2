package com.nft.common.utils;

import com.nft.common.constant.Constants;

/**
 * 路径工具类
 *
 * @author nft
 */
public class PathUtil {

    private static String separator = System.getProperty("file.separator");

    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String bathPath = "";
        if (os.toLowerCase().startsWith("win")) {
            bathPath = Constants.WIN_FILE_URL;
        } else {
            bathPath = Constants.DISK_FILE_URL;
        }
        bathPath = bathPath.replace("/", separator);
        return bathPath;
    }

    /**
     * 获得下载的路径
     *
     * @return
     */
    public static String getDownBasePath() {
        String os = System.getProperty("os.name");
        String bathPath = "";
        if (os.toLowerCase().startsWith("win")) {
            bathPath = Constants.WIN_FILE_URL_DOWN;
        } else {
            bathPath = Constants.DISK_FILE_URL_DOWN;
        }
        bathPath = bathPath.replace("/", separator);
        return bathPath;
    }

    public static String getEditorBasePath() {
        String os = System.getProperty("os.name");
        String bathPath = "";
        if (os.toLowerCase().startsWith("win")) {
            bathPath = Constants.WIN_EDITOR_FILE_URL;
        } else {
            bathPath = Constants.DISK_EDITOR_FILE_URL;
        }
        bathPath = bathPath.replace("/", separator);
        return bathPath;
    }

    public static String getPublicBasePath() {
        String os = System.getProperty("os.name");
        String bathPath = "";
        if (os.toLowerCase().startsWith("win")) {
            bathPath = Constants.WIN_PUBLIC_FILE_URL;
        } else {
            bathPath = Constants.DISK_PUBLIC_FILE_URL;
        }
        bathPath = bathPath.replace("/", separator);
        return bathPath;
    }

    public static String getImgPath(String imageName) {
        String imagePath = "/uploads/" + imageName.trim();
        return imagePath.replace("/", separator);
    }

    public static String getPublicPath(String imageName) {
        String imagePath = Constants.FILE_PUBLIC_URL + imageName.trim();
        return imagePath.replace("/", separator);
    }

}
