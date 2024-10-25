package com.db_ride_hailing_sys.Util.DepreUtil;

import cn.hutool.json.JSONObject;

import java.util.*;


public class depreAlgorithm {




    // alternative options for DBSCAN algorithm
        class KMeans {
        private int k;
        private List<double[]> centroids;
        private List<double[]> dataset;

        public KMeans(int k, List<double[]> dataset) {
            this.k = k;
            this.dataset = dataset;
            this.centroids = new ArrayList<>();
        }

        public void initializeCentroids() {
            Random rand = new Random();
            for (int i = 0; i < k; i++) {
                centroids.add(dataset.get(rand.nextInt(dataset.size())));
            }
        }

        public double distance(double[] a, double[] b) {
            double sum = 0;
            for (int i = 0; i < a.length; i++) {
                sum += Math.pow((a[i] - b[i]), 2);
            }
            return Math.sqrt(sum);
        }

        public int findClosestCentroid(double[] datapoint) {
            double minDistance = Double.MAX_VALUE;
            int cluster = 0;
            for (int i = 0; i < k; i++) {
                double tempDistance = distance(datapoint, centroids.get(i));
                if (tempDistance < minDistance) {
                    minDistance = tempDistance;
                    cluster = i;
                }
            }
            return cluster;
        }

        public void updateCentroids(List<List<double[]>> clusters) {
            for (int i = 0; i < k; i++) {
                double[] newCentroid = new double[dataset.get(0).length];
                for (List<double[]> cluster : clusters) {
                    for (double[] datapoint : cluster) {
                        for (int j = 0; j < datapoint.length; j++) {
                            newCentroid[j] += datapoint[j];
                        }
                    }
                }
                for (int j = 0; j < newCentroid.length; j++) {
                    newCentroid[j] = newCentroid[j] / clusters.get(i).size();
                }
                centroids.set(i, newCentroid);
            }
        }

        public void run() {
            initializeCentroids();
            boolean isConverged = false;
            while (!isConverged) {
                List<List<double[]>> clusters = new ArrayList<>();
                for (int i = 0; i < k; i++) {
                    clusters.add(new ArrayList<>());
                }
                for (double[] datapoint : dataset) {
                    int clusterId = findClosestCentroid(datapoint);
                    clusters.get(clusterId).add(datapoint);
                }
                List<double[]> oldCentroids = centroids;
                updateCentroids(clusters);
                if (oldCentroids.equals(centroids)) {
                    isConverged = true;
                }
            }
        }

    }

    // was used for path searching
    // replaced by GaoDe api
    static class Floyd{
        /**
         * use Floyd algorithm to judge if there's a path between some points
         * @param num number
         * @param pre edge
         * @param queries points
         * @return if there's a path between some points
         */
        public List<Boolean> checkIfPrerequisite(int num, int[][] pre, int[][] queries) {
            //先建图
            boolean[][] graphic = new boolean[num][num];
            int n = pre.length;
            for(int i = 0; i< num; i++){
                graphic[i][i] = true;
            }
            for(int i=0;i<n;i++){
                graphic[pre[i][0]][pre[i][1]] = true;
            }
            //跑floyd
            boolean[][] flo = graphic.clone();
            for(int k = 0; k< num; k++){
                for(int i = 0; i< num; i++){
                    for(int j = 0; j< num; j++){
                        if(!flo[i][j]){
                            flo[i][j] = flo[i][k] && flo[k][j];
                        }

                    }
                }
            }
            //查表
            List ans = new ArrayList<Boolean>();
            for(int i=0;i<queries.length;i++){
                if(flo[queries[i][0]][queries[i][1]]){
                    ans.add(true);
                }
                else{
                    ans.add(false);
                }
            }
            return ans;
        }
    }


    /**
     * ws example
     */
    private static abstract class WebSocketServer {
        public abstract void onOpen(WebSocket conn, WebSocketServerExample.ClientHandshake handshake);

        public abstract void onClose(WebSocket conn, int code, String reason, boolean remote);

        public abstract void onMessage(WebSocket conn, String message);

        public abstract void onError(WebSocket conn, Exception ex);

        public abstract void onStart();
    }

    private static class WebSocket {
        public String getRemoteSocketAddress() {
            return "";
        }

        public boolean isOpen() {
            return false;
        }

        public void send(String message) {

        }
    }

    static class WebSocketServerExample extends WebSocketServer {

        private Set<WebSocket> connections;

        public WebSocketServerExample(int port) {
//            super(new InetSocketAddress(port));
            this.connections = new HashSet<>();
        }

