import home.homeScene.HomeScene;
import home.homeScene.LoadHomeScene;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.mt4j.MTApplication;


import provider.GUI3DModels;
import provider.GUI3DModelsSimple;
import provider.GUIFonts;
import provider.GUITextures;
import provider.GUITexturesSimple;

import scenes.AbstractLoadScene;
import scenes.tests.SceneX2;
import widgets.factories.GUIFactory;


/**
 * The startup class.
 * 
 * @author langenhagen
 *
 */
public class Main extends MTApplication{

	/**
	 * The main method.
	 */
	private static final long serialVersionUID = 1L;
	
	private static MultiTouchDriverManager mtDriverManager = null;

	public static void main(String args[]){
		
		mtDriverManager = new MultiTouchDriverManager();
		
		//program exit listener 
		Thread hook = new Thread(mtDriverManager);
        Runtime.getRuntime().addShutdownHook(hook);
        
        // stop driver
        mtDriverManager.stopMultiTouchDriver();

		// initializes the processing settings
		initialize();
	}
	
	/**
	 * This method initializes mt4j and specifies the startup-scene
	 */
	@Override
	public void startUp(){
		
		List<String> modelfiles = new LinkedList<String>();
		modelfiles.add( "res/models");
		
		GUIFonts.setup( this, GUIFonts.class, "./../res/fonts");
		GUITextures.setup( this, GUITexturesSimple.class, "res/textures");
		GUI3DModels.setup( this, GUI3DModels.class, modelfiles);
		GUIFactory.setup( this, GUIFactory.class);
		
		//this.addScene(new SceneX2( this, "SceneX2"));
		//this.addScene(new PointTestScene( this, "PTS"));
		this.addScene(new LoadHomeScene(this, "Load Home Scene", "./saves/environments.xml"));
		//this.addScene(new HomeScene( this, "Smart Home", "ContextModel_offline.xmi"));
		//this.addScene( new Rotation3DScene( this, "Rotation 3D Scene"));
	}
	
	// INNER CLASSES //////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The multi touch driver manager used for the TinkerTouch table in order to work properly
	 * 
	 * @author Mathias Wilhelm
	 */
	private static class MultiTouchDriverManager
    implements Runnable
    {
		/**
		 * Main Constructor.
		 */
		private MultiTouchDriverManager() { /* EMPTY */ }
	    
		/**
		 * starting mt driver service at program exit.
		 * 
		 */
	    public void run()
	    {
	    	Runtime rt = Runtime.getRuntime();
	        try {
				Process p =rt.exec("net start \"multitouch driver\"");
				p.exitValue(); //TODO; causes exception at shutdown
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }   
	    
	    public void stopMultiTouchDriver() {
	    
	    	try 
	    	{
	    		Runtime rt = Runtime.getRuntime();
	    		rt.exec("net stop \"multitouch driver\"");
	    		
	    	} catch (Throwable t){
	    		t.printStackTrace();
	    	}
	    }
	};
}