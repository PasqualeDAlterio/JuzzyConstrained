/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import generic.Tuple;
import intervalType2.sets.IntervalT2MF_Interface;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.HashSet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import type1.sets.T1MF_Interface;

/**
 *
 * @author User
 */
public class JFreeChartPlotter {
    
    private static final Color[] USABLE_COLORS=new Color[]{Color.CYAN, Color.WHITE, Color.GREEN, Color.MAGENTA, Color.GRAY, Color.BLACK, Color.YELLOW, Color.PINK, Color.RED};
    private static final Stroke SOLID_LINE=new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,1.0f);
    private static final Stroke BASIC_STROKE=new BasicStroke(2.5f);
    
    public static void plotMFs(String plotName, T1MF_Interface[] sets, Tuple xAxisRange, int xDisc)
    {
        JFreeChart chart=ChartFactory.createXYLineChart(plotName, "X", "Y", null, PlotOrientation.VERTICAL, true, true, true);
        XYPlot plot=chart.getXYPlot();
        XYSeriesCollection discretized_mfs=new XYSeriesCollection();
        int k=0;
        HashSet<String> series_is=new HashSet<>();
        ValueAxis domainAxis = plot.getDomainAxis();
        ValueAxis rangeAxis = plot.getRangeAxis();
        //Discretize the boundary functions
        for(T1MF_Interface current_set : sets)
        {
            discretized_mfs.addSeries(discretizeMembershipFunction(current_set, xDisc, xAxisRange, series_is));
            plot.getRenderer().setSeriesStroke(k, BASIC_STROKE);
            k++;
        }
        plot.setDataset(discretized_mfs);
        //Set the axis
        domainAxis.setRange(xAxisRange.getLeft(), xAxisRange.getRight());
        rangeAxis.setRange(0,1);
        doPlot(chart);
        
    }
    
    
    public static void plotMFs(String plotName, IntervalT2MF_Interface[] sets, Tuple xAxisRange, int xDisc)
    {
        XYSeriesCollection current_boundaries;
        int k=0;
        JFreeChart chart=ChartFactory.createXYAreaChart(plotName, "X", "Y", null, PlotOrientation.VERTICAL, true, true, true);
        XYPlot plot=chart.getXYPlot();
        XYDifferenceRenderer fou_renderer;
        Color current_color;
        LegendItemCollection legend=new LegendItemCollection();
        ValueAxis domainAxis = plot.getDomainAxis();
        ValueAxis rangeAxis = plot.getRangeAxis();
        //Discretize the boundary functions
        for(IntervalT2MF_Interface current_set : sets)
        {
            current_boundaries=discretizeIntervalMembershipFunction(current_set, xDisc, xAxisRange);
            plot.setDataset(k, current_boundaries);
            k++;
        }
        //Give a different color to each FOU
        for(int i=0;i<sets.length;i++)
        {
            current_color=getColor(i);
            legend.add(new LegendItem(sets[i].getName(), current_color));
            fou_renderer=initializeFOURenderer(current_color);
            //Set the renderer for the current set (each DataSet structures contains 2 Series: UB and LB MF)
            plot.setRenderer(i, fou_renderer);
        }
        //Set the axis
        domainAxis.setRange(xAxisRange.getLeft(), xAxisRange.getRight());
        rangeAxis.setRange(0,1);
        //Set the legend
        plot.setFixedLegendItems(legend);
        doPlot(chart);
    }
    
    private static void doPlot(JFreeChart chart)
    {
        ApplicationFrame frame=new ApplicationFrame("");
        ChartPanel panel=new ChartPanel(chart);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    private static XYDifferenceRenderer initializeFOURenderer(Color color)
    {
        //Set FOU color
        XYDifferenceRenderer fou_renderer=new XYDifferenceRenderer(color, color, false);
        //Upperbound color and solid line stroke
        fou_renderer.setSeriesPaint(0, Color.black);
        fou_renderer.setSeriesStroke(0, SOLID_LINE );
        //Lowerbound color and solid line stroke
        fou_renderer.setSeriesPaint(1, Color.black);
        fou_renderer.setSeriesStroke(1, SOLID_LINE);
        return fou_renderer;
    }
    
    private static Color getColor(int index)
    {
        return USABLE_COLORS[index%USABLE_COLORS.length];
    }
    
    private static XYSeriesCollection discretizeIntervalMembershipFunction(IntervalT2MF_Interface set, int points, Tuple axis_range)
    {
        XYSeriesCollection distretized_boundaries=new XYSeriesCollection();
        HashSet<String> series_id=new HashSet<>();
        distretized_boundaries.addSeries(discretizeMembershipFunction(set.getUMF(), points, axis_range, series_id));
        distretized_boundaries.addSeries(discretizeMembershipFunction(set.getLMF(), points, axis_range, series_id));
        return distretized_boundaries;
    }
    
    public static XYSeries discretizeMembershipFunction(T1MF_Interface set, int points, Tuple axis_range, HashSet<String> series_ids)
    {
        String current_id=set.getName();
        //Find a suitable ID that has not been used before
        while(!series_ids.add(current_id))
            current_id+=(" (duplicate)");
        XYSeries discretized_mf=new XYSeries(current_id);
        for(double i=axis_range.getLeft();i<axis_range.getRight();i+=axis_range.getSize()/points)
            discretized_mf.add(i, set.getFS(i));
        return discretized_mf;
    }
    

}