        @Override
        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            connections.add(conn);
            System.out.println("New client connected: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            connections.remove(conn);
            System.out.println("Client disconnected: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            System.out.println("Received message from client " + conn.getRemoteSocketAddress() + ": " + message);
            broadcast(message);
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            System.err.println("Error occurred on connection " + conn.getRemoteSocketAddress() + ": " + ex);
        }

        @Override
        public void onStart() {
            System.out.println("Server started");
        }

        private void broadcast(String message) {
            for (WebSocket conn : connections) {
                if (conn.isOpen()) {
                    conn.send(message);
                }
            }
        }

        public static void main(String[] args) {
            int port = 8888;
            WebSocketServerExample server = new WebSocketServerExample(port);
            server.start();
            System.out.println("WebSocket server listening on port " + port);
        }

        private void start() {

        }

        private class ClientHandshake {
        }
    }

    /**
     * a deprecated util which was used to solve some ds problems of the trees
     */
    static class TreeUtil{
        public int numFactoredBinaryTrees(int[] arr) {
            Arrays.sort(arr);
            int n = arr.length;
            long[] dp = new long[n];
            long res = 0, mod = 1000000007;
            for (int i = 0; i < n; i++) {
                dp[i] = 1;
                for (int left = 0, right = i - 1; left <= right; left++) {
                    while (right >= left && (long) arr[left] * arr[right] > arr[i]) {
                        right--;
                    }
                    if (right >= left && (long) arr[left] * arr[right] == arr[i]) {
                        if (right != left) {
                            dp[i] = (dp[i] + dp[left] * dp[right] * 2) % mod;
                        } else {
                            dp[i] = (dp[i] + dp[left] * dp[right]) % mod;
                        }
                    }
                }
                res = (res + dp[i]) % mod;
            }
            return (int) res;
        }

        public TreeNode mergeTrees(TreeNode root1, TreeNode root2) {
            return merge(root1,root2);
        }

        public TreeNode merge(TreeNode n1,TreeNode n2){
            if(null==n1&&null==n2){
                return null;
            }
            int value =0;
            value+=null==n1?0:n1.val;
            value+=null==n2?0:n2.val;
            TreeNode nl = merge(null==n1?null:n1.left,null==n2?null:n2.left);
            TreeNode nr = merge(null==n1?null:n1.right,null==n2?null:n2.right);
            TreeNode cur = new TreeNode(value,nl,nr);
            return cur;
        }

        private class TreeNode {
              int val;
              TreeNode left;
              TreeNode right;
              TreeNode() {}
             TreeNode(int val) { this.val = val; }
              TreeNode(int val, TreeNode left, TreeNode right) {
                  this.val = val;
                  this.left = left;
                  this.right = right;
              }
        }
    }

    /**
     * deprecated jsonUtil which has been replaced by hu tool
     */
    static class JSONParser {
        public static void main(String[] args) {
            String jsonString = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
            JSONObject jsonObject = parseJSON(jsonString);
            System.out.println("Name: " + jsonObject.get("name"));
            System.out.println("Age: " + jsonObject.get("age"));
            System.out.println("City: " + jsonObject.get("city"));
        }

        public static JSONObject parseJSON(String jsonString) {
            JSONObject jsonObject = new JSONObject();
            Scanner scanner = new Scanner(jsonString);
            scanner.useDelimiter("\\Z");
            String json = scanner.next();
            int startIndex = 0;
            int endIndex = 0;
            String key = "";
            String value = "";
            while (endIndex < json.length()) {
                startIndex = json.indexOf("{", endIndex);
                if (startIndex == -1) {
                    break;
                }
                endIndex = json.indexOf("}", startIndex);
                String pair = json.substring(startIndex + 1, endIndex);
                int separatorIndex = pair.indexOf(":");
                key = pair.substring(0, separatorIndex);
                value = pair.substring(separatorIndex + 1);
                jsonObject.put(key, value);
            }
            return jsonObject;
        }
    }

    /**
     * class Graph for solving some ds problems of graph
     */
    class Graph {
        int vertices;
        List<List<Node>> adj;

        class Node {
            int node;
            int cost;

            Node(int node, int cost) {
                this.node = node;
                this.cost = cost;
            }
        }

        Graph(int vertices) {
            this.vertices = vertices;
            adj = new ArrayList<List<Node>>();

            for (int i = 0; i < vertices ; i++) {
                List<Node> item = new ArrayList<Node>();
                adj.add(item);
            }
        }

        void addEdge(int src, int dest, int cost) {
            adj.get(src).add(new Node(dest, cost));
            adj.get(dest).add(new Node(src, cost));
        }

