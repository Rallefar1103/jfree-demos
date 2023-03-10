package org.jfree.chart.Reflection;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFactoryReflection;
import org.jfree.chart.ChartLookupTable;
import org.jfree.chart.IReflectionFactory;
import org.jfree.chart.InvalidChartNameException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.MissingParamsException;
import org.jfree.chart.ReflectionStrategy;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.swing.ApplicationFrame;
import org.jfree.chart.swing.ChartPanel;
import org.jfree.chart.swing.UIUtils;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class SetTitleAndBackgroundReflection extends ApplicationFrame {
    /**
     * Creates a new demo instance.
     *
     * @param title the frame title.
     * @throws InvalidChartNameException
     * @throws MissingParamsException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public SetTitleAndBackgroundReflection(String title)
            throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, ClassNotFoundException, InstantiationException,
            MissingParamsException, InvalidChartNameException {
        super(title);
        CategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
    }

    /**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(7445, "JFreeSVG", "Warm-up");
        dataset.addValue(24448, "Batik", "Warm-up");
        dataset.addValue(4297, "JFreeSVG", "Test");
        dataset.addValue(21022, "Batik", "Test");
        return dataset;
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset the dataset.
     *
     * @return The chart.
     * @throws InvalidChartNameException
     * @throws MissingParamsException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    private JFreeChart createChart(CategoryDataset dataset)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, MissingParamsException, InvalidChartNameException, ClassNotFoundException,
            InstantiationException {

        JFreeChart chart = ChartFactory.getChartRegular("BarChart", "EmptyTitle", "Milliseconds", "Milliseconds",
                dataset);

        ArrayList<Object> titleParams = new ArrayList<Object>();
        titleParams.add("ReflectionAddedTitle");

        ReflectionStrategy strategy = new ReflectionStrategy(chart);
        strategy.setTitle("public void setTitleOnChart(String title)", titleParams);

        ArrayList<Object> paintParams = new ArrayList<Object>();
        paintParams.add(Color.WHITE);
        strategy.setBackgroundPaint("public void setBackgroundPaintOnChart(Paint paint)", paintParams);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        return chart;
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args ignored.
     * @throws InvalidChartNameException
     * @throws MissingParamsException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, ClassNotFoundException, InstantiationException,
            MissingParamsException, InvalidChartNameException {
        CreateBarChartReflection demo = new CreateBarChartReflection("JFreeChart: BarChartDemo1.java");
        demo.pack();
        UIUtils.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
