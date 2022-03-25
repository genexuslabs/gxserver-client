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
import java.net.MalformedURLException;

/**
 *
 * @author jlr
 */
public class ServerData {
    
        public static ServiceData getServerDatauserAndPassword() throws MalformedURLException {
            return new ServiceData(
                    "https://open.genexusserver.com/v17",
                    System.getenv("GXSERVER_USER"),
                    System.getenv("GXSERVER_PASSWORD")
                );
        }
        
        public static ServiceData getServerDataToken() throws MalformedURLException {
            return new ServiceData(
                    "https://open.genexusserver.com/v17",
                    "a9b30c7d-888a-45fa-9493-3f9aaadedae0!e874ed2c1dc477876adf73a2e7083ce9fc47441afcb38005315c6013dcccc79c06a320b1dbb2f4"
                );
        }
}
