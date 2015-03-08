package it.slumdroid.utilities.module.androidtest.efg;

import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.WidgetAdapter;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.droidmodels.model.WrapperInterface;
import it.slumdroid.droidmodels.xml.ElementWrapper;
import it.slumdroid.droidmodels.xml.NodeListWrapper;
import it.slumdroid.droidmodels.xml.XmlGraph;

import java.util.Iterator;

import org.w3c.dom.Element;
// TODO: Auto-generated Javadoc
/**
 * The Class EfgEvent.
 */
public class EfgEvent extends ElementWrapper implements UserEvent, Iterable<EfgEvent> {

	/**
	 * Instantiates a new efg event.
	 */
	public EfgEvent() {
		super();
	}

	/**
	 * Instantiates a new efg event.
	 *
	 * @param element the element
	 */
	public EfgEvent(Element element) {
		super(element);
	}

	/**
	 * Instantiates a new efg event.
	 *
	 * @param graph the graph
	 */
	public EfgEvent(XmlGraph graph) {
		super(graph, "EVENT");
	}

	/**
	 * Instantiates a new efg event.
	 *
	 * @param graph the graph
	 * @param event the event
	 */
	private EfgEvent(XmlGraph graph, UserEvent event) {
		this(graph);
		if (!event.getType().equals("")) {
			setType(event.getType());
		}
		if (!event.getValue().equals("")) {
			setValue(event.getValue());
		}
		setWidget(event.getWidget());	
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WrapperInterface#getWrapper(org.w3c.dom.Element)
	 */
	public WrapperInterface getWrapper(Element element) {
		return new EfgEvent (element);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#getType()
	 */
	public String getType() {
		return getElement().getAttribute("type");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#setType(java.lang.String)
	 */
	public void setType(String type) {
		getElement().setAttribute("type", type);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#getValue()
	 */
	public String getValue() {
		return getElement().getAttribute("value");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#setValue(java.lang.String)
	 */
	public void setValue(String value) {
		getElement().setAttribute("value", value);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserEvent#getWidget()
	 */
	public WidgetState getWidget() {
		return new WidgetAdapter () {

			public String getId() {
				return getWidgetId();
			}

			public String getName() {
				return getWidgetName();
			}

			public String getType() {
				return getWidgetType();
			}

			public void setId(String id) {
				setWidgetId(id);
			}

			public void setName(String name) {
				setWidgetName(name);				
			}

			public void setType(String type) {
				setWidgetType(type);
			}

		};
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserEvent#setWidget(it.slumdroid.droidmodels.model.WidgetState)
	 */
	public void setWidget(WidgetState widget) {
		if (!widget.getName().equals("")) {
			setWidgetName (widget.getName());
		}
		if (!widget.getId().equals("")) {
			setWidgetId (widget.getId());	
		}
		if (!widget.getType().equals("")) {
			setWidgetType (widget.getType());
		}
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserEvent#getWidgetName()
	 */
	public String getWidgetName() {
		return getElement().getAttribute("widget_name");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserEvent#setWidgetName(java.lang.String)
	 */
	public void setWidgetName(String name) {
		getElement().setAttribute("widget_name", name);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserEvent#getWidgetType()
	 */
	public String getWidgetType() {
		return getElement().getAttribute("widget_type");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserEvent#setWidgetType(java.lang.String)
	 */
	public void setWidgetType(String type) {
		getElement().setAttribute("widget_type", type);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#getWidgetId()
	 */
	public String getWidgetId() {
		return getElement().getAttribute("widget_id");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#setWidgetId(java.lang.String)
	 */
	public void setWidgetId(String id) {
		getElement().setAttribute("widget_id", id);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<EfgEvent> iterator() {
		return new NodeListWrapper<EfgEvent>(this, new EfgEvent());
	}

	/**
	 * Equals.
	 *
	 * @param event the event
	 * @return true, if successful
	 */
	public boolean equals(UserEvent event) {
		return event.getType().equals(this.getType()) && event.getWidgetId().equals(this.getWidgetId()) && event.getValue().equals(this.getValue()); 
	}

	/**
	 * From user event.
	 *
	 * @param graph the graph
	 * @param event the event
	 * @return the efg event
	 */
	public static EfgEvent fromUserEvent(XmlGraph graph, UserEvent event) {
		return new EfgEvent(graph, event);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#setId(java.lang.String)
	 */
	public void setId(String id) {
		getElement().setAttribute("id", id);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#getId()
	 */
	public String getId() {
		return getElement().getAttribute("id");
	}

}
