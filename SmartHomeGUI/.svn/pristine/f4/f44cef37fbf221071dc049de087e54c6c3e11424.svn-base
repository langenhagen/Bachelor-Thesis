package scenes;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.MTSuggestionTextArea;

import widgets.MTButton;
import widgets.factories.GUIFactory;

/**
 * This abstract class represents a scene for choosing a certain choice of file to load.
 * It loads and saves the possible paths to the file in an xml-file 
 * in a specified folder, from there it can be looked up at any time.
 * Note, that this class does not load a file exlicitly but presents
 * an interface for loading a file. Subclass this dialog to generate a 
 * fully functional loading-dialog.
 * 
 * TODO: make this dialog more beautiful
 * 
 * @author langenhagen
 * @version 20120515
 */
public abstract class AbstractLoadScene extends AbstractGUIScene {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** represents the file's name */
	protected String filename;
	/** the set of all choices that can be used */
	protected Set<String> choiceSet = new HashSet<String>();
	
	/** list of all known choices */
	protected MTList lstChoices;
	/** background-rectangle of the input field */
	protected MTRectangle pnlTxtCurrentChoice;
	/** the input field path of the choice to load */
	protected MTSuggestionTextArea txtCurrentChoice;
	/** the ok button */
	protected MTButton btnLoad;

	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main Constructor<br>.
	 * 
	 * @param mtApp
	 * The corresponding application as an <i>MTApplication</i>.
	 * @param name
	 * The name of the scene as a String.
	 * @param filename the complete path to the file to load as a <i>String</i>.
	 */
	public AbstractLoadScene(MTApplication mtApp, String name, String filename) {
		super(mtApp, name);

		this.filename = filename;
		
		GUIFactory factory = GUIFactory.instance();
		MTApplication app = this.getMTApplication();
		int width = app.getWidth();
		int height = app.getHeight();
		
		// build main gui elements
		
		lstChoices = factory.createList( width * 1/12, 10, width * 5/6, height * 1/3, "lstChoices", this.getCanvas());
		
		btnLoad = factory.createButton("Load", "btnLoad", this.getCanvas());
		btnLoad.setPosition(
				new Vector3D(
						width/2 - btnLoad.getWidth()/2,
						height * 2/3
				));
		btnLoad.addGestureListener( TapProcessor.class, new BtnLoadListener());
		
		pnlTxtCurrentChoice = GUIFactory.instance().createPanel(
				width*1/6,
				height/2,
				width*4/6,
				btnLoad.getHeight(),
				"pnlTxtCurrentChoice",
				this.getCanvas()		
		);
		
		
		txtCurrentChoice = factory.createInputField("txtCurrentChoice", pnlTxtCurrentChoice);
		txtCurrentChoice.setPositionGlobal( 
				pnlTxtCurrentChoice.getPosition(TransformSpace.GLOBAL).addLocal(
						new Vector3D(
								10,
								btnLoad.getHeight()/2 - txtCurrentChoice.getHeightXY(TransformSpace.GLOBAL)/2		
						)));
		
		txtCurrentChoice.setText("<current choice>");
		txtCurrentChoice.setNoFill(true);
		txtCurrentChoice.setNoStroke(true);
		
		// read xml
		processXML();
	}

	// PUBLIC / PROTECTED METHODS /////////////////////////////////////////////////////////////////
	
	@Override
	protected void keyEventHelper(KeyEvent e){
		/* empty implementation */
	}
	
