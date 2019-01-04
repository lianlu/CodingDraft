package com.company;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        //int res = new Solution().rectangleArea(new int[][]{{0,0,1000000000,1000000000}});
        //System.out.println(res);
        int[] step = new Solution().sumOfDistancesInTree(6, new int[][]{{0,1}, {0,2}, {2,3}, {2,4}, {2,5}});
        System.out.println(step);
    }
}

class Solution {

    Map<Integer, Integer> parentMap = new HashMap<>();
    Map<Integer, Set<Integer>> edge = new HashMap<>();
    Map<Integer, Integer> rootCount = new HashMap<>();
    Map<Integer, Integer> rootViewSum = new HashMap<>();
    public int[] sumOfDistancesInTree(int N, int[][] edges) {
        for (int []e : edges) {
            if (!edge.containsKey(e[0])) edge.put(e[0], new HashSet<>());
            edge.get(e[0]).add(e[1]);

            if (!edge.containsKey(e[1])) edge.put(e[1], new HashSet<>());
            edge.get(e[1]).add(e[0]);
        }

        fillRootCount(0, null);
        fillRootView(0, null);

        int[]res = new int[N];
        res[0] = rootViewSum.get(0);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        while(!queue.isEmpty()) {
            int cur = queue.remove();

                for (int nextNode : edge.get(cur)) {
                    if (nextNode != 0 && (parentMap.get(cur) == null || nextNode != parentMap.get(cur))) {
                        queue.add(nextNode);
                        res[nextNode] = getSumValue(nextNode, res);
                    }
                }
        }

        return res;
    }

    private int getSumValue(int nextNode, int[]res) {
        int parentNode = parentMap.get(nextNode);
        int parentSum = res[parentNode];
        int fromRootSum = rootViewSum.get(nextNode);

        return fromRootSum + parentSum - fromRootSum - rootCount.get(nextNode) + res.length - rootCount.get(nextNode);
    }

    private int fillRootView(int root, Integer previous) {
        int sum = 0;
        for (int nb : edge.get(root)) {
            if(previous != null && previous == nb) continue;
            else {
                sum += fillRootView(nb, root) + rootCount.get(nb);
            }
        }

        rootViewSum.put(root, sum);
        return sum;
    }

    private int fillRootCount(int root, Integer previous) {
        int count = 1;

        parentMap.put(root, previous);

        for(int nb : edge.get(root)) {
            if(previous != null && previous == nb) continue;
            else {
                count += fillRootCount(nb, root);
            }
        }

        rootCount.put(root, count);
        return count;
    }


