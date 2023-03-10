/* ==================
 * PieChartDemo1.java
 * ==================
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

package org.jfree.chart.DynamicProxy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFactoryReflection;
import org.jfree.chart.ChartInvocationHandler;
import org.jfree.chart.ChartLookupTable;
import org.jfree.chart.DynProxInvocHandler;
import org.jfree.chart.DynamicProxyChartCreator;
import org.jfree.chart.DynamicProxyStrategy;
import org.jfree.chart.IReflectionFactory;
import org.jfree.chart.InvalidChartNameException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.MissingParamsException;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.api.HorizontalAlignment;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.api.RectangleInsets;
import org.jfree.chart.plot.pie.PiePlot;
import org.jfree.chart.swing.ApplicationFrame;
import org.jfree.chart.swing.ChartPanel;
import org.jfree.chart.swing.UIUtils;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * A simple demonstration application showing how to create a pie chart using
 * data from a {@link DefaultPieDataset}.
 */
public class CreatePieChartDynamicProxy extends ApplicationFrame {

        private final long serialVersionUID = 1L;
        private ChartFactoryReflection factory;
        private ArrayList<Object> params = new ArrayList<Object>();
        private DynamicProxyChartCreator proxyCreator;
        private ChartInvocationHandler invocHandler;

        /**
         * Default constructor.
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
        public CreatePieChartDynamicProxy(String title)
                        throws NoSuchMethodException, SecurityException, IllegalAccessException,
                        IllegalArgumentException, InvocationTargetException, ClassNotFoundException,
                        InstantiationException,
                        MissingParamsException, InvalidChartNameException {
                super(title);
                setContentPane(createDemoPanel());
        }

        /**
         * Creates a sample dataset.
         * 
         * Source: http://www.bbc.co.uk/news/business-15489523
         *
         * @return A sample dataset.
         */
        private PieDataset createDataset() {
                DefaultPieDataset dataset = new DefaultPieDataset();
                dataset.setValue("Samsung", new Double(27.8));
                dataset.setValue("Others", new Double(55.3));
                dataset.setValue("Nokia", new Double(16.8));
                dataset.setValue("Apple", new Double(17.1));
                return dataset;
        }

        /**
         * Creates a chart.
         *
         * @param dataset the dataset.
         *
         * @return A chart.
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
        private JFreeChart createChart(PieDataset dataset) throws NoSuchMethodException, SecurityException,
                        IllegalAccessException, IllegalArgumentException, InvocationTargetException,
                        ClassNotFoundException,
                        InstantiationException, MissingParamsException, InvalidChartNameException {

                this.params.add("Smart Phones Manufactured / Q3 2011");
                this.params.add(dataset);
                this.params.add(false);
                this.params.add(true);
                this.params.add(false);

                this.factory = new ChartFactoryReflection();
                this.invocHandler = new ChartInvocationHandler(this.factory);
                this.proxyCreator = new DynamicProxyChartCreator(this.invocHandler);

                String classPath = ChartLookupTable.chartLookupTable.get("PieChart");
                JFreeChart chart = this.proxyCreator.getChartObject(classPath, this.params);

                // set a custom background for the chart
                DynProxInvocHandler handler = new DynProxInvocHandler(chart);
                DynamicProxyStrategy strategy = new DynamicProxyStrategy(handler);

                strategy.setBackgroundPaint(new GradientPaint(new Point(0, 0),
                                new Color(20, 20, 20), new Point(400, 200), Color.DARK_GRAY));

                strategy.setTitle(new TextTitle("DynamicProxyTitle"));

                // set a custom background for the chart
                // chart.setBackgroundPaint(new GradientPaint(new Point(0, 0),
                // new Color(20, 20, 20), new Point(400, 200), Color.DARK_GRAY));

                // customise the title position and font
                TextTitle t = chart.getTitle();
                t.setHorizontalAlignment(HorizontalAlignment.LEFT);
                t.setPaint(new Color(240, 240, 240));
                t.setFont(new Font("Arial", Font.BOLD, 26));

                PiePlot plot = (PiePlot) chart.getPlot();
                plot.setBackgroundPaint(null);
                plot.setInteriorGap(0.04);
                plot.setOutlineVisible(false);

                // use gradients and white borders for the section colours
                plot.setSectionPaint("Others",
                                createGradientPaint(new Color(200, 200, 255), Color.BLUE));
                plot.setSectionPaint("Samsung",
                                createGradientPaint(new Color(255, 200, 200), Color.RED));
                plot.setSectionPaint("Apple",
                                createGradientPaint(new Color(200, 255, 200), Color.GREEN));
                plot.setSectionPaint("Nokia",
                                createGradientPaint(new Color(200, 255, 200), Color.YELLOW));
                plot.setDefaultSectionOutlinePaint(Color.WHITE);
                plot.setSectionOutlinesVisible(true);
                plot.setDefaultSectionOutlineStroke(new BasicStroke(2.0f));

                // customise the section label appearance
                plot.setLabelFont(new Font("Courier New", Font.BOLD, 20));
                plot.setLabelLinkPaint(Color.WHITE);
                plot.setLabelLinkStroke(new BasicStroke(2.0f));
                plot.setLabelOutlineStroke(null);
                plot.setLabelPaint(Color.WHITE);
                plot.setLabelBackgroundPaint(null);

                // add a subtitle giving the data source
                TextTitle source = new TextTitle("Source: http://www.bbc.co.uk/news/business-15489523",
                                new Font("Courier New", Font.PLAIN, 12));
                source.setPaint(Color.WHITE);
                source.setPosition(RectangleEdge.BOTTOM);
                source.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                chart.addSubtitle(source);
                return chart;

        }

        /**
         * A utility method for creating gradient paints.
         * 
         * @param c1 color 1.
         * @param c2 color 2.
         * 
         * @return A radial gradient paint.
         */
        private RadialGradientPaint createGradientPaint(Color c1, Color c2) {
                Point2D center = new Point2D.Float(0, 0);
                float radius = 200;
                float[] dist = { 0.0f, 1.0f };
                return new RadialGradientPaint(center, radius, dist,
                                new Color[] { c1, c2 });
        }

        /**
         * Creates a panel for the demo (used by SuperDemo.java).
         *
         * @return A panel.
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
        public JPanel createDemoPanel() throws NoSuchMethodException, SecurityException, IllegalAccessException,
                        IllegalArgumentException, InvocationTargetException, ClassNotFoundException,
                        InstantiationException,
                        MissingParamsException, InvalidChartNameException {
                JFreeChart chart = createChart(createDataset());
                chart.setPadding(new RectangleInsets(4, 8, 2, 2));
                ChartPanel panel = new ChartPanel(chart, false);
                panel.setMouseWheelEnabled(true);
                panel.setPreferredSize(new Dimension(600, 300));
                return panel;
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
                        IllegalArgumentException, InvocationTargetException, ClassNotFoundException,
                        InstantiationException,
                        MissingParamsException, InvalidChartNameException {
                CreatePieChartDynamicProxy demo = new CreatePieChartDynamicProxy("JFreeChart: Pie Chart Demo 1");
                demo.pack();
                UIUtils.centerFrameOnScreen(demo);
                demo.setVisible(true);
        }

}
