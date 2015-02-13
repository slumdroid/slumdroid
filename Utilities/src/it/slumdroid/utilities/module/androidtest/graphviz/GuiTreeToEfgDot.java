package it.slumdroid.utilities.module.androidtest.graphviz;

import it.slumdroid.droidmodels.xml.NodeListIterator;
import it.slumdroid.utilities.module.androidtest.efg.EfgEvent;
import it.slumdroid.utilities.module.androidtest.efg.EventFlowGraph;

import static it.slumdroid.utilities.Resources.BREAK;
import static it.slumdroid.utilities.Resources.NEW_LINE;
import static it.slumdroid.utilities.Resources.TAB;
import static it.slumdroid.utilities.module.androidtest.graphviz.DotUtilities.getCaption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class GuiTreeToEfgDot.
 */
public class GuiTreeToEfgDot {

	/** The edges. */
	private List<String> edges;

	/** The efg. */
	private Document efg;

	/** The nodes. */
	private Map<String,String> nodes;

	/**
	 * Instantiates a new gui tree to efg dot.
	 *
	 * @param efg the efg
	 */
	public GuiTreeToEfgDot (EventFlowGraph efg) {
		this.efg = efg.getDom();
		this.edges = new ArrayList<String>();
		this.nodes = new HashMap<String,String>();
	}

	/**
	 * Extract edges.
	 */
	public void extractEdges () {
		Element efg = (Element) this.efg.getChildNodes().item(0);
		this.extractEdges (efg);
	}

	/**
	 * Extract edges.
	 *
	 * @param event the event
	 */
	private void extractEdges (Element event) {
		String parentName = event.getAttribute("id");
		for (Element element: new NodeListIterator (event)) {
			String transition = parentName + " -> " + element.getAttribute("id");
			this.edges.add (transition);
			this.extractEdges(element);
		}		
	}

	/**
	 * Extract nodes.
	 */
	public void extractNodes() {
		Element efg = (Element) this.efg.getChildNodes().item(0);
		this.extractNodes (efg);
	}

	/**
	 * Extract nodes.
	 *
	 * @param event the event
	 */
	private void extractNodes(Element event) {
		for (Element element: new NodeListIterator (event)) {
			String name = element.getAttribute("id");
			String nodeDesc = getCaption (new EfgEvent(element));
			this.nodes.put(name, nodeDesc);
			this.extractNodes(element);
		}		
	}

	/**
	 * Gets the dot.
	 *
	 * @return the dot
	 */
	public String getDot () {		
		StringBuilder dot = new StringBuilder ();
		dot.append("digraph EFG {" + NEW_LINE);
		extractEdges();
		dot.append(NEW_LINE + "## Edges" + BREAK);
		for (String edge: this.edges) {
			dot.append(TAB + edge + ";" + NEW_LINE);
		}
		extractNodes();
		dot.append(NEW_LINE + "## Nodes" + BREAK);
		for (String node: this.nodes.keySet()) {
			dot.append(TAB + node + " [label=\"" + nodes.get(node) + "\"];" + NEW_LINE);
		}
		dot.append(NEW_LINE + "}");		
		return dot.toString();
	}

}