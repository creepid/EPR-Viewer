/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.bsu.physics.serialcomm;

/**
 *
 * @author mirash
 */
public interface ISerialSettings {
   final static int ADC_TRANS_COMMAND=0xAA;
   
   final static int CHECK_ANSWER_COMMAND=0x31;
   
   final static int RIGHT_LIM_COMMAND=0x33;
       
   final static int COM_TIMEOUT=2000;
   
   final static int CHECK_TIMEOUT=100;
   
   final static String APP_WRITE_NAME="SimpleWriteApp";
   
   final static String APP_READ_NAME="SimpleReadApp";
    
}
