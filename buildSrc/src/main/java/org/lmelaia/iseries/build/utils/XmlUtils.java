/*   Copyright (C) 2016  Luke Melaia
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lmelaia.iseries.build.utils;

import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A set of utilities to aid in working with xml.
 * 
 * @author Luke Melaia
 */
public class XmlUtils {
    
    /**
     * An instance of the xml document the user is working on.
     */
    private final Document DOCUMENT;
    
    /**
     * @param doc the xml document being worked on. 
     */
    public XmlUtils(Document doc){
        DOCUMENT = Objects.requireNonNull(doc);
    }
    
    /**
     * Creates a new element with the xml document.
     * 
     * @param name the tag name of the element
     * @return the element object
     */
    public Element getNewElement(String name){
        return DOCUMENT.createElement(name);
    }
    
    /**
     * Creates a new element and appends a text node to it.
     * 
     * @param name the elements tag name.
     * @param content the text in the text node.
     * @return the element object.
     */
    public Element getNewElementWithContent(String name, String content){
        return appendTextToElement(getNewElement(name), content);
    }
    
    /**
     * Appends a text node to an element.
     * 
     * @param element the element
     * @param text the text in the text node.
     * @return the element passed in.
     */
    public Element appendTextToElement(Element element, String text){
        return appendNodeToElement(element, DOCUMENT.createTextNode(text));
    }
    
    /**
     * Appends a node to an element.
     * 
     * @param element the element.
     * @param node the node.
     * @return the element passed in.
     */
    public Element appendNodeToElement(Element element, Node node){
        element.appendChild(node);
        return element;
    }
    
    /**
     * Appends an element to the document.
     * 
     * @param element the element to append.
     * @return an instance of this class ({@code this}). Allows the chaining of
     * {@link #appendBaseElement(org.w3c.dom.Element)} calls.
     */
    public XmlUtils appendBaseElement(Element element){
        DOCUMENT.appendChild(element);
        return this;
    }
}
