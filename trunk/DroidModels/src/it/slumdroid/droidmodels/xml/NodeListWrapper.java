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
 * Copyright (C) 2014 Gennaro Imparato
 */

package it.slumdroid.droidmodels.xml;

import it.slumdroid.droidmodels.model.WrapperInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class NodeListWrapper<E extends WrapperInterface> implements Iterator<E> {
	
	private Iterator<Element> iterator;
	static Class<Element> elemento = Element.class;
	private WrapperInterface aWrapper;
	private Class<E> classe;
	Constructor<E> costruisci;

	public NodeListWrapper (Element parent, Class<E> classe) {
		this (parent.getChildNodes(), classe);
	}

	public NodeListWrapper (WrapperInterface parent, Class<E> classe) {
		this (parent.getElement(), classe);
	}

	public NodeListWrapper (NodeList lista, Class<E> classe) {
		this.iterator = new NodeListIterator (lista);
		this.classe = classe;
		try {
			this.costruisci = this.classe.getConstructor(NodeListWrapper.elemento);
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

	public NodeListWrapper (NodeList lista, E wrapper) {
		this.iterator = new NodeListIterator (lista);
		this.aWrapper = wrapper;
	}

	// Delegation
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	public E next() {
		Element unwrapped = this.iterator.next();
		E wrapped = null;
		try {
			wrapped = wrap (unwrapped);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return wrapped;
	}

	public void remove() {
		this.iterator.remove();
	}

	@SuppressWarnings("unchecked")
	private E wrap (Element e) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		if (aWrapper instanceof WrapperInterface)
			return ((E) aWrapper.getWrapper(e));
		if (classe instanceof Class)
			return this.costruisci.newInstance (e);
		return null;
	}

}
