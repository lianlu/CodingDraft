package com.company;

import java.util.*;

public class Main {
    public static void main(String[] args) {
       // int x = new Solution().numFactoredBinaryTrees(new int[]{2, 4, 5, 10});
        //System.out.println(x);
        TreeNode root = new TreeNode(0);
        root.left = new TreeNode(3);
        root.right = new TreeNode(0);

        String x = new Solution().findReplaceString("abcd", new int[]{0,2}, new String[]{"a", "cd"}, new String[]{"eee", "ffff"});
        System.out.println(x);
    }
}

class Solution {
    int mod = 1_000_000_007;
    public String findReplaceString(String S, int[] indexes, String[] sources, String[] targets) {
        Map<Integer, String> mapToReplace = new HashMap<>();
        Map<Integer, String> mapToSource = new HashMap<>();

        for (int i = 0; i < indexes.length; i++) {
            int startIndex = indexes[i];
            String sourceString = sources[i];
            mapToSource.put(startIndex, sourceString);

            int ending = Math.min(startIndex + sourceString.length(), S.length());
            if (sourceString.equals(S.substring(startIndex, ending))) {
                mapToReplace.put(startIndex, targets[i]);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < S.length(); i++) {
            if (mapToReplace.containsKey(i)) {
                sb.append(mapToReplace.get(i));
                i += mapToSource.get(i).length() - 1;
            }
            else {
                sb.append(S.charAt(i));
            }
        }

        return sb.toString();
    }

    public int consecutiveNumbersSum(int N) {
        int res = 1;
        for (int n = 1 ;; n++) {
            int mx = N - n * (n - 1)/2;
            if (mx <= 0) break;

            if (mx % n == 0) res++;
        }

        return res;
    }

    private boolean isPerfectSqure(long num){
        long x = (long)Math.sqrt(num);
        if(Math.pow(x,2) == num)
            return true;

        return false;
    }

    Map<String, Integer> cache = new HashMap<>();
    public int uniquePathsIII(int[][] grid) {
        int h = grid.length;
        int w = grid[0].length;
        int start = 0;
        int end = 0;
        int count = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (grid[i][j] == 1) {
                    start = i * w + j;
                }
                else if (grid[i][j] == 2) {
                    end = i * w + j;
                }
                else if (grid[i][j] == 0){
                    count++;
                }
            }
        }

        int res = dfs(start, end, grid, count);
        return res;
    }
    int []dxy = new int[]{-1, 0, 1, 0, -1};
    private int dfs(int curPos, int end, int[][] grid, int count) {
        if (curPos == end) {
            if (count == 0)
                return 1;
            else
                return 0;
        }
        else {
            String state = getState(curPos, grid);
            if (cache.containsValue(state)) {
                return cache.get(state);
            }
            else {
                int path = 0;
                int h = grid.length;
                int w = grid[0].length;
                int x = curPos/w;
                int y = curPos%w;

                for (int d = 0; d < 4; d++) {
                    int newX = x + dxy[d];
                    int newY = y + dxy[d+1];
                    if (newX < 0 || newX >= h || newY < 0 || newY >= w || grid[newX][newY] == -1 || grid[newX][newY] == 1) continue;

                    if (grid[newX][newY] == 0) {
                        grid[newX][newY] = -1;
                        path += dfs(newX * w + newY, end, grid, count-1);
                        grid[newX][newY] = 0;
                    }
                    else if (grid[newX][newY] == 2){
                        if (count == 0) {
                            path++;
                        }
                    }
                }

                cache.put(state, path);

                return path;
            }
        }
    }

    private String getState(int curPos, int[][] grid) {
        StringBuilder sb = new StringBuilder();
        sb.append(curPos).append(" ");
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                sb.append(grid[i][j]);
            }
        }
        return sb.toString();
    }

    Map<TreeNode, Integer> neededCoin;
    Map<TreeNode, Integer> hasCoin;
    int res = 0;
    public int distributeCoins(TreeNode root) {
        neededCoin = new HashMap<>();
        hasCoin = new HashMap<>();

        fillNeededMap(root);
        fillHashCoinMap(root);
        
        helper(root);
        return res;
    }

    private void helper(TreeNode root) {
        if (root == null) return;
        if (neededCoin.get(root) == hasCoin.get(root)) {
            helper(root.left);
            helper(root.right);

        }
        else {
            int neededRootMove = Math.abs(neededCoin.get(root) - hasCoin.get(root));

            res+=neededRootMove;
            helper(root.left);
            helper(root.right);
        }
    }

    private int fillHashCoinMap(TreeNode root) {
        if (root == null) return 0;
        int cur = root.val;
        int left = root.left == null ? 0 : fillHashCoinMap(root.left);
        int right = root.right == null ? 0 : fillHashCoinMap(root.right);
        hasCoin.put(root, cur + left + right);

        return cur + left + right;
    }

    private int fillNeededMap(TreeNode root) {
        if (root == null) return 0;
        int cur = 1;
        int left = root.left == null ? 0 : fillNeededMap(root.left);
        int right = root.right == null ? 0 : fillNeededMap(root.right);
        neededCoin.put(root, cur + left + right);

        return cur + left + right;
    }

    public int maxTurbulenceSize(int[] A) {
        int best = 1;
        int cur = 1;
        for (int i = 0; i < A.length - 1; i++) {
            if (i == 0) {
                if (A[i] == A[i+1])
                    continue;
                else {
                    cur++;
                }
            }
            else {
                if (A[i] == A[i+1]) {
                    cur = 1;
                }
                else if (A[i] < A[i+1]) {
                    if (A[i] < A[i-1]) {
                        cur++;
                    }
                    else {
                        cur = 2;
                    }
                }
                else if (A[i] > A[i+1]) {
                    if (A[i] > A[i-1]) {
                        cur++;
                    }
                    else {
                        cur = 2;
                    }
                }
            }

            best = Math.max(cur, best);
        }

        return best;
    }

    public int[] sortedSquares(int[] A) {
        for (int i = 0; i < A.length; i++) {
            A[i] = A[i] * A[i];
        }

        Arrays.sort(A);
        return A;
    }

    public List<String> findAndReplacePattern(String[] words, String pattern) {
        List<String> res = new LinkedList<>();
        for (String w : words) {
            if (isBiject(w, pattern)) {
                res.add(w);
            }
        }
        return res;
    }
    private boolean isBiject(String w, String pattern) {
        Map<Character, Character> patternToWord = new HashMap<>();
        for (int i = 0; i < pattern.length(); i++) {
            char pc = pattern.charAt(i);
            char wc = w.charAt(i);
            if (patternToWord.containsKey(pc)) {
                if (patternToWord.get(pc) != wc) return false;
            }
            else if (patternToWord.containsValue(wc)){
                return false;
            }
            else {
                patternToWord.put(pc, wc);
            }
        }

        return true;
    }
    public int numSpecialEquivGroups(String[] A) {
        Set<String> set = new HashSet<>();
        for (String a : A) {
            set.add(encode(a));
        }

        return set.size();
    }
    private String encode(String a) {
        List<Character> even = new LinkedList<>();
        for (int i = 0; i < a.length(); i += 2) {
            even.add(a.charAt(i));
        }

        List<Character> odd = new LinkedList<>();
        for (int i = 1; i < a.length(); i+=2) {
            odd.add(a.charAt(i));
        }

        Collections.sort(even);
        Collections.sort(odd);

        StringBuilder sb = new StringBuilder();
        for (Character e : even) sb.append(e);
        sb.append("-");
        for (Character o : odd) sb.append(o);

        return sb.toString();
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