	/**
	 * Simply override this method to add logic to the Load-button
	 * when needed.
	 */
	abstract protected void btnLoadTappedHelper();
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads an xml file with the filename specified in the filename instance variable
	 * and changes the values in the list and textfield.
	 * @return
	 * TRUE - Returns true if the file could be parsed.<br>
	 * FALSE - Returns false if anything went wrong.
	 */
	@SuppressWarnings("unchecked")
	private boolean processXML(){
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
			
		// open file
		if( ! new File( filename).exists()){

			System.out.println("Did not find file " + filename + ". Generating the file...");
			writeToFile( filename, new Document( new Element("directories")));
		}

		try{
			doc = builder.build( filename);	
		}catch(JDOMException e){
			System.err.println("JDOMException: could not load " + filename + "!");
			e.printStackTrace();
			return false;
		}catch(IOException e){
			System.err.println("IOException: could not load " + filename + "!");
			e.printStackTrace();
			return false;
		}

		// parse the file
		for( Element xmlElement : (List<Element>)doc.getRootElement().getChildren()){
			if(xmlElement.getName().equalsIgnoreCase( "currentChoice")){
				parseDir( xmlElement, true);
				
			}else if( xmlElement.getName().equalsIgnoreCase( "choice")){
				parseDir( xmlElement, false);
			}
		}

		return true;
	}
	
	/**
	 * Processes a dir-element or a currentDir-element in the xml file.
	 * Puts the elements in the file-list. En plus, the current one will
	 * also be put into the text box.
	 * @param e
	 * The element.
	 * @param isCurrent
	 * Specifies if the element is the shall be the current used one.
	 */
	private void parseDir( Element e, boolean isCurrent){
		String path = e.getTextTrim();
		choiceSet.add( path);
		
		MTApplication app = this.getMTApplication();
		int width = app.getWidth() * 4/6;
		
		MTButton btnChoice = GUIFactory.instance().createButton( 0, 0, path);
		btnChoice.setWidth( width);
		MTListCell cell =  new MTListCell( app, btnChoice.getWidth() , btnChoice.getHeight());
		cell.addChild(btnChoice);
		
		cell.setComposite(false); // TODO: do something about the composite-upfuck of mt4j
				
		// add logic to the buttons
		btnChoice.addGestureListener( TapProcessor.class, new BtnChoiceListener());
		
		lstChoices.addListElement(0, cell );
		
		// if the element is also the current element
		if( isCurrent){
			txtCurrentChoice.setText( path);
		}
	}
	
	/**
	 * Writes the given document to the specified path.
	 * @param path
	 * The whole path including filename to write XML to as a <i>String</i>.
	 * @param doc
	 * The <i>Document</i> to persist.
	 * @return
	 * Returns TRUE, if the action was successful, otherwise returns FALSE.
	 */
	private boolean writeToFile(String path, Document doc){
		
		File file = new File( path);	

		try{
			if( !file.exists())
				file.createNewFile();
					
			// write file
			FileOutputStream outstream = new FileOutputStream( file);
			XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
			
			serializer.output( doc, outstream);
		
		} catch(IOException e){
			System.err.println("Exception while writing XML-File to: " + path + ":");
			e.printStackTrace();
			return false;		
		}
		return true;		
	}
	
	
	
	// INNER CLASSES //////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This listener sets the text of the txtCurrentChoice-textbox according to the
	 * targeted button.
	 */
	private class BtnChoiceListener implements IGestureEventListener{
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			
			if( ge.getId() == MTGestureEvent.GESTURE_ENDED){
				
				txtCurrentChoice.setText( ((MTButton)ge.getTarget()).getText());
			}
			return false;
		}
		
	} // END BtnDirListener

	/**
	 * This listner saves the list xml file again with the values
	 * of the choicelist and a possibly new current choice.
	 */
	private class BtnLoadListener implements IGestureEventListener{
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			
			if( ge.getId() == MTGestureEvent.GESTURE_ENDED){
				
				Element root = new Element("choices");
				Document doc = new Document(root);

				// current dir
				Element currentChoice = new Element("currentChoice");
				currentChoice.setText( txtCurrentChoice.getText().trim());
				root.addContent( currentChoice);
				
				// all other dirs
				for( String path : choiceSet){
					
					if( currentChoice.getText().trim().equals( path))
						continue;
					
					Element choice = new Element("choice");
					choice.setText(path);
					root.addContent(choice);
				}
				
				btnLoadTappedHelper();
				
				writeToFile( filename, doc);
			}
			return false;
		}
		
	} // END BtnLoadListener
}
