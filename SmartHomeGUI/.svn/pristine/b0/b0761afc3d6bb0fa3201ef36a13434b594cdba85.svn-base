package widgets.std;

import java.util.LinkedList;
import java.util.List;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.clipping.Clip;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import provider.GUIFonts;
import provider.GUITextures;

import util.Mode;
import util.ProviderTarget;
import widgets.MTGraphPlotter;

/**
 * @author langenhagen
 * @version 20110615
 */
public class StdMTGraphPlotter extends MTGraphPlotter {

	// INSTANCE VARS //////////////////////////////////////////////////////////////////////////////
	
	/** the background of this plotter */
	private MTRectangle rect;
	/** the canvas On which the lines of the ploz "live" */
	private MTRectangle canvas;
	/** the label of the x-axis */
	private MTTextArea lblX;
	/** the label of the y-axis */
	private MTTextArea lblY;
	/** the smallest value of this plot. for formatting reasons */
	private double minVal;
	/** the biggest value of this plot. for formatting reasons */
	private double maxVal;
	/** specifies the distance between value-points in the graph */
	private float scale = 100;
	/** 
	 * the list of lines building the actual graph. the line On index n indicates the line going from
	 * the n-th value-point to the (n+1)-th value-point.
	 */
	List<MTLine> lines;
	/** the list of values in this graph, where the index of the values describes their actual index */
	List<Double> values;
	
	// CONSTRUCTORS ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor #1<br>
	 * simplified constructor which generates new graph plotter with no values to plot.
	 * @param x
	 * The x-position of this component as a <i>float</i>
	 * @param y
	 * The y-position of this component as a <i>float</i>
	 * @param pApp
	 * The application to render this component as a <i>float</i>
	 */
	public StdMTGraphPlotter( float x, float y, PApplet pApp){
		this( x, y, "Lorem Ipsum X", "Lorem Ipsum Y", new LinkedList<Double>(), pApp);
	}
	
	/**
	 * Constructor #2
	 * @param x
	 * The x-position of this component as a <i>float</i>
	 * @param y
	 * The y-position of this component as a <i>float</i>
	 * @param textX
	 * The caption of the x-axis as a <i>String</i>
	 * @param textY
	 * The caption of the y-axis as a <i>String</i>
	 * @param values
	 * The initial list of values to be plotted as a <i>List</i> of type <i>Double</i>,
	 * if it is null, then a new emty list will be created
	 * @param pApp
	 * The application to render this component as a <i>float</i>
	 */
	public StdMTGraphPlotter( float x, float y, String textX, String textY, List<Double> values, PApplet pApp){
		super( pApp);
		
		float width = 400;
		float height = 300;
		
		// set values & create lines
		if( values == null)
			values = new LinkedList<Double>();
		this.values = values;
		this.lines = new LinkedList<MTLine>();
		for( int i=0; i<values.size(); i++){
			MTLine line = new MTLine( pApp, new Vertex(), new Vertex());
			lines.add( line);
			canvas.addChild( line);
		}
		
		// set graphics
		rect = new MTRectangle( pApp, width, height);
		rect.setChildClip( new Clip( rect));
		rect.setAnchor( PositionAnchor.UPPER_LEFT);
		rect.setNoStroke( true);

		canvas = new MTRectangle( pApp, 0, rect.getHeightXY( TransformSpace.GLOBAL));
		canvas.setAnchor( PositionAnchor.UPPER_LEFT);
		canvas.setNoFill( true);
		canvas.setNoStroke( true);
		canvas.setComposite( true);
		
		lblX = new MTTextArea(pApp);
		lblY = new MTTextArea(pApp);
		lblX.setNoFill( true);
		lblX.setNoStroke( true);
		lblY.setNoFill( true);
		lblY.setNoStroke( true);
		lblX.setFont( GUIFonts.instance().get(ProviderTarget.PlotterNormal));
		lblY.setFont( GUIFonts.instance().get(ProviderTarget.PlotterNormal));
		lblX.setAnchor( PositionAnchor.UPPER_LEFT);
		lblY.setAnchor( PositionAnchor.UPPER_LEFT);
		
		// set functionality
		rect.unregisterAllInputProcessors();
		canvas.unregisterAllInputProcessors();
		lblX.unregisterAllInputProcessors();
		lblY.unregisterAllInputProcessors();
		rect.removeAllGestureEventListeners();
		canvas.removeAllGestureEventListeners();
		lblX.removeAllGestureEventListeners();
		lblY.removeAllGestureEventListeners();
		canvas.registerInputProcessor( new DragProcessor( pApp));
		canvas.addGestureListener( DragProcessor.class, new ScrollListener());
		
		// add children
		rect.addChild( canvas);
		canvas.setPositionGlobal( rect.getPosition( TransformSpace.GLOBAL));
		rect.addChild( lblX);
		rect.addChild( lblY);	
		this.addChild( rect);
		
		// set additional stuff
		setTextX( textX);
		setTextY( textY);
		this.translate( new Vector3D(x,y), TransformSpace.GLOBAL);
		
		setMode(Mode.NORMAL);
		revalidate();
	}
	
