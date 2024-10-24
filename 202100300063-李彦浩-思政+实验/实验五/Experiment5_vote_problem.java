import java.util.*;

public class Experiment5_vote_problem {
    private int[][] votes;
    private boolean[] visited;
    private boolean[] onStack;
    private int[] ranking;
    private int invalidVotes;

    public Experiment5_vote_problem(int[][] votes) {
        this.votes = votes;
        visited = new boolean[votes.length];
        onStack = new boolean[votes.length];
        ranking = new int[votes[0].length];
        invalidVotes = 0;
    }

    public boolean hasCycle() {
        for (int i = 0; i < votes.length; i++) {
            if (!visited[i]) {
                if (dfs(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfs(int v) {
        visited[v] = true;
        onStack[v] = true;
        for (int i = 0; i < votes[0].length; i++) {
            int w = votes[v][i] - 1;
            if (!visited[w]) {
                if (dfs(w)) {
                    return true;
                }
            } else if (onStack[w]) {
                return true;
            }
        }
        onStack[v] = false;
        return false;
    }

    public int[] getRanking() {
        if (hasCycle()) {
            removeInvalidVotes();
            ranking = new int[votes[0].length];
        }
        for (int i = 0; i < votes[0].length; i++) {
            Map<Integer, Integer> map = new HashMap<>();
            for (int j = 0; j < votes.length; j++) {
                int vote = votes[j][i];
                if (!map.containsKey(vote)) {
                    map.put(vote, 0);
                }
                map.put(vote, map.get(vote) + 1);
            }
            int maxCount = 0;
            int maxVote = 0;
            for (int vote : map.keySet()) {
                int count = map.get(vote);
                if (count > maxCount) {
                    maxCount = count;
                    maxVote = vote;
                }
            }
            ranking[i] = maxVote;
        }
        return ranking;
    }

    private void removeInvalidVotes() {
        for (int i = 0; i < votes.length; i++) {
            if (isInvalid(votes[i])) {
                invalidVotes++;
                votes[i] = null;
            }
        }
        int[][] validVotes = new int[votes.length - invalidVotes][votes[0].length];
        int index = 0;
        for (int[] vote : votes) {
            if (vote != null) {
                validVotes[index] = vote;
                index++;
            }
        }
        votes = validVotes;
        invalidVotes = 0;
    }

    private boolean isInvalid(int[] vote) {
        int maxIndex = 0;
        int maxVote = vote[0];
        for (int i = 1; i < vote.length; i++) {
            if (vote[i] > maxVote) {
                maxIndex = i;
                maxVote = vote[i];
            }
        }
        for (int i = maxIndex + 1; i < vote.length; i++) {
            if (vote[i] > maxVote) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[][] votes = {{1, 2, 3, 4, 5}, {3, 2, 1, 5, 4}, {2, 1, 3, 4, 5}, {1, 2, 3, 4, 5}, {1, 3, 2, 5, 4}, {1, 5, 4, 2, 3}, {2, 3, 4, 5, 1}, {3, 2, 1, 5, 4}, {5, 4, 3, 2, 1}, {4, 2, 3, 1, 5}};
        Experiment5_vote_problem sorter = new Experiment5_vote_problem(votes);
        int[] ranking = sorter.getRanking();
        System.out.println(Arrays.toString(ranking));
    }
}
