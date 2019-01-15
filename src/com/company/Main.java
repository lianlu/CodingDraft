package com.company;

import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.standard.MediaSizeName;
import java.util.*;
import java.util.function.BinaryOperator;

import static java.util.Collections.swap;
import static javax.print.attribute.standard.MediaSizeName.A;

public class Main {
    public static void main(String[] args) {
        List<Integer> x = new Solution().splitIntoFibonacci("1101111");
        System.out.println(x);
    }
}

class Solution {


    List<Integer> res = new ArrayList<>();
    public List<Integer> splitIntoFibonacci(String S) {
        List<Integer> cur = new ArrayList<>();
        helper(S, 0, cur);
        if (res.size() >= 3) return res;

        return new ArrayList<>();
    }
    private void helper(String S, int start, List<Integer> cur) {
        if (start == S.length()) {
            if (cur.size() >= 3)
                this.res = new ArrayList<>(cur);
        }
        else if (S.charAt(start) == '0') {
            int number = 0;
            if (cur.size() <= 1) {
                cur.add(number);
                helper(S, start + 1, cur);
                cur.remove(cur.size() - 1);
            }
            else {
                int last = cur.get(cur.size() - 1);
                int last1 = cur.get(cur.size() - 2);
                if (last == 0 - last1) {
                    cur.add(0);
                    helper(S, start + 1, cur);
                    cur.remove(cur.size() - 1);
                }
                else {
                    return;
                }
            }
        }
        else {
            for (int i = start; i < S.length(); i++) {
                String curParseNumber = S.substring(start, i + 1);
                long numberLong = Long.parseLong(curParseNumber);
                if (numberLong <= Integer.MAX_VALUE) {
                    int number = (int)numberLong;
                    if (cur.size() <= 1) {
                        cur.add(number);
                        helper(S, i + 1, cur);
                        cur.remove(cur.size() - 1);
                    }
                    else {
                        int last = cur.get(cur.size() - 1);
                        int last1 = cur.get(cur.size() - 2);
                        if (last == number - last1) {
                            cur.add(number);
                            helper(S, i + 1, cur);
                            cur.remove(cur.size() - 1);
                        }
                    }
                }
            }
        }
    }
    public int maxProfitAssignment(int[] difficulty, int[] profit, int[] worker) {
        Work[]works = new Work[profit.length];
        for (int i = 0; i < works.length; i++) {
            works[i] = new Work(difficulty[i], profit[i]);
        }

        Arrays.sort(works, new Comparator<Work>() {
            @Override
            public int compare(Work o1, Work o2) {
                if (o1.d != o2.d) {
                    return o1.d - o1.d;
                }
                else {
                    return o1.p - o2.p;
                }
            }
        });

        Arrays.sort(works);
        int res = 0;
        int index = 0;
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int i = 0; i < worker.length; i++) {
            while(index < works.length && worker[i] >= works[index].d) {
                pq.add(works[index].p);
                index++;
            }

            if (!pq.isEmpty()) {
                res += pq.peek();
            }
        }

