package provider;

import processing.core.PApplet;
import processing.core.PImage;
import util.ProviderTarget;

/**
 * This class provides the textures which are necessary in the GUI-context.
 * The two main advantages are first, that you have a rudimental resource management
 * and second, that you are able to switch whole texture sets (at least for the standard gui-elements)
 * in a concise way. However, you are not forced to use this class, but it would be appreciable 
 * if the standard gui-elements comply your requirements in matters of behaviour.
 * If you want to reuse this provider for subclassing, all you have to do
 * is to override the method setupHelper().<br>
 * <strong>Note:</strong> When adding new textures, please note, 
 * that you should only use textures which have 2^n values in their dimensions,
 * otherwise you'd have to deal with compatibility issues.
 * 
 * @author langenhagen
 * @version 20110607
 */
public class GUITexturesSimple extends provider.GUITextures {
	
	// PUBLIC TEXTURES ////////////////////////////////////////////////////////////////////////////
	
	// Application textures
	private PImage appBG;
	private PImage placeholder;
	
	// Button
	private PImage btnNormal;
	private PImage btnTap;
	private PImage btnDisabled;
	private PImage btnSignal;
	
	// Checkbutton
	private PImage checkNormal;
	private PImage checkTap;
	private PImage checkDisabled;
	private PImage checkSignal;
	
	// Tab
	private PImage tabNormal;
	private PImage tabTap;
	private PImage tabDisabled;
	private PImage tabSignal;
	private PImage tabSignal2;
	
	// Slider
	private PImage sliderNormal;
	private PImage sliderTap;
	private PImage sliderDisabled;
	private PImage sliderSignal;
	private PImage sliderSignal2;
	private PImage sliderKnobNormal;
	private PImage sliderKnobTap;
	private PImage sliderKnobDisabled;
	private PImage sliderKnobSignal;
	private PImage sliderKnobSignal2;
	
	// 2D-Manipulator
	private PImage twoDManipulatorNormal;
	private PImage twoDManipulatorTap;
	private PImage twoDManipulatorDisabled;
	private PImage twoDManipulatorSignal;
	private PImage twoDManipulatorSignal2;
	private PImage twoDManipulatorKnobNormal;
	private PImage twoDManipulatorKnobTap;
	private PImage twoDManipulatorKnobDisabled;
	private PImage twoDManipulatorKnobSignal;
	private PImage twoDManipulatorKnobSignal2;

	// Graph plotter
	private PImage plotterNormal;
	private PImage plotterTap;
	private PImage plotterDisabled;
	private PImage plotterSignal;
	private PImage plotterSignal2;
	
	// InputField
	private PImage inputFieldNormal;
	private PImage inputFieldTap;
	private PImage inputFieldDisabled;
	private PImage inputFieldSignal;
	private PImage inputFieldSignal2;
	
