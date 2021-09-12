package Utils;

public class PercolationStats {
    int times, size;
    double meanVal, stddevVal;
    double[] numOpened;

    public PercolationStats(int n, int t) {
        size = n;
        times = t;

        numOpened = new double[t];
        for (int i = 0; i < t; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                System.out.println("OPEN ANOTHER ONE");
                percolation.openNew();
            }
            System.out.println("TEST ________________________ENDED " + i);
//            numOpened[i] = percolation.getOpenedCount();
            numOpened[i] = percolation.result;
            System.out.println("WE GOT DATA " + numOpened[i]);
        }
    }

    public double mean() {
        double sum = 0;
        for (int i = 0; i < numOpened.length; i++) sum += numOpened[i];
        meanVal = sum / numOpened.length;
        return meanVal;
    }
    // рахує середнє

    public double stddev() {
        double sum = 0;
        for (int i = 0; i < numOpened.length; i++) {
            sum += Math.pow((numOpened[i] - meanVal), 2);
        }
        stddevVal = Math.sqrt(sum / (numOpened.length - 1));
        return stddevVal;
    }

    public String limits() {
        double upperLimit, lowerLimit;
        double addition = (1.96 * stddevVal / Math.sqrt(numOpened.length));
        upperLimit = meanVal + addition;
        lowerLimit = meanVal - addition;
        return "(95% interval: [" + lowerLimit + ", " + upperLimit + "]";
    }

    // рахує відхилення
    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(20, 30);
        System.out.println("Mean: " + stats.mean());
        System.out.println("Deviation: " + stats.stddev());
        System.out.println(stats.limits());
    }
    // запускаємо експерименти і рахуємо відповідні значення середнє, відхилення, інтервал довіри, та виводимо на екран

}
