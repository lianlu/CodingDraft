package com.company;

import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.standard.MediaSizeName;
import java.util.*;

import static java.util.Collections.swap;
import static javax.print.attribute.standard.MediaSizeName.A;

public class Main {
    public static void main(String[] args) {
        int x = new Solution().numSubarrayBoundedMax(new int[]{2,1,4,3}, 2, 3);
        System.out.println(x);
    }
}

class Solution {
    Map<TreeNode, Integer> depth;
    public TreeNode subtreeWithAllDeepest(TreeNode root) {
        depth = new HashMap<>();
        findDepth(root);
        return helperFindCommonRoot(root);
    }

    private TreeNode helperFindCommonRoot(TreeNode root) {
        if (root == null) return null;
        int leftDepth = root.left != null ? depth.get(root.left) : 0;
        int rightDepth = root.right != null ? depth.get(root.right) : 0;
        if (leftDepth == 0 && rightDepth == 0) return root;
        if (leftDepth == rightDepth) {
            return root;
        }
        else if (leftDepth > rightDepth) {
            return helperFindCommonRoot(root.left);
        }
        else {
            return helperFindCommonRoot(root.right);
        }
    }

    private int findDepth(TreeNode root) {
        if (root == null) return 0;
        int leftDepth = findDepth(root.left);
        int rightDepth = findDepth(root.right);
        depth.put(root, 1 + Math.max(leftDepth, rightDepth));
        return 1 + Math.max(leftDepth, rightDepth);
    }

    public boolean isNStraightHand(int[] hand, int W) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i : hand)
            map.put(i, map.getOrDefault(i, 0) + 1);

        Set<Integer> set = map.keySet();
        List<Integer> list = new ArrayList<>(set);
        Collections.sort(list);

        for (int i : list) {
            while(map.containsKey(i)) {
                for (int w = 0; w < W; w++) {
                    if (map.containsKey(i + w)) {
                        map.put(i + w, map.get(i+w)-1);
                        if (map.get(i+w) == 0) {
                            map.remove(i+w);
                        }
                    }
                    else {
                        return false;
                    }
                }
            }
        }

        return true;
    }
    int L;
    public int numSubarrayBoundedMax(int[] A, int L, int R) {
        int sum = 0;
        int left = -1;
        this.L = L;
        for (int i = 0; i < A.length; i++){
            if (A[i] > R) {
                sum += getSubRes(A, left, i);
                left = i;
            }
        }

        return sum + getSubRes(A, left, A.length);
    }
    private int getSubRes(int[] A, int left, int right) {
        if (left >= right) return 0;
        int res = totalBasedOnLength(right - left - 1);
        int lastIndex = left;
        for (int i = left + 1; i < right; i++) {
            if (A[i] >= L) {
                res -= totalBasedOnLength(i - lastIndex - 1);
                lastIndex = i;
            }
        }

        return res - totalBasedOnLength(right - lastIndex - 1);
    }
    private int totalBasedOnLength(int len) {
        return (1 + len) * len / 2;
    }
    public double champagneTower(int poured, int query_row, int query_glass) {
        double[][]water = new double[100][100];
        water[0][0] = poured;
        for (int i = 1; i <= query_row; i++) {
            for (int j = 0; j <= i; j++) {
                double leftParent = j>=1 ? water[i-1][j-1] : 0;
                double rightParent = j <= i-1 ? water[i-1][j] : 0;
                water[i][j] = (leftParent >= 1 ?  (leftParent - 1.0)/2.0 : 0 )+ (rightParent >= 1 ? (rightParent - 1.0)/2.0 : 0);
            }
        }

        return Math.min(water[query_row][query_glass], 1.0);
    }
    public int lenLongestFibSubseq(int[] A) {
        Map<Integer, Integer> v2Indexmap = new HashMap<>();
        for (int i = 0; i < A.length; i++){
            v2Indexmap.put(A[i], i);
        }

        int[][]dp = new int[A.length][A.length];
        int max = 0;
        for (int i = 0; i < A.length; i++){
            for (int j = 0; j < i; j++) {
                int otherValue = A[i] - A[j];
                if (v2Indexmap.containsKey(otherValue)) {
                    int k = v2Indexmap.get(otherValue);
                    dp[i][j] = 1 + Math.max(2, dp[j][k]);
                    max = Math.max(max, dp[i][j]);
                }
            }
        }

        return max;
    }
    public int numTilings(int N) {
        int mod = 1_000_000_007;
        int[][]dp = new int[N + 1][2];
        int FULL = 0;
        int HALF = 1;

        for (int i = 1; i <= N; i++) {
            if (i == 1) {
                dp[i][FULL] = 1;
                dp[i][HALF] = 0;
            }
            else if (i == 2) {
                dp[i][FULL] = 2;
                dp[i][HALF] = 2;
            }
            else if (i == 3) {
                dp[i][FULL] = 5;
                dp[i][HALF] = dp[i-2][FULL] * 2 + dp[i-1][HALF];
            }
            else {
                dp[i][HALF] = 2 * dp[i-1][FULL] + dp[i-1][HALF];
                dp[i][FULL] = dp[i-1][FULL] + dp[i-1][HALF] + dp[i-2][FULL];
            }
        }

        return dp[N][FULL];
    }
    public List<Integer> eventualSafeNodes(int[][] graph) {
        List<Integer> res = new ArrayList<>();
        if (graph.length == 0) return res;

        int[]outGoing = new int[graph.length];
        for (int i = 0; i < graph.length; i++) {
            for (int k : graph[i]) {
                if (k != i) {
                    outGoing[i]++;
                }
            }
        }

        Map<Integer, Set<Integer>> incoming = new HashMap<>();
        for (int i = 0; i < graph.length; i++) {
            for (int nb : graph[i]) {
                incoming.putIfAbsent(nb, new HashSet<>());
                if (nb!=i)
                incoming.get(nb).add(i);
            }

        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < outGoing.length; i++) {
            if (outGoing[i] == 0) {
                queue.add(i);
            }
        }

        while(!queue.isEmpty()) {
            int cur = queue.remove();
            res.add(cur);
            for (int nb : incoming.get(cur)) {
                outGoing[nb]--;
                if (outGoing[nb] == 0) {
                    queue.add(nb);
                }
            }
        }

        Collections.sort(res);
        return res;
    }
    public boolean possibleBipartition(int N, int[][] dislikes) {
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int[]dislike : dislikes) {
            map.putIfAbsent(dislike[0], new HashSet<>());
            map.putIfAbsent(dislike[1], new HashSet<>());
            map.get(dislike[0]).add(dislike[1]);
            map.get(dislike[1]).add(dislike[0]);
        }

        int[]visit = new int[N+1];
        for (int i = 1; i <= N; i++) {
            if (!canArrage(i, map, visit))
                return false;
        }

        return true;
    }
    private boolean canArrage(int i, Map<Integer, Set<Integer>> map, int[] visit) {
        if (visit[i] > 0) return true;

        int candidateGroup = 0;
        for (int nb : map.get(i)) {
            if (visit[nb] > 0) {
                if (candidateGroup == 0) {
                    candidateGroup = 2 - visit[nb];
                }
                else if (candidateGroup == visit[nb]) {
                    return false;
                }
            }
        }

        visit[i] = candidateGroup;
        for (int nb : map.get(i)) {
            if (!canArrage(i, map, visit)) {
                return false;
            }
        }

        return true;
    }
    public int[] advantageCount(int[] A, int[] B) {
        Arrays.sort(A);

        int[][]Bdata = new int[B.length][2];
        for (int i = 0; i < B.length; i++) {
            Bdata[i][0] = i;
            Bdata[i][1] = B[i];
        }

        Arrays.sort(Bdata, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if (o1[1] > o2[1]) return 1;
                else if (o1[1] < o2[1]) return -1;
                return 0;
            }
        });

        int i = 0;
        int j = 0;
        List<Integer> toFill = new ArrayList<>();
        int res [] = new int[A.length];
        boolean[]hasFilled = new boolean[A.length];
        while(j < B.length && i < A.length) {
            if (A[i] > Bdata[j][1]) {
                res[Bdata[j][0]] = A[i];
                hasFilled[Bdata[j][0]] = true;
                i++;
                j++;
            }
            else {
                toFill.add(A[i]);
                i++;
            }
        }

        i = 0;

        Iterator<Integer> iter = toFill.iterator();

        for (int x = 0; x < hasFilled.length; x++) {
            if (!hasFilled[x]) {
                res[x] = iter.next();
            }
        }

        return res;
    }
}
class FreqStack {
    class Node{
        int count;
        int value;
        int time;
        public Node(int count, int value, int time){
            this.count = count;
            this.value = value;
            this.time = time;
        }
    }

