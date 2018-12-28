package com.company;

import java.util.*;


public class Main {
    public static void main(String[] args) {
        //double s = new Solution().openLock(new String[]{"8887","8889","8878","8898","8788","8988","7888","9888"}, "8888");
        //List<List<Interval>> input = new ArrayList<>();
        // List<Interval>
        // System.out.println(s);
        List<String> input = new ArrayList<String>();
        input.add("XYD");
        input.add("YZE");
        input.add("DEA");
        // boolean s = new Solution().pyramidTransition("XYZ", input);
        System.out.println(new Solution().numMatchingSubseq("abcde", new String[]{"a", "bb", "acd", "ace"}));
    }
}

class Solution {
    public int minSwapsCouples(int[] row) {
        return 0;
    }

    public int numMatchingSubseq(String S, String[] words) {
        int[][] index = new int[S.length()][26]; // at index i, what is the next char j

        int[] next = new int[26];
        Arrays.fill(next, -1);
        for (int i = 0; i < 26; i++)
            next[i] = findNext(S, 0, i, next);

        for (int i = 0; i < S.length(); i++) {
            for (int j = 0; j < 26; j++) {
                next[j] = findNext(S, i, j, next);
                //if (next[j] == 100)
                //System.out.println(next[j]);
                index[i][j] = next[j];
            }
        }

        int count = 0;
        for (String w : words) {
            if (isSubstr(index, w)) count++;
        }

        return count;
    }

    private boolean isSubstr(int[][] index, String words) {
        int curIndex = 0;
        for (int i = 0; i < words.length(); i++) {
            if (curIndex >= index.length) return false;

            curIndex = index[curIndex][words.charAt(i) - 'a'];
            if (curIndex == -1) return false;
            else curIndex++;
        }

        return true;
    }

    private int findNext(String S, int startIndex, int targetChar, int[] next) {
        if (next[targetChar] >= startIndex) return next[targetChar];

        for (int i = startIndex; i < S.length(); i++) {
            if (S.charAt(i) - 'a' == targetChar) {
                return i;
            }
        }

        return -1;
    }


    class DJSet {
        int[] root;

        public DJSet(int N) {
            this.root = new int[N];
            for (int i = 0; i < N; i++) this.root[i] = i;
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

