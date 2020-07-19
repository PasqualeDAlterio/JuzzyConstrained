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
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import type1.sets.T1MF_Interface;
import java.awt.Font;
import java.util.Iterator;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 *
 * @author Pasquale
 */
public class JFreeChartPlotter {
    
    private static final Color[] USABLE_COLORS=new Color[]{Color.MAGENTA, Color.RED, Color.BLACK, Color.CYAN, Color.GREEN, Color.YELLOW, Color.PINK, Color.GRAY};
    private static final Stroke SOLID_LINE=new BasicStroke(4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,1.0f);
    private static final Stroke BASIC_STROKE=new BasicStroke(4.5f);
    private static final Stroke DASHED_STROKE=new BasicStroke(0.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[] {6.0f, 6.0f}, 0.0f);
    private static final Font BASE_FONT=new Font("Dialog", Font.PLAIN, 23);
    //The maximum Y value on the plot is slightly bigger than 1 otherwise the parts where LB=1 and UB=1 wouldn't show correctly
    private static final double Y_AXIS_MAXIMUM=1.005;
    
    public static void plotMFs(String plotName, T1MF_Interface[] sets, Tuple xAxisRange, int xDisc)
    {
        doPlot(getT1Chart(plotName, sets, xAxisRange, xDisc));   
    }
    
    private static JFreeChart getT1Chart(String plotName, T1MF_Interface[] sets, Tuple xAxisRange, int xDisc)
    {
        JFreeChart chart=ChartFactory.createXYLineChart(plotName, "X", "Y", null, PlotOrientation.VERTICAL, true, true, true);
        XYPlot plot=chart.getXYPlot();
        chart.getTitle().setFont(new Font(plotName, Font.BOLD, 30));
        XYSeriesCollection discretized_mfs=new XYSeriesCollection();
        int k=0;
        HashSet<String> series_is=new HashSet<>();
        ValueAxis domainAxis = plot.getDomainAxis();
        ValueAxis rangeAxis = plot.getRangeAxis();
        LegendItemCollection legend=new LegendItemCollection();
        //Discretize the boundary functions
        for(T1MF_Interface current_set : sets)
        {
            discretized_mfs.addSeries(discretizeMembershipFunction(current_set, xDisc, xAxisRange, series_is));
            plot.getRenderer().setSeriesStroke(k, BASIC_STROKE);
            plot.getRenderer().setSeriesPaint(k, getColor(k));
            LegendItem legend_item=new LegendItem(current_set.getName(), getColor(k));
            legend_item.setLabelFont(BASE_FONT);
            legend.add(legend_item);
            k++;
        }
        plot.setDataset(discretized_mfs);
        plot.setFixedLegendItems(legend);
        plot=setupFontsAndBackground(plot);
        //Set the axis
        domainAxis.setRange(xAxisRange.getLeft(), xAxisRange.getRight());
        rangeAxis.setRange(0,Y_AXIS_MAXIMUM);
        return chart;
    }
    
