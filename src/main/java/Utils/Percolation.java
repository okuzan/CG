package Utils;

@SuppressWarnings("Duplicates")
public class Percolation {
    private int size;
    private int openCount = 0;
    private boolean[][] map;
    public double result;
    private int[] id;

    public Percolation(int n) {
        //global size var
        size = n;
        //map of real open/closed
        map = new boolean[n][n];
        System.out.println("Created  Percolation " + n + " X " + n);
        //at start all are open

        //+ beginning and ending
        id = new int[n * n + 2];
        for (int i = 0; i < n * n + 2; i++)
            id[i] = i;
        //приєднання першого і останнього рядку до стоку і витоку
        for (int i = 0; i < size; i++) {
            union(0, i);
            union((size*size + 1), size*size - size - 1 + i);
        }

    }

    public int find(int p) {
        return id[p];
    }

    public void union(int p, int q) {
        int pid = find(p);
        int qid = find(q);
        if (pid == qid) return;
        for (int i = 0; i < id.length; i++)
            if (id[i] == pid)
                id[i] = qid;
    }

    public int getOpenedCount() {
        return openCount;
    }

    public boolean connected(int p, int q) {
        return id[p] == id[q];
    }

    public void open(int i, int j) {
        //this starts only when I am sure it is not already opened.
        map[i][j] = true;
        openCount++;
        System.out.println("Open left " + openCount);

        //+- 1 бо сток і виток
        if (i - 1 >= 0) {
            if (map[i - 1][j]) {

                union(idOf(i, j), idOf(i - 1, j));
            }
        }
        if (i + 1 <= size - 1) {
            if (map[i + 1][j]) {
                union(idOf(i, j), idOf(i + 1, j));
            }
        }
        if (j - 1 >= 0) {
            if (map[i][j - 1]) {
                union(idOf(i, j), idOf(i, j - 1));
            }
        }
        if (j + 1 <= size - 1) {
            if (map[i][j + 1]) {
                union(idOf(i, j), idOf(i, j + 1));
            }
        }
    }

    public int idOf(int j, int k) {
        return j * size + k + 1;
    }

//    public boolean isOpened(int i, int j) {
//        return map[i][j];
//    }

    public void openNew() {
        int xCoor;
        int yCoor;
        do {
            xCoor = (int) (Math.random() * size);
            yCoor = (int) (Math.random() * size);
        } while (map[xCoor][yCoor]);
        System.out.println("FOUND!" + xCoor + yCoor);
        //exit only if found closed.
        open(xCoor, yCoor);
    }

    public boolean percolates() {
        System.out.println("Percolates? " + connected(0, id.length - 1));
        //check if first and last are connected
        if (connected(0, id.length - 1)){
            result = this.getOpenedCount()/Math.pow(size,2);
        }
        return connected(0, id.length - 1);
//        for (int i = 0; i < size; i++) {
//            for (int k = 0; k < size; k++) {
//                if (connected((1 + i), (n * n - size - 1 + k))) {
//                    result = this.getOpenedCount()/Math.pow(size,2);
//                    return true;
//                }
//            }
//        }
//        return false;
    }
}
