package Merge_File_Demo.UploadFiles;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class Upload {
    public static void main(String[] args) {
        // HDFS配置
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000");
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        // 本地文件目录
        String localDirPath = args[0];

        // HDFS目标路径
        String hdfsTargetPath = args[1];

        try {
            // 获取HDFS文件系统对象
            FileSystem fs = FileSystem.get(URI.create(conf.get("fs.defaultFS")), conf, args[2]);

            // 遍历本地目录并上传文件
            File localDir = new File(localDirPath);
            File[] files = localDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // 创建HDFS路径对象
                        Path hdfsPath = new Path(hdfsTargetPath + "/" + file.getName());

                        // 打开本地文件输入流
                        InputStream in = new FileInputStream(file);

                        // 使用Hadoop的API将文件写入HDFS
                        fs.copyFromLocalFile(new Path(file.getAbsolutePath()), hdfsPath);

                        // 关闭输入流
                        IOUtils.closeStream(in);
                        System.out.println("File " + file.getName() + " uploaded to HDFS successfully.");
                    }
                }
            } else {
                System.out.println("No files found in the local directory: " + localDirPath);
            }

            // 关闭文件系统连接
            fs.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}