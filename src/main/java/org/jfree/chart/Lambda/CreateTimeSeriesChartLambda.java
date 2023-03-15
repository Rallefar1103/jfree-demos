/* =========================
 * TimeSeriesChartDemo1.java
 * =========================
 *
 * Copyright 2013-2022, by David Gilbert. All rights reserved.
 *
 * https://github.com/jfree/jfree-demos
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   - Neither the name of the JFree organisation nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL OBJECT REFINERY LIMITED BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.jfree.chart.Lambda;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartLookupTable;
import org.jfree.chart.ISetBackground;
import org.jfree.chart.ISetTitle;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LambdaChartCreator;
import org.jfree.chart.LambdaStrategy;
import org.jfree.chart.NewChart;
import org.jfree.chart.api.RectangleInsets;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.swing.ApplicationFrame;
import org.jfree.chart.swing.ChartPanel;
import org.jfree.chart.swing.UIUtils;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;

/**
 * An example of a time series chart create using JFreeChart. For the most
 * part, default settings are used, except that the renderer is modified to
 * show filled shapes (as well as lines) at each data point.
 */
public class CreateTimeSeriesChartLambda extends ApplicationFrame {

    private final long serialVersionUID = 1L;
    private LambdaChartCreator lambdaCreator;

    /**
     * A demonstration application showing how to create a simple time series
     * chart. This example uses monthly data.
     *
     * @param title the frame title.
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     */
    public CreateTimeSeriesChartLambda(String title)
            throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, InstantiationException {
        super(title);
        ChartPanel chartPanel = (ChartPanel) createDemoPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    /**
     * Creates a chart.
     *
     * @param dataset a dataset.
     *
     * @return A chart.
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     */
    private JFreeChart createChart(XYDataset dataset)
            throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, InstantiationException {

        // Init the lamda chart creator class
        this.lambdaCreator = new LambdaChartCreator();

        // create the functional interface to dynamically invoke the static get chart
        // regular method
        NewChart<String, XYDataset, JFreeChart> function = (type, title, category, value,
                chartData) -> ChartFactory.getChartRegular(type, title, category, value, chartData);

        // dynamically create the TimeSeriesChart using the functional interface and the
        // lambdaCreator createChart method
        JFreeChart chart = this.lambdaCreator.createChart(function, "TimeSeriesChart",
                "Legal & General Unit Trust Prices", "Date", "Price Per Unit", dataset);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setDefaultShapesVisible(true);
            renderer.setDefaultShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }

        // Init the lambda strategy class
        LambdaStrategy strategy = new LambdaStrategy<>();

        // init the functional interface to setting the title
        ISetTitle<String> titleFunc = (title) -> chart.setTitle(title);

        // invoke the setTitle method using the functional interface on runtime
        strategy.setTitle(titleFunc, "LambdaTitle");

        // init the functional interface to setting the background
        ISetBackground<Paint> backgroundFunc = (color) -> chart.setBackgroundPaint(color);

        // invoke the setBackgroundPaint method using the function interface
        strategy.setBackgroundPaint(backgroundFunc, Color.WHITE);

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));

        return chart;

    }

    /**
     * Creates a dataset, consisting of two series of monthly data.
     *
     * @return The dataset.
     */
    private XYDataset createDataset() {

        TimeSeries s1 = new TimeSeries("L&G European Index Trust");
        s1.add(new Month(2, 2001), 181.8);
        s1.add(new Month(3, 2001), 167.3);
        s1.add(new Month(4, 2001), 153.8);
        s1.add(new Month(5, 2001), 167.6);
        s1.add(new Month(6, 2001), 158.8);
        s1.add(new Month(7, 2001), 148.3);
        s1.add(new Month(8, 2001), 153.9);
        s1.add(new Month(9, 2001), 142.7);
        s1.add(new Month(10, 2001), 123.2);
        s1.add(new Month(11, 2001), 131.8);
        s1.add(new Month(12, 2001), 139.6);
        s1.add(new Month(1, 2002), 142.9);
        s1.add(new Month(2, 2002), 138.7);
        s1.add(new Month(3, 2002), 137.3);
        s1.add(new Month(4, 2002), 143.9);
        s1.add(new Month(5, 2002), 139.8);
        s1.add(new Month(6, 2002), 137.0);
        s1.add(new Month(7, 2002), 132.8);

        TimeSeries s2 = new TimeSeries("L&G UK Index Trust");
        s2.add(new Month(2, 2001), 129.6);
        s2.add(new Month(3, 2001), 123.2);
        s2.add(new Month(4, 2001), 117.2);
        s2.add(new Month(5, 2001), 124.1);
        s2.add(new Month(6, 2001), 122.6);
        s2.add(new Month(7, 2001), 119.2);
        s2.add(new Month(8, 2001), 116.5);
        s2.add(new Month(9, 2001), 112.7);
        s2.add(new Month(10, 2001), 101.5);
        s2.add(new Month(11, 2001), 106.1);
        s2.add(new Month(12, 2001), 110.3);
        s2.add(new Month(1, 2002), 111.7);
        s2.add(new Month(2, 2002), 111.0);
        s2.add(new Month(3, 2002), 109.6);
        s2.add(new Month(4, 2002), 113.2);
        s2.add(new Month(5, 2002), 111.6);
        s2.add(new Month(6, 2002), 108.8);
        s2.add(new Month(7, 2002), 101.6);

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);

        return dataset;

    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     *
     * @return A panel.
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     */
    public JPanel createDemoPanel() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        JFreeChart chart = createChart(createDataset());
        ChartPanel panel = new ChartPanel(chart, false);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        return panel;
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args ignored.
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {

        CreateTimeSeriesChartLambda demo = new CreateTimeSeriesChartLambda(
                "Time Series Chart Demo 1");
        demo.pack();
        UIUtils.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
