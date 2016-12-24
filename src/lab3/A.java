package lab3;

import java.io.*;
import java.util.*;

/**
 * Created by Sofia on 19.12.16.
 */
public class A {

    static final int N = 107;
    static final int M = 5300;

    private static boolean used[] = new boolean[M];
    private static int p[] = new int[N];
    private static int rr[] = new int[N];
    private static boolean cAr[] = new boolean[N];
    private static boolean canUnite[] = new boolean[M];
    private static boolean diffCol[] = new boolean[M];
    private static HashSet<Integer> allFreeEdges = new HashSet<>();
    private static HashSet<Integer> ans = new HashSet<>();
    private static ArrayList<Integer> g[] = new ArrayList[M];
    private static ArrayList<Edge> graph;
    private static int prev[] = new int[M];

    private static class Edge {

        private Edge(int u, int v, int c, int i) {
            this.u = u;
            this.v = v;
            this.c = c;
            this.i = i;
        }

        int u, v, c, i;
    }

    private static void make(int v) {
        p[v] = v;
        rr[v] = 0;
    }

    private static int find(int v) {
        return (v == p[v]) ? v : (p[v] = find(p[v]));
    }

    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a != b) {
            if (rr[a] < rr[b]) {
                int t = a;
                a = b;
                b = t;
            }
            p[b] = a;
            if (rr[a] == rr[b]) {
                ++rr[a];
            }
        }
    }

    private static void selection() {
        Arrays.fill(canUnite, false);
        Arrays.fill(diffCol, false);
        prepare();
        for (int x : ans) {
            Edge e = graph.get(x);
            union(e.v, e.u);
        }
        for (int x : allFreeEdges) {
            Edge e = graph.get(x);
            canUnite[x] = (find(e.u) != find(e.v));
            diffCol[x] = !cAr[e.c];
        }
    }

    public static void main(String[] args) throws IOException {
        MyReader in = new MyReader(new FileInputStream("rainbow.in"));
        MyWriter out = new MyWriter(new FileOutputStream("rainbow.out"));

        int n = in.nextInt();
        int m = in.nextInt();
        graph = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            graph.add(new Edge(
                    in.nextInt() - 1,
                    in.nextInt() - 1,
                    in.nextInt() - 1,
                    i
            ));
            allFreeEdges.add(i);
        }
        Arrays.fill(g, new ArrayList<Integer>());
        Arrays.fill(cAr, false);
        while (true) {
            for (int i = 0; i < m; i++) {
                g[i].clear();
            }
            for (int x : ans) {
                prepare();
                ans.stream().filter(y -> y != x).forEachOrdered(y -> {
                    Edge e = graph.get(y);
                    union(e.u, e.v);
                });
                for (int z : allFreeEdges) {
                    Edge eX = graph.get(x);
                    Edge eZ = graph.get(z);
                    if (!cAr[eX.c] || eX.c == eZ.c) {
                        g[z].add(x);
                    }
                    if (find(eZ.u) != find(eZ.v)) {
                        g[x].add(z);
                    }
                }

            }
            selection();
            if (!intersection(m)) {
                out.println(ans.size() + "");
                for (int x : ans) {
                    out.print((x + 1) + " ");
                }
                out.close();
                System.exit(0);
            }
        }
    }


    private static boolean intersection(int m) {

        PriorityQueue<Integer> q = new PriorityQueue<>();


        for (int i = 0; i < m; i++) {
            used[i] = false;
            if (canUnite[i]) {
                q.add(i);
                used[i] = true;
                prev[i] = -100;
            }
        }

        while (!q.isEmpty()) {
            int vertex = q.poll();
            int v = vertex;
            if (diffCol[vertex]) {
                while (vertex != -100) {
                    int c = graph.get(vertex).c;
                    cAr[c] = allFreeEdges.contains(vertex);
                    if (cAr[c]) {
                        ans.add(vertex);
                        allFreeEdges.remove(vertex);
                    } else {
                        ans.remove(vertex);
                        allFreeEdges.add(vertex);
                    }
                    vertex = prev[vertex];
                }
                return true;
            }
            (g[v]).stream().filter(to -> !used[to]).forEachOrdered(to -> {
                used[to] = true;
                q.add(to);
                prev[to] = v;

            });
        }
        return false;
    }

    private static void prepare() {
        for (int i = 0; i < N; i++) {
            p[i] = i;
            rr[i] = 0;
        }
    }

    static class MyReader {
        BufferedInputStream in;

        final int bufSize = 1 << 16;
        final byte b[] = new byte[bufSize];

        MyReader(InputStream in) {
            this.in = new BufferedInputStream(in, bufSize);
        }

        int nextInt() throws IOException {
            int c;
            while ((c = nextChar()) <= 32) ;
            int x = 0, sign = 1;
            if (c == '-') {
                sign = -1;
                c = nextChar();
            }
            while (c >= '0') {
                x = x * 10 + (c - '0');
                c = nextChar();
            }
            return x * sign;
        }

        StringBuilder _buf = new StringBuilder();

        String nextWord() throws IOException {
            int c;
            _buf.setLength(0);
            while ((c = nextChar()) <= 32 && c != -1)
                ;
            if (c == -1)
                return null;
            while (c > 32) {
                _buf.append((char) c);
                c = nextChar();
            }
            return _buf.toString();
        }

        int bn = bufSize, k = bufSize;

        int nextChar() throws IOException {
            if (bn == k) {
                k = in.read(b, 0, bufSize);
                bn = 0;
            }
            return bn >= k ? -1 : b[bn++];
        }

        int nextNotSpace() throws IOException {
            int ch;
            while ((ch = nextChar()) <= 32 && ch != -1)
                ;
            return ch;
        }
    }

    static class MyWriter {
        BufferedOutputStream out;

        final int bufSize = 1 << 16;
        int n;
        byte b[] = new byte[bufSize];

        MyWriter(OutputStream out) {
            this.out = new BufferedOutputStream(out, bufSize);
            this.n = 0;
        }

        byte c[] = new byte[20];

        void print(int x) throws IOException {
            int cn = 0;
            if (n + 20 >= bufSize)
                flush();
            if (x < 0) {
                b[n++] = (byte) ('-');
                x = -x;
            }
            while (cn == 0 || x != 0) {
                c[cn++] = (byte) (x % 10 + '0');
                x /= 10;
            }
            while (cn-- > 0)
                b[n++] = c[cn];
        }

        void print(char x) throws IOException {
            if (n == bufSize)
                flush();
            b[n++] = (byte) x;
        }

        void print(String s) throws IOException {
            for (int i = 0; i < s.length(); i++)
                print(s.charAt(i));
        }

        void println(String s) throws IOException {
            print(s);
            println();
        }

        static final String newLine = System.getProperty("line.separator");

        void println() throws IOException {
            print(newLine);
        }

        void flush() throws IOException {
            out.write(b, 0, n);
            n = 0;
        }

        void close() throws IOException {
            flush();
            out.close();
        }
    }


}