	// Panel
	private PImage panel;
	
	
	// CONSTRUCTOR ////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor. Loads the Textures in the memory and initializes the variables.
	 * This is the only method you have to override, if you want to subclass this provider.<br>
	 * <strong>Note:</strong> When adding new textures, please note, 
	 * that you should only use textures which have 2^n values in their dimensions,
	 * otherwise you'd have to deal with compatibility issues.
	 * @param pApp
	 * The programm as an <i>PApplet</i>
	 */
	protected GUITexturesSimple(){
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Helper method for setting up the provider.
	 * Truly sets up the textures.
	 * Override this method when you create a child class.
	 * @param pApp
	 * The Application to render the fonts as a <i>PApplet</i>.
	 * @param rootDirectory
	 * The root directory of all textures as a <i>String</i>.
	 */
	protected void setupHelper( PApplet pApp, String rootDirectory){
		
		appBG				= pApp.loadImage( rootDirectory + "/placeholder.png");		
		placeholder			= pApp.loadImage( rootDirectory + "/placeholder.png");
		
		PImage lightGray = pApp.loadImage( rootDirectory + "/simple/light_gray.png");
		PImage darkGray = pApp.loadImage( rootDirectory + "/simple/dark_gray.png");
		PImage green = pApp.loadImage( rootDirectory + "/simple/green.png");
		
		btnNormal			= darkGray;
		btnTap				= green;
		btnDisabled			= lightGray;
		btnSignal			= lightGray;
		
		checkNormal			= pApp.loadImage(rootDirectory + "/simple/checkNormal.png");
		checkTap			= green;
		checkDisabled		= lightGray;
		checkSignal			= lightGray;
		
		tabNormal			= darkGray;
		tabTap				= lightGray;
		tabDisabled			= lightGray;
		tabSignal			= lightGray;
		tabSignal2			= lightGray;
		
		sliderNormal		= darkGray;
		sliderTap			= lightGray;
		sliderDisabled		= lightGray;
		sliderSignal		= lightGray;
		sliderSignal2		= lightGray;
		sliderKnobNormal	= green;
		sliderKnobTap		= green;
		sliderKnobDisabled	= lightGray;
		sliderKnobSignal	= green;
		sliderKnobSignal2	= green;
		
		twoDManipulatorNormal		= darkGray;
		twoDManipulatorTap			= green;
		twoDManipulatorDisabled		= lightGray;
		twoDManipulatorSignal		= lightGray;
		twoDManipulatorSignal2		= lightGray;
		twoDManipulatorKnobNormal	= green;
		twoDManipulatorKnobTap		= lightGray;
		twoDManipulatorKnobDisabled	= green;
		twoDManipulatorKnobSignal	= green;
		twoDManipulatorKnobSignal2	= green;
		
		plotterNormal	=  green;
		plotterTap		=  green;
		plotterDisabled	=  green;
		plotterSignal	=  green;
		plotterSignal2	=  green;
		
		inputFieldNormal = lightGray;
		inputFieldTap = green;
		inputFieldDisabled = darkGray;
		inputFieldSignal = green;
		inputFieldSignal2 = green;
		
		panel = lightGray;
		
		blind = pApp.loadImage( rootDirectory + "/devices/blind.png");
		cooker = pApp.loadImage( rootDirectory + "/devices/cooker.png");
		cookTop = pApp.loadImage( rootDirectory + "/devices/cookTop.png");
		dishwasher = pApp.loadImage( rootDirectory + "/devices/dishwasher.png"); 
		fan = pApp.loadImage( rootDirectory + "/devices/fan.png");
		fridge = pApp.loadImage( rootDirectory + "/devices/fridge.png");
		heater = pApp.loadImage( rootDirectory + "/devices/heater.png");
		hob = pApp.loadImage( rootDirectory + "/devices/hob.png");
		hood = pApp.loadImage( rootDirectory + "/devices/hood.png");
		lamp = pApp.loadImage( rootDirectory + "/devices/lamp.png");
		mixer = pApp.loadImage( rootDirectory + "/devices/mixer.png");
		notebook = pApp.loadImage( rootDirectory + "/devices/notebook.png");
		oven = pApp.loadImage( rootDirectory + "/devices/oven.png");
		pc = pApp.loadImage( rootDirectory + "/devices/pc.png");
		radio = pApp.loadImage( rootDirectory + "/devices/radio.png");
		remoteControl = pApp.loadImage( rootDirectory + "/devices/remoteControl.png");
		tv = pApp.loadImage( rootDirectory + "/devices/tv.png");
		defaultDevice = pApp.loadImage( rootDirectory + "/devices/default.png");
	}
	
	// PUBLIC GETTERS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the corresponding texture to the specified target.
	 * @param target
	 * The <i>ProviderTarget</i> to which you want the font for.
	 * If the target is somewhat nonsensical with respect to a specific texture,
	 * then most likely the placeholder-texture will be retrieved.
	 */
	public PImage get( ProviderTarget target)
	{
		PImage ret;
		
		switch( target){
		case Std: ret = placeholder; break;
		case ApplicationBackground:	ret = appBG; break;
		case Placeholder: ret = placeholder; break;
		case Panel: ret = panel; break;
		
		case ButtonNormal: ret = btnNormal; break;
		case ButtonTap: ret = btnTap; break;
		case ButtonDisabled: ret = btnDisabled; break;
		case ButtonSignal:
		case ButtonSignal2: ret = btnSignal; break;
		
		case CheckNormal: ret = checkNormal; break;
		case CheckTap: ret = checkTap; break;
		case CheckDisabled: ret = checkDisabled; break;
		case CheckSignal:
		case CheckSignal2: ret = checkSignal; break;
			
		case TabNormal: ret = tabNormal; break;
		case TabTap: ret = tabTap; break;
		case TabDisabled: ret = tabDisabled; break;
		case TabSignal: ret = tabSignal; break;
		case TabSignal2: ret = tabSignal2; break;
		
		case SliderNormal: ret = sliderNormal; break;
		case SliderTap: ret = sliderTap; break;
		case SliderDisabled: ret = sliderDisabled; break;
		case SliderSignal: ret = sliderSignal; break;
		case SliderSignal2: ret = sliderSignal2; break;
		case SliderKnobNormal: ret = sliderKnobNormal; break;
		case SliderKnobTap: ret = sliderKnobTap; break;
		case SliderKnobDisabled: ret = sliderKnobDisabled; break;
		case SliderKnobSignal: ret = sliderKnobSignal; break;
		case SliderKnobSignal2: ret = sliderKnobSignal2; break;
		
		case TwoDManipulatorNormal: ret = twoDManipulatorNormal; break;
		case TwoDManipulatorTap: ret = twoDManipulatorTap; break;
		case TwoDManipulatorDisabled: ret = twoDManipulatorDisabled; break;
		case TwoDManipulatorSignal: ret = twoDManipulatorSignal; break;
		case TwoDManipulatorSignal2: ret = twoDManipulatorSignal2; break;
		case TwoDManipulatorKnobNormal: ret = twoDManipulatorKnobNormal; break;
		case TwoDManipulatorKnobTap: ret = twoDManipulatorKnobTap; break;
		case TwoDManipulatorKnobDisabled: ret = twoDManipulatorKnobDisabled; break;
		case TwoDManipulatorKnobSignal: ret = twoDManipulatorKnobSignal; break;
		case TwoDManipulatorKnobSignal2: ret = twoDManipulatorKnobSignal2; break;
		
		case PlotterNormal: ret = plotterNormal; break;
		case PlotterTap:  ret = plotterTap; break;
		case PlotterDisabled: ret = plotterDisabled; break;
		case PlotterSignal: ret = plotterSignal; break;
		case PlotterSignal2: ret = plotterSignal2; break;
		
		case InputFieldNormal: ret = inputFieldNormal; break;
		case InputFieldTap: ret = inputFieldTap; break;
		case InputFieldDisabled: ret = inputFieldDisabled; break;
		case InputFieldSignal: ret = inputFieldSignal; break;
		case InputFieldSignal2: ret = inputFieldSignal2; break;
			
		case Blind: ret = blind; break;
		case Cooker: ret = cooker; break;
		case CookTop: ret = cookTop; break;
		case Dishwasher: ret = dishwasher; break;
		case Fan: ret = fan; break;
		case Fridge: ret = fridge; break;
		case Heater: ret = heater; break;
		case Hob: ret = hob; break;
		case Hood: ret = hood; break;
		case Lamp: ret = lamp; break;
		case Mixer: ret = mixer; break;
		case Notebook: ret = notebook; break;
		case Oven: ret = oven; break;
		case PC: ret = pc; break;
		case Radio: ret = radio; break;
		case RemoteControl: ret = remoteControl; break;
		case TV: ret = tv; break;
		case DefaultDevice: ret = defaultDevice; break;
		
		
		default:
			ret = placeholder;
			break;
		}
		
		return ret;
	}
	
}