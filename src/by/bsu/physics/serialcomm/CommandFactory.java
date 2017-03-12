/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.bsu.physics.serialcomm;

/**
 *
 * @author mirash
 */
public class CommandFactory {
	
public static int getCommand(String Type){
	CommandType commandType=CommandType.valueOf(Type.toUpperCase());		
			switch(commandType){
				case START: return 0x34;
				case CHECK: return 0x30;
				case PRIMARY_POSITION: return 0x32;
				case STOP: return 0x36;
				default: return (-1);
			}
			
	}

}
