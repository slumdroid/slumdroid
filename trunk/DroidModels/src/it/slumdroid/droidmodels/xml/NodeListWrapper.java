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

public class NodeListWrapper<E extends WrapperInterface> implements Iterator<E> {
	
	private Iterator<Element> theIterator;
	static Class<Element> theElement = Element.class;
	private WrapperInterface theWrapper;
	private Class<E> theClassE;
	Constructor<E> theConstructor;

	public NodeListWrapper (Element parent, Class<E> theClass) {
		this (parent.getChildNodes(), theClass);
	}

	public NodeListWrapper (WrapperInterface parent, Class<E> theClass) {
		this (parent.getElement(), theClass);
	}

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

	public NodeListWrapper (Element parent, E wrapper) {
		this (parent.getChildNodes(), wrapper);
	}

	public NodeListWrapper (WrapperInterface parent, E wrapper) {
		this (parent.getElement(), wrapper);
	}

	public NodeListWrapper (NodeList list, E wrapper) {
		this.theIterator = new NodeListIterator (list);
		this.theWrapper = wrapper;
	}

	// Delegation
	public boolean hasNext() {
		return this.theIterator.hasNext();
	}

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

	public void remove() {
		this.theIterator.remove();
	}

	@SuppressWarnings("unchecked")
	private E wrap (Element element) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		if (this.theWrapper instanceof WrapperInterface)
			return ((E) this.theWrapper.getWrapper(element));
		if (this.theClassE instanceof Class)
			return this.theConstructor.newInstance (element);
		return null;
	}

}
