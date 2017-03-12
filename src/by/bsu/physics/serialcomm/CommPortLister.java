/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.bsu.physics.serialcomm;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.comm.CommPortIdentifier;

/**
 *
 * @author mirash
 */
public class CommPortLister {
    
private List<String> commList;

public CommPortLister(){
    commList=new ArrayList<String>();
}
    
public List<String> getList() {
    Enumeration pList = CommPortIdentifier.getPortIdentifiers();
    while (pList.hasMoreElements()) {
      CommPortIdentifier cpi = (CommPortIdentifier) pList.nextElement();
      if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        commList.add(cpi.getName());
      }     
    }
    return commList;
 }


}