package util;

import org.sercho.masp.models.Context.Blind;
import org.sercho.masp.models.Context.CookTop;
import org.sercho.masp.models.Context.Cooker;
import org.sercho.masp.models.Context.Dishwasher;
import org.sercho.masp.models.Context.Fan;
import org.sercho.masp.models.Context.Fridge;
import org.sercho.masp.models.Context.Heater;
import org.sercho.masp.models.Context.HeatingRod;
import org.sercho.masp.models.Context.Hob;
import org.sercho.masp.models.Context.Hood;
import org.sercho.masp.models.Context.Lamp;
import org.sercho.masp.models.Context.Notebook;
import org.sercho.masp.models.Context.Oven;
import org.sercho.masp.models.Context.PC;
import org.sercho.masp.models.Context.PhysicalDevice;
import org.sercho.masp.models.Context.Radio;
import org.sercho.masp.models.Context.RemoteControl;
import org.sercho.masp.models.Context.Socket;
import org.sercho.masp.models.Context.TV;
import org.sercho.masp.models.Context.WashingMachine;
import org.sercho.masp.models.Context.WaterStorageTank;
import org.sercho.masp.models.Context.Mixer;
import  org.sercho.masp.models.Context.Meter;

/**
 * This enumeration provides the user with different types for
 * devices (found in the ContextModel-API by Grzegorz Lehmann).
 * 
 * @author langenhagen
 * @version 20111222
 */
public enum DeviceType{
	
	Blind,
	Cooker,
	CookTop,
	Dishwasher,
	Fan,
	Fridge,
	Heater,
	Hob,
	Hood,
	Lamp,
	Mixer,
	Notebook,
	Oven,
	PC,
	Radio,
	RemoteControl,
	TV,
	WaterStorageTank,
	HeatingRod,
	Socket,
	Meter,
	WashingMachine,
	/** a device that cannot be assigned to another given type */
	DefaultDevice;
	
	
	/**
	 * This method extracts a <i>DeviceType</i> out of a <i>PhysicalDevice</i>.
	 * 
	 * @param device
	 * An arbitrary <i>PhysicalDevice</i>.
	 * @return
	 * Returns the corresponding <i>DeviceType</i> to the given device
	 * or <i>DefaultDevice</i> in case of error.
	 */
	public static DeviceType getDeviceType( PhysicalDevice device){
		
		DeviceType type = DeviceType.DefaultDevice;
		
    	// process concrete types
    	@SuppressWarnings("rawtypes")
		Class clazz = device.getClass();
    	if( Hood.class.isAssignableFrom( clazz)){        		       		
    		type = DeviceType.Hood;
    	
    	}else if( Notebook.class.isAssignableFrom( clazz)){
    		type = DeviceType.Notebook;
    	
    	}else if( Dishwasher.class.isAssignableFrom( clazz)){
    		type = DeviceType.Dishwasher;
    	
    	}else if( TV.class.isAssignableFrom( clazz)){
    		type = DeviceType.TV;
    	
    	}else if( PC.class.isAssignableFrom( clazz)){
    		type = DeviceType.PC;
    	
    	}else if( RemoteControl.class.isAssignableFrom( clazz)){
    		type = DeviceType.RemoteControl;
    	
    	}else if( Oven.class.isAssignableFrom( clazz)){
    		type = DeviceType.Oven;
    	
    	}else if( Fan.class.isAssignableFrom( clazz)){
    		type = DeviceType.Fan;
    	
    	}else if( Lamp.class.isAssignableFrom( clazz)){
    		type = DeviceType.Lamp;
    	
    	}else if( Blind.class.isAssignableFrom( clazz)){
    		type = DeviceType.Blind;
    	
    	}else if( Fridge.class.isAssignableFrom( clazz)){
    		type = DeviceType.Fridge;
    	
    	}else if( Hob.class.isAssignableFrom( clazz)){
    		type = DeviceType.Hob;
    	
    	}else if( Cooker.class.isAssignableFrom( clazz)){
    		type = DeviceType.Cooker;
    	
    	}else if( Heater.class.isAssignableFrom( clazz)){
    		type = DeviceType.Heater;
    	
    	}else if( CookTop.class.isAssignableFrom( clazz)){
    		type = DeviceType.CookTop;
    	
    	}else if( Radio.class.isAssignableFrom( clazz)){
    		type = DeviceType.Radio;
    	
    	}else if( Mixer.class.isAssignableFrom( clazz)){
    		type = DeviceType.Mixer;
    	
    	}else if( WaterStorageTank.class.isAssignableFrom( clazz)){
    		type = DeviceType.WaterStorageTank;
    		
    	}else if( HeatingRod.class.isAssignableFrom( clazz)){
    		type = DeviceType.HeatingRod;
    		
    	}else if( Socket.class.isAssignableFrom( clazz)){
    		type = DeviceType.Socket;

    	}else if( Meter.class.isAssignableFrom( clazz)){
    		type = DeviceType.Meter;
    		
    	}else if( WashingMachine.class.isAssignableFrom( clazz)){
    		type = DeviceType.WashingMachine;
    		
    	}else{
    		System.err.println("ERROR at DeviceType.java: The device " + device + " is not a known physical device!");
    		System.err.println("Setting device's type to DefaultDevice");
    	}
    	
    	return type;
	}
}
