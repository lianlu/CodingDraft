package com.company;

import java.security.spec.DSAGenParameterSpec;
import java.util.*;




public class Main {
    public static void main(String[] args) {
        //System.out.println(s);
    }
}

class Solution {



    Map<TreeNode, Integer> noCover = new HashMap<>();
    Map<TreeNode, Integer> needsCover = new HashMap<>();
    public int minCameraCover(TreeNode root) {
        return needsToCoverRoot(root);
    }
    private int needsToCoverRoot(TreeNode root){
        if (root == null) return 0;
        if (root.left == null && root.right == null) return 1;
        if (needsCover.containsKey(root)) return needsCover.get(root);

        int op1 = 1 + noCoverRoot(root.left) + noCoverRoot(root.right);
        if (root.left != null) {
            int op2 = 1 + noCoverRoot(root.left.left) + noCoverRoot(root.left.right) + needsToCoverRoot(root.right);
            op1 = Math.min(op1, op2);
        }

        if (root.right != null) {
            int op2 = 1 + noCoverRoot(root.right.left) + noCoverRoot(root.right.right) + needsToCoverRoot(root.left);
            op1 = Math.min(op1, op2);
        }

        needsCover.put(root, op1);
        return op1;
    }

    private int noCoverRoot(TreeNode root){
        if (root == null) return 0;
        if (root.left == null && root.right == null) return 0;
        if (noCover.containsKey(root)) return noCover.get(root);

        int op1 = needsToCoverRoot(root.left) + needsToCoverRoot(root.right);
        int op2 = 1 + noCoverRoot(root.left) + noCoverRoot(root.right);

        int res = Math.min(op1, op2);
        noCover.put(root, res);
        return res;
    }

    public String[] spellchecker(String[] wordlist, String[] queries) {
        Set<String> originalList = new HashSet<>();
        for (String w : wordlist)
            originalList.add(w);

        Map<String, String> caseMatches = new HashMap<>();
        for (String w : wordlist) {
            if (!caseMatches.containsKey(w.toLowerCase())) {
                caseMatches.put(w.toLowerCase(), w);
            }
        }

        Map<String, String> vowelMap = new HashMap<>();
        for (String w : wordlist) {
            String regexWord = convertWord(w.toLowerCase());
            if (!vowelMap.containsKey(regexWord)) {
                vowelMap.put(regexWord.toLowerCase(), w);
            }
        }

        String []res = new String[queries.length];
        for (int i = 0; i < queries.length; i++){
            String curWord = queries[i];
            if (originalList.contains(curWord)) res[i] = curWord;
            else if (caseMatches.containsKey(curWord.toLowerCase())) {
                res[i] = caseMatches.get(curWord.toLowerCase());
            }
            else {
                String cw = convertWord(curWord.toLowerCase());
                if (vowelMap.containsKey(cw)) {
                    res[i] = vowelMap.get(cw);
                }
                else {
                    res[i] = "";
                }
            }
        }

        return res;
    }