    public boolean splitArraySameAverage(int[] A) {
        int len = A.length;
        int sum = 0;
        for (int a : A) sum += a;

        double target = sum*1.0/len;
        Map<Integer, Set<Integer>> canBuild = new HashMap<>();

        for (int i = 0; i < len; i++) {
            double curAverage = A[i]*1.0;
            if (curAverage == target) return true;

            for (int count = len; count >= 1; count--) {
                if (!canBuild.containsKey(count)) continue;

                for (int value : canBuild.get(count)) {
                    int newSum = value + A[i];
                    if (count+1 < len && target == newSum * 1.0/(count+1))
                        return true;

                    if (!canBuild.containsKey(count+1)) {
                        canBuild.put(count+1, new HashSet<>());
                    }

                    canBuild.get(count+1).add(newSum);
                }
            }

            if (!canBuild.containsKey(1))
                canBuild.put(1, new HashSet<>());

            canBuild.get(1).add(A[i]);
        }

        return false;
    }
    public int shortestSubarray(int[] A, int K) {
        int[] accSum = new int[A.length];
        for (int i = 0; i < A.length; i++)
        {
            if (i == 0) accSum[i] = A[i];
            else accSum[i] = A[i] + accSum[i-1];
        }

        int[]dp = new int[A.length]; // increasing on accSum and i
        int len = 0;
        int bestValue = Integer.MAX_VALUE;
        Map<Integer, Integer> indexMap = new HashMap<>();

        for (int i = 0; i < A.length; i++) {
            if (accSum[i] >= K) {
                bestValue = Math.min(i + 1, bestValue);
            }

            int targetSum = accSum[i] - K;

            int searchIndex = Arrays.binarySearch(dp, 0, len, targetSum);

            if (searchIndex >= 0) {
                bestValue = Math.min(bestValue, i - indexMap.get(searchIndex));
            }
            else {
                searchIndex = - searchIndex - 1;
                if (searchIndex > 0) {
                    bestValue = Math.min(bestValue, i - indexMap.get(searchIndex - 1));
                }
            }

            int curSumIndex = Arrays.binarySearch(dp, 0, len, accSum[i]);

            if (curSumIndex < 0) curSumIndex = -curSumIndex-1;

            dp[curSumIndex] = accSum[i];
            indexMap.put(curSumIndex, i);

            len = curSumIndex + 1;
        }

        return bestValue == Integer.MAX_VALUE ? -1 : bestValue;
    }
    public int racecar(int target) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{0, 1});

        int step = 0;
        Set<String> visit = new HashSet<>();
        visit.add(0 +" "+ 1);
        while(!queue.isEmpty()) {
            step++;
            int qsize = queue.size();
            for (int q = 0; q < qsize; q++) {
                int[] cur = queue.remove();

                // use A
                int nextPos = cur[0] + cur[1];

                if (nextPos <= 2 * target && nextPos >= -2 * target) {
                    if (nextPos == target) return step;
                    int nextSpeed = cur[1] * 2;
                    int[] nextState = new int[]{nextPos, nextSpeed};
                    if (visit.add(enCode(nextState))) {
                        queue.add(nextState);
                    }
                }

                // use R
                nextPos = cur[0];
                if (nextPos <= 2 * target && nextPos >= -2 * target) {
                    int nextSpeed = cur[1] > 0 ? -1 : 1;
                    int []nextState = new int[]{nextPos, nextSpeed};
                    if (visit.add(enCode(nextState))) {
                        queue.add(nextState);
                    }
                }
            }
        }

        return step;
    }
    private String enCode(int[]cur){
        return cur[0] + " " + cur[1];
    }
    public double mincostToHireWorkers(int[] quality, int[] wage, int K) {
        Integer[]index = new Integer[wage.length];

        for (int i = 0; i < index.length; i++)
            index[i] = i;

        Arrays.sort(index, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                double ratio1 = wage[o1]*1.0/quality[o1];
                double ratio2 = wage[o2]*1.0/quality[o2];
                if (ratio1 < ratio2) return -1;
                else return 1;
            }
        });

        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        int curQualitySum = 0;
        for (int i = 0; i < K; i++) {
            curQualitySum += quality[index[i]];
            pq.add(quality[index[i]]);
        }

        double curTotalWage = curQualitySum * wage[index[K-1]] * 1.0 / quality[index[K-1]];

        for (int i = K; i < wage.length; i++) {
            if (pq.peek() > quality[index[i]]) {
                curQualitySum -= pq.remove();
                curQualitySum += quality[index[i]];
                pq.add(quality[index[i]]);

                curTotalWage = Math.min(curTotalWage, curQualitySum * wage[index[i]] * 1.0 / quality[index[i]]);
            }
        }

        return curTotalWage;
    }
    public int shortestPathAllKeys(String[] grid) {
        int h = grid.length;
        int w = grid[0].length();

        int targetKeySet = 0;
        int[]start = new int[]{0, 0};
        char[][] g = new char[h][w];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                g[j][i] = grid[j].charAt(i);
                if (g[j][i] == '@') {
                    start = new int[]{j, i};
                }

                if (g[j][i] >= 'a' && g[j][i] <= 'f') {
                    int curLockValue = (1 << (g[j][i] - 'a'));
                    targetKeySet  |= curLockValue;
                }
            }
        }

        int [][][]dp = new int[h][w][64];
        Queue<int[]> queue = new LinkedList<>();
        Set<Integer> visit = new HashSet<>();
        queue.add(new int[]{start[0], start[1], 0});
        visit.add(enCodeState(queue.peek()));

        int step = 0;
        int[]dxy = new int[]{-1, 0, 1, 0, -1};
        while(!queue.isEmpty()) {
            step++;
            int qsize = queue.size();
            for (int q = 0; q < qsize; q++) {
                int[] curIndex = queue.remove();
                for (int d = 0; d < 4; d++) {
                    int newI = curIndex[0] + dxy[d];
                    int newJ = curIndex[1] + dxy[d+1];

                    if (newI >= 0 && newI < h && newJ >= 0 && newJ < w) {
                        char c = g[newI][newJ];

                        if (c == '#') {
                            continue;
                        }
                        else if (c >= 'A' && c <= 'F') {
                            if (canStepOn(c, curIndex[2]) && visit.add(enCodeState(new int[]{newI, newJ, curIndex[2]}))) {
                                queue.add(new int[]{newI, newJ, curIndex[2]});
                            }
                        }
                        else {
                            int newState = updateLockState(curIndex[2], c);
                            if (newState == targetKeySet) return step;

                            if (visit.add(enCodeState(new int[]{newI, newJ, newState}))) {
                                queue.add(new int[]{newI, newJ, newState});
                            }
                        }
                    }
                }
            }
        }

        return -1;
    }
    private int updateLockState(int lockState, char c) {
        if (c >= 'a' && c <= 'f') {
            int curLockValue = (1 << (c - 'a'));

            return (lockState | curLockValue);
        }
        else {
            return lockState;
        }
    }
    private boolean canStepOn(char c, int lockState) {
        int needValue = (1 << (c - 'A'));

        return (lockState & needValue) != 0;
    }
    private int enCodeState(int[]state){
        return 900 * state[2] + 30 * state[1] + state[0];
    }
    public int numBusesToDestination(int[][] routes, int S, int T) {
        Map<Integer, List<Integer>> stopToBus = new HashMap<>();
        for (int i = 0; i < routes.length; i++) {
            for (int j = 0; j < routes[i].length; j++) {
                if (!stopToBus.containsKey(routes[i][j])) stopToBus.put(routes[i][j], new ArrayList<>());

                stopToBus.get(routes[i][j]).add(i);
            }
        }

        boolean[][]table = new boolean[routes.length][routes.length];
        for (int stop : stopToBus.keySet()) {
            List<Integer> connection = stopToBus.get(stop);
            for (int i = 0; i < connection.size(); i++) {
                for (int j = 0; j < connection.size(); j++) {
                    if (i != j) {
                        table[connection.get(i)][connection.get(j)] = true;
                    }
                }
            }
        }

        Set<Integer> visit = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        for (int bus : stopToBus.get(S)) {
            visit.add(bus);
            queue.add(bus);
        }

        Set<Integer> targetSet = new HashSet<>();
        for (int bus : stopToBus.get(T)) {
            targetSet.add(bus);
        }

        int steps = 0;
        while(!queue.isEmpty()) {
            int qsize = queue.size();
            steps++;
            for (int q = 0; q < qsize; q++) {
                int curBus = queue.remove();
                if (targetSet.contains(curBus)) {
                    return steps;
                }

                for (int nextBus = 0; nextBus < table.length; nextBus++) {
                    if (table[curBus][nextBus]) {
                        if (visit.add(nextBus)) {
                            queue.add(nextBus);
                        }
                    }
                }
            }
        }

        return -1;
    }
    public int minSwap(int[] A, int[] B) {
        int SWAP = 0;
        int NO_SWAP = 1;

        int[][]dp = new int[A.length][2];

        dp[0][SWAP] = 1;
        dp[0][NO_SWAP] = 0;

        int max = A.length;

        for (int i = 1; i < A.length; i++) {
            dp[i][SWAP] = max;
            dp[i][NO_SWAP] = max;

            if (A[i] > A[i-1] && B[i] > B[i-1]) {
                dp[i][SWAP] = Math.min(dp[i][SWAP], 1 + dp[i-1][SWAP]);
                dp[i][NO_SWAP] = Math.min(dp[i][NO_SWAP], dp[i-1][NO_SWAP]);
            }

            if (A[i] > B[i-1] && B[i] > A[i-1]) {
                dp[i][SWAP] = Math.min(dp[i][SWAP], 1 + dp[i-1][NO_SWAP]);
                dp[i][NO_SWAP] = Math.min(dp[i][NO_SWAP], dp[i-1][SWAP]);
            }
        }

        return Math.min(dp[A.length - 1][SWAP], dp[A.length - 1][NO_SWAP]);
    }
    public int[] kthSmallestPrimeFraction(int[] A, int K) {
        PriorityQueue<int[]> pq = new PriorityQueue<int[]>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                double n1 = A[o1[0]]*1.0/A[o1[1]];
                double n2 = A[o2[0]]*1.0/A[o2[1]];
                if (n1 > n2) return 1;
                else if (n1 < n2) return -1;
                return 0;
            }
        });

        for (int i = A.length - 1; i >= 1; i--) {
            pq.add(new int[]{i - 1, i});
        }

        for (int i = 1; i < K; i++){
            int[] current = pq.remove();
            current[0]--;
            if (current[0] > 0) {
                pq.add(current);
            }
        }

        return pq.peek();
    }
    public int kthGrammar(int N, int K) {
        if (N == 1) return 0;
        else if (N == 2) {
            if (K == 1) return 0;
            else return 1;
        }
        else {
            int bit = kthGrammar(N - 1, (K+1)/2);
            if (bit == 1) {
                // next bit will be 10
                if (K % 2 == 0) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
            else {
                // next bit will be 01
                if (K % 2 == 1) {
                    return 0;
                }
                else {
                    return 1;
                }
            }
        }
    }
}

class Interval {
    int start;
    int end;

    Interval() {
        start = 0;
        end = 0;
    }

    Interval(int s, int e) {
        start = s;
        end = e;
    }
}
class TreeNode {
     int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
  }

class DJSet {
    int[] root;
    int[] size;
    public DJSet(int N) {
        this.root = new int[N];
        this.size = new int[N];
        for (int i = 0; i < N; i++) {
            this.root[i] = i;
            size[i] = 1;
        }
    }

    public int findRoot(int i) {
        if (i != root[i]) root[i] = findRoot(root[i]);

        return root[i];
    }

    public void union(int i, int j) {
        int rooti = findRoot(i);
        int rootj = findRoot(j);
        if (rooti == rootj) return;
        root[rootj] = rooti;
        size[rooti] += size[rootj];
    }

    public int size(int i){
        return size[findRoot(i)];
    }

    public Set<Integer> count() {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < root.length; i++)
            set.add(findRoot(i));

        return set;
    }

    public boolean isConnected(int i, int j) {
        return findRoot(i) == findRoot(j);
    }
}