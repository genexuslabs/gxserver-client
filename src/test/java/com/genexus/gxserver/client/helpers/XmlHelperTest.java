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
import java.util.ArrayList;
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

    private static final String EOL = System.lineSeparator();
    private static final String EOL_LINUX = "\n";
    private static final String EOL_WINDOWS = "\r\n";
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    /**
     * Test of normalizeXmlString method, of class XmlHelper.
     */
    @Test
    public void testNormalizeXmlString() throws Exception {
        System.out.println("normalizeXmlString");

        TestCases testCases = CreateNormalizeTestCases();
        for (int i = 0; i < testCases.size(); i++) {
            String expectedResult = testCases.getExpectedOutput(i);
            String result = XmlHelper.normalizeXmlString(testCases.getInput(i));

            assertEquals(expectedResult.length(), result.length());
            assertEquals(String.format("Expected %s, obtained %s", expectedResult, result), expectedResult, result);
            assertEquals(expectedResult, result);
        }
    }

    private TestCases CreateNormalizeTestCases() {
        TestCases cases = new TestCases();

        String baseCaseInput = "<root>  <node>  </node>  <node att1=\"att\">" + EOL
                + "</node>" + EOL_LINUX
                + "       <node att1= \"att\" >" + EOL_WINDOWS
                + "</node> <node>      <subnode> </subnode>" + EOL
                + "</node></root>";

        String normalized = XML_HEADER + "<root>" + EOL
                + "  <node/>" + EOL
                + "  <node att1=\"att\"/>" + EOL
                + "  <node att1=\"att\"/>" + EOL
                + "  <node>" + EOL
                + "    <subnode/>" + EOL
                + "  </node>" + EOL
                + "</root>";

        cases.addCase(baseCaseInput, normalized);

        cases.addCase(XML_HEADER + baseCaseInput, normalized);
        cases.addCase(XML_HEADER + EOL_LINUX + baseCaseInput, normalized);
        cases.addCase(XML_HEADER + EOL_WINDOWS + baseCaseInput, normalized);

        cases.addCase(XML_HEADER + baseCaseInput + EOL_LINUX, normalized);
        cases.addCase(XML_HEADER + EOL_LINUX + baseCaseInput + EOL_LINUX, normalized);
        cases.addCase(XML_HEADER + EOL_WINDOWS + baseCaseInput + EOL_LINUX, normalized);

        cases.addCase(XML_HEADER + baseCaseInput + EOL_WINDOWS, normalized);
        cases.addCase(XML_HEADER + EOL_LINUX + baseCaseInput + EOL_WINDOWS, normalized);
        cases.addCase(XML_HEADER + EOL_WINDOWS + baseCaseInput + EOL_WINDOWS, normalized);

        return cases;
    }

    private class TestCases {

        public final ArrayList<String> inputs = new ArrayList<>();
        public final ArrayList<String> expectedOutputs = new ArrayList<>();

        public void addCase(String input) {
            addCase(input, input);
        }

        public void addCase(String input, String output) {
            inputs.add(input);
            expectedOutputs.add(output);
        }

        public int size() {
            return inputs.size();
        }

        public String getInput(int index) {
            return inputs.get(index);
        }

        public String getExpectedOutput(int index) {
            return expectedOutputs.get(index);
        }
    }
}
