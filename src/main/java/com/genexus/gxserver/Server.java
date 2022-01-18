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
package com.genexus.gxserver;

import com.genexus.gxserver.client.clients.ServerHelperClient;
import com.genexus.gxserver.client.clients.common.ServiceData;
import com.genexus.gxserver.client.info.ServerInfo;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 *
 * @author jlr
 */
public class Server {
    private static final String NOT_INSTANTIATED_ERROR_MESSAGE = "Invalid GXserver connection data. Make sure to call setConnectionData()";

    private ServiceData connectionData = null;
    private ServerHelperClient shClient = null;

    public Server() {
    }

    public void setConnectionData(String serverURL, String user, String password) throws MalformedURLException {
        connectionData = new ServiceData(serverURL, user, password);
        shClient = new ServerHelperClient(serverURL, user, password);
    }

    private ServerHelperClient getShClient() throws IOException {
        if (shClient == null) {
            if (connectionData == null) {
                throw new IOException(NOT_INSTANTIATED_ERROR_MESSAGE);
            }

            shClient = new ServerHelperClient(connectionData);
        }

        return shClient;
    }

    /**
     * Uses server connection data to check for a successful connection and
     * response.
     *
     * @return true if connection is successful and the server reports being
     * alive.
     * @throws java.io.IOException
     */
    public Boolean isAlive() throws IOException {
        return getShClient().isServerAlive();
    }

    /**
     * Obtains a {@link ServerInfo} detailing server configuration and
     * capabilities
     *
     * @return ServerInfo
     * @throws java.io.IOException
     */
    public ServerInfo getInfo() throws IOException {
        return getShClient().getServerInfo();
    }
}
