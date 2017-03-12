/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.bsu.physics.graph;

import java.util.List;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author mirash
 */
public interface IGraph {
       
    public void redraw(List<Integer> DataToView);
    
    
}