        return res;
    }
    class Work {
        int d;
        int p;
        public Work(int d, int p){
            this.d = d;
            this.p = p;
        }
    }
    public String pushDominoes(String dominoes) {
        int len = dominoes.length();
        int[][]faillingTime = new int[len][2];
        int index = -1;
        for (int i = 0; i < len; i++) {
            char c = dominoes.charAt(i);
            if (c == 'R') {
                index = i;
                faillingTime[i][0] = 0;
            }
            else if (c == '.') {
                if (index != -1) {
                    faillingTime[i][0] = (i - index);
                }
                else {
                    faillingTime[i][0] = Integer.MAX_VALUE;
                }
            }
            else { // c == 'L'
                faillingTime[i][0] = Integer.MAX_VALUE;
                index = -1;
            }
        }

        index = -1;
        for (int i = len - 1; i >= 0; i--) {
            char c = dominoes.charAt(i);
            if (c == 'L') {
                index = i;
                faillingTime[i][1] = 0;
            }
            else if (c == '.') {
                if (index != -1) {
                    faillingTime[i][1] = (index - i);
                }
                else {
                    faillingTime[i][1] = Integer.MAX_VALUE;
                }
            }
            else { // c == 'R'
                faillingTime[i][1] = Integer.MAX_VALUE;
                index = -1;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (faillingTime[i][0] == faillingTime[i][1]) {
                sb.append(".");
            }
            else if (faillingTime[i][0] < faillingTime[i][1]) {
                sb.append("R");
            }
            else {
                sb.append("L");
            }
        }

        return sb.toString();
    }
    public int carFleet(int target, int[] position, int[] speed) {
        Node[]nodes = new Node[speed.length];
        for (int i = 0; i < speed.length; i++) {
            nodes[i] = new Node(position[i], position[i]/speed[i]);
        }

        Arrays.sort(nodes, (x,y) -> y.p - x.p);

        int count = 0;
        int lastHitTime = 0;
        for (int i = 0; i < nodes.length; i++) {
            int time = nodes[i].t;
            if (time <= lastHitTime) {
                continue;
            }
            else {
                count++;
                lastHitTime = time;
            }
        }

        return count;
    }
    class Node{
        int p;
        int t;
        public Node(int p, int t){
            this.p = p;
            this.t = t;
        }
    }
    public int oddEvenJumps(int[] A) {
        int alen = A.length;

        TreeSet<Integer> tree = new TreeSet<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (A[o1] != A[o2])
                    return A[o1] - A[o2];

                return o1 - o2;
            }
        });

        TreeSet<Integer> lowerTree = new TreeSet<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (A[o1] != A[o2])
                    return A[o1] - A[o2];

                return - o1 + o2;
            }
        });

        DJSet djSet = new DJSet(alen * 2);
        int res = 1;
        tree.add(alen - 1);
        lowerTree.add(alen - 1);
        int v1 = (alen - 1) * 2;
        int v2 = v1 + 1;

        for (int i = alen - 2; i >= 0; i--) {

            Integer higher = tree.higher(i);
            int oddJump = 2 * i + 0;
            if (higher != null) {
                djSet.union(oddJump, higher * 2 + 1);
            }

            Integer lower = lowerTree.lower(i);
            int evenJump = 2 * i + 1;
            if (lower != null) {
                djSet.union(evenJump, lower * 2 + 0);
            }

            if (djSet.isConnected(oddJump, v1)
                    || djSet.isConnected(oddJump,v2)) {
                res++;
            }

            tree.add(i);
            lowerTree.add(i);
        }

        return res;
    }
    public int subarraysDivByK(int[] A, int K) {
        int []dp = new int[K];
        int cur = 0;
        for (int i = 0; i < A.length; i++) {
            cur += A[i];
            int mod = (cur % K + K)%K;
            dp[mod]++;
        }

        int sum = 0;
        for (int i = 0; i < K; i++) {
            if (dp[i] > 1) {
                sum += (dp[i] * (dp[i] - 1))/2;
            }
        }

        sum += dp[0];

        return sum;
    }
    public int largestPerimeter(int[] A) {
        Arrays.sort(A);

        int best = 0;
        for (int i = A.length - 3; i >= 0; i--) {
            if (A[i] + A[i+1] > A[i+2]) {
                best = Math.max(best, A[i] + A[i+1] + A[i+2]);
            }
        }

        return best;
    }
    public int[][] kClosest(int[][] points, int K) {
        PriorityQueue<int[]> pq = new PriorityQueue<int[]>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return - o1[0] * o1[0] - o1[1] * o1[1] + o2[0] * o2[0] + o2[1] * o2[1];
            }
        });

        for (int [] p : points) {
            if (pq.size() < K) {
                pq.add(p);
            }
            else {
                pq.add(p);
                pq.remove();
            }
        }

        int[][]res = new int[K][2];
        for (int i = 0; i < K; i++) {
            res[i] = pq.remove();
        }

        return res;
    }
    public boolean lemonadeChange(int[] bills) {
        int numberOfBill[] = new int[2]; // number of 5 and 10
        for (int b : bills) {
            if (b == 20) {
                numberOfBill[1]--;
                numberOfBill[0]--;
                if (numberOfBill[1] < 0 || numberOfBill[1] < 0)
                    return false;
            }
            else if (b == 10) {
                numberOfBill[1]++;
                numberOfBill[0]--;
                if (numberOfBill[0] < 0)
                    return false;
            }
            else {
                numberOfBill[0]++;
            }
        }

        return true;
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