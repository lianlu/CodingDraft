package com.company;

import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        int x = new Solution().countTriplets(new int[]{4,13,10,12});
        System.out.println(x);
    }
}

class TimeMap {
    Map<String, TreeMap<Integer, String>> map;
    /** Initialize your data structure here. */
    public TimeMap() {
        map = new HashMap<>();
    }

    public void set(String key, String value, int timestamp) {
        if (map.containsValue(key)) {
            map.get(key).put(timestamp, value);
        }
        else {
            map.put(key, new TreeMap<>());
            map.get(key).put(timestamp, value);
        }
    }

    public String get(String key, int timestamp) {
        if (!map.containsKey(key)) return "";
        else {
            TreeMap<Integer, String> tree = map.get(key);
            Integer floor = tree.floorKey(timestamp);
            if (floor == null) return "";
            else {
                return tree.get(floor);
            }
        }
    }
}

class Solution{
    public int countTriplets(int[] A) {
        int res = 0;
        int len = A.length;
        int max = 65536;
        int[][]dp = new int[len][max]; // two element AND result j count at most index i
        res += findOne(A);
        res += findTwo(A);

        for (int i = 0; i < len; i++) {
            if (i != 0) {
                for (int j = 0; j < max; j++)
                    dp[i][j] = dp[i-1][j];
            }

            for (int j = 0; j < i; j++) {
                dp[i][A[i] & A[j]]+=2;
            }
        }

        for (int i = 2; i < len; i++) {
            for (int j = 0; j < max; j++) {
                if ((j & A[i]) == 0) {
                    res += 3*dp[i-1][j];
                }
            }
        }

        return res;
    }

    private int findTwo(int[] a) {
        int res = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < i; j++) {
                if ((a[i] & a[j]) == 0)
                    res += 6;
            }
        }

        return res;
    }

    private int findOne(int[] a) {
        int res = 0;
        for (int i : a){
            if (i == 0) res++;
        }

        return res;
    }


    public int mincostTickets(int[] days, int[] costs) {
        int len = days.length;
        int[][]dp = new int[len][31];
        Map<Integer, Integer> dayToIndex = new HashMap<>();
        for (int i = 0; i < days.length; i++) {
            dayToIndex.put(days[i], i);
        }

        for (int i = 0; i < len; i++) {
            Arrays.fill(dp[i], -1);
        }

        dp[0][1] = costs[0];
        dp[0][7] = costs[1];
        dp[0][30] = costs[2];

        for (int i = 1; i < len; i++) {
            int lastIndexMin = Integer.MAX_VALUE;
            for (int j = 1; j <= 30; j++) {
                if (dp[i-1][j] != -1) {
                    lastIndexMin = Math.min(lastIndexMin, dp[i-1][j]);
                }
            }

            for (int j = 1; j <= 30; j++) {
                if (j == 1) dp[i][j] = lastIndexMin + costs[0];
                else if (j == 7) dp[i][j] = lastIndexMin + costs[1];
                else if (j == 30) dp[i][j] = lastIndexMin + costs[2];

                for (int prev = 1; prev <= 29 && days[i] - prev >= 1; prev++) {
                    int ealierDay = days[i] - prev;
                    if (dayToIndex.containsKey(ealierDay)) {
                        int earilerIndex = dayToIndex.get(ealierDay);
                        int needPass = j + prev;
                        if (needPass <= 30 && dp[earilerIndex][needPass] != -1) {
                            if (dp[i][j] == -1) dp[i][j] = dp[earilerIndex][needPass];
                            else {
                                dp[i][j] = Math.min(dp[i][j], dp[earilerIndex][needPass]);
                            }
                        }
                    }
                }

            }
        }

        int best = Integer.MAX_VALUE;
        for (int i = 1; i <= 30; i++) {
            if (dp[len - 1][i] != -1) {
                best = Math.min(best, dp[len - 1][i]);
            }
        }

        return best;
    }



    public String strWithout3a3b(int A, int B) {
        StringBuilder sb = new StringBuilder();
        var x = StringBuilder.class;
        while(A > 0 || B > 0) {
            if (A > B) {
                if (canAppend(sb, 'a')) {
                    sb.append("a");
                    A--;
                }
                else {
                    sb.append("b");
                    B--;
                }
            }
            else if (A < B){
                if (canAppend(sb, 'b')) {
                    sb.append('b');
                    B--;
                }
                else {
                    sb.append('a');
                    A--;
                }
            }
            else { // A==B
                if (sb.length() == 0 || sb.charAt(sb.length() - 1) == 'a') {
                    sb.append('b');
                    B--;
                }
                else {
                    sb.append('a');
                    A--;
                }
            }
        }

        return sb.toString();
    }

    private boolean canAppend(StringBuilder sb, char c) {
        if (sb.length() <= 1) return true;
        char last2 = sb.charAt(sb.length() - 2);
        char last1 = sb.charAt(sb.length() - 1);
        if (last1 != last2) return true;

        if (last1 == c && last2 == c) return false;
        return true;
    }
}


class Demo implements Runnable {
    private String name;
    public Demo(String name) throws InterruptedException {
        //super(name);
        Thread.sleep(10000);
        this.name = name;
    }

    public void show(){
        for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; i++) {
            for (int j = Integer.MIN_VALUE; j < Integer.MAX_VALUE; j++) {}
        }
        for (int x = 0; x < 10; x++)
            System.out.println(Thread.currentThread().getName() + " = " +x);
    }

    public void run(){
        this.show();
    }
}
class RangeModule {
    TreeMap<Integer, Integer> tree;

    public RangeModule() {
        tree = new TreeMap<Integer, Integer>();
    }

    public void addRange(int left, int right) {
        if (left >= right) return;
        int oldL = left;
        int oldR = right;
        Integer lower = tree.floorKey(left);
        Integer higher = tree.floorKey(right);
        if (lower != null || higher != null) {
            if (lower == null || tree.get(lower) < left) {
                right = Math.max(right, tree.get(higher));
            }
            else if (left <= tree.get(lower)){
                left = lower;
                right = Math.max(right, tree.get(higher));
            }
        }

        tree.put(left, right);

        Set<Integer> subMap = new HashSet<>(tree.subMap(oldL, false, oldR, true).keySet());
        tree.keySet().removeAll(subMap);
    }

    public boolean queryRange(int left, int right) {
        if (left >= right) return false;
        Integer lower = tree.floorKey(left);
        if (lower != null) {
            return tree.get(lower) >= right;
        }

        return false;
    }

    public void removeRange(int left, int right) {
        if (left >= right) return;
        Integer lower = tree.floorKey(left);
        Integer higher = tree.floorKey(right);


        if (higher != null && tree.get(higher) > right) {
            tree.put(right, tree.get(higher));
        }
        if (lower != null && tree.get(lower) > left) {
            tree.put(lower, left);
        }

        Map<Integer, Integer> subMap = new HashMap(tree.subMap(left, true, right, false));
        tree.keySet().removeAll(subMap.keySet());
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

    @Override
    public boolean equals(Object obj) {
        Interval other = (Interval)obj;
        return this.start == other.start;
    }

    @Override
    public int hashCode(){
        return this.start;
    }
}
class SegmentTreeNode {
    Interval interval;
    SegmentTreeNode left;
    SegmentTreeNode right;
    public SegmentTreeNode(Interval interval){
        this.interval = interval;
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