	// PUBLIC METHODS /////////////////////////////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see widgets.MTGraphPlotter#setTextX(java.lang.String)
	 */
	@Override
	public void setTextX(String text){	
		lblX.setText( text);
		lblX.setPositionRelativeToParent( 
				new Vector3D(
						rect.getWidthXY( TransformSpace.LOCAL) - lblX.getWidthXY( TransformSpace.RELATIVE_TO_PARENT),
						rect.getHeightXY( TransformSpace.LOCAL) - lblX.getHeightXY( TransformSpace.RELATIVE_TO_PARENT)));
	}

	/* (non-Javadoc)
	 * @see widgets.MTGraphPlotter#setTextY(java.lang.String)
	 */
	@Override
	public void setTextY(String text){
		lblY.setText( text);
		lblY.setPositionRelativeToParent( new Vector3D( 2, 10));
	}

	/* (non-Javadoc)
	 * @see widgets.MTGraphPlotter#getTextX()
	 */
	@Override
	public String getTextX(){
		return lblX.getText();
	}

	/* (non-Javadoc)
	 * @see widgets.MTGraphPlotter#getTextY()
	 */
	@Override
	public String getTextY(){
		return lblY.getText();
	}

	/* (non-Javadoc)
	 * @see widgets.MTGraphPlotter#clear()
	 */
	@Override
	public void clear(){
		canvas.removeAllChildren();
		lines.clear();
		values.clear();
	}

	/* (non-Javadoc)
	 * @see widgets.MTGraphPlotter#setAllValues(java.util.List)
	 */
	@Override
	public void setAllValues(List<Double> values){
		this.values = values;
		lines.clear();
		
		for(double d : values)
			addValue( d);
	}

	/* (non-Javadoc)
	 * @see widgets.MTGraphPlotter#getAllValues()
	 */
	@Override
	public List<Double> getAllValues(){
		return values;
	}

	/* (non-Javadoc)
	 * @see widgets.MTGraphPlotter#addValue(double)
	 */
	@Override
	public void addValue(double value){
		addValueAt( value, -1);
	}

	/* (non-Javadoc)
	 * @see widgets.MTGraphPlotter#addValueAt(double, int)
	 */
	@Override
	public void addValueAt(double value, int pos){
		if( pos<0 || pos>=values.size())
			values.add( value);
		else			
			values.add( pos, value);
			
		MTLine line = new MTLine( this.getRenderer(), new Vertex(), new Vertex());
		lines.add( line);
		canvas.addChild( line);
		line.setStrokeWeight( 3);
		
		// remove input processors & gestureListeners
		line.unregisterAllInputProcessors();
		line.removeAllGestureEventListeners();
		
		// set graphical styles & redraw
		setMode(this.mode);
		revalidate();
	}
	
	/* (non-Javadoc)
	 * @see widgets.MTGraphPlotter#getValueAt(int)
	 */
	@Override
	public double getValueAt( int pos){
		if( pos<0 || pos>=values.size())
			return Double.NaN;
		return values.get( pos);
	}

