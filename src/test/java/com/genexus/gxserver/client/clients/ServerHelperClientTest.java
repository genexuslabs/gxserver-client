/*
 * The MIT License
 *
 * Copyright 2021 GeneXus S.A..
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
package com.genexus.gxserver.client.clients;

import com.genexus.gxserver.client.clients.common.ServiceData;
import com.genexus.gxserver.client.info.ServerInfo;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jlr
 */
public class ServerHelperClientTest {

    public ServerHelperClientTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isServerAlive method, of class ServerHelperClient.
     * @throws java.lang.Exception
     */
    @Test
    public void testIsServerAlive() throws Exception {
        System.out.println("isServerAlive");

        ServiceData serverData = ServerData.getServerDataUserAndPassword();
        ServerHelperClient instance = new ServerHelperClient(
                serverData.getServerURL().toString()
        );

        assertEquals(true, instance.isServerAlive());
    }

    /**
     * Test of getServerInfo method, of class ServerHelperClient.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetServerInfo() throws Exception {
        System.out.println("getServerInfo");

        ServiceData serverData = ServerData.getServerDataUserAndPassword();
        ServerHelperClient instance = new ServerHelperClient(
                serverData.getServerURL().toString()
        );
        
        ServerInfo info = instance.getServerInfo();
        assertNotNull(info);
    }
}
