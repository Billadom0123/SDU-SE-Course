import java.util.*;

public class InformationDiffusionSimulation {

    public static void main(String[] args) {
        // TODO: 根据需要修改网络结构、初始节点集合和门槛值
        int[][] graph = {{0, 1, 1, 0, 0, 0},
                {1, 0, 1, 1, 0, 0},
                {1, 1, 0, 1, 1, 0},
                {0, 1, 1, 0, 1, 1},
                {0, 0, 1, 1, 0, 1},
                {0, 0, 0, 1, 1, 0}};
        int[] initialNodes = {0, 1};
        int threshold = 3;

        // 模拟信息扩散过程
        Map<Integer, Integer> spreadTimeMap = new HashMap<>(); // 记录各个节点的延时
        Queue<Integer> queue = new LinkedList<>();
        for (int node : initialNodes) {
            spreadTimeMap.put(node, 0);
            queue.offer(node);
        }
        while (!queue.isEmpty()) {
            int currNode = queue.poll();
            for (int i = 0; i < graph[currNode].length; i++) {
                if (graph[currNode][i] == 1 && !spreadTimeMap.containsKey(i)) {
                    spreadTimeMap.put(i, spreadTimeMap.get(currNode) + 1);
                    if (spreadTimeMap.get(i) <= threshold) {
                        queue.offer(i);
                    }
                }
            }
        }

        // 计算平均延时
        double totalSpreadTime = 0;
        for (int time : spreadTimeMap.values()) {
            totalSpreadTime += time;
        }
        double avgSpreadTime = totalSpreadTime / spreadTimeMap.size();

        // 输出结果
        System.out.println("节点延时：");
        for (int node : spreadTimeMap.keySet()) {
            System.out.println("节点" + node + "： " + spreadTimeMap.get(node));
        }
        System.out.println("平均延时： " + avgSpreadTime);
    }
}
