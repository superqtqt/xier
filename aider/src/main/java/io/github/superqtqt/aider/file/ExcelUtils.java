package io.github.superqtqt.aider.file;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zhangxiangnan 2021-06-27
 */
@Slf4j
public final class ExcelUtils {
    /**
     * 写出文件到servlet流，下载
     * @param fileName 文件名称
     * @param rows 行，每个list是一行数据，
     * @param response servlet输出流
     */
    public void writeToServlet(String fileName, List<List<String>> rows, HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.getWriter();
        ServletOutputStream out = null;
        try {
            writer.write(rows);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            String encodeFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName + ".xls");
            out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            IoUtil.close(writer);
            IoUtil.close(out);
        }
    }

    /**
     * 写出数据到指定文件
     * @param rows 行，每个list是一行数据，
     * @param destFilePath 目标文件路径
     */
    public void writeDataToFile(List<List<String>> rows, String destFilePath) {
        //通过工具类创建writer
        ExcelWriter writer = null;
        try {
            writer = ExcelUtil.getWriter(destFilePath);
            writer.write(rows);
        } catch (Exception e) {
            IoUtil.close(writer);
        }
    }

    /**
     * 写出大数据到指定文件
     * @param rows 行，每个list是一行数据，
     * @param destFilePath 目标文件路径
     */
    public void writeBigDataToFile(List<List<String>> rows, String destFilePath){
        ExcelWriter writer = null;
        try {
            writer = ExcelUtil.getBigWriter(destFilePath);
            writer.write(rows);
            writer.close();
        } catch (Exception e) {
            log.error("", e);
        } finally {
            IoUtil.close(writer);
        }
    }

    /**
     * 写出大数据到指定文件
     * @param rows 行，每个list是一行数据，
     * @param out 目标流
     */
    public void writeToStream(List<List<String>> rows, OutputStream out) {
        ExcelWriter writer = null;
        try {
            // 通过工具类创建writer，默认创建xls格式
            writer = ExcelUtil.getWriter();
            // 一次性写出内容，使用默认样式，强制输出标题
            writer.write(rows, true);
            //out为OutputStream，需要写出到的目标流
            writer.flush(out);
            // 关闭writer，释放内存
            writer.close();
        } catch (Exception e) {
            log.error("", e);
        } finally {
            IoUtil.close(writer);
            IoUtil.close(out);
        }
    }
}
