import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class Main {
    private static final double S = 0.55; // норма сбережения
    private static double sigma = 1.9; // средняя производительность капитала
    private static final double Y0 = 40; // начальное значение выпуска
    private static final int T = 11; // расчетный интервал
    private static final int DIGITS_AFTER_COMMA = 4; // знаков после запятой
    public static final double[] SIGMAS = {1.2, 1.6, 1.8, 2};

    public static void main(String[] args) {
        double[] y = new double[T + 1];
        y[0] = round(Y0);

        double[] c = new double[T + 1];
        double[] i = new double[T + 1];
        double[] deltaY = new double[T + 1];
        double[] deltaC = new double[T + 1];
        double[] deltaI = new double[T + 1];

        for (int j = 0; j <= T; j++) {
            c[j] = round((1 - S) * y[j]);
            i[j] = round(S * y[j]);
            deltaY[j] = round(sigma * i[j]);
            deltaC[j] = round((1 - S) * deltaY[j]);
            deltaI[j] = round(S * deltaY[j]);

            if (j < T) {
                y[j + 1] = round(y[j] + deltaY[j]);
            }
        }

        System.out.println("\tY(t)\t|\t" +
                "C(t)\t|\t" +
                "I(t)\t||\t" +
                "delta Y(t)\t|\t" +
                "delta C(t)\t|\t" +
                "delta I(t)");
        for (int j = 0; j <= T; j++) {
            System.out.println("[" + j + "]: " +
                    y[j] + "\t|\t" +
                    c[j] + "\t|\t" +
                    i[j] + "\t||\t" +
                    deltaY[j] + "\t|\t" +
                    deltaC[j] + "\t|\t" +
                    deltaI[j]);
        }


        System.out.println("\n\tПроверка, равны ли дельты \"sigma*s\":");
        double sigma2S = round(sigma * S);
        boolean areDeltasEqualSigma2S = true;
        for (int j = 0; j <= T; j++) {
            double deltaY2Y = round(deltaY[j] / y[j]);
            double deltaC2C = round(deltaC[j] / c[j]);
            double deltaI2I = round(deltaI[j] / i[j]);

            if (sigma2S != deltaY2Y || sigma2S != deltaC2C || sigma2S != deltaI2I) {
                areDeltasEqualSigma2S = false;
                break;
            }
        }

        if (areDeltasEqualSigma2S) {
            System.out.println("Проверка выполнена успешно!");
        } else {
            System.out.println("ОШИБКА! Проверка не выполнена!");
        }


        int length = 10;
        double[] y1 = new double[length];
        double[] deltaY1 = new double[length];

        yAndDeltaOfSigma(y1, deltaY1, length, SIGMAS[0]);

        double[] y2 = new double[length];
        double[] deltaY2 = new double[length];

        yAndDeltaOfSigma(y2, deltaY2, length, SIGMAS[1]);

        double[] y3 = new double[length];
        double[] deltaY3 = new double[length];

        yAndDeltaOfSigma(y3, deltaY3, length, SIGMAS[2]);

        double[] y4 = new double[length];
        double[] deltaY4 = new double[length];

        yAndDeltaOfSigma(y4, deltaY4, length, SIGMAS[3]);

        System.out.print("\nsigma = ");
        for (int j = 0; j < SIGMAS.length; j++) {
            if (j == SIGMAS.length - 1) {
                System.out.print("\t\t" + SIGMAS[j]);
                break;
            }
            System.out.print("\t\t" + SIGMAS[j] + "\t\t\t|\t");
        }

        System.out.println("\n\tY1(t)\t|\tdelta1 Y(t)" +
                "\t||\tY2(t)\t|\tdelta2 Y(t)" +
                "\t||\tY3(t)\t|\tdelta3 Y(t)" +
                "\t||\tY4(t)\t|\tdelta4 Y(t)");
        for (int j = 0; j < length; j++) {
            System.out.println("[" + j + "]: " +
                    y1[j] + "\t|\t" + deltaY1[j] + "\t||\t" +
                    y2[j] + "\t|\t" + deltaY2[j] + "\t||\t" +
                    y3[j] + "\t|\t" + deltaY3[j] + "\t||\t" +
                    y4[j] + "\t|\t" + deltaY4[j]);
        }


        // Построение графиков Y(t), C(t) и I(t)
        XYSeries seriesY = new XYSeries("Y(t)");
        XYSeries seriesC = new XYSeries("C(t)");
        XYSeries seriesI = new XYSeries("I(t)");

        for (int j = 0; j <= T; j++) {
            seriesY.add(j, y[j]);
            seriesC.add(j, c[j]);
            seriesI.add(j, i[j]);
        }

        XYSeriesCollection xy1 = new XYSeriesCollection(seriesY);
        xy1.addSeries(seriesC);
        xy1.addSeries(seriesI);

        createChart(xy1, "Chart1");


        // Построение графиков Y1(t), Y2(t), Y3(t) и Y4(t)
        XYSeries seriesY1 = new XYSeries("Y1(t)");
        XYSeries seriesY2 = new XYSeries("Y2(t)");
        XYSeries seriesY3 = new XYSeries("Y3(t)");
        XYSeries seriesY4 = new XYSeries("Y4(t)");

        for (float j = 0.1f; j < 1.1; j += 0.1) {
            int index = (int) (j * 10 - 1);
            seriesY1.add(j, y1[index]);
            seriesY2.add(j, y2[index]);
            seriesY3.add(j, y3[index]);
            seriesY4.add(j, y4[index]);
        }

        XYSeriesCollection xy2 = new XYSeriesCollection(seriesY1);
        xy2.addSeries(seriesY2);
        xy2.addSeries(seriesY3);
        xy2.addSeries(seriesY4);

        createChart(xy2, "Chart2");
    }

    private static double round(double number) {
        return (double) Math.round(number * Math.pow(10, Main.DIGITS_AFTER_COMMA)) / Math.pow(10, Main.DIGITS_AFTER_COMMA);
    }

    private static void createChart(XYSeriesCollection xy, String chartName) {
        JFreeChart chart = ChartFactory
                .createXYLineChart("", "t", "", xy, PlotOrientation.VERTICAL, true, true, true);
        JFrame frame = new JFrame(chartName);
        frame.getContentPane().add(new ChartPanel(chart));
        frame.setSize(400, 300);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private static void yAndDeltaOfSigma(double[] y, double[] deltaY, int length, double sigma) {
        y[0] = round(Y0);

        for (int j = 0; j < length; j++) {
            deltaY[j] = round(sigma * S * y[j]);

            if (j < length - 1) {
                y[j + 1] = round(y[j] + deltaY[j]);
            }
        }
    }
}
