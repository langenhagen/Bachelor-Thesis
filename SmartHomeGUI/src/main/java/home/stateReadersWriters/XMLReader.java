package home.stateReadersWriters;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.mt4j.util.math.Matrix;

import widgets.MT3DObject;


import home.Home;
import home.Item;

/**
 * This concrete <i>StateReader</i> loads home-information from an xml file.
 * Its counterpart is the <i>XMLWriter</i>.
 * 
 * @author langenhagen
 * @version 20120619
 */
public class XMLReader implements StateReader {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the filename */
	private final String fname;
	
	/** represents the file's prefix */
	//private final String filePrefix = "saves/home_";
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Main constructor.
	 * @param fname
	 * The filename as a <i>String</i>.
	 */
	public XMLReader( String fname){
		super();
		this.fname = fname;
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see home.deviceCommands.StateReader#load(home.Home)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean load(Home home){
		
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;

		// open file
		if( ! new File( fname).exists()){

			System.out.println("Did not find file " + fname + ". Using standard values");
			return false;
		}
		
		try{
			doc = builder.build( fname);	
		} catch(JDOMException e){
			System.err.println("JDOMException: could not load " + fname + "!");
			e.printStackTrace();
			return false;
		} catch(IOException e){
			System.err.println("IOException: could not load " + fname + "!");
			e.printStackTrace();
			return false;
		}
		
		// parse the file
		for( Element xmlElement : (List<Element>)doc.getRootElement().getChildren()){
			if(xmlElement.getName().equalsIgnoreCase( "Item")){
				
				parseItem(xmlElement, home);
				
			}else if( xmlElement.getName().equalsIgnoreCase( "globalMatrix")){
				
				MT3DObject obj = home.getView();
				
				obj.transform( obj.getGlobalInverseMatrix());
				obj.transform( parseMatrix(xmlElement));
			}
		}
		
		return true;
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Parses <i>Items</i> and changes the according 
	 * values in its equivalent in the model.
	 * @param element
	 * The itemID in unparsed form as an <i>Element</i>.
	 * @param home
	 * The <i>Home</i>, wherein to find the item.
	 */
	private void parseItem( Element element, Home home){

		Item item = home.getItem( element.getAttributeValue( "id"));
		if(item==null){
			System.err.println("Error: Item with id " + element.getAttributeValue("id") + " cannot be found!");
			return;
		}
			
		MT3DObject obj = item.getView();
		
		// set variant
		obj.setVariant( new Integer( element.getChild( "variant").getTextTrim()));
		
		// set global matrix
		obj.transform( obj.getGlobalInverseMatrix());
		obj.transform( parseMatrix( element.getChild( "globalMatrix")));
	}
	
	/**
	 * Parses a <i>String</i> from an <i>Element</i> and forms a 4x4 Matrix out of it.
	 * @param element
	 * The <i>Element</i> to parse from.
	 * @return
	 * A <i>Matrix</i> or NULL in case of error.
	 */
	private Matrix parseMatrix( Element element){
		
		Matrix ret = new Matrix();
		
		// convert string-values into float-array
		String[] stringValues = element.getTextTrim().split( ";");
		float[] values = new float[16];
		for( byte i=0; i<16; i++)
			values[i] = new Float( stringValues[i]);
		
		// set matrix
		try{
			ret.set( values);
		} catch(Exception e){
			System.err.println("XMLReader.java: Error while generating new Matrix:");
			e.printStackTrace();
			return null;
		}
		return ret;
	}
}
