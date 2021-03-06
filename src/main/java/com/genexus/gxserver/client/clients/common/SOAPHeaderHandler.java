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
package com.genexus.gxserver.client.clients.common;

import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.w3c.dom.DOMException;

/**
 *
 * @author jlr
 */
public class SOAPHeaderHandler implements SOAPHandler<SOAPMessageContext> {

    private final int MESSAGE_TIME_RANGE_MILLISECONDS = 5 * 60 * 1000;

    @Override
    public Set<QName> getHeaders() {
        final HashSet<QName> headers = new HashSet<>();
        headers.add(getSecurityQName());
        return headers;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        // nothing to do for incoming messages
        Boolean isOutbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (!isOutbound) {
            return true;
        }

        // nothing to do when there are no credentials
        Boolean isSecure = getIsSecure(context);
        if (!isSecure) {
            return true;
        }

        try {
            SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
            SOAPHeader header = envelope.getHeader();
            if (header == null) {
                header = envelope.addHeader();
            }

            SOAPHeaderElement security = header.addHeaderElement(getSecurityQName());
            if (!security.getMustUnderstand()) {
                security.setMustUnderstand(true);
            }

            addTimestampToken(security, context);
            addUsernameToken(security, context);

        } catch (SOAPException | DOMException e) {
            Logger.getLogger(SOAPHeaderHandler.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }

        return true;
    }

    private void addUsernameToken(SOAPHeaderElement security, SOAPMessageContext context) throws SOAPException {
        SOAPElement usernameToken = security.addChildElement(WSConstants.USERNAME_TOKEN_LNAME, WSConstants.WSSE_PREFIX);

        usernameToken.addAttribute(
                new QName(WSConstants.WSU_NS, WSConstants.ID_ATTRIBUTE_NAME, WSConstants.WSU_PREFIX),
                getNewUsernameTokenId());

        SOAPElement usernameNode = usernameToken.addChildElement("Username", WSConstants.WSSE_PREFIX);
        usernameNode.addTextNode(getUsername(context));

        SOAPElement passwordNode = usernameToken.addChildElement("Password", WSConstants.WSSE_PREFIX);
        passwordNode.setAttribute("Type", WSConstants.PASSWORD_TEXT_NS);
        passwordNode.addTextNode(getPassword(context));
    }

    private void addTimestampToken(SOAPHeaderElement security, SOAPMessageContext context) throws SOAPException {
        SOAPElement timestampToken = security.addChildElement(
                new QName(WSConstants.WSU_NS, WSConstants.TIMESTAMP_LNAME, WSConstants.WSU_PREFIX));

        timestampToken.addAttribute(
                new QName(WSConstants.WSU_NS, WSConstants.ID_ATTRIBUTE_NAME, WSConstants.WSU_PREFIX),
                "_0");

        Instant createTime = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        SOAPElement createdNode = timestampToken.addChildElement("Created", WSConstants.WSU_PREFIX);
        createdNode.addTextNode(createTime.toString());

        Instant expireTime = createTime.plusMillis(MESSAGE_TIME_RANGE_MILLISECONDS);
        SOAPElement expiresdNode = timestampToken.addChildElement("Expires", WSConstants.WSU_PREFIX);
        expiresdNode.addTextNode(expireTime.toString());
    }

    @Override
    public boolean handleFault(SOAPMessageContext c) {
        return false;
    }

    @Override
    public void close(MessageContext mc) {
    }

    private static QName getSecurityQName() {
        return new QName(WSConstants.WSSE_NS, WSConstants.WSSE_SECURITY_LNAME, WSConstants.WSSE_PREFIX);
    }

    private Boolean getIsSecure(MessageContext context) {
        return getSafeBooleanProp(context, ServiceData.GXSERVER_ISSECURE_PROPERTY);
    }

    private String getUsername(MessageContext context) {
        return getSafeStringProp(context, ServiceData.GXSERVER_USERNAME_PROPERTY);
    }

    private String getPassword(MessageContext context) {
        return getSafeStringProp(context, ServiceData.GXSERVER_PASSWORD_PROPERTY);
    }

    private String getSafeStringProp(MessageContext context, String propName) {
        String value = (String) context.get(propName);
        return value != null ? value : "";
    }

    private Boolean getSafeBooleanProp(MessageContext context, String propName) {
        String value = (String) context.get(propName);
        return value != null ? Boolean.parseBoolean(value) : false;
    }

    private String getNewUsernameTokenId() {
        final String prefix = "uuid-";
        return prefix + UUID.randomUUID().toString();
    }
}
