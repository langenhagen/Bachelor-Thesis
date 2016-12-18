package home.stateVisualizers;

import home.Item;

/**
 * This class implements a dummy <i>StateVisualizer</i>
 * that alters the displayed view of an item in no way.
 * It is used instead of a null-reference in an <i>Item</i>,
 * therefore one doesn't need to beware of a null pointer exception,
 * when an <i>Item</i> has  "no" state visualizer.<br><br>
 * 
 * Instead of implementing the <i>StateVisualizer</i> itself with an empty body, 
 * I created this subclass, letting other programmers implicitly know which methods 
 * in <i>StateVisualizer</i> are to be implemented when sublcassing, 
 * because these methods are still abstract.
 * 
 * @author langenhagen
 * @version 20111219
 */
public class DummyVisualizer extends StateVisualizer {

	public DummyVisualizer(Item item){
		super( item);
	}

	@Override
	public void notifyVisualizer(){
		// *** IMPLEMENTS AN EMPTY BODY *** //
	}
}