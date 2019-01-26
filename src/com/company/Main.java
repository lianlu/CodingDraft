package com.company;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        new Demo();
        new Demo();
        new Demo();
    }
}

class Demo{
    protected void finalize(){

    }

}

class Solution {
    int mod = 1_000_000_007;
    public List<String> ipToCIDR(String ip, int n) {
        long start = 0;
        for (String s : ip.split("\\.")) {
            start = start * 256 + Integer.parseInt(s);
        }

        List<String> res = new ArrayList<>();
        while(n > 0) {
            int num = Math.min((int)(start & -start), Integer.highestOneBit(n));

            res.add(getCIDR(start, num));

            start += num;
            n -= num;
        }

        return res;
    }

    private String getCIDR(long start, int step) {
        int[] ans = new int[4];
        ans[0] = (int) (x & 255); x >>= 8;
        ans[1] = (int) (x & 255); x >>= 8;
        ans[2] = (int) (x & 255); x >>= 8;
        ans[3] = (int) x;
        int len = 33;
        while (step > 0) {
            len --;
            step /= 2;
        }
        return ans[3] + "." + ans[2] + "." + ans[1] + "." + ans[0] + "/" + len;
    }

    Map<Integer, Integer> parenthesisPair;
    public int calculate(String s) {
        s = s.replaceAll(" ", "");
        parenthesisPair = new HashMap<>();
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') stack.push(i);
            else if (c == ')'){
                parenthesisPair.put(stack.pop(), i);
            }
        }

        return (int)evalaute(s, 0, s.length() - 1);
    }

    private long evalaute(String s, int start, int end) {

        if (start > end) return 0;
        //System.out.println("eva" + s.substring(start, end + 1));
        if (s.charAt(start) == '(' && parenthesisPair.get(start) == end) {
            return evalaute(s, start + 1, end - 1);
        }

        boolean isAllDigit = true;
        for (int i = start; i <= end; i++) {
            if (!Character.isDigit(s.charAt(i))){
                isAllDigit = false;
                break;
            }
        }

        if (isAllDigit) {
            return Long.parseLong(s.substring(start, end + 1));
        }

        int lastOperatorIndex = findIndex(s, start, end);
        char op = s.charAt(lastOperatorIndex);
        long left = evalaute(s, start, lastOperatorIndex - 1);
        long right = evalaute(s, lastOperatorIndex + 1, end);

        if (op == '+') {
            return left + right;
        }
        else if (op == '-') {
            return left - right;
        }
        else if (op == '*') {
            return left*right;
        }
        else {
            return left/right;
        }
    }

    private int findIndex(String s, int start, int end) {
        int lastOpIndex = -1;
        int i = start;
        while(i <= end) {
            char c = s.charAt(i);
            if (c == '(') {
                i = parenthesisPair.get(i) + 1;
            }
            else if (c == '+'){
                return i;
            }
            else if (c == '-') {
                lastOpIndex = i;
                i++;
            }
            else if (c == '*' || c == '/') {
                if (lastOpIndex == -1 || s.charAt(lastOpIndex) != '-') lastOpIndex = i;
                i++;
            }
            else {
                i++;
            }
        }

        return lastOpIndex;
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