        void dijkstra(int src) {
            PriorityQueue<Node> queue = new PriorityQueue<Node>(vertices, new Comparator<Node>() {
                public int compare(Node node1, Node node2) {
                    if (node1.cost < node2.cost)
                        return -1;
                    if (node1.cost > node2.cost)
                        return 1;
                    return 0;
                }
            });

            boolean[] visited = new boolean[vertices];
            int[] dist = new int[vertices];
            for (int i = 0; i < vertices ; i++)
                dist[i] = Integer.MAX_VALUE;
            dist[src] = 0;
            queue.add(new Node(src, 0));

            while (!queue.isEmpty()) {
                int u = queue.remove().node;
                if (visited[u])
                    continue;
                visited[u] = true;
                List<Node> list = adj.get(u);
                for (Node node : list) {
                    int v = node.node;
                    if (!visited[v] && dist[u] != Integer.MAX_VALUE) {
                        int newDist = dist[u] + node.cost;
                        if (newDist < dist[v])
                            dist[v] = newDist;
                        queue.add(new Node(v, dist[v]));
                    }
                }
            }
            printSolution(dist);
        }

        void printSolution(int[] dist) {
            System.out.println("Vertex Distance from Source");
            for (int i = 0; i < vertices; i++)
                System.out.println(i + " \t\t " + dist[i]);
        }
    }


    static class MaxFlow {
        private int V; // 节点数
        private LinkedList<Integer>[] adj; // 邻接表
        private int[] capacity; // 边的容量
        private int[] flow; // 边的流量
        private int[] parent; // BFS使用的父节点数组
        private boolean[] visited; // BFS使用的访问标记数组
        private int maxFlow; // 最大流
        private int src, sink; // 源节点和汇节点

        public MaxFlow(int V, int src, int sink) {
            this.V = V;
            this.src = src;
            this.sink = sink;
            adj = new LinkedList[V];
            capacity = new int[V];
            flow = new int[V];
            parent = new int[V];
            visited = new boolean[V];
            maxFlow = 0;
            for (int i = 0; i < V; i++) {
                adj[i] = new LinkedList<>();
                capacity[i] = 0;
                flow[i] = 0;
                parent[i] = -1;
                visited[i] = false;
            }
        }

        public void addEdge(int u, int v, int c) {
            adj[u].add(v);
            adj[v].add(u);
            capacity[u] += c;
            capacity[v] += c;
        }

        public void fordFulkerson() {
            while (BFS()) {
                int pathFlow = Integer.MAX_VALUE;
                int s = sink;
                while (s != src) {
                    pathFlow = Math.min(pathFlow, capacity[parent[s]] - flow[parent[s]]);
                    s = parent[s];
                }
                maxFlow += pathFlow;
                int v = sink;
                while (v != src) {
                    int u = parent[v];
                    flow[u] += pathFlow;
                    flow[v] -= pathFlow;
                    v = parent[v];
                }
            }
        }

