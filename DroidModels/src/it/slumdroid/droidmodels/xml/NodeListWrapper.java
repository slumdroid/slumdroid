/* This file is part of SlumDroid <https://code.google.com/p/slumdroid/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 * for more details.
 * 
 * Copyright (C) 2013-2015 Gennaro Imparato
 */

package it.slumdroid.droidmodels.xml;

import it.slumdroid.droidmodels.model.WrapperInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

// TODO: Auto-generated Javadoc
/**
 * The Class NodeListWrapper.
 *
 * @param <E> the element type
 */
public class NodeListWrapper<E extends WrapperInterface> implements Iterator<E> {
	
	/** The iterator. */
	private Iterator<Element> theIterator;
	
	/** The element. */
	static Class<Element> theElement = Element.class;
	
	/** The wrapper. */
	private WrapperInterface theWrapper;
	
	/** The class e. */
	private Class<E> theClassE;
	
	/** The constructor. */
	Constructor<E> theConstructor;

	/**
	 * Instantiates a new node list wrapper.
	 *
	 * @param parent the parent
	 * @param theClass the the class
	 */
	public NodeListWrapper (Element parent, Class<E> theClass) {
		this (parent.getChildNodes(), theClass);
	}

	/**
	 * Instantiates a new node list wrapper.
	 *
	 * @param parent the parent
	 * @param theClass the the class
	 */
	public NodeListWrapper (WrapperInterface parent, Class<E> theClass) {
		this (parent.getElement(), theClass);
	}

	/**
	 * Instantiates a new node list wrapper.
	 *
	 * @param list the list
	 * @param theClass the the class
	 */
	public NodeListWrapper (NodeList list, Class<E> theClass) {
		this.theIterator = new NodeListIterator (list);
		this.theClassE = theClass;
		try {
			this.theConstructor = this.theClassE.getConstructor(NodeListWrapper.theElement);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates a new node list wrapper.
	 *
	 * @param parent the parent
	 * @param wrapper the wrapper
	 */
	public NodeListWrapper (Element parent, E wrapper) {
		this (parent.getChildNodes(), wrapper);
	}

	/**
	 * Instantiates a new node list wrapper.
	 *
	 * @param parent the parent
	 * @param wrapper the wrapper
	 */
	public NodeListWrapper (WrapperInterface parent, E wrapper) {
		this (parent.getElement(), wrapper);
	}

	/**
	 * Instantiates a new node list wrapper.
	 *
	 * @param list the list
	 * @param wrapper the wrapper
	 */
	public NodeListWrapper (NodeList list, E wrapper) {
		this.theIterator = new NodeListIterator (list);
		this.theWrapper = wrapper;
	}

	// Delegation
	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return this.theIterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public E next() {
		Element unwrapped = this.theIterator.next();
		E wrapped = null;
		try {
			wrapped = wrap (unwrapped);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wrapped;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		this.theIterator.remove();
	}

	/**
	 * Wrap.
	 *
	 * @param element the element
	 * @return the e
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	@SuppressWarnings("unchecked")
	private E wrap (Element element) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		if (this.theWrapper instanceof WrapperInterface)
			return ((E) this.theWrapper.getWrapper(element));
		if (this.theClassE instanceof Class)
			return this.theConstructor.newInstance (element);
		return null;
	}

}
