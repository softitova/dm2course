package lab2;

import java.io.*;
import java.util.*;

public class Check {

    static HashSet<String> set = new HashSet<>();
    static ArrayList<Integer> var[];

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("check.in"));
        PrintWriter out = new PrintWriter(new File("check.out"));
        StringTokenizer st = new StringTokenizer(in.readLine());
        boolean[] used = new boolean[1 << 10];
        boolean f = false;
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        var = new ArrayList[m];

        for (int i = 0; i < m; i++) {
            var[i] = new ArrayList<>();
            st = new StringTokenizer(in.readLine());
            int sz = Integer.parseInt(st.nextToken());
            if (sz == 0) {
                f = true;
            }
            for (int j = 0; j < sz; j++) {
                var[i].add(Integer.valueOf(st.nextToken()));
            }
            //moveToSet(i);
        }
        if (!f) {
            out.println("NO");
            out.close();
            System.exit(0);
        }

        for (int i = 0; i < m; i++) {
            int mask = 0;
            for (int j = 0; j < var[i].size(); j++) {
                mask |= (1 << (var[i].get(j) - 1));
            }
            used[mask] = true;
        }


        for (int i = 0; i < (1 << n); ++i) {
            if (used[i]) {
                label:
                for (int j = 0; j < (1 << n); ++j) {
                    for (int k = 0; k < n; k++) {
                        if ((((i & (1 << k)) == 0) && ((1 << k) == (j & (1 << k)))) || !count(i, j, n)) {
                            continue label;
                        }
                    }
                    if (!used[j]) {
                        out.println("NO");
                        out.close();
                        System.exit(0);
                    }
                }
            }
        }


        for (int i = 1; i < (1 << n); ++i) {
            label:
            for (int j = 0; j < (1 << n); ++j) {
                if (used[i] && used[j] && count(i, j, n)) {
                    int mm = i ^ j;
                    for (int k = 0; k < n; k++) {
                        if ((mm & (1 << k)) != 0) {
                            if (used[(j | (1 << k))] && ((j | (1 << k)) != j)

                                    ) {
                                continue label;
                            }
                        }
                    }
                    out.println("NO");
                    out.close();
                    System.exit(0);
                }
            }
        }
        out.println("YES");
        out.close();


    }

    private static boolean count(int i, int j, int n) {
        int sumi = 0;
        int sumj = 0;
        for (int k = 0; k < n; k++) {
            sumi += (i & (1 << k)) >> k;
            sumj += (j & (1 << k)) >> k;
        }
        return (sumi > sumj);
    }
}