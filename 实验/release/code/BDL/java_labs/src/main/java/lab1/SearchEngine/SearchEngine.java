package lab1.SearchEngine;

import lab1.Cache.Cache;
import lab1.Cache.LRU_Cache;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static lab1.Cache.CacheLoader.loadCache;

public class SearchEngine {

    public static Configuration configuration;
    public static Connection connection;
    public static Admin admin;
    private static String table_name,cf,cq;
    private static Cache cache = null;
    private static boolean enableCache = false;
    public static void main(String[] args) {
        String hbase_dir = args[0];
        String address = args[1];
        table_name = args[2];
        cf = args[3];
        cq = args[4];
        init(hbase_dir,address);

        String sb = "欢迎使用数据集检索引擎--SearchEngine\n" +
                "你可以使用help指令来查看所有可用的命令\n" +
                "祝您使用愉快!";
        System.out.println(sb);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            while(true){
                System.out.print("SearchEngine>>>");
                String s = br.readLine();
                String[] argv = getArgs(s);
                stateMachine(s,argv);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        close();
    }

    private static void stateMachine(String s,String[] argv) throws Exception {
        if(s.equals("help")){
            helpDetail();
        }else if(s.equals("exit")){
            System.exit(0);
        }else if(s.startsWith("get ")){
            cmd_get(argv);
        }else if(s.startsWith("getWindow ")){
            cmd_getWindow(argv);
        }else if(s.startsWith("getRange ")){
            cmd_getRange(argv);
        }else if(s.startsWith("save ")){
            cmd_save(argv);
        }else if(s.startsWith("saveWindow ")){
            cmd_save_window(argv);
        }else if(s.startsWith("saveRange ")){
            cmd_save_range(argv);
        }else if(s.equals("enableCache")){
            enableCache = true;
            if(cache==null){
                cache = new LRU_Cache(100);
            }
        }else if(s.startsWith("loadCache ")){
            if(argv==null||argv.length==0){
                System.out.println("命令错误");
                return;
            }
            enableCache = true;
            cache = loadCache(argv[0]);
        }else if(s.startsWith("setCapacity ")){
            if(!enableCache){
                System.out.println("你必须先启用cache");
                return;
            }else if(argv==null||argv.length==0){
                System.out.println("命令错误");
                return;
            }
            cache.setCapacity(Integer.parseInt(argv[0]));
        }else if(s.equals("listCache")){
            if(cache==null||!enableCache){
                System.out.println("未启用Cache");
            }else{
                System.out.println(cache.getClass());
            }
        }else if(s.startsWith("execute")){
            cmd_execute_usr_file(argv);
        }
        else{
            System.out.println("不存在这样的指令,请使用help来查看指令集");
        }
    }

    private static String[] getArgs(String cmd){
        String[] s = cmd.split(" ");
        if(s.length<2){
            return null;
        }

        return s[1].split(",");
    }

    private static int[] cmd_get_args(String[] args){
        int[] res = new int[args.length*2];

        for (int i = 0; i < args.length; i+=2) {
            res[i] = Integer.parseInt(args[i]);
            res[i+1] = Integer.parseInt(args[i]);
        }

        return res;
    }

    private static int[] cmd_get_window_args(String[] args){
        int[] arr = new int[6];
        arr[0] = Integer.parseInt(args[0]);
        arr[1] = Integer.parseInt(args[3])+arr[0];
        arr[2] = arr[3] = Integer.parseInt(args[1]);
        arr[4] = arr[5] = Integer.parseInt(args[2]);

        return arr;
    }

    private static int[] cmd_get_range_args(String[] args){
        int[] arr = new int[6];
        int i = 0;
        for (String arg : args) {
            String[] nums = arg.split(":");
            if(nums.length!=2){
                System.out.println("命令错误");
                return null;
            }
            for (String num : nums) {
                arr[i++] = Integer.parseInt(num);
            }
        }

        return arr;
    }

    private static void cmd_get(String[] args) throws IOException {
        if(args==null||args.length!=3){
            System.out.println("命令错误");
            return;
        }

        int[] arr = cmd_get_args(args);
        printList(getRange(arr));
    }

    private static void cmd_getWindow(String[] args) throws IOException {
        if(args==null||args.length!=4){
            System.out.println("命令错误");
            return;
        }

        int[] arr = cmd_get_window_args(args);
        printList(getRange(arr));
    }

    private static void cmd_getRange(String[] args) throws IOException {
        if(args==null||args.length!=3){
            System.out.println("命令错误");
            return;
        }

        int[] arr = cmd_get_range_args(args);
        if(arr==null){
            return;
        }

        printList(getRange(arr));
    }

    private static void printList(List<Double> range){
        StringBuilder sb = new StringBuilder();
        for (Double v : range) {
            sb.append(v).append(" ");
        }
        System.out.println(sb);
    }

    private static void cmd_save(String[] args) throws IOException {
        int[] arr = cmd_get_args(Arrays.copyOf(args, 3));
        saveRange(arr,args[3]);
    }

    private static void cmd_save_window(String[] args) throws IOException {
        int[] arr = cmd_get_window_args(Arrays.copyOf(args,4));
        saveRange(arr,args[4]);
    }

    private static void cmd_save_range(String[] args) throws IOException {
        int[] arr = cmd_get_range_args(Arrays.copyOf(args,3));
        saveRange(arr,args[3]);
    }

    private static void cmd_execute_usr_file(String[] args){
        if(args==null||args.length==0){
            System.out.println("命令错误");
            return;
        }

        String usr_file = args[0];
        try(BufferedReader br = new BufferedReader(new FileReader(usr_file))){
            String s;
            while((s=br.readLine())!=null){
                String[] argv = getArgs(s);
                stateMachine(s,argv);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void helpDetail(){
        String sb = "以下是你可以使用的其他命令\n" +
                "exit:\t退出程序\n" +
                "get \"i,j,k\":\t指定数据在data中的索引i,j,k,返回y值\n" +
                "getWindow \"i,j,k,window_size\":\t指定数据在data中的索引i,j,k,窗口长度为window_size,返回窗口内的所有y值,以及后一个数据点的监督值,窗口是针对第一维度而言的\n" +
                "getRange \"si:ei,sj:ej,sk:ik\":\t指定数据在data中的范围,返回所有数据点的y值\n" +
                "save \"i,j,k,file_path\":\t将data[i,j,k]以拼接的方式存储到指定文件中,换行符分隔\n" +
                "saveWindow \"i,j,k,window_size,file_path\":\t将data[i:i+window_size,j,k]以拼接的方式存储到指定文件中,组内空格分隔,组间换行符分隔\n" +
                "saveRange \"si:ei,sj:ej,sk:ek,file_path\":\t将指定范围内的数据保存到指定文件中,组内空格分隔,组间换行符分隔\n" +
                "enableCache:\t允许使用缓存(默认LRU)\n" +
                "loadCache file_path:\t从指定的文件中加载缓存插件\n" +
                "execute file_path:\t根据用户的脚本执行命令\n";
        System.out.print(sb);
    }

    private static long gen_rk(int i,int j,int k){
        return ((long) i <<32)|((long) j <<16)|k;
    }

    private static Double get(int i,int j,int k) throws IOException {
        long rk = gen_rk(i, j, k);
        if(enableCache){
            assert cache!=null;

            Double val = cache.get(i, j, k);
            if(val!=null){
                return val;
            }else{
                double data = getData(table_name, rk + "", cf, cq);
                cache.put(i,j,k,data);
                return data;
            }
        }else{
            return getData(table_name, rk + "", cf, cq);
        }
    }

    private static List<Double> getRange(int[] indexes) throws IOException {
        ArrayList<Double> res = new ArrayList<>();
        for(int i=indexes[0];i<=indexes[1];i++){
            for(int j=indexes[2];j<=indexes[3];j++){
                for(int k=indexes[4];k<=indexes[5];k++){
                    res.add(get(i,j,k));
                }
            }
        }

        return res;
    }

    private static void saveRange(int[] indexes,String file_path) throws IOException {
        File file = new File(file_path);
        if (!file.exists()) {
            boolean newFile = file.createNewFile();
            assert newFile;
        }

        try(FileWriter fw = new FileWriter(file,true)){
            StringBuilder sb = new StringBuilder();
            for(int i=indexes[0];i<=indexes[1];i++){
                for(int j=indexes[2];j<=indexes[3];j++){
                    for(int k=indexes[4];k<=indexes[5];k++){
                        sb.append(get(i,j,k)).append(" ");
                    }
                }
            }
            sb.append("\n");
            fw.append(sb.toString());
        }

    }


    public static double getData(String tableName,String rowKey,String colFamily, String col)throws  IOException{
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(rowKey.getBytes());
        get.addColumn(colFamily.getBytes(),col.getBytes());
        Result result = table.get(get);
        byte[] value = result.getValue(colFamily.getBytes(), col.getBytes());
        table.close();

        return byteArrayToDouble(value);
    }

    public static double byteArrayToDouble(byte[] bytes) {
        return Double.parseDouble(new String(bytes));
    }

    public static void init(String hbase_dir,String address){
        configuration  = HBaseConfiguration.create();
        configuration.set(hbase_dir,address);
        try{
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
        }catch (IOException e){
            System.out.println("数据库连接失败");
            System.exit(-1);
        }
    }

    public static void close(){
        try{
            if(admin != null){
                admin.close();
            }
            if(null != connection){
                connection.close();
            }
        }catch (IOException e){
            System.out.println("释放与数据库的连接失败");
            System.exit(-1);
        }
    }

}
