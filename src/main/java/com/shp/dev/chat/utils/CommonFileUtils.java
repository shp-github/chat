package com.shp.dev.chat.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * 通用文件操作工具类
 */
@Slf4j
public class CommonFileUtils {

    /**
     * 保存文件
     * @param object 源文件： 本地文件路径、 本地文件、 在线文件地址、 base64字符串 （上传在线图片和base64字符串时需要传文件名，目的是获取文件后缀，因为其没有后缀）
     * @param fileName 文件名称
     * @param frist 路径开头
     * @param last 路径结尾
     * @return 返回文件所在地址 默认会创建年/月/日作为中间的文件夹
     */
    public static String saveFile(Object object, String fileName, String frist, String last) {
        fileName = isNull(fileName) ? UUID.randomUUID().toString().replace("-", "") : fileName;
        return saveFile(object, createDirectory(frist, last) + fileName);
    }

    /**
     * 创建目录
     */
    public static String createDirectory(String frist, String last) {
        String path = "";
        if (noNull(frist)) {
            path += frist;
        }
        path += "/" + new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + "/";
        if (noNull(last)) {
            path += last + "/";
        }
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            boolean mkdirs = dirFile.mkdirs();
            log.info("创建目录：{}",mkdirs);
        }
        return path;
    }


    /**
     * 保存文件到本地
     */
    public static String saveFile(Object object, String outFile) {
        try {
            //如果是输入是字符串
            if (object instanceof String) {
                String str = (String) object;
                File file = new File(str);
                String suffix = getSuffix(object);
                outFile += str.contains(suffix) ? "" : suffix;
                //本地有此文件
                if (file.exists()) {
                    return writeByFile(file, outFile);
                } else {
                    //保存在线图片
                    if (writeUrl(str, outFile) != null) {
                        return outFile;
                    } else {
                        //转base64
                        return writeByBase64(str, outFile);
                    }
                }
            } else if (object instanceof File) {
                File file = (File) object;
                String suffix = getSuffix(object);
                outFile += file.getName().contains(suffix) ? "" : suffix;
                return writeByFile(file, outFile);
            } else if (object instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) object;
                String suffix = getSuffix(object);
                outFile += file.getName().contains(suffix) ? "" : suffix;
                return writeFileByMultipartFile(file, outFile);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        log.error("没有匹配保存文件的类型");
        return null;
    }

    public static String getSuffix(Object object) {
        try {
            if (object instanceof File) {
                File file = (File) object;
                return file.getName().substring(file.getName().indexOf("."));
            } else if (object instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) object;
                return Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().indexOf("."));
            } else if (object instanceof String) {
                File file = new File((String) object);
                if(file.isFile()){
                    return file.getName().substring(file.getName().indexOf("."));
                }
                return "";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
        return "";
    }

    /**
     * 写文件到本地
     */
    public static String writeFileByMultipartFile(MultipartFile file, String outFile) {
        try {
            if (isNull(file, outFile)) {
                log.error("参数为空或者文件不存在");
                return null;
            }
            InputStream is = file.getInputStream();
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = Files.newOutputStream(Paths.get(outFile));
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            is.close();
            return outFile;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 写文件到本地
     */
    public static String writeByBase64(String file, String outFile) {
        try {
            if (isNull(file, outFile)) {
                log.error("参数为空或者文件不存在");
                return null;
            }
            BASE64Decoder decoder = new BASE64Decoder();
            OutputStream out = Files.newOutputStream(Paths.get(outFile));
            byte[] b = decoder.decodeBuffer(file);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
            out.close();
            return outFile;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 写文件到本地
     */
    public static String writeUrl(String file, String outFile) {
        try {
            if (isNull(file, outFile)) {
                log.error("参数为空或者文件不存在");
                return null;
            }
            URL url = new URL(file);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            byte[] bs = new byte[1024];
            int len;
            FileOutputStream os = new FileOutputStream(outFile);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            is.close();
            return outFile;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 写文件到本地
     */
    public static String writeByFile(File file, String outFile) {
        try {
            if (isNull(file, outFile)) {
                log.error("参数为空或者文件不存在");
                return null;
            }
            FileInputStream fis = new FileInputStream(file);
            writeClose(outFile, fis);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return outFile;
    }

    private static void writeClose(String outFile, FileInputStream fis) throws IOException {
        FileOutputStream fos = new FileOutputStream(outFile);
        int len;
        byte[] b = new byte[1024];
        while ((len = fis.read(b)) != -1) {
            fos.write(b, 0, len);
        }
        fos.close();
        fis.close();
    }


    /**
     * 如果有一个为空则返回true
     */
    public static boolean isNull(Object... objects) {
        if (objects == null || objects[0] == null) {
            return true;
        }
        for (Object object : objects) {
            String name = object.getClass().getName();
            log.info("比较的类型为{}", name);
            if ("java.io.File".equalsIgnoreCase(name)) {
                File file = (File) object;
                return !file.isFile();
            }
            if ("org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile".equalsIgnoreCase(name)) {
                MultipartFile multipartFile = (MultipartFile) object;
                return multipartFile.isEmpty();
            }
            return "".equals(object);
        }
        return false;
    }


    /**
     * 都不为空则返回true
     */
    public static boolean noNull(Object... objects) {

        if (objects == null || objects[0] == null) {
            return false;
        }

        for (Object object : objects) {
            String name = object.getClass().getName();
            log.info("比较的类型为{}", name);
            if ("java.io.File".equalsIgnoreCase(name)) {
                File file = (File) object;
                return !file.isFile();
            }
            if ("org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile".equalsIgnoreCase(name)) {
                MultipartFile multipartFile = (MultipartFile) object;
                return !multipartFile.isEmpty();
            }
            return true;
        }
        return false;
    }


}