        private boolean BFS() {
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(src);
            visited[src] = true;
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int v : adj[u]) {
                    if (!visited[v] && capacity[v] > flow[v]) {
                        parent[v] = u;
                        visited[v] = true;
                        queue.offer(v);
                        if (v == sink) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public int getMaxFlow() {
            return maxFlow;
        }
    }

    static class StringMatch {
        public static int[] getNextArray(String pattern) {
            int[] next = new int[pattern.length()];
            next[0] = -1;
            int i = 0, j = -1;
            while (i < pattern.length() - 1) {
                if (j == -1 || pattern.charAt(i) == pattern.charAt(j)) {
                    i++;
                    j++;
                    next[i] = j;
                } else {
                    j = next[j];
                }
            }
            return next;
        }

        public static int kmpSearch(String text, String pattern, int[] next) {
            int i = 0, j = 0;
            while (i < text.length() && j < pattern.length()) {
                if (j == -1 || text.charAt(i) == pattern.charAt(j)) {
                    i++;
                    j++;
                } else {
                    j = next[j];
                }
            }
            if (j == pattern.length()) {
                return i - j;
            } else {
                return -1;
            }
        }
    }


    //粒子群算法
    static class PSO {

        static double f(double[] x) {
            double summ = 0;
            for (double e : x) {
                summ = summ + Math.pow(e, 2);//目标函数，这里是寻找最小值
            }
            return summ;

        }

        public static void main(String[] args) {
            // TODO Auto-generated method stub
            int N, D, G, i, j;
            double c1, c2, w, w_max, w_min, Xmax, Xmin, Vmax, Vmin;
            N = 100;
            D = 10;
            G = 5000;
            c1 = 2;
            c2 = 2;
            w = 1.5;
            w_max = 1.5;
            w_min = 0.1;
            Xmax = 20;
            Xmin = -20;
            Vmax = 0.00001;
            Vmin = -0.00001;
            double[][] X = new double[N][D];
            //初始化种群，与对应的速度
            for (i = 1; i <= N; i += 1) {
                for (j = 1; j <= D; j += 1) {
                    X[i - 1][j - 1] = Math.random() * (Xmax - Xmin) + Xmin;
                }
            }

            double[][] V = new double[N][D];
            for (i = 1; i <= N; i += 1) {
                for (j = 1; j <= D; j += 1) {
                    V[i - 1][j - 1] = Math.random() * (Vmax - Vmin) + Vmin;
                }
            }

            double[][] p = new double[N][D];
            p = X;
            double[] p_best = new double[N];
            for (i = 1; i <= N; i += 1) {
                p_best[i - 1] = f(X[i - 1]);
                //计算每一组自变量对应的目标函数值
            }

            double p_best_min = Arrays.stream(p_best).min().getAsDouble();
            int index = 0;
            for (i = 0; i < N; i += 1) {
                if (p_best_min == p_best[i]) {
                    index = i;//确定最优值，对应的索引
                }
            }
            double g_best = p_best_min;
            double[] g = X[index];//根据索引返回最优值所对应的自变量组
            double[] gb = new double[G];
            //粒子群算法开始迭代
            for (i = 0; i < G; i += 1) {
                for (j = 0; j < N; j += 1) {
                    double X0 = f(X[j]);
                    double d_p = X0 - p_best[j];//看看当前第j个与p_best哪个好
                    if (d_p < 0) {     //小于0 ，则说明当前个体X0更优
                        p_best[j] = X0;//将更好的X0替换掉p_best之前的
                        p[j] = X[j];//同时将最好的X0对应自变量提取出来，放到p里面储存
                    }
                    double d_g = p_best[j] - g_best;//看看当前第j个个体最优值与群体最优值哪个好
                    if (d_g < 0) {
                        g_best = p_best[j];
                        //将更好的p_best赋值到g_best,替换之前的，作为新的群体最优值
                        g = p[j];//同时将p_best对应自变量提取出来，放到g里储存
                    }
                    double w_V, p_X, g_X, M_rand1, M_rand2;
                    M_rand1 = Math.random();
                    M_rand2 = Math.random();
                    for (int k = 0; k < D; k += 1) {
                        w_V = w * V[j][k];
                        p_X = p[j][k] - X[j][k];
                        g_X = g[k] - X[j][k];
                        V[j][k] = w_V + c1 * M_rand1 * (p_X) + c2 * M_rand2 * (g_X);
                        X[j][k] = X[j][k] + V[j][k];
                    }
                    //以上为粒子群算法核心公式，

                    //判断是否有值超出了边界，超出则拉回
                    for (int ii = 0; ii < D; ii += 1) {
                        if ((V[j][ii] > Vmax) || (V[j][ii] < Vmin)) {
                            V[j][ii] = Math.random() * (Vmax - Vmin) + Vmin;
                        }
                        if ((X[j][ii] > Xmax) || (X[j][ii] < Xmin)) {
                            X[j][ii] = Math.random() * (Xmax - Xmin) + Xmin;
                        }
                    }
                }
                gb[i] = g_best;//将每次迭代的最优值储存起来

                w = (w_max - w_min) * (G - i) / G + w_min;//随着迭代增加动态调整惯性权重
                //if(i%1000==0) {
                //Vmax=Vmax*10;
                //Vmin=Vmin*10;
                //}
            }

            System.out.println(String.format("%.2f", g_best));
            System.out.println("最小值为" + g_best);

        }
    }

    class Node {
        public int[] sol;  //存放路径
        public int value;  //存放该路径的距离代价

        public Node(int num){
            sol=new int[num];
            value=Integer.MAX_VALUE;
        }
    }

    class SA {
        private int[][] Dist;   //距离矩阵
        private float Temperature;//初始温度
        private int City_num;   //城市数目
        private Node cur_node;  //初始化结点
        private Node best_neighbor; //最优邻结点
        private float a;        //降温系数

        /**
         * 传入初始化参数：
         *      1.距离矩阵
         *      2.初始温度
         * @param dist
         * @param temperature
         */
        public SA(int[][] dist, int temperature,float a) {
            Dist = dist;
            Temperature = temperature;
            this.a=a;
            City_num=dist.length;

        }

        /**
         * 将node2的内容复制到node1中
         * @param node1
         * @param node2
         */
        public void CopyNode(Node node1,Node node2){
            for(int i=0;i<City_num;i++){
                node1.sol[i]=node2.sol[i];
            }
            node1.value=node2.value;
        }

        /**
         * 模拟退火算法，主函数
         * @return
         */
        public Node SA_mian(){
            //先初始化各个参数
            init();

            Random random=new Random(); //用于生成随机数

            //外层循环退出条件，温度小于0.005
            while(Temperature>0.005){
                int step=0; //温度步长，内层循环，每一个温度执行200次计算
                while(step<1000){
                    //随机获取当前结点的邻结点
                    ArrayList<Node> neighbors = builder_neighbors(cur_node);//获取当前结点的邻居结点的集合
                    int index= random.nextInt(neighbors.size());   //随机邻结点在集合中的下标

                    Node neighbor=neighbors.get(index); //获取随机邻结点

                    //判断随机邻结点的距离代价和当前结点的距离代价
                    if(neighbor.value<cur_node.value){
                        //符合，则接受该邻居结点作为当前结点
                        CopyNode(cur_node,neighbor);
                        CopyNode(best_neighbor,neighbor);
                        //   System.out.println(best_neighbor.value);
                    }
                    else{
                        if(Math.pow(Math.E,-(neighbor.value-cur_node.value)/Temperature)>random.nextDouble()){
                            CopyNode(cur_node,neighbor);
                        }
                    }

                    step++;
                }

                Temperature=Temperature*a;
            }

            return best_neighbor;
        }

        /**
         * 生成传入结点的邻结点集合
         * @param node
         * @return
         */
        public ArrayList<Node> builder_neighbors(Node node){
            ArrayList<Node> neighbors=new ArrayList<>();
            Node neighbor;//临时变量存放邻居结点
            int temp;   //临时变量存放城市编号
            for(int i=1;i<City_num-1;i++){
                for (int j=2;j<City_num;j++){
                    neighbor=new Node(City_num);

                    //将当前结点复制给；邻居结点
                    CopyNode(neighbor,node);

                    //交换第i个城市和第j个城市的位置
                    temp=neighbor.sol[i];
                    neighbor.sol[i]=neighbor.sol[j];
                    neighbor.sol[j]=temp;

                    //计算邻居结点的距离代价
                    neighbor.value=evaluate(neighbor);

                    //将该邻居结点加入集合
                    neighbors.add(neighbor);
                }
            }

            return neighbors;
        }

        //初始化结点
        private void init(){
            cur_node=new Node(City_num);
            best_neighbor=new Node(City_num);

            for(int i=0;i<City_num;i++){
                cur_node.sol[i]=i;
            }

            //计算初始化结点的距离代价
            cur_node.value=evaluate(cur_node);

            //将初始化结点作为最优解结点
            CopyNode(best_neighbor,cur_node);
        }

        /**
         * 计算传入结点的距离代价
         * @param node
         */
        public int evaluate(Node node){
            int distance=0;
            for(int i=1;i<City_num;i++){
                distance=distance+Dist[node.sol[i-1]][node.sol[i]];
            }

            distance=distance+Dist[node.sol[City_num-1]][node.sol[0]];

            return distance;
        }

         void main(String[] args) {
            int max=6555;

            String[] cities={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u"};

            int[][] Dist={
                    {max ,22  ,90  ,86  ,133 ,147 ,148 ,228 ,272 ,426 ,490 ,527 ,465 ,63  ,180 ,145 ,371 ,488 ,225 ,102 ,109 },
                    {22  ,max ,112 ,78  ,128 ,169 ,170 ,250 ,286 ,456 ,520 ,557 ,495 ,85  ,229 ,132 ,349 ,466 ,198 ,66  ,87  },
                    {90  ,112 ,max ,92  ,132 ,57  ,162 ,134 ,207 ,340 ,404 ,441 ,379 ,153 ,297 ,202 ,461 ,578 ,274 ,122 ,199 },
                    {86  ,78  ,92  ,max ,25  ,121 ,161 ,240 ,265 ,409 ,420 ,425 ,385 ,158 ,309 ,191 ,317 ,396 ,201 ,43  ,144 },
                    {133 ,128 ,132 ,25  ,max ,162 ,195 ,282 ,307 ,452 ,462 ,467 ,427 ,202 ,354 ,230 ,328 ,407 ,199 ,87  ,193 },
                    {147 ,169 ,57  ,121 ,162 ,max ,90  ,177 ,169 ,343 ,351 ,333 ,317 ,198 ,324 ,265 ,420 ,499 ,303 ,144 ,218 },
                    {148 ,170 ,162 ,161 ,195 ,90  ,max ,87  ,130 ,259 ,284 ,289 ,249 ,188 ,271 ,278 ,452 ,531 ,335 ,180 ,231 },
                    {228 ,250 ,134 ,240 ,282 ,177 ,87  ,max ,219 ,190 ,269 ,286 ,246 ,211 ,251 ,335 ,534 ,613 ,418 ,266 ,288 },
                    {272 ,286 ,207 ,265 ,307 ,169 ,130 ,219 ,max ,221 ,200 ,182 ,165 ,317 ,400 ,407 ,568 ,647 ,452 ,293 ,360 },
                    {426 ,456 ,340 ,409 ,452 ,343 ,259 ,190 ,221 ,max ,135 ,155 ,110 ,398 ,360 ,522 ,708 ,787 ,592 ,436 ,475 },
                    {490 ,520 ,404 ,420 ,462 ,351 ,284 ,269 ,200 ,135 ,max ,49  ,29  ,421 ,439 ,545 ,718 ,797 ,601 ,446 ,498 },
                    {527 ,557 ,441 ,425 ,467 ,333 ,289 ,286 ,182 ,155 ,49  ,max ,46  ,426 ,466 ,550 ,723 ,802 ,606 ,450 ,503 },
                    {465 ,495 ,379 ,385 ,427 ,317 ,249 ,246 ,165 ,110 ,29  ,46  ,max ,387 ,413 ,511 ,684 ,763 ,567 ,412 ,464 },
                    {63  ,85  ,153 ,158 ,202 ,198 ,188 ,211 ,317 ,398 ,421 ,426 ,387 ,max ,168 ,171 ,379 ,463 ,286 ,151 ,123 },
                    {180 ,229 ,297 ,309 ,354 ,324 ,271 ,251 ,400 ,360 ,439 ,466 ,413 ,168 ,max ,323 ,531 ,615 ,436 ,301 ,293 },
                    {145 ,132 ,202 ,191 ,230 ,265 ,278 ,335 ,407 ,522 ,545 ,550 ,511 ,171 ,323 ,max ,244 ,328 ,182 ,148 ,56  },
                    {371 ,349 ,461 ,317 ,328 ,420 ,452 ,534 ,568 ,708 ,718 ,723 ,684 ,379 ,531 ,244 ,max ,96  ,130 ,275 ,267 },
                    {488 ,466 ,578 ,396 ,407 ,499 ,531 ,613 ,647 ,787 ,797 ,802 ,763 ,463 ,615 ,328 ,96  ,max ,208 ,354 ,348 },
                    {225 ,198 ,274 ,201 ,199 ,303 ,335 ,418 ,452 ,592 ,601 ,606 ,567 ,286 ,436 ,182 ,130 ,208 ,max ,158 ,201 },
                    {102 ,66  ,122 ,43  ,87  ,144 ,180 ,266 ,293 ,436 ,446 ,450 ,412 ,151 ,301 ,148 ,275 ,354 ,158 ,max ,104 },
                    {109 ,87  ,199 ,144 ,193 ,218 ,231 ,288 ,360 ,475 ,498 ,503 ,464 ,123 ,293 ,56  ,267 ,348 ,201 ,104 ,max }
            };
            int temperature=10000;
            float a=0.98f;
            SA sa=new SA(Dist,temperature,a);
            Node node = sa.SA_mian();

            for (int i=0;i<Dist.length;i++){
                System.out.print(cities[node.sol[i]]+"\t");
            }

            System.out.println("最优解的距离代价："+node.value);
        }
    }

    static class JudgeWhetherCouldFormTheAllocatedSum {
        //我不想采取类字段的形式将该类封装为一个单纯的工具类，需要为每一个实例创建对象加以判断
        private final int[] A;//给定的正整数集合A
        private final int sum;//给定的和S
        private boolean[][] DPTable;//用来迭代更新的DP2维数组
        private boolean[] solution;

        public JudgeWhetherCouldFormTheAllocatedSum(int[] A, int sum) {
            //先按照升序排列一下
            Arrays.sort(A);
            this.A = A;
            this.sum = sum;
            initDPTable();
            initSolution();
        }

        private void initDPTable() {
            this.DPTable = new boolean[sum + 1][A.length];
            for (boolean[] rows : DPTable) {
                for (boolean grid : rows) {
                    grid = false;
                }
            }
            for (boolean firstRow : DPTable[0]) {
                firstRow = true;
            }
        }

        private void initSolution() {
            this.solution = new boolean[A.length];
            for (boolean b : solution) {
                b = false;
            }
        }

        /**
         * 状态转移方程:
         * T(P,i) = T(P,i-1) || (ai==P) || T(P-ai,i-1) (注意ai范围防止数组越界)
         * ∑是累or的意思
         *
         * @return 是否能得到给定和
         */
        public boolean Judge() {
            //应不断迭代列,i为列,P为行
            for (int i = 1; i <= A.length - 1; i++) { //A.length为(n+1),规定A零索引弃用

                for (int P = 1; P <= sum; P++) {
                    //第一个是前一列能不能形成P，第二个是第i个元素能否形成P，第三个是由当前元素和前一列能够形成的所有数中的某一个组合能否形成P
                    DPTable[P][i] = DPTable[P][i - 1] || (A[i] == P) || JudgeForeColumnCombineWithCurrentNumberForAllocatedSum(P, i);
                    if (P == sum && DPTable[P][i]) {
                        return true;
                    }
                }

            }
            return false;
        }

        /**
         * T(P,i)的形成可能是由当前元素和前一列能够形成的所有数中的某一个组合形成的
         *
         * @param P 给定的和(local)
         * @param i 给定的元素索引
         * @return 能否组成给定和P
         */
        private boolean JudgeForeColumnCombineWithCurrentNumberForAllocatedSum(int P, int i) {
            if (A[i] > P) {
                return false;
            } else if (A[i] == P) {
                return true;
            } else {
                return DPTable[P - A[i]][i - 1];
            }
        }

        public List<Integer> getSolution() {
            generateSolution();
            ArrayList<Integer> result = new ArrayList<>();
            for (int i = 1; i <= A.length - 1; i++) {
                if (solution[i]) {
                    result.add(A[i]);
                }
            }
            return result;
        }

        private void generateSolution() {
            //没解直接返回
            if (!Judge()) {
                return;
            }
            int tempSum = sum;
            //检查DPTable的相邻两列
            for (int i = A.length - 1; i >= 2; i--) {
                //找到第一个达成sum的列
                if (!DPTable[tempSum][i]) {
                    continue;
                }
                //还需要tempSum,结果ai就是tempSum，直接返回了
                if (A[i] == tempSum) {
                    solution[i] = true;
                    return;
                }
                //tempSum是由ai和前i-1项的自然数系数的线性组合组合成的
                if (DPTable[tempSum - A[i]][i - 1]) {
                    solution[i] = true;
                    solution[i - 1] = true;
                    tempSum -= A[i];
                    continue;
                }
                //这一段其实可以不用写，但是为了表意，还是写了，意思就是前i-1个的自然数系数的线性组合已经组成了tempSum
                if (DPTable[tempSum][i - 1]) {
                    solution[i] = false;
                }
            }
        }
    }

    class moveChess {
        private final int[] weights;//零索引弃用
        private int[][] DPTable;
        private int[][] solutionTable;//保存解的二维表
        private final int n;//方阵行数

        public moveChess(int[] weights) {
            this.weights = weights;
            this.n = weights.length - 1;
            initDPTable();
            initSolutionTable();
        }

        private void initDPTable() {
            //其实DP表是个三角矩阵，可以通过一些映射函数缩小申请的内存空间，但我懒得做了，因为渐进复杂度都是n²
            this.DPTable = new int[weights.length][weights.length];
            //一堆棋子没有合并的必要，对角线元素均为0
            for (int i = 1; i <= n; i++) {
                DPTable[i][i] = 0;
            }
        }

        private void initSolutionTable() {
            this.solutionTable = new int[weights.length][weights.length];
        }

        //其实一堆int相加不一定也是int，但这里主要考虑的内容是算法，溢出无所谓，就当是脏数据

        /**
         * 状态转移方程:
         * T(i,j) = min(0≤k≤(j-i)/2) { T(i,i+k) + T(i+k+1,j) + ∑(p start from i,j) weight[p] }
         *
         * @return 最小耗费
         */
        public int calculateMinimalCost() {
            //先两个两个，再三个三个，最后n个n个
            //eg. 1-2,2-3,...,n-1-n; 1-3,2-4,......
            //l为本次迭代的序列长度
            for (int l = 2; l <= n; l++) {

                //i为本次迭代的行号
                for (int i = 1; i <= n - l + 1; i++) {
                    int j = i + l - 1;//j为对应的列号,(i,j)意即从第i堆棋子到第j堆棋子
                    int subSum = sumOfItoJ(i, j);
                    //DP方程计算计算最小消费
                    int min = DPTable[i][i] + DPTable[i + 1][j] + subSum;
                    int minK = i;//记录断点位置
                    for (int k = 0; k <= l - 2; k++) {
                        int cur = DPTable[i][i + k] + DPTable[i + k + 1][j] + subSum;
                        if (cur < min) {
                            min = cur;
                            minK = i + k;
                        }
                    }
                    //迭代二维表
                    DPTable[i][j] = min;
                    //更新解表断点
                    solutionTable[i][j] = minK;
                }

            }
            return DPTable[1][n];//返回T(1,n)的值
        }

        private int sumOfItoJ(int i, int j) {
            int sum = 0;
            for (int index = i; index <= j; index++) {
                sum += weights[index];
            }
            return sum;
        }

        public String getSolution() {
            return traceBackSolution(1, n);
        }

        private String traceBackSolution(int start, int end) {
            if (start == end) {
                return "a" + start;
            } else {
                return "(" +
                        traceBackSolution(start, solutionTable[start][end]) +
                        traceBackSolution(solutionTable[start][end] + 1, end) +
                        ")";
            }
        }
    }

    class GreedyMoveChess {
        private  int[] weights;//零索引弃用
        private int n;//dynamic length
        private String[][] solutionTable;//和合并过程一起迭代

        public GreedyMoveChess(int[] weights) {
            this.weights = weights;
            this.n = weights.length - 1;
            initSolutionTable();
        }

        private void initSolutionTable() {
            this.solutionTable = new String[weights.length][weights.length];
            //为了防空指针我还是全部初始化成空串了，其实不需要的
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    solutionTable[i][j] = "";
                }
            }
            for (int j = 1; j <= n; j++) {
                solutionTable[1][j] = "a" + j;
            }
        }

        public int calculateMinimalCost() {
            int totalCost = 0;
            int row = 1;//记录解表行数
            while (n > 1) { //合并到最后一个值
                totalCost += mergeAdjacentMinimalSum(row);
                row += 1;
            }
            return totalCost;
        }

        /**
         * 合并相邻和最小的两个元素
         *
         * @return 本次合并的开销
         */
        private int mergeAdjacentMinimalSum(int row) {
            if (n <= 1) {
                return weights[1];
            }
            int min = weights[1] + weights[2];
            int formerIndex = 1;
            //找到当前合并的两个元素的前面的索引，并更新最小值
            for (int i = 1; i <= n - 1; i++) {
                int sum = weights[i] + weights[i + 1];
                if (sum < min) {
                    formerIndex = i;
                    min = sum;
                }
            }
            weights[formerIndex] = weights[formerIndex] + weights[formerIndex + 1];//merge
            for (int i = formerIndex + 2; i <= n; i++) {
                weights[i - 1] = weights[i];//update
            }
            //下一行索引
            int nextRow = row + 1;
            //把formerIndex之前的全部顺延挪到下一行
            for (int i = row; i < formerIndex + row - 1; i++) {
                solutionTable[nextRow][i + 1] = solutionTable[row][i];
            }
            //formerIndex和formerIndex+1合并到下一行的formerIndex+1处
            solutionTable[nextRow][formerIndex + 1 + row - 1] = "(" + solutionTable[row][formerIndex + row - 1] + solutionTable[row][formerIndex + 1 + row - 1] + ")";
            //formerIndex+1之后的直接挪到下一行
            for (int i = formerIndex + 1 + 1 + row - 1; i <= weights.length - 1; i++) {
                solutionTable[nextRow][i] = solutionTable[row][i];
            }
            n -= 1;
            return weights[formerIndex];
        }

        public String getSolution() {
            return solutionTable[weights.length - 1][weights.length - 1];
        }
    }

