package home.stateReadersWriters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.mt4j.util.math.Matrix;

import home.Home;
import home.Item;

/**
 * This concrete <i>StateWriter</i> stores home-information in an xml file.
 * Its counterpart is the <i>XMLReader</i>.
 * 
 * @author langenhagen
 * @version 20120619
 */
public class XMLWriter implements StateWriter{

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the filename */
	private final String fname;
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor.
	 * @param fname
	 * The filename as a <i>String</i>.
	 */
	public XMLWriter( String fname){
		super();
		this.fname = fname;
	}
	
	/* (non-Javadoc)
	 * @see home.deviceCommands.StateReader#save(home.Home)
	 */
	@Override
	public boolean save(Home home){

		Element root = new Element("Home");
		root.setAttribute( "name", home.getName());
		
		Element homeMatrix = new Element("globalMatrix");
		homeMatrix.setText( createMatrixString( home.getView().getGlobalMatrix()));
		root.addContent( homeMatrix);
		
		for( Item i : home.getAllItems()){
			
			// CREATE child elements/attributes of items here
			Element itemEl = new Element("Item");
			itemEl.setAttribute( "id", i.getID());
			
			Element globalMatrix = new Element("globalMatrix");
			globalMatrix.setText( createMatrixString( i.getView().getGlobalMatrix()));

			Element variant = new Element("variant");
			variant.setText( "" + i.getView().getVariant());
			
			
			// APPEND child elements/attributes of items here
			itemEl.addContent( globalMatrix);
			itemEl.addContent( variant);
			root.addContent( itemEl);
		}
		
		// save XML
		return writeToFile( fname, new Document(root) );
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Takes a 4x4 Matrix and linearizes it into a row-major string-form.
	 * @param matrix
	 * A 4x4 <i>Matrix</i>.
	 * @return
	 * The Matrix as a row-major <i>String</i>.
	 */
	private String createMatrixString( Matrix matrix){
		
		String ret = new String();
		
		float[] values = new float[16];
		matrix.get( values);
		
		for( byte i=0; i<15; i++ ){
			ret += values[i] + ";";
		}
		ret += values[15];
		
		return ret;
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
	
}
