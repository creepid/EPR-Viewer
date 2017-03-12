/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.bsu.physics.graph;

import java.awt.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author mirash
 */
public class GraphPanel extends JPanel implements IGraph{
    
     /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    final static String CHART_TITLE="EPR amplitude";
    final static String CHART_X_RANGE_LABEL="X Range";
    final static String CHART_Y_RANGE_LABEL="Amplitude";
    final static String SERIES_NAME="Main series";
    
    private List<Integer> DataToView=new ArrayList<Integer>();
    private XYSeriesCollection dataset=new XYSeriesCollection();
    private JFreeChart chart;
    private ChartPanel chartPanel;
    
    
    public GraphPanel() {
       super();
       this.redraw(DataToView);
    }

  private synchronized JFreeChart createGraph(final XYDataset dataset) {
            
       chart = ChartFactory.createXYAreaChart(
            CHART_TITLE,
            CHART_X_RANGE_LABEL, 
            CHART_Y_RANGE_LABEL,
            dataset,
            PlotOrientation.VERTICAL,
            true,  // legend
            true,  // tool tips
            false  // URLs
        );
        
        chart.setBackgroundPaint(Color.white);
        
        final XYPlot plot = chart.getXYPlot();
            //plot.setOutlinePaint(Color.black);
            plot.setBackgroundPaint(Color.lightGray);
            plot.setForegroundAlpha(0.65f);
            plot.setDomainGridlinePaint(Color.white);
            plot.setRangeGridlinePaint(Color.white);
        
        final ValueAxis domainAxis = plot.getDomainAxis();
            domainAxis.setTickMarkPaint(Color.black);
            domainAxis.setLowerMargin(0.0);
            domainAxis.setUpperMargin(0.0);
        
        final ValueAxis rangeAxis = plot.getRangeAxis();
            rangeAxis.setTickMarkPaint(Color.black);
        
        return chart;
    }
    
    public synchronized void redraw(List<Integer> DataToView){
        this.DataToView=DataToView;
        this.removeAll();
        
        this.construct(this.DataToView);
        
        chart = createGraph(dataset);
        
        chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        this.add(chartPanel);
        this.validate();
    }
    
   private synchronized void construct(List<Integer> DataToView){
        this.DataToView=DataToView;
        dataset.removeAllSeries();
        XYSeries series = new XYSeries(SERIES_NAME);
        int i=0;
        Iterator it=DataToView.iterator();
        
        while (it.hasNext()){
            series.add(i, Integer.valueOf(it.next().toString()));
            i++;
        }
        dataset = new XYSeriesCollection(series);
    }

    public List<Integer> getDataToView() {
        return DataToView;
    }
  
    
}
