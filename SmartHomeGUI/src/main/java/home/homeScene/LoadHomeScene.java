package home.homeScene;

import java.io.FileInputStream;

import org.mt4j.MTApplication;

import scenes.AbstractLoadScene;


/**
 * This class represents a dialog that loads a smart home.
 * 
 * @version 20120619
 * @author langenhagen
 */
public class LoadHomeScene extends AbstractLoadScene {

	/**
	 * Main Constructor<br>.
	 * 
	 * @param mtApp
	 * The corresponding application as an <i>MTApplication</i>.
	 * @param name
	 * The name of the scene as a String.
	 * @param filename the complete path to the file to load as a <i>String</i>.
	 */
	public LoadHomeScene(MTApplication mtApp, String name, String filename) {
		super(mtApp, name, filename);
	}

	/* (non-Javadoc)
	 * @see void home.homeScene.LoadHomeDialog.btnLoadTappedHelper()
	 */
	@Override
	protected void btnLoadTappedHelper() {
		
		// check for non existing files when opening a file
		try
		{
			new FileInputStream( txtCurrentChoice.getText().trim());
			getMTApplication().changeScene( new HomeScene( getMTApplication(), "Home Scene", txtCurrentChoice.getText().trim()));
		}
		catch( Exception e){
			System.err.println( this.getClass() + ": ERROR Using " + txtCurrentChoice.getText().trim() + "!");
			e.getMessage();
			e.printStackTrace();
		}
	}

}
