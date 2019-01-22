package com.company;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        //var x = new Solution().expressiveWords("heeellooo", new String[]{"hello", "hi", "helo"});
        //System.out.println(x);
        Solution test = new Solution();
        var x = test.evaluate("(mult 3 (add 2 3))");
        System.out.println(x);
    }
}

class Solution {
    int mod = 1_000_000_007;

    public int flipgame(int[] fronts, int[] backs) {
        Set<Integer> samePage = new HashSet<>();
        for (int i = 0; i < fronts.length; i++) {
            if (fronts[i] == backs[i]) samePage.add(fronts[i]);
        }

        Arrays.sort(fronts);
        Arrays.sort(backs);
        int min = Integer.MAX_VALUE;
        for (int f : fronts) {
            if (!samePage.contains(f)) {
                min = f;
                break;
            }
        }

        for (int b : backs) {
            if (!samePage.contains(b)) {
                min = Math.min(min, b);
                break;
            }
        }

        return min == Integer.MAX_VALUE ? 0 : min;
    }

    public int snakesAndLadders(int[][] board) {
        int N = board.length;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(1);
        Set<Integer> visit = new HashSet<>();
        visit.add(1);
        int step = 0;
        while(!queue.isEmpty()) {
            int qsize = queue.size();
            step++;
            for (int q = 0; q < qsize; q++) {
                int cur = queue.remove();
                for (int k = 1; k <= 6; k++) {
                    int next = cur + k;
                    if (next == N * N) return step;

                    int[]index = findIndex(next, N);
                    if (board[index[0]][index[1]] != -1) {
                        next = board[index[0]][index[1]];

                    }

                    if (visit.add(next)) {
                        queue.add(next);
                    }
                }
            }
        }

        return step;
    }

    private int[] findIndex(int next, int N) {
        int index = next - 1;
        int fromBotton = index / N;
        int fromLeft = index % N;
        boolean isFromLeft = fromBotton % 2 == 0;
        if (!isFromLeft) {
            fromLeft = N - 1 - fromLeft;
        }
        return new int[]{N - 1 - fromBotton, fromLeft};
    }

    public int maxIncreaseKeepingSkyline(int[][] grid) {
        int h = grid.length;
        int w = grid[0].length;

        int hMax[] = new int[h];
        int wMax[] = new int[w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                hMax[i] = Math.max(hMax[i], grid[i][j]);
                wMax[j] = Math.max(wMax[j], grid[i][j]);
            }
        }