    private String convertWord(String w) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length(); i++) {
            char c = w.charAt(i);
            if (c == 'a' || c == 'e' || c== 'i' || c == 'o' || c == 'u') {
                sb.append("*");
            }
            else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public int[] numsSameConsecDiff(int N, int K) {
        if (N == 1) {
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        }
        else {
            int[]lastResult = numsSameConsecDiff(N - 1, K);
            List<Integer> res = new LinkedList<>();
            for (int v : lastResult) {
                Integer nextValue = findNextValue(v, K);
                if (nextValue != null) {
                    res.add(nextValue);
                }

                if (K != 0) {
                    Integer prevValue = findPrevValue(v, K);
                    if (prevValue != null && prevValue != nextValue)
                        res.add(prevValue);

                }
            }

            int []r = new int[res.size()];
            int i = 0;
            for (int x : res)
                r[i++] = x;

            return r;
        }
    }

    private Integer findPrevValue(int v, int K) {
        if (v == 0) return null;
        int lastDigit = v%10;
        if (lastDigit - K >= 0 ) {
            return v * 10 + (lastDigit - K);
        }

        return null;
    }

    private Integer findNextValue(int v, int K) {
        if (v == 0) return null;
        int lastDigit = v%10;
        if (lastDigit + K < 10 ) {
            return v * 10 + (lastDigit + K);
        }

        return null;
    }

    public boolean isUnivalTree(TreeNode root) {
        if (root == null) return true;
        if (root.left != null && root.left.val != root.val) return false;
        if (root.right != null && root.right.val != root.val) return false;
        return isUnivalTree(root.left) && isUnivalTree(root.right);
    }

    public int maxChunksToSorted(int[] arr) {
        int[]arr2 = new int[arr.length];
        for (int i = 0; i < arr.length; i++)
            arr2[i] = arr[i];

        Arrays.sort(arr2);

        Map<Integer, Integer> map = new HashMap<>();
        int count = 0;

        for (int i = 0; i < arr2.length; i++) {
            map.put(arr2[i], map.getOrDefault(arr2[i], 0) + 1);
            map.put(arr[i], map.getOrDefault(arr[i], 0) - 1);

            if (map.get(arr[i]) == 0) map.remove(arr[i]);
            if (map.containsKey(arr2[i]) && map.get(arr2[i]) == 0) map.remove(arr2[i]);

            if (map.size() == 0) {
                count++;
                map.clear();
            }
        }

        return count;
    }

    public double minmaxGasDist(int[] stations, int K) {
        double lo = 0;
        Arrays.sort(stations);
        double hi = stations[stations.length - 1];
        double eps = 0.000001;
        while(hi - lo > eps) {
            double mid = (lo + hi)/2;
            if (canFill(stations, K, mid)) {
                hi = mid;
            }
            else {
                lo = mid;
            }
        }

        return lo;
    }

    private boolean canFill(int[] stations, int k, double mid) {
        for (int i = 1; i < stations.length; i++) {
            double diff = stations[i] - stations[i - 1];
            int wanted = (int)(diff/mid);
            k -= wanted;
            if (k < 0) return false;
        }

        return true;
    }

    public String reorganizeString(String S) {
        int[]count = new int[26];
        int[]validPos = new int[26];
        for (char c : S.toCharArray()) {
            count[c-'a']++;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < S.length(); i++) {
            int candidate = findCandidate(count, validPos, i);
            if (candidate == -1) return "";
            sb.append((char)('a' + candidate));
        }

        return sb.toString();
    }

    private int findCandidate(int[] count, int[] validPos, int curIndex) {
        int maxIndex = -1;
        for (int i = 0; i < count.length; i++) {
            if (validPos[i] >= curIndex) {
                if (maxIndex == -1 || count[maxIndex] < count[i]) {
                    maxIndex = i;
                }
            }
        }

        return maxIndex;
    }

    public int minSwapsCouples(int[] row) {
        Map<Integer, Integer> map = new HashMap<>(); // group to index
        DJSet djSet = new DJSet(row.length);

        for (int i = 0; i < row.length; i++) {
            if (map.containsKey(row[i]/2)) {
                djSet.union(map.get(row[i]/2), i);
            }
            else {
                map.put(row[i]/2, i);
            }

            // connect even odd
            if (i%2 == 1) {
                djSet.union(i-1, i);
            }
        }

        Map<Integer, Integer> compSize = new HashMap<>();
        for (int i = 0; i < djSet.root.length; i++) {
            int curRoot = djSet.findRoot(i);
            compSize.put(curRoot, compSize.getOrDefault(curRoot, 0) + 1);
        }

        int swapCount = 0;
        for (int key : compSize.keySet()) {
            swapCount+= (compSize.get(key)/2 - 1);
        }

        return swapCount;
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