    class MaximumSubarray {
        public  int[] linearTimeFind(int[] arr) {
            //maxStart\End记录最大子数组的开始结束索引
            int maxStart = 0;
            int maxEnd = 0;
            //curStart\End记录当前子数组的开始结束索引
            int curStart = 0;
            int curEnd = 0;
            //CurSum记录当前最大子数组和，maxSum记录最大子数组和,sum记录当前子数组和
            int CurSum = 0;
            int maxSum = 0;
            int sum = 0;
            for (int i = 0; i < arr.length; i++) {
                sum += arr[i];
                //如果sum入不敷出，那么前方所有的位置都应该被舍弃，因为这个连续的串可以看作一个负数
                if (sum <= 0) {
                    //前方弃用，更新最大子数组和以及其开始结束索引
                    if (maxSum < CurSum) {
                        maxSum = CurSum;
                        maxStart = curStart;
                        maxEnd = curEnd;
                    }
                    //重置sum与CurSum，准备重新记录一个新的最大子数组
                    sum = 0;
                    CurSum = 0;
                    //重置curStart与curEnd
                    if (i + 1 < arr.length) {
                        curStart = i + 1;
                        curEnd = i + 1;
                    }
                }
                //和增加了，就把当前尾指针移动到当前索引上
                if (CurSum + arr[i] > CurSum && sum > CurSum) {
                    curEnd = i;
                    //sum记录了连续的和
                    CurSum = sum;
                }
                if (i == arr.length - 1) {
                    if (CurSum > maxSum) {
                        maxStart = curStart;
                        maxEnd = curEnd;
                    }
                }
            }
            return new int[]{maxStart, maxEnd};
        }
    }

}
