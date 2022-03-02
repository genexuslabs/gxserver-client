/*
 * The MIT License
 *
 * Copyright 2022 GeneXus S.A..
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

// BASED ON SOOURCE: https://gist.github.com/AlainODea/1779a7c6a26a5c135280bc9b3b71868f
// which is in turn based on https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#JAXP_DocumentBuilderFactory.2C_SAXParserFactory_and_DOM4J
package com.genexus.gxserver.client.helpers;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author jlr
 */
public interface DocumentBuilders
{
    interface ParserConfigurer
    {
        void configureParser(DocumentBuilderFactory factory) throws ParserConfigurationException;
    }

    static DocumentBuilder createSaferDocumentBuilder(ParserConfigurer parserConfigurer) throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        // This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all
        // XML entity attacks are prevented
        // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        
        // If you can't completely disable DTDs, then at least do the following:
        // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
        // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
        // JDK7+ - http://xml.org/sax/features/external-general-entities
        // This feature has to be used together with the following one, otherwise it will not protect you from XXE for sure
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        
        // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
        // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
        // JDK7+ - http://xml.org/sax/features/external-parameter-entities
        // This feature has to be used together with the previous one, otherwise it will not protect you from XXE for sure
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        
        // Disable external DTDs as well
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        
        // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        
        // And, per Timothy Morgan: "If for some reason support for inline DOCTYPEs are a requirement, then
        // ensure the entity settings are disabled (as shown above) and beware that SSRF attacks
        // (http://cwe.mitre.org/data/definitions/918.html) and denial
        // of service attacks (such as billion laughs or decompression bombs via "jar:") are a risk."

        parserConfigurer.configureParser(factory);

        return factory.newDocumentBuilder();
    }
}
