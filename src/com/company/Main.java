package com.company;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int x = new Solution().kSimilarity("cdebcdeadedaaaebfbcf", "baaddacfedebefdabecc");
        System.out.println(x);
    }
}

class Solution {
    public int kSimilarity(String A, String B) {
        Queue<String> queue = new LinkedList<>();
        int step = 0;
        if (A.equals(B)) return 0;

        Set<String> visit = new HashSet<>();
        visit.add(A);
        queue.add(A);

        while(!queue.isEmpty()) {
            step++;
            int qsize = queue.size();
            for (int q = 0; q < qsize; q++) {
                String cur = queue.remove();
                List<String> nb = findNb(cur, B);
                for (String next : nb) {
                    if (visit.add(next)) {
                        queue.add(next);
                        if (next.equals(B)) {
                            return step;
                        }
                    }
                }
            }
        }

        return 0;
    }

    private List<String> findNb(String cur, String target) {
        List<String> nb = new ArrayList<>();
        for (int i = 0; i < cur.length(); i++){
            if (cur.charAt(i) != target.charAt(i)) {
                for (int j = 0; j < target.length(); j++) {
                    if (target.charAt(j) == cur.charAt(i) && target.charAt(i) == cur.charAt(j)) {
                        nb.clear();
                        nb.add(swap(cur, i, j));

                        return nb;
                    }
                }
            }
        }

        for (int i = 0; i < cur.length(); i++){
            if (cur.charAt(i) != target.charAt(i)) {
                boolean[]used = new boolean[6];

                for (int j = 0; j < target.length(); j++) {
                    if (cur.charAt(j) != target.charAt(j) && cur.charAt(i) != target.charAt(j) && !used[cur.charAt(j) - 'a']) {
                        used[cur.charAt(j) - 'a'] = true;
                        nb.add(swap(cur, i, j));
                    }
                }
            }
        }

        return nb;
    }

    private String swap(String s, int i, int j) {
        char[]array = s.toCharArray();
        char c = array[i];
        array[i] = array[j];
        array[j] = c;
        return new String(array);
    }

    public int numSimilarGroups(String[] A) {
        Set<String> set = new HashSet<>();
        for (String a : A) set.add(a);
        A = new String[set.size()];
        int ii = 0;
        for (String ss : set) {
            A[ii++] = ss;
        }

        DJSet djSet = new DJSet(A.length);
        for (int i = 0; i < A.length; i++) {
            for (int j = i + 1; j < A.length; j++) {
                if (isNearBy(A[i], A[j])) {
                    djSet.union(i, j);
                }
            }
        }

        return djSet.count().size();
    }
    private boolean isNearBy(String s, String t) {
        int diffCount = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != t.charAt(i))
                diffCount++;
        }

        return diffCount == 2;
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