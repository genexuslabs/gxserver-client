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
package com.genexus.gxserver.client.info;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 *
 * @author jlr
 */
public class ServerInfo {

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public String serverVersion = "";

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean isAvailable = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean isSecure = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean supportsTokenAuthentication = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean allowsGXtest = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean allowsCustomBinding = false;
    
    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public int restServicesAPIVersion = 0;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean allowsSourceChecksum = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean allowsPartialUpdate = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean allowsObjectLock = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean allowsAdvancedVersions = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean expectsReceiveLastUpdateDate = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean allowsAdvancedCheckout = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean allowsBCHRemind = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean allowsVersionManagement = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean allowsAdvancedHostedKB = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean allowsObjectShowDifference = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean supportsMetadataActions = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean usesNewPermissionsName = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean usesToledoUrlFormat = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean allowsCheckoutVersion = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean supportsExploreRemoteKb = false;

    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public Boolean supportsContinuousIntegration = false;
}
