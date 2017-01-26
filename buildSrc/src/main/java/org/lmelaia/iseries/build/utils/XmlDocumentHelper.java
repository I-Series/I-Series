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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A set of utilities which aid in working with xml documents.
 * 
 * <p>
 * The main use of this class is creating and editing xml documents through the
 * use of chained method calls. This makes it easy to add elements one after
 * the other with fewer calls and less boilerplate code.
 * </p>
 * 
 * <p>
 * This class provides useful methods for creating new documents and elements,
 * adding text and nodes to elements, appending elements to other elements or
 * documents, and a few other useful methods.
 * </p>
 * 
 * <h3>Example:</h3>
 * <pre>
 * Document doc = XmlDocumentHelper.getInstanceWithNewDocument()
 *              .newRootElement("rootElement")
 *              .addNewElement("element1").addText("element1 text")
 *              .getRootElement()
 *              .addNewElement("element2").addText("element2 text")
 *              .getDocument();
 * </pre>
 * 
 * Will create the xml document:
 * 
 * <pre>
 * {@code 
 * <rootElement>
 *   <element1>element1 text</element1>
 *   <element2>element2 text</element2>
 * </rootElement>
 * }
 * </pre>
 * 
 * @author Luke Melaia
 */
public class XmlDocumentHelper {
    
    /**
     * The xml document this classes instance is working on.
     */
    private final Document DOCUMENT;
    
    /**
     * The newest root element of the xml document. Assigned by calling
     * {@link #newRootElement(java.lang.String)}.
     */
    private ElementHelper latestRootElement;
    
    /**
     * Creates a new XmlDocumentHelper instance with a new xml document.
     * 
     * @return an XmlDocumentHelper with a new document.
     * @throws ParserConfigurationException 
     */
    public static final XmlDocumentHelper getInstanceWithNewDocument()
            throws ParserConfigurationException{
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().newDocument();
        
        return new XmlDocumentHelper(doc);
    }
    
    /**
     * Creates a new XmlDocumentHelper with an instance of an xml document.
     * 
     * @param doc the xml document this object will be working on. 
     */
    public XmlDocumentHelper(Document doc){
        this.DOCUMENT = Objects.requireNonNull(doc);
    }
    
    /**
     * @param tagName the elements tag name.
     * @return a new ElementHelper instance with a new element, which is linked
     * to the current xml document.
     */
    public ElementHelper newElement(String tagName){
        return ElementHelper.getInstanceWithNewElement(this, tagName);
    }
    
    /**
     * Creates a new element and appends it to the document.
     * 
     * @param tagName the elements tag name
     * @return a new ElementHelper instance with a new Element added to the
     * document.
     */
    public ElementHelper newRootElement(String tagName){
        ElementHelper helper = newElement(tagName);
        this.DOCUMENT.appendChild(helper.ELEMENT);
        this.latestRootElement = helper;
        return helper;
    }
    
    /**
     * @return the document this instanced is linked to. 
     */
    public Document getXmlDocument(){
        return this.DOCUMENT;
    }
    
    /**
     * @return the last element added to document contained in an ElementHelper
     * instance.
     */
    public ElementHelper getRootElement(){
        return this.latestRootElement;
    }
    
    /**
     * A set of utilities which aid in working with xml elements.
     */
    public static class ElementHelper{
        
        /**
         * The element this classes instance is working on.
         */
        private final Element ELEMENT;
        
        /**
         * The document this object is working on.
         */
        private final XmlDocumentHelper DOCUMENT;
        
        /**
         * Creates a new ElementHelper instance with a new element instance.
         * 
         * @param doc the document being worked on.
         * @param tagName the tag name of the element.
         * @return a new ElementHelper instance with a new Element instance.
         */
        public static ElementHelper getInstanceWithNewElement(
                XmlDocumentHelper doc, String tagName){
            return new ElementHelper(doc,
                    doc.getXmlDocument().createElement(tagName));
        }
        
        /**
         * Creates a new ElementHelper instance with a new document and new
         * Element.
         * 
         * @param tagName the elements tag name.
         * @return a new ElementHelper instance with a new Document and Element.
         * @throws ParserConfigurationException 
         */
        public static ElementHelper getInstanceWithNewDocumentAndElement(
                String tagName) throws ParserConfigurationException{
            Document doc = getInstanceWithNewDocument().getXmlDocument();
            return new ElementHelper(new XmlDocumentHelper(doc),
                    doc.createElement(tagName));
        }
        
        /**
         * Creates a new instance of an element helper.
         * 
         * @param element the element which this object will be working on.
         * @param doc the xml document this object will be working on.
         */
        public ElementHelper(XmlDocumentHelper doc, Element element){
            this.ELEMENT = Objects.requireNonNull(element);
            this.DOCUMENT = Objects.requireNonNull(doc);
        }
        
        /**
         * @return the element object this object is working on.
         */
        public Element getElement(){
            return this.ELEMENT;
        }
        
        /**
         * @return the xml document being worked on. 
         */
        public Document getDocument(){
            return this.DOCUMENT.getXmlDocument();
        }
        
        public XmlDocumentHelper getDocumentHelper(){
            return this.DOCUMENT;
        }
        
        /**
         * Appends a node to the current element.
         * 
         * @param node the node.
         * @return an instance of this so {@link #addNode(org.w3c.dom.Node)}
         * calls can be chained together.
         */
        public ElementHelper addNode(Node node){
            this.ELEMENT.appendChild(node);
            return this;
        }
        
        /**
         * Appends a new text node to the element.
         * 
         * @param text the text nodes text.
         * @return an instance of this so {@link #addText(java.lang.String)}
         * calls can be chained together.
         */
        public ElementHelper addText(String text){
            return addNode(DOCUMENT.getXmlDocument().createTextNode(text));
        }
        
        /**
         * Appends a new element to the element contained in this object.
         * 
         * @param tagName the new elements tag name
         * @return the new element created wrapped in an ElementHelper instance.
         */
        public ElementHelper addNewElement(String tagName){
            ElementHelper helper = getInstanceWithNewElement(DOCUMENT, tagName);
            this.addNode(helper.ELEMENT);
            return helper;
        }
        
        /**
         * Appends the Element object this class holds to a node.
         * 
         * @param node the node the element object will be appended to.
         * @return an instance of this so calls can be chained.
         */
        public ElementHelper addTo(Node node){
            node.appendChild(this.ELEMENT);
            return this;
        }
        
        /**
         * @return the element helper instance obtained by calling 
         * {@link XmlDocumentHelper#getRootElement()} on the current document.
         */
        public ElementHelper getRootElement(){
            return this.DOCUMENT.getRootElement();
        }
    }
}
