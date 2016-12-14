package lab2;

/**
 * Created by Sofia on 14.12.16.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;


public class Cicles {

    static int n;
    static int m;
    static int w[];
    static int used[];


    public static void main(String[] args) throws FileNotFoundException {


        Scanner in = new Scanner(new File("cycles.in"));
        PrintWriter out = new PrintWriter(new File("cycles.out"));
        n = in.nextInt();
        m = in.nextInt();
        int total = 0;
        int curSize = 0;
        int sz = 1 << n;
        w = new int[n];
        for (int i = 0; i < n; i++) {
            w[i] = in.nextInt();
        }
        used = new int[(1 << n) + 1];
        Arrays.fill(used, 0);

        for (int i = 0; i < m; i++) {
            int count = in.nextInt();
            int valCur = 0;
            for (int j = 0; j < count; j++)
                valCur += (1 << (in.nextInt() - 1));
            solve(valCur);
        }
        out.print(run(sz, curSize, total));
        out.close();
    }

    static void solve(int x) {
        used[x] = 1;
        if (x == ((1 << n) - 1)) {
            return;
        }
        for (int i = 0; i < n; i++) {
            int val = 1 << i;
            boolean pred = ((x & (val)) == 0);
            if (pred && (used[x + val] == 0)) {
                solve(x + val);
            }
        }
    }

    private static int run(int sz, int curSize, int total) {
        for (int mask = 0; mask < sz; mask++) {
            if (0 == used[mask]) {
                int count = 0;
                int result = 0;
                for (int k = 0; k < n; k++) {
                    int ll = 1 << k;
                    if ((mask & ll) > 0) {
                        count++;
                        result += w[k];
                    }
                }
                if (count > curSize) {
                    total = 0;
                    curSize = count;
                }
                total = Math.max(total, result);
            }
        }
        return total;
    }
}
