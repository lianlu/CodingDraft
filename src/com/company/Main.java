package com.company;

import java.util.*;

public class Main {
    public static void main(String[] args) {
    int res = new Solution().rectangleArea(new int[][]{{0,0,1000000000,1000000000}});
    System.out.println(res);
    }
}

class Solution {
    public boolean rotateString(String A, String B) {
        StringBuilder sb = new StringBuilder(A);
       for (int i = 0; i <= A.length(); i++) {
            if (sb.toString().equals(B)) return true;
            sb.append(sb.deleteCharAt(0));
       }

       return false;
    }

    int mod = 1_000_000_007;
    public int rectangleArea(int[][] rectangles) {
        int[]verticalIndex = new int[rectangles.length*2];
        for (int i = 0; i < rectangles.length; i++){
            verticalIndex[2*i] = rectangles[i][1];
            verticalIndex[2*i+1] = rectangles[i][3];
        }

        Arrays.sort(verticalIndex);
        List<Node> xNodes = new ArrayList<>();
        for (int[]rect : rectangles) {
            xNodes.add(new Node(true, rect[1], rect[3], rect[0]));
            xNodes.add(new Node(false, rect[1], rect[3], rect[2]));
        }

        Collections.sort(xNodes, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.x != o2.x) return o1.x - o2.x;
                if (o2.isStart) return 1;
                return -1;
            }
        });

        int area = 0;
        for (int i = 1; i < verticalIndex.length; i++) {
            if (verticalIndex[i] != verticalIndex[i-1]) {
                int height = verticalIndex[i] - verticalIndex[i-1];
                area += findArea(xNodes, verticalIndex[i-1], verticalIndex[i]);
                area %= mod;
            }
        }

        return area;
    }

    private int findArea(List<Node> xNodes, int y0, int y1) {
        int height = y1 - y0;
        long area = 0;
        Integer lastStartIndex = null;
        int remainingCount = 0;

        for (Node node : xNodes) {
            if (withinHeightRange(node, y0, y1)) {
                if (node.isStart) {
                    remainingCount++;
                }
                else {
                    remainingCount--;
                }

                if (lastStartIndex == null && node.isStart) lastStartIndex = node.x;

                if (remainingCount == 0) {
                    int curArea = node.x - lastStartIndex;
                    area += (long)curArea * (long)height;
                    area %= mod;
                    lastStartIndex = null;
                }
            }
        }

        return (int)(area % mod);
    }

    private boolean withinHeightRange(Node node, int y0, int y1) {
        return node.y1 >= y1 && node.y0 <= y0;
    }

    class Node{
        boolean isStart;
        int y0;
        int y1;
        int x;
        public Node(boolean isStart, int y0, int y1, int x){
            this.isStart = isStart; this.y0 = y0; this.y1 = y1; this.x = x;
        }
    }



    public int[] hitBricks(int[][] grid, int[][] hits) {
        int h = grid.length;
        int w = grid[0].length;

        DJSet djSet = new DJSet(h*w+1);
        boolean[]isEmpty = new boolean[hits.length];

        int ii = 0;
        for (int [] hit : hits) {
            isEmpty[ii++] = grid[hit[0]][hit[1]] == 0;
            grid[hit[0]][hit[1]] = 0;
        }

        int[]res = new int[hits.length];
        int[]dxy = new int[]{0, 1, 0};
        int topIndex = h * w;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (grid[i][j] == 1) {
                    if (i == 0) djSet.union(i*w+j , topIndex);

                    for(int d = 0; d < 2; d++) {
                        int nx = i + dxy[d];
                        int ny = j + dxy[d+1];
                        if (nx < h && ny < w && grid[nx][ny] == 1) {
                            djSet.union(i*w+j, nx * w + ny);
                        }
                    }
                }
            }
        }

        dxy = new int[]{-1, 0, 1, 0, -1};

        for (int i = hits.length - 1; i >= 0; i--) {
            if (!isEmpty[i]) {
                grid[hits[i][0]][hits[i][1]] = 1;

                int previousValue = djSet.size(topIndex);
                if (hits[i][0] == 0) djSet.union(topIndex, hits[i][0]  * w + hits[i][1] );

                for (int d = 0; d < 4; d++) {
                    int newX = hits[i][0] + dxy[d];
                    int newY = hits[i][1] + dxy[d+1];
                    if (newX >= 0 && newX < h && newY >= 0 && newY < w && grid[newX][newY] == 1) {
                        djSet.union(newX * w + newY, hits[i][0] * w + hits[i][1]);
                    }
                }

                res[i] = Math.max(0, djSet.size(topIndex) - previousValue - 1);
            }
        }

        return res;
    }

    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        return findPath(graph, 0, graph.length - 1);
    }

    private List<List<Integer>> findPath(int[][] graph, int start, int end) {
        if (start == end)
        {
            List<List<Integer>> res = new LinkedList<>();
            List<Integer> cur = new LinkedList<>();
            cur.add(start);
            res.add(cur);
            return res;
        }
        else {
            List<List<Integer>> res = new LinkedList<>();
            for (int i = 0; i < graph[start].length; i++) {
                if (i != start)
                    res.addAll(findPath(graph, i, end));
            }

            for (List<Integer> cur : res) {
                cur.add(0, start);
            }

            return res;
        }
    }

    public int swimInWater(int[][] grid) {
        int N = grid.length;
        DJSet djSet = new DJSet(N * N);

        Map<Integer, List<int[]>> map = new HashMap<>();
        int[] dxy = new int[]{0, 1, 0};

        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++) {
                for (int d = 0; d < 2; d++) {
                    int newX = i + dxy[d];
                    int newY = i + dxy[d+1];
                    if (newX >= 0 && newX < N && newY >= 0 && newY < N) {
                        int diff = Math.abs(grid[i][j] - grid[newX][newY]);
                        if (map.containsKey(diff) == false) map.put(diff, new LinkedList<>());

                        map.get(diff).add(new int[]{i*N + j, newX * N + newY});
                    }
                }
            }
        }

        for (int i = 1; i < N * N; i++) {
            List<int[]> curLinked = map.get(i);
            if (curLinked == null) continue;

            for (int[]edge : curLinked) {
                djSet.union(edge[0], edge[1]);
            }

            if (djSet.isConnected(0, N * N - 1)) return i;
        }

        return N*N - 1;
    }

    public boolean reachingPoints(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        if (sx > tx || sy > ty) return false;
        while (tx >= sx && ty >= sy) {
            if (tx == sx && ty == sy) return true;
            if (tx > ty) {
                int n = (tx - sx)/ty;
                n = Math.max(n, 1);
                tx -= n * ty;
            }
            else {
                int n = (ty - sy)/tx;
                n = Math.max(n, 1);
                ty -= n * tx;
            }
        }

        return false;
    }

    public String customSortString(String S, String T) {
        Character []tArray = new Character[T.length()];
        for (int i = 0; i < T.length(); i++)
            tArray[i] = T.charAt(i);

        Arrays.sort(tArray, new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                if (o1 == o2) return 0;

                int in1 = S.indexOf(o1);
                int in2 = S.indexOf(o2);

                if (in1 == -1 && in2 == -1) {
                    return o1 - o2;
                }
                else if (in1 == -1) {
                    return -1;
                }
                else if(in2 == -1) {
                    return 1;
                }
                else {
                    return in1 - in2;
                }
            }
        });

        StringBuilder sb = new StringBuilder();
        for (char c : tArray)
            sb.append(c);

        return sb.toString();
    }

    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int K) {
        if (src == dst) return 0;
        int[][]dp = new int[n][K+1]; // to dst
        int[][]table = new int[n][n];
        for (int[]f : flights) {
            table[f[0]][f[1]] = f[2];
        }

        for (int k = 0; k <= K; k++) {
            for (int s = 0; s < n; s++) {
                if (k == 0) {
                    dp[s][k] = Integer.MAX_VALUE;
                    dp[dst][k] = 0;
                    continue;
                }

                if (s == dst) {
                    dp[s][k] = 0;
                }
                else {
                    dp[s][k] = table[s][dst] > 0 ? table[s][dst] : Integer.MAX_VALUE;
                    dp[s][k] = Math.min(dp[s][k-1], dp[s][k]);

                    for (int other = 0; other < n; other++) {
                        if (other != s && other != dst && table[s][other] > 0 && dp[other][k-1] < Integer.MAX_VALUE) {
                            dp[s][k] = Math.min(dp[s][k], table[s][other] + dp[other][k-1]);
                        }
                    }

                }
            }
        }


        return dp[src][K] < Integer.MAX_VALUE ? dp[src][K]  : -1;
    }

    public boolean canTransform(String start, String end) {
        String neatStart = start.replaceAll("X", "");
        String neatEnd = end.replaceAll("X", "");
        if (!neatEnd.equals(neatStart)) {
            return false;
        }

        if (neatStart.length() == 0) return true;

        for (int i = 0; i < end.length(); i++) {
            if (end.charAt(i) == 'L') break; // other part should handle it
            if (end.charAt(i) == 'R'){
                for (int j = 0; j < end.length(); j++) {
                    if (start.charAt(j) == 'R') {
                        return canTransform(start.substring(j + 1), end.substring(i + 1));
                    }
                    else if (start.charAt(j) == 'L') {
                        return false;
                    }
                }
            }
        }

        for (int i = end.length() - 1; i >= 0; i--) {
            if (end.charAt(i) == 'R') break; // other part should handle it
            if (end.charAt(i) == 'L'){
                for (int j = end.length() - 1; j >= 0; j--) {
                    if (start.charAt(j) == 'L') {
                        return canTransform(start.substring(0, j), end.substring(0, i));
                    }
                    else if (start.charAt(j) == 'R') {
                        return false;
                    }
                }
            }
        }

        return false;
    }

    public TreeNode[] splitBST(TreeNode root, int V) {
        TreeNode[]res = new TreeNode[2];
        if (root == null) return res;
        if (root.val < V) {
            TreeNode rightNode = root.right;
            TreeNode[] rightRes = splitBST(rightNode, V);
            TreeNode smallerPart = null;

            if (rightRes[0] != null && rightRes[0].val < V) {
                smallerPart = rightRes[0];
            }
            else if (rightRes[1] != null && rightRes[1].val < V) {
                smallerPart = rightRes[1];
            }

            root.right = smallerPart;
            res[0] = root;
            res[1] = rightRes[0] == smallerPart ? rightRes[1] : rightRes[0];

            return res;
        }
        else if (root.val > V){
            TreeNode leftNode = root.left;
            TreeNode[] leftRes = splitBST(leftNode, V);
            TreeNode largePart = null;

            if (leftRes[0] != null && leftRes[0].val > V) {
                largePart = leftRes[0];
            }
            else if (leftRes[1] != null && leftRes[1].val > V) {
                largePart = leftRes[1];
            }

            root.left = largePart;
            res[0] = root;
            res[1] = leftRes[0] == largePart ? leftRes[1] : leftRes[0];

            return res;
        }
        else {
            res[0] = root;
            res[1] = root.right;
            root.right = null;
            return res;
        }
    }

    public boolean isIdealPermutation(int[] A) {
        int len = A.length;
        int[]rightMin = new int[len];
        for (int i = len - 1; i >= 0; i--) {
            if (i == len - 1) rightMin[i] = A[i];
            else rightMin[i] = Math.min(A[i], rightMin[i + 1]);
        }

        for (int i = len - 3; i >= 0; i--) {
            if (A[i] > rightMin[i + 2]) return false;
        }

        return true;
    }

    public int slidingPuzzle(int[][] board) {
        Set<String> visit = new HashSet<>();
        Queue<int[][]> queue = new LinkedList<>();
        int[][] target = new int[][]{{1,2,3}, {4, 5, 0}};
        String targetString = convert2String(target);
        int step = 0;
        queue.add(board);
        String sourceString = convert2String(board);
        if (sourceString.equals(targetString)) return 0;

        visit.add(sourceString);

        int[]dxy = new int[]{-1, 0, 1, 0, -1};

        while(!queue.isEmpty()) {
            step++;
            int qsize = queue.size();
            for (int q = 0; q < qsize; q++) {
                int[][]cur = queue.remove();

                String aa = convert2String(cur);

                int i = 0;
                while(i < 6) {
                    if (cur[i/3][i%3] == 0) break;
                    else i++;
                }

                int x = i/3;
                int y = i%3;
                for (int d = 0; d < 4; d++) {
                    int newX = x + dxy[d];
                    int newY = y + dxy[d+1];
                    if (newX >= 0 && newX < 2 && newY >= 0 && newY < 3){
                        int[][]newBoard = new int[2][3];
                        for (int k1 = 0; k1 < 2; k1++)
                            for (int k2 = 0; k2 < 3; k2++)
                                newBoard[k1][k2] = cur[k1][k2];

                        swap(newBoard, newX, newY, x, y);

                        String nextBoard = convert2String(newBoard);
                        if (nextBoard.equals(targetString)) return step;

                        if (visit.add(nextBoard)) {
                            queue.add(newBoard);
                        }
                    }
                }
            }
        }

        return -1;
    }

    private void swap(int[][] newBoard, int newX, int newY, int x, int y) {
        int t = newBoard[newX][newY];
        newBoard[newX][newY] = newBoard[x][y];
        newBoard[x][y] = t;
    }

    private String convert2String(int[][] target) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 3; j++)
                sb.append(target[i][j]);

        return sb.toString();
    }

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