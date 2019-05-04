import home.homeScene.HomeScene;
import home.homeScene.LoadHomeScene;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.mt4j.MTApplication;


import provider.GUI3DModels;
import provider.GUI3DModelsSimple;
import provider.GUIFonts;
import provider.GUITextures;
import provider.GUITexturesSimple;

import scenes.AbstractLoadScene;
import scenes.tests.SceneX2;
import util.GlobalVariables;
import widgets.factories.GUIFactory;


/**
 * The startup class.
 * 
 * @author langenhagen
 *
 */
public class Main extends MTApplication{

	private static final long serialVersionUID = 1L;
	private static MultiTouchDriverManager mtDriverManager = null;

	/**
	 * The main method.
	 */
	public static void main(String args[]){
		
		// init global variables that come from file
		if( args.length == 0){
			System.out.println(
					"Smart Home Viewer: No variables config file found. " +
					"Using standard ./HomeViewer_Variables.barn");
		}
		GlobalVariables.setup(GlobalVariables.class, args.length == 0 ? "HomeViewer_Variables.barn" : args[0]);
		
		
		// something for the multitouch table
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
		
		GlobalVariables vars = GlobalVariables.instance();
		
		List<String> modelfiles = new LinkedList<String>();
		modelfiles.add( vars.get("models"));
		
		GUIFonts.setup( this, GUIFonts.class, vars.get("fonts"));
		GUITextures.setup( this, GUITexturesSimple.class, vars.get("textures"));
		GUI3DModels.setup( this, GUI3DModels.class, modelfiles);
		GUIFactory.setup( this, GUIFactory.class);
		
		// choose between MT4j file chooser or classic one
		
		if( vars.get("useHardCodedContextModel").equals("false")){
			// let the user decide which context model to take
			
			if( vars.get("askForFileChooser").equals( "true")){
				// user defined action
				
				final JOptionPane dlgFileChooserType = new JOptionPane(
					    "Do you want to use the MT4j file chooser?",
					    JOptionPane.QUESTION_MESSAGE,
					    JOptionPane.YES_NO_OPTION);
				dlgFileChooserType.setVisible(true);
				
				int dialogResult = JOptionPane.showOptionDialog(
						null,
						"Do you want to use the MT4j file chooser?",
						"Smart Home GUI",
						JOptionPane.YES_NO_OPTION,
						0,
						null,
						null,
						null);
				
				if( dialogResult == JOptionPane.YES_OPTION){
					// user defined file chooser - mt4j file chooser
					this.addScene(new LoadHomeScene(this, "Load Home Scene", "./saves/environments.xml"));
				}else{
					
					// user defined file chooser - classic file chooser
					File file = chooseFileByDialog();
					if( file != null){
						this.addScene(new HomeScene( this, "Smart Home", file.getAbsolutePath() ));
					}else{
						System.exit(1);
					}
				}	
			}else if( vars.get("useMT4jFileChooser").equals("true") ){
				// mt4j file chooser
				this.addScene(new LoadHomeScene(this, "Load Home Scene", "./saves/environments.xml"));
			}else{
				// classic file chooser
				File file = chooseFileByDialog();
				if( file != null){
					this.addScene(new HomeScene( this, "Smart Home", file.getAbsolutePath() ));
				}else{
					System.exit(1);
				}
			}
		}else{
			// use hard coded context model file
			this.addScene(new HomeScene( this, "Smart Home", vars.get("contextModelPath") ));
		}
		
	}
	
	// private helpers ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Starts a file chooser looking for an xmi file. If this file exists
	 */
	private File chooseFileByDialog(){
		
		JFileChooser chooser = new JFileChooser();
	    // Note: source for ExampleFileFilter can be found in FileChooserDemo,
	    // under the demo/jfc directory in the JDK.
	    FileFilter filter = new FileFilter(){

		private final String[] okFileExtensions = new String[] {".xmi"};
		
		@Override
		public boolean accept(File pathname) {
		
			for (String extension : okFileExtensions)
		    {				
				if (pathname.getName().toLowerCase().endsWith(extension) ||
					pathname.isDirectory())
				{
					return true;
				}
		    }
			return false;
		}

			@Override
			public String getDescription() {
				return "An xmi file";
			}
	    	
	    };
	    
	    chooser.setFileFilter(filter);
	    
	    if( chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
	    	 return chooser.getSelectedFile();
	    }
	    return null;
	}
	
	// INNER CLASSES //////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The multi touch driver manager used for the TinkerTouch table in order to work properly
	 * 
	 * @author Mathias Wilhelm
	 */
	private static class MultiTouchDriverManager implements Runnable{
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