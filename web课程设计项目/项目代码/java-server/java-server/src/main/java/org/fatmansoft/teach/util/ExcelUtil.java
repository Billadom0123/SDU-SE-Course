package org.fatmansoft.teach.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.fatmansoft.teach.models.CourseSheet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ExcelUtil {
    public static void writeExcel(HttpServletResponse response, List<CourseSheet> list) throws IOException {
        //response 返回对象 list excel表中对象
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
        //定义工作表对象
        WriteSheet sheet = EasyExcel.writerSheet(0,"sheet").head(CourseSheet.class).build();
        //内容对象写入excel
        excelWriter.write(list,sheet);
        //关闭输出流
        excelWriter.finish();
    }

}
