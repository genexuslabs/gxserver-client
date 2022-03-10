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
package com.genexus.gxserver.client.helpers;

import com.genexus.gxserver.client.info.RevisionList;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author jlr
 */
public class XmlHelperTest {

    public XmlHelperTest() {
    }

    /**
     * Test of parse method, of class XmlHelper.
     */
    @Test
    public void testWriteAndParse() throws Exception {
        System.out.println("parse");

        RevisionList someObject = new RevisionList();

        Path tempPath1 = Files.createTempFile("", ".tmp");
        Path tempPath2 = Files.createTempFile("", ".tmp");
        assertNotEquals(tempPath1, tempPath2);

        File tempFile1 = tempPath1.toFile();
        File tempFile2 = tempPath2.toFile();
        assertEquals(tempPath1, tempFile1.toPath());
        assertEquals(tempPath2, tempFile2.toPath());

        // write to file 1
        XmlHelper.writeXml(someObject, tempFile1);

        // delete file1
        // which also makes sure the file was properly closed after writing to it
        Files.delete(tempPath1);

        // write to file 2 and parse from it
        XmlHelper.writeXml(someObject, tempFile2);
        RevisionList parsedObject = XmlHelper.parse(tempFile2, someObject.getClass());

        // make sure it was properly parsed
        assertEquals(someObject, parsedObject);

        // delete file1
        // which also makes sure the file was properly closed after writing to
        // and reading from it
        Files.delete(tempPath2);
    }

    /**
     * Test of normalizeXmlString method, of class XmlHelper.
     */
    @Test
    public void testNormalizeXmlString() throws Exception {
        System.out.println("normalizeXmlString");

        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

        String inputString1 = "<root>  <node>  </node>  <node att1=\"att\">\r\n"
                + "</node>\r\n"
                + "       <node att1= \"att\" >\r\n"
                + "</node> <node>      <subnode> </subnode>\r\n"
                + "</node></root>";

        String expResult = header
                + "<root>\r\n"
                + "  <node/>\r\n"
                + "  <node att1=\"att\"/>\r\n"
                + "  <node att1=\"att\"/>\r\n"
                + "  <node>\r\n"
                + "    <subnode/>\r\n"
                + "  </node>\r\n"
                + "</root>";
        String result = XmlHelper.normalizeXmlString(inputString1);
        assertEquals(expResult.length(), result.length());
        assertEquals(String.format("Expected %s, obtained %s", expResult, result), expResult, result);
        assertEquals(expResult, result);

        String inputString2 = header + inputString1;
        result = XmlHelper.normalizeXmlString(inputString2);
        assertEquals(expResult, result);

        String inputString3 = header + "\r\n" + inputString1;
        result = XmlHelper.normalizeXmlString(inputString3);
        assertEquals(expResult, result);
    }
}
