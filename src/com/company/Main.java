package com.company;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        double s = new Solution().largestSumOfAverages(new int[]{9,1,2,3,9}, 3);
        System.out.println(s);
    }
}

class Solution {
    public double largestSumOfAverages(int[] A, int K) {
        int len = A.length;
        double[][]dp = new double[len][len+1];
        int accSum[] = new int[len];

        for (int i = 0; i < len; i++) {
            if (i == 0) accSum[i] = A[i];
            else accSum[i] = accSum[i-1] + A[i];

            for (int j = 1; j <= len; j++) {
                if (j == 1) {
                    dp[i][j] = accSum[i]*1.0/(i+1);
                }
                else {
                    dp[i][j] = Integer.MIN_VALUE;
                    for (int k = 1; k <= i - j + 2; k++) {
                        if (dp[i-k][j-1] >= 1) {
                            dp[i][j] = Math.max(dp[i][j], dp[i-k][j-1] + (accSum[i] - accSum[i-k])*1.0/k);
                        }
                    }
                }
            }
        }

        return dp[len - 1][K];
    }

    public int scoreOfParentheses(String S) {
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < S.length(); i++) {
            char c = S.charAt(i);
            if (c == '(') {
                stack.push(-1);
            }
            else {
                if (stack.peek() == -1) {
                    stack.pop();
                    if (stack.isEmpty() || stack.peek() == -1) {
                        stack.push(1);
                    }
                    else {
                        stack.push(stack.pop() + 1);
                    }
                }
                else {// is a number
                    int number = stack.pop();
                    stack.pop();
                    int newNumber = number * 2;

                    if (stack.isEmpty() || stack.peek() == -1) {
                        stack.push(newNumber);
                    }
                    else {
                        stack.push(newNumber + stack.pop());
                    }
                }
            }
        }

        return stack.pop();
    }
    public boolean leafSimilar(TreeNode root1, TreeNode root2) {
        List<Integer> leafSeq = dfs(root1);
        List<Integer> leafSeq2 = dfs(root2);
        if (leafSeq.size() != leafSeq2.size()) return false;
        for (int i = 0; i < leafSeq.size(); i++) {
            if (leafSeq.get(i) != leafSeq2.get(i)) {
                return false;
            }
        }

        return true;
    }
    private List<Integer> dfs(TreeNode root) {
        List<Integer> res = new ArrayList<>();

        if (root.left == null && root.right == null) {
            res.add(root.val);
            return res;
        }
        else{
            if (root.left != null) {
                res.addAll(dfs(root.left));
            }

            if (root.right != null) {
                res.addAll(dfs(root.right));
            }
        }

        return res;
    }
    public List<TreeNode> allPossibleFBT(int N) {
        List<TreeNode> res = new ArrayList<>();
        if (N <= 0) return res;
        if(N == 1) {
            res.add(new TreeNode(0));
            return res;
        }

        for (int leftCount = 1; N  - 1 - leftCount > 0; leftCount++) {
            List<TreeNode> leftRes = allPossibleFBT(leftCount);
            List<TreeNode> rightRes = allPossibleFBT(N  - 1 - leftCount);
            for (TreeNode l : leftRes)
                for (TreeNode r : rightRes) {
                    TreeNode root = new TreeNode(0);
                    root.left = l;
                    root.right = r;
                    res.add(root);
                }
        }

        return res;
    }
    Set<Integer> visit = new HashSet<>();
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        int N = rooms.size();
        dfs(0, rooms);
        return visit.size() == N;
    }
    private void dfs(int node, List<List<Integer>> rooms) {
        if (visit.add(node)) {
            for (int nb : rooms.get(node)) {
                dfs(nb, rooms);
            }
        }
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