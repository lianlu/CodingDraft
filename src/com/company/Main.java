package com.company;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        new Sol().intervalIntersection()

    }
}

class Sol{
    Map<Integer, List<TreeNode>> verticalView;
    Map<TreeNode, int[]> nodeToIndex; // (vertical, level)
    public List<List<Integer>> verticalTraversal(TreeNode root) {
        verticalView = new HashMap<>();
        nodeToIndex = new HashMap<>();
        dfs(root, 0, 0);

        List<Integer> keys = new ArrayList(verticalView.keySet());
        Collections.sort(keys);
        List<List<Integer>> res = new ArrayList<>();
        for (int k : keys) {
            Collections.sort(verticalView.get(k), new Comparator<TreeNode>() {
                @Override
                public int compare(TreeNode o1, TreeNode o2) {
                    int[]i1 = nodeToIndex.get(o1);
                    int[]i2 = nodeToIndex.get(o2);
                    if (i1[0] != i2[0]) {
                        return i1[0] - i2[0];
                    }

                    if (i1[1] != i2[1]) {
                        return i1[1] - i2[1];
                    }

                    return o1.val - o2.val;
                }
            });

            List<Integer> cur = new ArrayList<>();
            for (TreeNode node : verticalView.get(k)) {
                cur.add(node.val);
            }
        }

        return res;
    }

    private void dfs(TreeNode root, int vertical, int level) {
        if (root == null){
            return;
        }

        nodeToIndex.put(root, new int[]{vertical, level});
        if (!verticalView.containsKey(vertical)) {
            verticalView.put(vertical, new ArrayList<>());
        }

        verticalView.get(vertical).add(root);

        dfs(root, vertical - 1, level + 1);
        dfs(root, vertical + 1, level + 1);
    }


    public Interval[] intervalIntersection(Interval[] A, Interval[] B) {
        int i = 0;
        int j = 0;
        int alen = A.length;
        int blen = B.length;
        List<Interval> res = new ArrayList<>();
        while(i < alen && j < blen) {
            Interval aInt = A[i];
            Interval bInt = B[j];
            if (aInt.end < bInt.start) {
                i++;
                continue;
            }

            if (bInt.end < aInt.start) {
                j++;
                continue;
            }

            Interval interval = getInterSect(aInt, bInt);
            res.add(interval);

            if(aInt.end > bInt.end) {
                j++;
            }
            else {
                i++;
            }
        }

        Interval[]r = new Interval[res.size()];
        for (int x = 0; x < res.size(); x++) {
            r[i] = res.get(x);
        }

        return r;
    }

    private Interval getInterSect(Interval aInt, Interval bInt) {
        int start = Math.max(aInt.start, bInt.start);
        int end = Math.min(aInt.end, bInt.end);
        return new Interval(start, end);
    }

    public String smallestFromLeaf(TreeNode root) {
        char c = (char)('a' + root.val);
        if (root.left == null && root.right == null) return "" +c;

        String res = "";
        if (root.left != null) {
            String leftRes = smallestFromLeaf(root.left);
            res = leftRes + c;
        }

        if (root.right != null) {
            String rightRes = smallestFromLeaf(root.right);
            String otherRes = rightRes + c;
            if (res.length() == 0) {
                res = otherRes;
            }
            else if (otherRes.compareTo(res) < 0){
                res = otherRes;
            }
        }

        return res;
    }

    public int[] sumEvenAfterQueries(int[] A, int[][] queries) {
        int sum = 0;
        for (int a : A) {
            if (a%2 == 0) {
                sum += a;
            }
        }

        int[]res = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int index = queries[i][1];
            int val = queries[i][0];

            int origin = A[index];
            int newValue = A[index] + val;
            if (origin % 2 == 0) {
                if (newValue%2 == 0) {
                    res[i] = (i == 0 ? sum : res[i-1]) + val;
                }
                else {
                    res[i] = (i == 0 ? sum : res[i-1]) - origin;
                }
            }
            else {// origin is odd
                if (newValue%2 == 0) {
                    res[i] = (i == 0 ? sum : res[i-1]) + newValue;
                }
                else {
                    res[i] = (i == 0 ? sum : res[i-1]);
                }
            }

            A[index] += val;
        }

        return res;
    }
}

  class TreeNode {
     int val;
      TreeNode left;
      TreeNode right;
     TreeNode(int x) { val = x; }
  }

  class Interval {
     int start;
     int end;
     Interval() { start = 0; end = 0; }
     Interval(int s, int e) { start = s; end = e; }
  }