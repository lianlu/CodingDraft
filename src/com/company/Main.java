package com.company;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int x = new Solution().numFactoredBinaryTrees(new int[]{2, 4, 5, 10});
        System.out.println(x);
    }
}

class Solution {
    int mod = 1_000_000_007;
    public int numFactoredBinaryTrees(int[] A) {
        Arrays.sort(A);
        int []dp = new int[A.length];
        Arrays.fill(dp, 1);

        int sum = 1;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < A.length; i++)
            map.put(A[i], i);

        for (int i = 1; i < A.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (A[i] % A[j] == 0) {
                    int otherValue = A[i]/A[j];
                    if (map.containsKey(otherValue)) {
                        int thirdIndex = map.get(otherValue);

                            dp[i] += dp[j] * dp[thirdIndex] % mod;

                    }
                }
            }

            sum += dp[i];
            sum %= mod;
        }

        return sum % mod;
    }
    public int bestRotation(int[] A) {
        List<int[]> intervals = new ArrayList();
        for (int i = 0; i < A.length; i++) {

            int a = A[i];
            if (a == A.length) continue;

            if (i >= a) {
                intervals.add(new int[]{0, i - a});
            }

            if (a < A.length) {
                if (i + A.length - a < A.length)
                    intervals.add(new int[]{i + 1, i + A.length - a});
                else {
                    intervals.add(new int[]{i + 1, A.length});
                }
            }
        }

        List<int[]> nodes = new ArrayList<>();
        for (int[] interval : intervals) {
            nodes.add(new int[]{interval[0], -1});
            nodes.add(new int[]{interval[0], 1});
        }

        Collections.sort(nodes, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if (o1[0] != o2[0]) return o1[0] - o2[0];
                return o1[1] - o2[1];
            }
        });

        int count = 0;
        int best = 0;
        int bestMove = 0;
        for (int[] node : nodes) {
            if (node[1] == -1) {
                count++;
                if (count > best) {
                    bestMove = node[0];
                    best = count;
                }
            } else {
                count--;
            }
        }

        return bestMove;
    }
}

class Node{
        int x;
        boolean isStart;
        public Node(int x, boolean isStart){
            this.x = x; this.isStart = isStart;
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