    int time;
    Map<Integer, Integer> map;
    PriorityQueue<Node> pq;
    public FreqStack() {
        time = 0;

        map = new HashMap<>();
        pq = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.count != o2.count) {
                    return o2.count - o1.count;
                }

                if (o1.time != o2.time) {
                    return o2.time - o1.time;
                }

                return 0; // impossible
            }
        });
    }

    public void push(int x) {
        time++;
        int previousCount = map.getOrDefault(x, 0);
        int newCount = previousCount + 1;
        map.put(x, newCount);
        Node node = new Node(newCount, x, time);
        pq.add(node);
    }

    public int pop() {
        Node node = pq.remove();
        map.put(node.value, map.getOrDefault(node.value, 0) - 1);

        return node.value;
    }
}
class ExamRoom {
    PriorityQueue<int[]> pq;
    int N;
    Map<Integer, int[]> map;
    public ExamRoom(int N) {
        this.N = N;
        map = new HashMap<>();// current seat to left and right bound
        pq = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                int interval1Length = (o1[1] - o1[0] -2 )/2;
                int interval2Length = (o1[1] - o1[0] -2 )/2;
                if ((o1[1] - o1[0] -2 )/2!= (o2[1] - o2[0] -2)/2)
                    return  (o2[1] - o2[0] -2)/2 - (o1[1] - o1[0] -2 )/2;

                return o1[0] - o2[0];
            }
        });

        pq.add(new int[]{-1, N});
    }

    public int seat() {
        int[]cur = pq.remove();
        if (cur[0] == -1) {
            pq.add(new int[]{0, cur[1]});
            map.put(0, new int[]{-1, cur[1]});
            if (map.containsKey(cur[1]))
                map.put(cur[1], new int[]{0, map.get(cur[1])[1]});
            return 0;
        }
        else if (cur[1] == N ) {
            pq.add(new int[]{cur[0], N - 1});
            map.put(N-1, new int[]{cur[0], N});
            map.put(cur[0], new int[]{map.get(cur[0])[0], N - 1});
            return N - 1;
        }
        else {
            int mid = (cur[0] + cur[1])/2;
            pq.add(new int[]{cur[0], mid});
            pq.add(new int[]{mid, cur[1]});

            map.put(mid, new int[]{cur[0], cur[1]});
            map.put(cur[0], new int[]{map.get(cur[0])[0], mid});
            map.put(cur[1], new int[]{mid, map.get(cur[1])[1]});

            return mid;
        }
    }

    public void leave(int p) {
        int[]range = map.get(p);
        pq.add(new int[]{range[0], range[1]});
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
class ListNode {
      int val;
      ListNode next;
     ListNode(int x) { val = x; }
  }