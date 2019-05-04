package widgets.std;

import processing.core.PApplet;

/**
 * This class is a special Slider, which only carries discrete values and therefore doesn't 
 * let you choose some floating-point numbers.
 * 
 * TODO: complete
 * 
 * @author langenhagen
 * @version 20110609
 */
public class StdMTSwitch extends StdMTSlider {

	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see widgets.StdMTSlider#StdMTSlider(float, float, processing.core.PApplet)
	 */
	public StdMTSwitch(float x, float y, PApplet pApp){
		super( x, y, 0, 100, 0, true, pApp);
	}
	
	/* (non-Javadoc)
	 * @see widgets.StdMTSlider#StdMTSlider(float, float, float, float, processing.core.PApplet)
	 */
	public StdMTSwitch( float x, float y, float minVal, float maxVal, PApplet pApp){
		super( x, y, minVal, maxVal, minVal, true, pApp);
	}
	
	/* (non-Javadoc)
	 * @see widgets.StdMTSlider#StdMTSlider(float, float, float, float, float, boolean, processing.core.PApplet)
	 */
	public StdMTSwitch( float x, float y, float minVal, float maxVal, float val, boolean vertical, PApplet pApp){
		super( x, y, minVal, maxVal, val, vertical, pApp);
	}
	
	/* (non-Javadoc)
	 * @see widgets.StdMTSlider#StdMTSlider(float, float, float, boolean, processing.core.PApplet)
	 */
	public StdMTSwitch( float x, float y, float val, PApplet pApp){
		super( x, y, 0, 100, val, true, pApp);
	}
	
}