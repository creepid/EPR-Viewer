/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.bsu.physics.serialcomm;

import by.bsu.physics.graph.IGraph;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;
import javax.swing.JOptionPane;

/**
 *
 * @author mirash
 */
public class SerialComm implements ISerialSettings, Runnable, SerialPortEventListener {

 //private static SerialComm instance=null;    
 private List < Integer > dataList = new ArrayList < Integer > ();

 private static String COM_PORT_NUM;
 private static int COM_BOUD_RATE;

 private IGraph graph;

 private InputStream inputStream;
 private Thread readThread;
 private String portIdName;
 private String command;
 private String messageDlg;
 private boolean isCheckAnswer = false;
 private boolean isTransStart = false;
 private boolean isFirstByte = false;

 private int commTimeMilliSec;

 private static int firstByte = 0;
 private static int secondByte = 0;

 private int numBytes = 0;

 private static OutputStream outputStream;
 private static CommPortIdentifier portId;
 private static Enumeration portList;
 private static SerialPort serialPort;


 public void getCommunication(String command) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
  portList = CommPortIdentifier.getPortIdentifiers();
  int commToSend = CommandFactory.getCommand(command);
  while (portList.hasMoreElements()) {
   portId = (CommPortIdentifier) portList.nextElement();
   if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
    if (portId.getName().equals(COM_PORT_NUM)) {
     portIdName = portId.getName();
     try {
      serialPort = (SerialPort) portId.open(APP_WRITE_NAME, COM_TIMEOUT);
     } catch (PortInUseException e) {}

     try {
      outputStream = serialPort.getOutputStream();
     } catch (IOException e) {}
     try {
      serialPort.setSerialPortParams(COM_BOUD_RATE,
       SerialPort.DATABITS_8,
       SerialPort.STOPBITS_1,
       SerialPort.PARITY_NONE);
     } catch (UnsupportedCommOperationException e) {}

     try {
      if (commToSend != -1)
       outputStream.write(commToSend);
      System.out.println(commToSend);
     } catch (IOException e) {}

    }
   }
  }
 }


 public void getCommData() {
  try {
   serialPort = (SerialPort) portId.open(APP_READ_NAME, COM_TIMEOUT);
  } catch (PortInUseException e) {}

  try {
   inputStream = serialPort.getInputStream();
  } catch (IOException e) {
   System.out.println("Cannot open input stream");
  }
  try {
   serialPort.addEventListener(this);
  } catch (TooManyListenersException e) {}

  serialPort.notifyOnDataAvailable(true);
  try {
   serialPort.setSerialPortParams(COM_BOUD_RATE,
    SerialPort.DATABITS_8,
    SerialPort.STOPBITS_1,
    SerialPort.PARITY_NONE);
  } catch (UnsupportedCommOperationException e) {}
 }

 public void serialEvent(SerialPortEvent event) {
  switch (event.getEventType()) {
   case SerialPortEvent.BI:
    break;
   case SerialPortEvent.OE:
    break;
   case SerialPortEvent.FE:
    break;
   case SerialPortEvent.PE:
    break;
   case SerialPortEvent.CD:
    break;
   case SerialPortEvent.CTS:
    break;
   case SerialPortEvent.DSR:
    break;
   case SerialPortEvent.RI:
    break;
   case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
    break;
   case SerialPortEvent.DATA_AVAILABLE:
    try {
     while (inputStream.available() > 0) {
      numBytes = inputStream.read();

      if ((this.command.equals("check")) && (numBytes == CHECK_ANSWER_COMMAND))
       this.isCheckAnswer = true;

      if ((this.command.equals("primary_position")) && (numBytes == RIGHT_LIM_COMMAND))
       JOptionPane.showMessageDialog(null, messageDlg, "Information", JOptionPane.INFORMATION_MESSAGE);

      if (this.command.equals("start")) {
       if ((isTransStart == true) && (numBytes != CommandFactory.getCommand("start")) && (numBytes != CommandFactory.getCommand("stop"))) {
        if (isFirstByte == false) {
         secondByte = numBytes;
         System.out.println(secondByte);
         isFirstByte = true;
        } else {
         firstByte = numBytes;
         dataList.add((secondByte << 8) + firstByte);
         System.out.println(firstByte);
         isTransStart = false;
         isFirstByte = false;
        }
       }

       if (numBytes == ADC_TRANS_COMMAND)
        isTransStart = true;
      }
     }
    } catch (IOException e) {
     //System.out.println("There is no byte to read.");
    }
    break;
  }
 }

 public static void setCOM_BOUD_RATE(int COM_BOUD_RATE) {
  SerialComm.COM_BOUD_RATE = COM_BOUD_RATE;
 }

 public static void setCOM_PORT_NUM(String COM_PORT_NUM) {
  SerialComm.COM_PORT_NUM = COM_PORT_NUM;
 }

 public static int getCOM_BOUD_RATE() {
  return COM_BOUD_RATE;
 }

 public static String getCOM_PORT_NUM() {
  return COM_PORT_NUM;
 }


 public SerialComm(String command) {

  try {
   getCommunication(command);
   this.command = command;
  } catch (ClassNotFoundException e1) {
   // TODO Auto-generated catch block
   e1.printStackTrace();
  } catch (InstantiationException e1) {
   // TODO Auto-generated catch block
   e1.printStackTrace();
  } catch (IllegalAccessException e1) {
   // TODO Auto-generated catch block
   e1.printStackTrace();
  }
  if (command.equals("check"))
   this.commTimeMilliSec = CHECK_TIMEOUT;

  if (command.equals("start") || command.equals("check"))
   getCommData();
  readThread = new Thread(this);
  readThread.start();
 }

 /*public static SerialComm getInstance(String command){
     if (instance==null){
         instance=new SerialComm(command);}
     return instance;
 }*/

 public void run() {
  try {
   if (commTimeMilliSec != 0) {
    Thread.sleep(commTimeMilliSec);
    getCommunication("stop");
    graph.redraw(dataList);
   }
   System.out.println("End of thread.");
  } catch (Exception e) {}
  serialPort.close();
 }


 public List < Integer > getDataList() {
  return dataList;
 }

 public void setDataList(List < Integer > dataList) {
  this.dataList = dataList;
 }


 public int getCommTimeMilliSec() {
  return commTimeMilliSec;
 }


 public void setCommTimeSec(int commTimeSec) {
  this.commTimeMilliSec = commTimeSec;
 }

 public boolean isCheckAnswer() {
  return isCheckAnswer;
 }


 public void setGraph(IGraph graph) {
  this.graph = graph;
 }

 public void setMessageDlg(String messageDlg) {
  this.messageDlg = messageDlg;
 }

 public String getMessageDlg() {
  return messageDlg;
 }






}