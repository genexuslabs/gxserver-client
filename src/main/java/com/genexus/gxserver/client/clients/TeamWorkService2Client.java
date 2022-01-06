/*
 * The MIT License
 *
 * Copyright 2020 GeneXus S.A..
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

import com.genexus.gxserver.client.helpers.XmlHelper;
import com.genexus.gxserver.client.clients.common.ServiceData;
import com.genexus.gxserver.client.clients.common.ServiceInfo;
import com.genexus.gxserver.client.clients.common.TransferPropConstants;
import com.genexus.gxserver.client.clients.common.TransferPropHelper;
import com.genexus.gxserver.client.clients.common.WithLocalContextClassLoader;
import com.genexus.gxserver.client.info.KBList;
import com.genexus.gxserver.client.info.RevisionList;
import com.genexus.gxserver.client.info.VersionList;
import com.genexus.gxserver.client.services.contracts.ArrayOfServerMessage;
import com.genexus.gxserver.client.services.contracts.ArrayOfTransferProp;
import com.genexus.gxserver.client.services.teamwork.FileTransfer;
import com.genexus.gxserver.client.services.teamwork.ITeamWorkService2;
import com.genexus.gxserver.client.services.teamwork.ITeamWorkService2GetRevisionsGXServerExceptionFaultFaultMessage;
import com.genexus.gxserver.client.services.teamwork.ITeamWorkService2GetVersionsGXServerExceptionFaultFaultMessage;
import com.genexus.gxserver.client.services.teamwork.ITeamWorkService2HostedKBsGXServerExceptionFaultFaultMessage;
import com.genexus.gxserver.client.services.teamwork.SimpleTransfer;
import com.genexus.gxserver.client.services.teamwork.TeamWorkService2;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.soap.MTOMFeature;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author jlr
 */
public class TeamWorkService2Client extends BaseClient {

    private static final ServiceInfo TEAMWORK_SERVICE2_INFO = new ServiceInfo(
            "TeamWorkService2.svc/secure",
            "TeamWorkService2.svc",
            "CustomBinding_ITeamWorkService2",
            "BasicHttpBinding_ITeamWorkService2"
    );

    @Override
    protected ServiceInfo getServiceInfo() {
        return TEAMWORK_SERVICE2_INFO;
    }

    public TeamWorkService2Client(String serverURL, String user, String password) throws MalformedURLException {
        super(new ServiceData(serverURL, user, password));
    }

    private LocalContextServiceWrapper teamWorkService2 = null;

    private LocalContextServiceWrapper getTeamWorkService2() throws IOException {
        if (teamWorkService2 == null) {

            ITeamWorkService2 port = WithLocalContextClassLoader.call(() -> {
                TeamWorkService2 service = new TeamWorkService2();
                return service.getCustomBindingITeamWorkService2(new MTOMFeature(true));
            });

            prepareClient((BindingProvider) port);

            teamWorkService2 = new LocalContextServiceWrapper(port);
        }

        return teamWorkService2;
    }

