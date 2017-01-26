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

import javax.xml.parsers.ParserConfigurationException;
import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Tests the xml document helper class.
 *
 * @author Luke Melaia
 */
public class XmlDocumentHelperTest {

    public XmlDocumentHelperTest() {
    }

    @Test
    public void testClassAndDocumentCreation()
            throws ParserConfigurationException {
        XmlDocumentHelper doc = XmlDocumentHelper.getInstanceWithNewDocument();

        if (doc == null || doc.getXmlDocument() == null) {
            fail("Xml document or helper class are null");
        }
    }

    @Test
    public void testNewRootElement() throws ParserConfigurationException {
        Element el = XmlDocumentHelper.getInstanceWithNewDocument()
                .newRootElement("root").getElement();

        if (el == null || !el.getTagName().equals("root")) {
            fail("Root element not created properly");
        }
    }

    @Test
    public void testNewElement() throws ParserConfigurationException {
        if (!XmlDocumentHelper.getInstanceWithNewDocument().newElement("element")
                .getElement().getTagName().equals("element")) {
            fail("New element not created properly");
        }
    }

    @Test
    public void testAddNode() throws ParserConfigurationException {
        XmlDocumentHelper helper
                = XmlDocumentHelper.getInstanceWithNewDocument();

        helper.newRootElement("root").addNode(
                helper.newElement("element").getElement());

        if (!helper.getRootElement().getElement().hasChildNodes()) {
            fail("Element not added to root");
        }
    }

    @Test
    public void testAddText() throws ParserConfigurationException {
        if (!XmlDocumentHelper.getInstanceWithNewDocument()
                .newRootElement("root").addText("hello").getElement()
                .getFirstChild().getNodeValue().equals("hello")) {
            fail("Couldn't add text to root node: \"root\"");
        }
    }

    @Test
    public void testAddNewElement() throws ParserConfigurationException {
        if (!XmlDocumentHelper.getInstanceWithNewDocument()
                .newRootElement("root").addNewElement("name").getElement()
                .getTagName().equals("name")) {
            fail("Couldn't add new element to root");
        }
    }

    @Test
    public void testChaining() throws ParserConfigurationException {
        Document doc
                = XmlDocumentHelper.getInstanceWithNewDocument()
                .newRootElement("rootElement")
                .addNewElement("someElement").addText("text")
                .getRootElement().addNewElement("ElementA")
                .addNewElement("ElementA1").addNewElement("ElementA2")
                .addText("someText here").getRootElement()
                .addNewElement("ElementB").addText("tteexxtt").getDocument();
        
        //If no exception is thrown, this should work.
    }
}
