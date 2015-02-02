package it.slumdroid.utilities.module.androidtest.efg;

import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.xml.XmlGraph;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class EventFlowTree.
 */
public class EventFlowGraph extends XmlGraph {
	
	/** The efg. */
	private Document efg;
	
	/** The root element. */
	private Element rootElement;
	
	/** The navigator. */
	private Element navigator;
	
	
	/**
	 * Instantiates a new event flow graph.
	 *
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	public EventFlowGraph () throws ParserConfigurationException {
		super ("eventtree.dtd","EFG");
		this.efg = getBuilder().newDocument();
		this.rootElement = this.efg.createElement("EFG");
		this.rootElement.setAttribute("id", "root");
		this.efg.appendChild(this.rootElement);
	}
	
	// Sets attributes for the whole graph (not the nodes!) They are stored as attributes of the root node
	/**
	 * Sets the attribute.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void setAttribute (String key, String value) {
		this.rootElement.setAttribute(key, value);
	}
	
	/**
	 * Sets the date time.
	 *
	 * @param value the new date time
	 */
	public void setDateTime (String value) {
		setAttribute ("session_date_time",value);
	}

	/**
	 * From session.
	 *
	 * @param guiTree the gui tree
	 * @return the event flow graph
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	public static EventFlowGraph fromSession (GuiTree guiTree) throws ParserConfigurationException {
		EventFlowGraph efg = new EventFlowGraph();
		efg.setDateTime(guiTree.getDateTime());		

		for (Task task: guiTree) {
			efg.addTrace(task);
		}
		
		return efg;
	}
	
	/**
	 * Adds the trace.
	 *
	 * @param task the task
	 */
	public void addTrace (Task task) {
		resetNavigator();
		for (Transition transition: task) {
			addEvent(transition.getEvent(),true);		
		}
	}

	// When the traverse flag is true, if an event is already present, the current position is moved after it. No new element is added.
	/**
	 * Adds the event.
	 *
	 * @param newEvent the new event
	 * @param traverse the traverse
	 * @return true, if successful
	 */
	private boolean addEvent (UserEvent newEvent, boolean traverse) {
		if (traverse) {
			EfgEvent oldEvent = hasEvent(newEvent);
			if (oldEvent==null) {
				return addEvent(newEvent);
			} else {
				setNavigator (oldEvent);
				return false; // Returns false if the event is not added (because it already exists)
			}
		} else {
			return (this.addEvent(newEvent));
		}
	}
	
	// Adds a new child node at the current position
	/**
	 * Adds the event.
	 *
	 * @param newEvent the new event
	 * @return true, if successful
	 */
	protected boolean addEvent (UserEvent newEvent) {
		EfgEvent event = EfgEvent.fromUserEvent (this, newEvent);
		event.setId(newEvent.getId());
		getNavigator().appendChild(event.getElement());
		setNavigator (event);
		return true;
	}

	// Checks if the given node is present as a direct child of the current node and returns it. Returns null otherwise.
	/**
	 * Checks for event.
	 *
	 * @param event the event
	 * @return the efg event
	 */
	public EfgEvent hasEvent (UserEvent event) {
		for (EfgEvent candidate: new EfgEvent (getNavigator())) {
			if (candidate.getId().equals(event.getId())) {
				return candidate;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.xml.XmlGraph#getDom()
	 */
	public Document getDom () {
		return this.efg;
	}
	
	/**
	 * Gets the navigator.
	 *
	 * @return the navigator
	 */
	public Element getNavigator() {
		return this.navigator;
	}
	
	/**
	 * Sets the navigator.
	 *
	 * @param element the new navigator
	 */
	public void setNavigator (Element element) {
		this.navigator = element;
	}
	
	/**
	 * Sets the navigator.
	 *
	 * @param efg the new navigator
	 */
	public void setNavigator (EfgEvent efg) {
		this.navigator = efg.getElement();
	}

	/**
	 * Reset navigator.
	 */
	public void resetNavigator () {
		this.navigator = this.rootElement;
	}
	
}