    public KBList getHostedKBs() throws IOException {
        try {
            SimpleTransfer parameters = new SimpleTransfer();
            Holder<ArrayOfServerMessage> messages = new Holder<>(new ArrayOfServerMessage());
            Holder<ArrayOfTransferProp> properties = new Holder<>(createBasicProperties());

            FileTransfer transfer = getTeamWorkService2().hostedKBs(parameters, messages, properties);
            byte[] bytes = transfer.getFileByteStream();
            InputStream stream = new ByteArrayInputStream(bytes);

            return XmlHelper.parse(stream, KBList.class);
        } catch (ITeamWorkService2HostedKBsGXServerExceptionFaultFaultMessage ex) {
            Logger.getLogger(TeamWorkService2Client.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("Error accessing GXserver", ex);
        } catch (SAXException | ParserConfigurationException | JAXBException ex) {
            Logger.getLogger(TeamWorkService2Client.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("Failed to parse KB list", ex);
        }
    }

    public VersionList getVersions(String kbName) throws IOException {
        try {
            SimpleTransfer parameters = new SimpleTransfer();
            Holder<ArrayOfServerMessage> messages = new Holder<>(new ArrayOfServerMessage());
            Holder<ArrayOfTransferProp> properties = new Holder<>(createGetVersionsProperties(kbName));

            FileTransfer transfer = getTeamWorkService2().getVersions(parameters, messages, properties);
            byte[] bytes = transfer.getFileByteStream();
            InputStream stream = new ByteArrayInputStream(bytes);

            return XmlHelper.parse(stream, VersionList.class);
        } catch (ITeamWorkService2GetVersionsGXServerExceptionFaultFaultMessage ex) {
            Logger.getLogger(TeamWorkService2Client.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("Error accessing GXserver", ex);
        } catch (SAXException | ParserConfigurationException | JAXBException ex) {
            Logger.getLogger(TeamWorkService2Client.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("Failed to parse Version list", ex);
        }
    }

    public RevisionList getRevisions(String kbName, int versionId, String query, int page) throws IOException {
        try {
            byte[] bytes = getRevisionsPage(kbName, versionId, query, page);
            InputStream stream = new ByteArrayInputStream(bytes);
            return XmlHelper.parse(stream, RevisionList.class);
        } catch (SAXException | ParserConfigurationException | JAXBException ex) {
            Logger.getLogger(TeamWorkService2Client.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("Failed to parse Revision list", ex);
        }
    }

    private byte[] getRevisionsPage(String kbName, int versionId, String query, int page) throws IOException {
        try {
            SimpleTransfer parameters = new SimpleTransfer();
            Holder<ArrayOfServerMessage> messages = new Holder<>(new ArrayOfServerMessage());
            Holder<ArrayOfTransferProp> properties = new Holder<>(createGetRevisionsProperties(kbName, versionId, query, page));

            FileTransfer transfer = getTeamWorkService2().getRevisions(parameters, messages, properties);
            return transfer.getFileByteStream();

        } catch (ITeamWorkService2GetRevisionsGXServerExceptionFaultFaultMessage ex) {
            Logger.getLogger(TeamWorkService2Client.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("Error accessing GXserver", ex);
        }
    }

    private ArrayOfTransferProp createBasicProperties() {
        ArrayOfTransferProp properties = new ArrayOfTransferProp();

        properties.getTransferProp().addAll(Arrays.asList(
                TransferPropHelper.createStringProp(TransferPropConstants.CLIENT_GXVERSION, getClientVersion()),
                TransferPropHelper.createStringProp(TransferPropConstants.CLIENT_USER, "Anonymous"),
                TransferPropHelper.createGuidProp(TransferPropConstants.CLIENT_INSTANCE, UUID.randomUUID().toString())
        ));
        return properties;
    }

    private ArrayOfTransferProp createGetVersionsProperties(String kbName) {
        ArrayOfTransferProp properties = createBasicProperties();

        properties.getTransferProp().add(
                TransferPropHelper.createStringProp(TransferPropConstants.SERVER_KB_NAME, kbName)
        );

        return properties;
    }

    private ArrayOfTransferProp createGetRevisionsProperties(String kbName, int versionId, String query, int page) {
        ArrayOfTransferProp properties = createBasicProperties();

        properties.getTransferProp().addAll(Arrays.asList(
                TransferPropHelper.createStringProp(TransferPropConstants.SERVER_KB_NAME, kbName),
                TransferPropHelper.createIntProp(TransferPropConstants.SERVER_VERSION_ID, versionId),
                TransferPropHelper.createStringProp(TransferPropConstants.SERVER_REVISIONS_QUERY, query),
                TransferPropHelper.createIntProp(TransferPropConstants.SERVER_REVISIONS_PAGE, page)
        ));

        return properties;
    }

    private static class LocalContextServiceWrapper {

        private final ITeamWorkService2 service;

        LocalContextServiceWrapper(ITeamWorkService2 service) {
            this.service = service;
        }

        public FileTransfer hostedKBs(SimpleTransfer parameters, Holder<ArrayOfServerMessage> messages, Holder<ArrayOfTransferProp> properties) throws ITeamWorkService2HostedKBsGXServerExceptionFaultFaultMessage {
            return WithLocalContextClassLoader.call(() -> service.hostedKBs(parameters, messages, properties));
        }

        public FileTransfer getVersions(SimpleTransfer parameters, Holder<ArrayOfServerMessage> messages, Holder<ArrayOfTransferProp> properties) throws ITeamWorkService2GetVersionsGXServerExceptionFaultFaultMessage {
            return WithLocalContextClassLoader.call(() -> service.getVersions(parameters, messages, properties));
        }

        public FileTransfer getRevisions(SimpleTransfer parameters, Holder<ArrayOfServerMessage> messages, Holder<ArrayOfTransferProp> properties) throws ITeamWorkService2GetRevisionsGXServerExceptionFaultFaultMessage {
            return WithLocalContextClassLoader.call(() -> service.getRevisions(parameters, messages, properties));
        }
    }
}
