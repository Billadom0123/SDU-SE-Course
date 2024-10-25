package lab1.Cache;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CacheLoader {
    // 自定义类加载器
    static class CustomClassLoader extends URLClassLoader {
        public CustomClassLoader(URL[] urls) {
            super(urls);
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                byte[] data = loadClassData(name);
                return defineClass(name, data, 0, data.length);
            } catch (IOException e) {
                throw new ClassNotFoundException("Class not found: " + name, e);
            }
        }

        private byte[] loadClassData(String name) throws IOException {
            try (InputStream in = getResourceAsStream(name.replace('.', '/') + ".class")) {
                if (in == null) {
                    throw new FileNotFoundException();
                }
                try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    return out.toByteArray();
                }
            }
        }
    }

    public static Cache loadCache(String cacheJavaFilePath) throws Exception {
        // 1. 编译.java文件到.class文件
        compileJavaFile(cacheJavaFilePath);

        // 2. 使用自定义类加载器加载.class文件
        CustomClassLoader classLoader = new CustomClassLoader(new URL[]{new File(cacheJavaFilePath).getParentFile().toURI().toURL()});
        String cacheClassName = getCacheClassName(cacheJavaFilePath);
        System.out.println("载入类:"+cacheClassName);
        Class<?> cacheClass = classLoader.loadClass(cacheClassName);

        // 3. 检查类是否实现了Cache接口
        if (!Cache.class.isAssignableFrom(cacheClass)) {
            throw new RuntimeException("The loaded class does not implement the Cache interface");
        }

        // 4. 实例化类并返回
        return (Cache) cacheClass.getDeclaredConstructor().newInstance();
    }

    private static void compileJavaFile(String javaFilePath) throws IOException, InterruptedException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, javaFilePath);
        if (compilationResult != 0) {
            throw new RuntimeException("Error compiling cache Java file");
        }
    }

    private static String getCacheClassName(String javaFilePath) {
        try {
            // 读取文件内容
            String content = new String(Files.readAllBytes(Paths.get(javaFilePath)));

            // 提取包名（如果有的话）
            Pattern packagePattern = Pattern.compile("^package ([^;]+);", Pattern.MULTILINE);
            Matcher packageMatcher = packagePattern.matcher(content);
            String packageName = "";
            if (packageMatcher.find()) {
                packageName = packageMatcher.group(1);
            }
            System.out.println("解析包名:"+packageName);

            // 提取类名
            Pattern classPattern = Pattern.compile("public class\\s+([A-Z][a-zA-Z0-9_]*)\\s+");
            Matcher classMatcher = classPattern.matcher(content);
            if (classMatcher.find()) {
                String className = classMatcher.group(1);
                System.out.println("解析类名:"+className);
                return (packageName.isEmpty() ? "" : packageName + ".") + className;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