        int res = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int min = Math.min(hMax[i], wMax[j]);
                res += Math.max(0, min - grid[i][j]);
            }
        }

        return res;
    }
    Map<Integer, Integer> parPair = new HashMap<>();
    String let = "let";
    String add = "add";
    String mult = "mult";

    public int evaluate(String expression) {
        Map<String, Integer> localValue = new HashMap<>();
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                stack.push(i);
            }
            else if (expression.charAt(i) == ')'){
                parPair.put(stack.pop(), i);
            }
        }

        return evaluate(expression, 0, expression.length()-1, localValue);
    }
    public int evaluate(String exp, int start, int end, Map<String, Integer> localValue){
        if (exp.charAt(start) == ' ') {
            return evaluate(exp, start + 1, end, localValue);
        }
        else if (exp.charAt(end) == ' ') {
            return evaluate(exp, start, end - 1, localValue);
        }
        else if (exp.charAt(start) == '(') {
            return evaluate(exp, start + 1, end - 1, localValue);
        }
        else if (exp.startsWith(add, start)) {
            int nextIndex = start + 4;
            int breakIndex = findBreakOfParallelExp(exp, nextIndex, end);
            int leftValue = evaluate(exp, nextIndex, breakIndex - 1, localValue);
            int rightValue = evaluate(exp, breakIndex, end, localValue);
            return leftValue + rightValue;
        }
        else if (exp.startsWith(mult, start)) {
            int nextIndex = start + 5;
            int breakIndex = findBreakOfParallelExp(exp, nextIndex, end);
            int leftValue = evaluate(exp, nextIndex, breakIndex -1 , localValue);
            int rightValue = evaluate(exp, breakIndex, end, localValue);
            return leftValue * rightValue;
        }
        else if (exp.startsWith(let, start)){
            int nextIndex = start + 4;
            List<Integer> breakIndex = parseLetBreakIndex(exp, nextIndex, end);
            int lastStart = nextIndex;
            Map<String, Integer> backup = new HashMap<>(localValue);

            for (int i = 0; i + 1 < breakIndex.size(); i += 2) {
                updateLetVariable(exp, lastStart, breakIndex.get(i), breakIndex.get(i + 1), backup);
                lastStart = breakIndex.get(i+1);
            }

            int lastIndex = breakIndex.get(breakIndex.size() - 1);
            int value = evaluate(exp, lastIndex, end, new HashMap<>(backup));

            return value;
        }
        else {
            // no operator.
            // still need to parse out something, for an integer
            boolean hasSym = false;
            for (int i = start; i <= end; i++) {
                if (exp.charAt(i) >= 'a' && exp.charAt(i) <= 'z') {
                    hasSym = true;
                    break;
                }
            }

            if (!hasSym) {
                return Integer.parseInt(exp.substring(start, end + 1).trim());
            } else {
                return localValue.get(exp.substring(start, end + 1).trim());
            }
        }
    }
    public void updateLetVariable(String exp, int start, int middle, int end,Map<String, Integer> localValue) {
        String sym = exp.substring(start, middle).trim();
        int value = evaluate(exp, middle, end - 1, localValue);
        localValue.put(sym.trim(), value);
    }
    public List<Integer> parseLetBreakIndex(String exp, int nextIndex, int end) {
        List<Integer> res = new ArrayList<>();
        int i = nextIndex;
        while(i <= end) {
            char c = exp.charAt(i);
            if (c == ' ') {
                i++;
                res.add(i);
            }
            else if (c == '('){
                i = parPair.get(i) + 1;
            }
            else {
                i++;
            }
        }

        return res;
    }
    public int findBreakOfParallelExp(String exp, int nextIndex, int end) {
        if (exp.charAt(nextIndex) == '(') {
            return parPair.get(nextIndex) + 1;
        }

        int nextSpace = exp.indexOf(" ", nextIndex);
        return nextSpace + 1;
    }
    public int expressiveWords(String S, String[] words) {
        List<Node> sString = parseOutString(S);
        int res = 0;
        for (String w : words) {
            if (w.length() > S.length()) continue;

            List<Node> queryString = parseOutString(w);
            if (canExtended(queryString, sString)) 
                res++;
        }
        return res;
    }
    private boolean canExtended(List<Node> queryString, List<Node> targetString) {
        if (queryString.size() != targetString.size()) return false;
        for (int i = 0; i < queryString.size(); i++) {
            Node query = queryString.get(i);
            Node target = targetString.get(i);
            if (query.c != target.c) return false;
            if (query.count > target.count) return false;
            if (target.count < 3) {
                if (target.count != query.count) return false;
            }
        }

        return true;
    }
    private List<Node> parseOutString(String s) {
        List<Node> res = new ArrayList<>();
        int i = 0;
        while(i < s.length()) {
            int j = i;
            while(j < s.length() && s.charAt(i) == s.charAt(j))
                j++;

            res.add(new Node(s.charAt(i), j - i));

            i = j;
        }

        return res;
    }
    class Node{
        char c;
        int count;
        public Node(char c, int count){
            this.c = c;
            this.count = count;
        }
    }
    public String toGoatLatin(String S) {
        String[]words = S.split(" ");
        StringBuilder sb = new StringBuilder();
        String aSeq = "a";
        for (int i = 0; i < words.length; i++) {
            String cur = words[i];
            char c = cur.charAt(0);
            StringBuilder curWord = new StringBuilder();
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
            || c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
                curWord.append(cur).append("ma");
            }
            else {
                curWord = new StringBuilder(cur.substring(1)).append(c).append("ma");
            }

            curWord.append(aSeq);
            aSeq += "a";
            if (sb.length() == 0) {
                sb.append(curWord);
            }
            else {
                sb.append(" ").append(curWord);
            }
        }

        return sb.toString();
    }
    public int numFriendRequests(int[] ages) {
        int count[] = new int[121];
        for (int a : ages)
            count[a]++;

        int res = 0;
        for (int base = 15; base <= 120; base++) {
            for (int other = base/2 + 7 + 1; other <= base; other++) {
                if (base >= 100 || other <= 100) {
                    if (base == other) {
                        if (count[base] > 1) {
                            res += count[base] * (count[base] - 1);
                        }
                    }
                    else {
                        res += count[base] * count[other];
                    }
                }
            }
        }

        return res;
    }
    public int superEggDrop(int K, int N) {
        int[][]dp = new int[K+1][N+1]; // k eggs, n floors
        for (int n = 1; n <= N; n++) {
            for (int k = 1; k <= K; k++) {
                if (n == 1) {
                    dp[k][n] = 1;
                }
                else if (k == 1) {
                    dp[k][n] = n;
                }
                else {
                    int half = n/2;
                    int lower = n%2==1 ? half : half - 1;
                    int higher = half;
                    int broken = 1 + dp[k-1][lower];
                    int nonbroken = 1 + dp[k][higher];
                    dp[k][n] = Math.max(broken, nonbroken);
                }
            }
        }

        int best = Integer.MAX_VALUE;
        for (int k = 1; k <= K; k++) {
            best = Math.min(dp[k][N], best);
        }

        return best;
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