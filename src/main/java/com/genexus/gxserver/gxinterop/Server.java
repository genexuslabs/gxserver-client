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
package com.genexus.gxserver.gxinterop;

import com.genexus.gxserver.client.clients.ServerHelperClient;
import com.genexus.gxserver.client.clients.TeamWorkService2Client;
import com.genexus.gxserver.client.clients.common.ServiceData;
import com.genexus.gxserver.client.info.KBList;
import com.genexus.gxserver.client.info.ServerInfo;
import com.genexus.gxserver.client.info.VersionInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 *
 * @author jlr
 */
public class Server {

    private static final String NOT_INSTANTIATED_ERROR_MESSAGE = "Invalid GXserver connection data. Make sure to call setConnectionData()";

    private ServiceData connectionData = null;
    private ServerHelperClient shClient = null;
    private TeamWorkService2Client tws2Client = null;

    public boolean gotError;
    public String exceptionType;
    public String exceptionDetails;
    public String exceptionStack;

    public Server() {
        resetResults();
    }

    @SuppressFBWarnings("URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD")
    private void resetResults() {
        gotError = false;
        exceptionType = "";
        exceptionDetails = "";
        exceptionStack = "";
    }

    private void setErrorResults(Exception ex) {
        gotError = true;
        exceptionType = ex.getClass().toString();
        exceptionDetails = ex.getMessage();

        exceptionStack = "";
        for (StackTraceElement line : ex.getStackTrace()) {
            exceptionStack += line.toString();
        }
    }

    /**
     * Calls some predicate. In case of any exception it captures it, calls  the
     * setErrorResults to save the exception details and returns the received
     * default value
     *
     * @return the result of the Callable invocation or the default value in
     * case of exception.
     */
    private <T> T callAndCatch(Callable<T> s, T defaultValue) {
        resetResults();

        T result = defaultValue;
        try {
            result = s.call();
        } catch (Exception ex) {
            setErrorResults(ex);
        }

        return result;
    }

    /**
     * Sets server connection data.
     *
     * @param serverURL
     * @param user
     * @param password
     */
    public void setConnectionData(String serverURL, String user, String password) {
        callAndCatch(
                () -> {
                    connectionData = new ServiceData(serverURL, user, password);
                    shClient = new ServerHelperClient(connectionData);
                    tws2Client = new TeamWorkService2Client(connectionData);

                    // no actual return value
                    return 0;
                },
                0
        );
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

    private TeamWorkService2Client getTws2Client() throws IOException {
        if (tws2Client == null) {
            if (connectionData == null) {
                throw new IOException(NOT_INSTANTIATED_ERROR_MESSAGE);
            }

            tws2Client = new TeamWorkService2Client(connectionData);
        }

        return tws2Client;
    }

    /**
     * Uses server connection data to check for a successful connection and
     * response.
     *
     * @return true if connection is successful and the server reports being
     * alive.
     */
    public Boolean isAlive() {
        return callAndCatch(
                () -> getShClient().isServerAlive(),
                false
        );
    }

    /**
     * Obtains a {@link ServerInfo} detailing server configuration and
     * capabilities
     *
     * @return ServerInfo
     */
    public ServerInfo getInfo() {
        return callAndCatch(
                () -> getShClient().getServerInfo(),
                new ServerInfo()
        );
    }
    
    /**
     * Obtains {@link KBInfo} for each KB currently hosted by the server
     *
     * @return ArrayList&lt;KBInfo&gt;
     */
    public ArrayList<KBInfo> getHostedKBs() {
        return callAndCatch(
                () -> {
                    KBList hostedList = getTws2Client().getHostedKBs();
                    ArrayList<KBInfo> hostedArray = new ArrayList<>();
                    hostedList.forEach(info -> {
                        hostedArray.add(new KBInfo(info));
                    });

                    return hostedArray;
                },
                new ArrayList<>()
        );
    }

    /**
     * Obtains {@link VersionInfo} for each version of a KB
     *
     * @param kbName Name of Knowledge Base
     * @return ArrayList&lt;VersionInfo&gt;
     */
    public ArrayList<VersionInfo> getVersions(String kbName) {
        return callAndCatch(
                () -> getTws2Client().getVersions(kbName),
                new ArrayList<>()
        );
    }
}
