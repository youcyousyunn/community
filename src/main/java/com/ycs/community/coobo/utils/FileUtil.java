package com.ycs.community.coobo.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.ycs.community.spring.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class FileUtil extends cn.hutool.core.io.FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 定义GB的计算常量
     */
    private static final int GB = 1024 * 1024 * 1024;
    /**
     * 定义MB的计算常量
     */
    private static final int MB = 1024 * 1024;
    /**
     * 定义KB的计算常量
     */
    private static final int KB = 1024;

    /**
     * 格式化小数
     */
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    /**
     * MultipartFile转File
     * @param multipartFile
     * @return
     */
    public static File toFile(MultipartFile multipartFile) {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix="."+getExtensionName(fileName);
        File file = null;
        try {
            // 用uuid作为文件名，防止生成的临时文件重复
            file = File.createTempFile(IdUtil.simpleUUID(), prefix);
            // MultipartFile to File
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 删除文件
     * @param file
     * @return
     * @throws IORuntimeException
     */
    public static boolean deleteFile(File file) throws IORuntimeException {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                boolean isOk = clean(file);
                if (!isOk) {
                    return false;
                }
            }

            return file.delete();
        } else {
            return true;
        }
    }

    /**
     * 获取文件扩展名
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     * @param filename
     * @return
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 文件大小转换
     * @param size
     * @return
     */
    public static String getSize(long size) {
        String resultSize = "";
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = DF.format(size / (float) GB) + "GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = DF.format(size / (float) MB) + "MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = DF.format(size / (float) KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }

    /**
     * inputStream转File
     * @param ins
     * @param name
     * @return
     * @throws Exception
     */
    public static File inputStreamToFile(InputStream ins, String name) throws Exception {
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + name);
        if (file.exists()) {
            return file;
        }
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
        return file;
    }

    /**
     * 将文件名解析成文件的上传路径
     * @param multipartFile
     * @param filePath
     * @return 上传到服务器的文件名
     */
    public static File upload(MultipartFile multipartFile, String filePath) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssS");
        String name = getFileNameNoEx(multipartFile.getOriginalFilename());
        String suffix = getExtensionName(multipartFile.getOriginalFilename());
        String timestamp = "-" + format.format(date);
        try {
            String fileName = name + timestamp + "." + suffix;
            String path = filePath + fileName;
            File dest = new File(path);
            // 检测目录是否存在
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs(); // 新建目录
            }
            multipartFile.transferTo(dest); // 文件写入
            return dest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * File转Base64
     * @param file
     * @return
     * @throws Exception
     */
    public static String fileToBase64(File file) throws Exception {
        FileInputStream inputFile = new FileInputStream(file);
        String base64 =null;
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        base64=new Base64().encode(buffer);
        String encoded = base64.replaceAll("[\\s*\t\n\r]", "");
        return encoded;
    }

    /**
     * 导入数据到Excel
     * @param list
     * @return
     */
    public static void downloadExcel(List<Map<String, Object>> list, HttpServletResponse response) {
        String tempPath =System.getProperty("java.io.tmpdir") + IdUtil.fastSimpleUUID() + ".xlsx";
        File file = new File(tempPath); // 临时文件
        BigExcelWriter writer= ExcelUtil.getBigWriter(file);
        writer.write(list, true); // 一次性写出内容，使用默认样式，强制输出标题
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=utf-8");
        // test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=file.xlsx");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            logger.error("导出附件信息到Excel失败{}", e);
            throw new BadRequestException("导出附件信息到Excel失败");
        } finally {
            // 终止后删除临时文件
            file.deleteOnExit();
            writer.flush(out, true);
            // 此处记得关闭输出Servlet流
            IoUtil.close(out);
        }
    }

    /**
     * 获取文件类型
     * @param type
     * @return
     */
    public static String getFileType(String type) {
        String documents = "txt doc pdf ppt pps xlsx xls";
        String music = "mp3 wav wma mpa ram ra aac aif m4a";
        String video = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg";
        String image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg";
        if(image.indexOf(type) != -1){
            return "图片";
        } else if(documents.indexOf(type) != -1){
            return "文档";
        } else if(music.indexOf(type) != -1){
            return "音乐";
        } else if(video.indexOf(type) != -1){
            return "视频";
        } else return "其他";
    }

    public static String getFileTypeByMimeType(String type) {
        String mimeType = new MimetypesFileTypeMap().getContentType("." + type);
        return mimeType.split("\\/")[0];
    }

    /**
     * 校验文件大小
     * @param maxSize
     * @param size
     */
    public static boolean isAllowedSize(long maxSize, long size) {
        if (size < (maxSize * 1024 * 1024)) {
            return true;
        }
        return false;
    }
}
