package com.company;

import javax.print.attribute.standard.MediaSizeName;
import java.util.*;

import static java.util.Collections.swap;
import static javax.print.attribute.standard.MediaSizeName.A;

public class Main {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        new Solution().shortestBridge(new int[][]{{0,1}, {1, 0}});
       // pancakeSort
    }
}

class Solution {
    public int largestIsland(int[][] grid) {
        int h = grid.length;
        int w = grid[0].length;

        DJSet djSet = new DJSet(h*w);

        int dxy[] = new int[]{-1, 0, 1, 0, -1};
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (grid[i][j] == 1) {
                    for (int d = 1; d <= 2; d++) {
                        int newI = i + dxy[d];
                        int newJ = j + dxy[d+1];
                        if (newI >= 0 && newI < h && newJ >= 0 && newJ < w && grid[newI][newJ] == 1) {
                            djSet.union(i*w + j, newI * w + newJ);
                        }
                    }
                }
            }
        }

        int best = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (grid[i][j] == 0) {
                    int sum = 1;
                    Set<Integer> set = new HashSet<>();
                    for (int d = 0; d < 4; d++) {
                        int newI = i + dxy[d];
                        int newJ = j + dxy[d+1];
                        if (newI >= 0 && newI < h && newJ >= 0 && newJ < w && grid[newI][newJ] == 1) {
                            int root = djSet.findRoot(newI * w + newJ);
                            if (set.add(root)) {
                                sum += djSet.size(root);
                            }
                        }
                    }

                    best = Math.max(sum, best);
                }
            }
        }

        return best;
    }
    int[]dxy = new int[]{-1, 0, 1, 0, -1};
    public int shortestBridge(int[][] A) {
        int h = A.length;
        int w = A[0].length;
        int i = 0;
        int j = 0;
        for (int ii = 0; ii < h; ii++){
            for (int jj = 0; jj < w; jj++) {
                if (A[ii][jj] == 1){
                    i = ii;
                    j = jj;
                    break;
                }
            }
        }


        Queue<int[]> BFSqueue = new LinkedList<>();
        Set<Integer> visit = new HashSet<>();
        BFSqueue.add(new int[]{i, j});
        Queue<int[]> queue = new LinkedList<>();
        visit.add(i * w + j);
        while(!BFSqueue.isEmpty()) {
            int []cur = BFSqueue.remove();
            if (hasEmptyNb(cur, A)) {
                queue.add(cur);
            }

            for (int d = 0; d < 4; d++) {
                int nextI = cur[0] + dxy[d];
                int nextJ = cur[1] + dxy[d+1];
                if (nextI >= 0 && nextI < h && nextJ >= 0 && nextJ < w && A[nextI][nextJ] == 1 && visit.add(nextI * w + nextJ) ) {
                    BFSqueue.add(new int[]{nextI, nextJ});
                }
            }
        }

        //visit has all elmenets in the islant that is 1, if we find an element that is not on visit, then we have merge it
        int step = 0;
        while(!queue.isEmpty()) {
            step++;
            int qsize = queue.size();
            for (int q = 0; q < qsize; q++) {
                int[] cur = queue.remove();
                for (int d = 0; d < 4; d++) {
                    int nextI = cur[0] + dxy[d];
                    int nextJ = cur[1] + dxy[d+1];
                    if (nextI >= 0 && nextI < h && nextJ >= 0 && nextJ < w) {
                        if (visit.add(nextI * w + nextJ)) {
                            if (A[nextI][nextJ] == 1)
                                return step - 1;
                            else {
                                queue.add(new int[]{nextI, nextJ});
                            }
                        }

                    }
                }

            }
        }

        return 0;
    }
    private boolean hasEmptyNb(int[] cur, int[][] a) {
        for (int d = 0; d < 4; d++) {
            int nextI = cur[0] + dxy[d];
            int nextJ = cur[1] + dxy[d+1];
            if (nextI >= 0 && nextI < a.length && nextJ >= 0 && nextJ < a[0].length && a[nextI][nextJ] == 0)
                return true;
        }

        return false;
    }
    public int knightDialer(int N) {
        int MOD = 1_000_000_007;
        int[][] moves = new int[][]{
                {4,6},{6,8},{7,9},{4,8},{3,9,0},
                {},{1,7,0},{2,6},{1,3},{2,4}};

        int[][]dp = new int[N][10];
        for (int n = 0; n < N; n++) {

            for (int m = 0; m < 10; m++) {
                if (m == 0) {
                    dp[n][m] = 1;
                    continue;
                }
                for (int next : moves[m]) {
                    dp[n][next] += dp[n][m];
                    dp[n][next] %= MOD;
                }
            }
        }

        int sum = 0;
        for (int v : dp[N - 1]) {
            sum += v;
            sum %= MOD;
        }

        return sum;
    }
    private int[] processNumber(String t) {
        int[]res = new int[3];
        String[] number = t.split(".");
        res[0] = Integer.parseInt(number[0]);

        if (number.length > 1) {
            String[]parse = number[1].split("(|)");
            res[1] = Integer.parseInt(parse[0]);
            if (parse.length > 1) {
                // we need to make the degitial as simple as possible
                res[2] = Integer.parseInt(parse[1]);
            }
        }

        return res;
    }
    Map<Integer, Integer> map = new HashMap();
    public List<Integer> flipMatchVoyage(TreeNode root, int[] voyage) {
        for (int i = 0; i < voyage.length; i++) {
            map.put(voyage[i], i);
        }
        List<Integer> res = helper(root, 0, voyage.length - 1, voyage);

        if (res == null) {
            res = new ArrayList<>();
            res.add(-1);
            return res;
        }
        else {
            return res;
        }
    }
    private List<Integer> helper(TreeNode root, int start, int end, int[]voyage) {
        if (start > end) {
            if (root == null) return null;
            return new ArrayList<>();
        }
        else if (start == end) {
            if (root == null || root.val != voyage[start]) return null;
            return new ArrayList<>(); // no flip needed
        }
        else {
            int rootValue = voyage[start];
            if (root == null || root.val != rootValue) {
                return null;
            }

            if (root.left == null || root.right == null) {
                TreeNode nonEmptyChild = root.left == null ? root.right : root.left;
                List<Integer> childRes = helper(nonEmptyChild, start + 1, end, voyage);
                if (childRes == null) return null;
                else return childRes;
            }
            else {
                int leftChildIndex = map.get(root.left.val);
                int rightChildIndex = map.get(root.right.val);
                if (leftChildIndex == start + 1) {
                    List<Integer> leftChildRes = helper(root.left, start + 1, rightChildIndex - 1, voyage);
                    List<Integer> rightChildRes = helper(root.right, rightChildIndex, end, voyage);
                    if (leftChildRes == null || rightChildRes == null) return null;
                    for (int x : rightChildRes)
                        leftChildRes.add(x);

                    return leftChildRes;
                }
                else if (rightChildIndex == start + 1) {
                    List<Integer> res = new ArrayList<>();
                    res.add(root.val);

                    List<Integer> rightChildRes = helper(root.right, start + 1, leftChildIndex - 1, voyage);
                    List<Integer> leftChildRes = helper(root.left, leftChildIndex, end, voyage);
                    if (leftChildRes == null || rightChildRes == null) return null;
                    for (int x : rightChildRes)
                        res.add(x);

                    for (int x : leftChildRes)
                        res.add(x);

                    return res;
                }
                else {
                    return null;
                }
            }
        }
    }
    public List<Integer> pancakeSort(int[] A) {
        List<Integer> res = new ArrayList<>();

        for (int i = 0; i < A.length; i++) {
            for (int j = i; j < A.length; j++) {
                if (A[j] == i + 1)
                {
                    swapHelper(res, i, j, A);
                }
            }
        }

        return res;
    }
    private void swapHelper(List<Integer> res, int i, int j, int[]A) {
        if (i == j) return;
        reverseToIndex(A, j);
        res.add(j+1);
        reverseToIndex(A, j - i);
        res.add(j - i+1);
        reverseToIndex(A, j);
        res.add(j+1);
    }
    private void reverseToIndex(int[] A, int j) {
        int i = 0;
        while(i < j) {
            swap(A, i++, j--);
        }
    }
    private void swap(int[]a, int i, int j){
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }
    public List<Integer> powerfulIntegers(int x, int y, int bound) {
        int xPower = 1;
        int yPower = 1;

        Set<Integer> set = new HashSet<>();
        while(xPower + yPower <= bound) {
            while(true) {
                int curValue = xPower + yPower;
                if (curValue <= bound) {
                    set.add(curValue);
                    if (yPower == yPower * y) break;

                    yPower *= y;
                }
                else {
                    break;
                }
            }

            if (xPower == x * xPower) break;
            xPower *= x;
            yPower = 1;
            if (xPower + 1 > bound) break;
        }

        List<Integer> res = new ArrayList<Integer>(set);
        return res;
    }
    public int longestMountain(int[] A) {
        int best = 0;
        for (int i = 1; i < A.length; i++) {
            if (A[i] > A[i+1] && A[i] > A[i-1]){
                int left = countLeft(A, i-1);
                int right= countRight(A, i +1);
                best = Math.max(1+left+right, best);
            }
        }

        return best;
    }
    private int countRight(int[] a, int i) {
        int count = 0;
        while(i < a.length) {
            if (a[i] < a[i-1]) {
                count++;
                i++;
            }
            else{
                break;
            }
        }

        return count;
    }
    private int countLeft(int[] a, int i) {
        int count = 0;
        while(i >= 0) {
            if (a[i] < a[i+1])
            {
                i--;
                count++;
            }
            else {
                break;
            }
        }
        return count;
    }
    public int numComponents(ListNode head, int[] G) {
        Set<Integer> set = new HashSet<>();
        for (int g : G) set.add(g);
        int count = 0;
        ListNode last = null;

        while(head != null) {
            if (set.contains(head.val)) {
                if (last == null || !set.contains(last.val))
                    count++;
            }

            last = head;
            head = head.next;
        }

        return count;
    }
    public List<List<Integer>> largeGroupPositions(String S) {
        List<List<Integer>> res = new ArrayList<>();
        int i = 0;
        int j = 0;
        while(i < S.length() && j < S.length()) {
            char c = S.charAt(i);
            while(j < S.length() && c == S.charAt(j))
                j++;

            if (j - i >= 3) {
                List<Integer> cur = new ArrayList<>();
                cur.add(i);
                cur.add(j-1);
                res.add(cur);
            }

            i = j;
        }

        return res;
    }
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
class ListNode {
      int val;
      ListNode next;
     ListNode(int x) { val = x; }
  }