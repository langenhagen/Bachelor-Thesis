package home.homeScene;

import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.util.camera.Icamera;
import org.mt4j.util.math.Vector3D;

import widgets.MTSlider;


/**
 * This class is just a wrapper to contain all Gesture Listeners of its scene
 * It is just for the programmer's convenience, since java does not support 
 * more than one class in one file or partial classes.
 * 
 * @author langenhagen
 * @version 20111209
 */
public class ListenerFactory {
	
	// PUBLIC CREATOR METHODS /////////////////////////////////////////////////////////////////////
	
	public SliderZoomListener createSliderZoomListener( MTSlider slider, Icamera cam){
		return new SliderZoomListener( slider, cam);
	}
	
	public SliderPitchListener createSliderPitchListener( MTSlider slider, Icamera cam){
		return new SliderPitchListener( slider, cam);
	}
	
	// LISTENER CLASSES ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Implements the Zooming for an <i>MTSlider</i>
	 * @author langenhagen
	 * @version 20110929
	 */
	public class SliderZoomListener implements IGestureEventListener{
		
		private Icamera cam;
		private MTSlider slider;
		
		public SliderZoomListener( MTSlider slider, Icamera cam){
			this.cam = cam;
			this.slider = slider;
		}
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			// get direction vector
			Vector3D dir = cam.getViewCenterPos().getSubtracted( cam.getPosition()).normalizeLocal();
			float factor = slider.getMaxValue() - slider.getValue()+1;
			
			cam.setPosition( cam.getViewCenterPos().getSubtracted( dir.scaleLocal( factor)));
			return false;
		}
	} // END SliderZoomListener
	
	
	/**
	 * Implements the pitching of the camera for an <i>MTSlider</i>
	 * @author langenhagen
	 * @version 20110929
	 */
	public class SliderPitchListener implements IGestureEventListener{
		
		private Icamera cam;
		private MTSlider slider;
		
		public SliderPitchListener( MTSlider slider, Icamera cam){
			this.cam = cam;
			this.slider = slider;
		}
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){

			// get direction vector
			Vector3D currentDirVec = cam.getViewCenterPos().getSubtracted( cam.getPosition()).normalizeLocal();
			Vector3D dirVec0 = new Vector3D( 0, 0, -1);

			// put the camera around the coordinate space center, then rotate it and then move it back about its offset
			cam.setPosition( cam.getPosition().getSubtracted( cam.getViewCenterPos()));
			cam.setPosition( cam.getPosition().rotateX( Vector3D.angleBetween( currentDirVec, dirVec0)).rotateX( -slider.getValue()));
			cam.setPosition( cam.getPosition().getAdded( cam.getViewCenterPos()));
			
			return false;
		}
	} // END SliderPitchListener
	
	
	
	
	
	
	
	
	
	

}