    public static void plotFOUAES(String plotName, IntervalT2MF_Interface[] fou, T1MF_Interface[] aes, Tuple xAxisRange, int xDisc)
    {
        XYSeriesCollection current_boundaries;
        CombinedDomainXYPlot test= new CombinedDomainXYPlot();

        int k=0;
        JFreeChart chart=ChartFactory.createXYAreaChart(plotName, "X", "Y", null, PlotOrientation.VERTICAL, true, true, true);
        XYDifferenceRenderer fou_renderer;
        XYPlot plot=chart.getXYPlot();
        Color current_color;
        chart.getTitle().setFont(new Font(plotName, Font.BOLD, 30));
        LegendItemCollection legend=new LegendItemCollection();
        ValueAxis domainAxis = new NumberAxis();
        ValueAxis rangeAxis = new NumberAxis();
        //Discretize the boundary functions
        for(IntervalT2MF_Interface current_set : fou)
        {
            current_boundaries=discretizeIntervalMembershipFunction(current_set, xDisc, xAxisRange);
            plot.setDataset(k, current_boundaries);
            k++;
        }
        //Give a different color to each FOU
        for(int i=0;i<fou.length;i++)
        {
            current_color=getColor(i);
            LegendItem legend_item=new LegendItem(fou[i].getName(), current_color);
            legend_item.setLabelFont(BASE_FONT);
            legend.add(legend_item);
            fou_renderer=initializeFOURenderer(current_color, 40);
            //Set the renderer for the current set (each DataSet structures contains 2 Series: UB and LB MF)
            plot.setRenderer(i, fou_renderer);
        }
        
        int i=0;
        XYSeriesCollection discretized_mfs;
        HashSet<String> series_is=new HashSet<>();
        //Discretize the boundary functions
        for(T1MF_Interface current_set : aes)
        {
            discretized_mfs=new XYSeriesCollection();
            discretized_mfs.addSeries(discretizeMembershipFunction(current_set, xDisc, xAxisRange, series_is));
//            plot.getRenderer().setSeriesStroke(k, BASIC_STROKE);
//            plot.getRenderer().setSeriesPaint(k, getColor(k));
            plot.setDataset(k,discretized_mfs);
            plot.setRenderer(k, initializeLineRenderer(getColor(i).brighter().brighter()));
            k++;
            i++;
        }
        //Set the axis
        domainAxis.setRange(xAxisRange.getLeft(), xAxisRange.getRight());
        rangeAxis.setRange(0,Y_AXIS_MAXIMUM);
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);
        //Set the legend
        plot.setFixedLegendItems(legend);
        plot.setForegroundAlpha(0.6f);
        plot=setupFontsAndBackground(plot);
//        plot.setSeriesRenderingOrder(SeriesRenderingOrder.REVERSE);
        doPlot(chart);
    }
    
    
    public static void plotMFs(String plotName, IntervalT2MF_Interface[] sets, Tuple xAxisRange, int xDisc)
    {
        doPlot(getFOUChart(plotName, sets, xAxisRange, xDisc));
    }
    
    private static JFreeChart getFOUChart(String plotName, IntervalT2MF_Interface[] sets, Tuple xAxisRange, int xDisc)
    {
        XYSeriesCollection current_boundaries;
        int k=0;
        JFreeChart chart=ChartFactory.createXYAreaChart(plotName, "X", "Y", null, PlotOrientation.VERTICAL, true, true, true);
        XYPlot plot=chart.getXYPlot();
        chart.getTitle().setFont(new Font(plotName, Font.BOLD, 30));
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
            LegendItem legend_item=new LegendItem(sets[i].getName(), current_color);
            legend_item.setLabelFont(BASE_FONT);
            legend.add(legend_item);
            fou_renderer=initializeFOURenderer(current_color, 100);
            //Set the renderer for the current set (each DataSet structures contains 2 Series: UB and LB MF)
            plot.setRenderer(i, fou_renderer);
        }
        //Set the axis
        domainAxis.setRange(xAxisRange.getLeft(), xAxisRange.getRight());
        rangeAxis.setRange(0,Y_AXIS_MAXIMUM);
        //Set the legend
        plot.setFixedLegendItems(legend);
        plot=setupFontsAndBackground(plot);
        return chart;
    }


    private static XYPlot setupFontsAndBackground(XYPlot plot)
    {
        Font font = BASE_FONT;
        TickUnits ticks = new TickUnits();
        for(double i=0;i<=1;i+=0.1)
            ticks.add(new NumberTickUnit(i));
        plot.setDomainGridlineStroke(DASHED_STROKE);
        plot.setRangeGridlineStroke(DASHED_STROKE);
        plot.getDomainAxis().setTickLabelFont(font);
        plot.getRangeAxis().setStandardTickUnits(ticks);
        plot.getRangeAxis().setTickLabelFont(font);
        plot.getDomainAxis().setLabelFont(font);
        plot.getRangeAxis().setLabelFont(font);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint(Color.gray);
        return plot;
    }
    
    private static void doPlot(JFreeChart chart)
    {
        ApplicationFrame frame=new ApplicationFrame("");
        ChartPanel panel=new ChartPanel(chart);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    private static XYDifferenceRenderer initializeFOURenderer(Color color, int alpha)
    {
        Color boundary_color=color;
        if(alpha<100)
        {
            color=new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            boundary_color=new Color(boundary_color.getRed(), boundary_color.getGreen(), boundary_color.getBlue(), alpha);
        }
        //Set FOU color
        XYDifferenceRenderer fou_renderer=new XYDifferenceRenderer(color, color, false);
        //Upperbound color and solid line stroke
        fou_renderer.setSeriesPaint(0, boundary_color.darker().darker());
        fou_renderer.setSeriesStroke(0, SOLID_LINE);
        //Lowerbound color and solid line stroke
        fou_renderer.setSeriesPaint(1, boundary_color.darker().darker());
        fou_renderer.setSeriesStroke(1, SOLID_LINE);
        return fou_renderer;
    }
    
    private static XYLineAndShapeRenderer initializeLineRenderer(Color color)
    {
        XYLineAndShapeRenderer renderer=new XYLineAndShapeRenderer(true, false);
        color=color.brighter().brighter();
        renderer.setSeriesPaint(0, new Color(color.getRed(), color.getGreen(), color.getBlue(), 255));
        renderer.setSeriesStroke(0, new BasicStroke(4.5f));
        renderer.setSeriesOutlinePaint(0, Color.BLACK);
        //renderer.setSeriesShapesFilled(0, false);
        return renderer;
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
        XYSeries lowerbound_discretization=discretizeMembershipFunction(set.getLMF(), points, axis_range, series_id);
        for(int i=0;i<lowerbound_discretization.getItemCount();i++)
        {
            if(lowerbound_discretization.getY(i).doubleValue()==1)
                lowerbound_discretization.updateByIndex(i, 0.995);
        }
        distretized_boundaries.addSeries(lowerbound_discretization);
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