	/* (non-Javadoc)
	 * @see widgets.MTGraphPlotter#removeValueAt(int)
	 */
	@Override
	public double removeValueAt(int pos){
		if( pos<0 || pos>=values.size())
			return Double.NaN;
		
		double ret = values.remove( pos);
		lines.remove( pos);
		revalidate();
		return ret;
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getPosition(org.mt4j.components.TransformSpace)
	 */
	@Override
	public Vector3D getPosition(TransformSpace space){
		return rect.getPosition( space);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setPosition(org.mt4j.util.math.Vector3D, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setPosition(Vector3D pos, TransformSpace space){
		switch(space){
		case LOCAL:
			rect.setPositionRelativeToOther( rect, pos);
			break;
		case RELATIVE_TO_PARENT:
			rect.setPositionRelativeToOther( this.getParent(), pos);
			break;
		case GLOBAL:
			rect.setPositionGlobal( pos);
		}
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getWidth(org.mt4j.components.TransformSpace)
	 */
	@Override
	public float getWidth(TransformSpace space){
		return rect.getWidthXY( space);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#getHeight(org.mt4j.components.TransformSpace)
	 */
	@Override
	public float getHeight(TransformSpace space){
		return rect.getHeightXY( space);
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setWidth(float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setWidth(float width, TransformSpace space){
		switch(space){
		case LOCAL:
			rect.setWidthLocal( width);
			break;
		case RELATIVE_TO_PARENT:
			rect.setWidthXYRelativeToParent( width);
			break;
		case GLOBAL:
			rect.setWidthXYGlobal( width);
		}
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setHeight(float, org.mt4j.components.TransformSpace)
	 */
	@Override
	public void setHeight(float height, TransformSpace space){
		switch(space){
		case LOCAL:
			rect.setHeightLocal( height);
			break;
		case RELATIVE_TO_PARENT:
			rect.setHeightXYRelativeToParent( height);
			break;
		case GLOBAL:
			rect.setHeightXYGlobal( height);
		}
	}

	/* (non-Javadoc)
	 * @see widgets.AbstractGUIComponent#setMode(util.Mode)
	 */
	@Override
	public void setMode(Mode mode){
		switch(mode){
		case NORMAL:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.PlotterNormal));
			for(MTLine l : lines){
				l.setStrokeColor( MTColor.RED);
			}
			break;
		case TAP:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.PlotterTap));
			break;
		case DISABLED:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.PlotterDisabled));
			break;
		case SIGNAL:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.PlotterSignal));
			break;
		case SIGNAL2:
			rect.setTexture( GUITextures.instance().get(ProviderTarget.PlotterSignal2));
		}
		this.mode = mode;
	}
	
	// PRIVATE HELPERS ////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This method revalidates the view by repositioning/resizing 
	 * the corresponding members.
	 */
	private void revalidate(){
		
		// adjust canvas width 
		canvas.setWidthLocal( (values.size()-1)*scale);
		
		// set minVal & maxVal
		minVal = 0;
		maxVal = 0;
		for(double d : values){
			minVal = d < minVal ? d : minVal;
			maxVal = d >= maxVal ? d : maxVal;
		}
	
		float height = canvas.getHeightXY( TransformSpace.LOCAL);
		// for every line: adjust position of this line
		for(int i=0; i < lines.size()-1; i++){
			MTLine line = lines.get( i);
			
			// calculate local heights
			float v0Y = height - (float)((values.get(  i) - minVal) * height / (maxVal-minVal));
			float v1Y = height - (float)((values.get(i+1) - minVal) * height / (maxVal-minVal));
			
			// to global
			Vector3D vec0 = canvas.localToGlobal( new Vector3D( i*scale, v0Y));
			Vector3D vec1 = canvas.localToGlobal( new Vector3D( i*scale+scale, v1Y));
			vec0.subtractLocal( this.getPosition());
			vec1.subtractLocal( this.getPosition());
			
			// from Vecto3D to Vertex
			Vertex[] v = {
					new Vertex( vec0.getX(), vec0.getY()),
					new Vertex( vec1.getX(), vec1.getY())
			};
			line.setVertices( v);
		}
	}
	
	// PRIVATE LISTENERS //////////////////////////////////////////////////////////////////////////
	
	/**
	 * This class contains the logic that lets the user slide along the graph-plots,
	 * indirectly move the graph-canvas along its local x-axis.
	 * @author langenhagen
	 * @version 20110618
	 */
	private class ScrollListener implements IGestureEventListener{
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge){
			
			MTRectangle canvas = (MTRectangle)ge.getTarget();
			
			switch (ge.getId()){
			case MTGestureEvent.GESTURE_STARTED:	// On clicking
				setMode( Mode.TAP);
			case MTGestureEvent.GESTURE_UPDATED:	// On updating
				
				canvas.translate(
						new Vector3D( ((DragEvent)ge).getTranslationVect().getX(), 0),
						TransformSpace.LOCAL);
				
				float xNew = canvas.getPosition( TransformSpace.RELATIVE_TO_PARENT).getX();
				if( xNew >=0)
					canvas.setPositionRelativeToParent( Vector3D.ZERO_VECTOR);
				else if( xNew + canvas.getWidthXY( TransformSpace.RELATIVE_TO_PARENT) <= rect.getWidthXY( TransformSpace.LOCAL))
					canvas.setPositionRelativeToParent(
							new Vector3D( 
									rect.getWidthXY( TransformSpace.LOCAL) - canvas.getWidthXY( TransformSpace.RELATIVE_TO_PARENT),
									0));
				break;
			case MTGestureEvent.GESTURE_ENDED:		// On letting go
				setMode(Mode.NORMAL);
				break;
			}
			return false;
		}
	}
}