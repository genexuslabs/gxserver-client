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
import com.genexus.gxserver.client.info.ActionInfo;
import com.genexus.gxserver.client.info.KBInfo;
import com.genexus.gxserver.client.info.KBList;
import com.genexus.gxserver.client.info.RevisionInfo;
import com.genexus.gxserver.client.info.VersionInfo;
import com.genexus.gxserver.client.info.VersionList;
import java.io.IOException;
import java.util.UUID;
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
public class TeamWorkService2ClientTest {


    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }
    public TeamWorkService2ClientTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getHostedKBs, getVersions and getRevisions methods, of class
     * TeamWorkService2Client.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testAccessByCredentials() throws Exception {
        ServiceData serverData = ServerData.getCredentialsServerData();
        TeamWorkService2Client twClient = new TeamWorkService2Client(
                serverData.getServerURL().toString(),
                serverData.getUserName(),
                serverData.getUserPassword()
        );

        CheckServerAccess(twClient);
    }

    /**
     * Test of getHostedKBs, getVersions and getRevisions methods, of class
     * TeamWorkService2Client.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testAccessByToken() throws Exception {
        ServiceData serverData = ServerData.getTokenServerData();
        TeamWorkService2Client twClient = new TeamWorkService2Client(
                serverData.getServerURL().toString(),
                serverData.getToken()
        );

        CheckServerAccess(twClient);
    }

    private void CheckServerAccess(TeamWorkService2Client twClient) throws IOException {
        // check there are KBs
        KBList kbs = twClient.getHostedKBs();
        assertNotNull(kbs);
        assertTrue(!kbs.isEmpty());

        // take some KB
        KBInfo kb = kbs.get(kbs.size() / 2);

        // check there are versions in it
        VersionList versions = twClient.getVersions(kb.name);
        assertNotNull(versions);
        assertTrue(!versions.isEmpty());

        // take first version
        VersionInfo version = versions.get(0);

        // iterate revisions and actions
        RevisionsQuery query = new RevisionsQuery(
                twClient,
                kb.name,
                version.name
        );

        final String verPropsGuid = "00000000-0000-0000-0000-000000000010";

        assert (query.iterator().hasNext());
        for (RevisionInfo revision : query) {
            assertTrue(revision.id >= 0);
            assertNotNull(revision.guid);
            assertNotNull(revision.date);
            assertFalse(revision.author.isEmpty());
            assertNotEquals(new UUID(0L, 0L), revision.guid);
            assertFalse(revision.author.isEmpty());
            assertFalse(revision.comment.isEmpty());
            assertNotNull(revision.date);

            for (ActionInfo action : revision.getActions()) {
                assertNotEquals(new UUID(0L, 0L), action.objectGuid);
                assertFalse(action.objectKey.isEmpty());
                if (!action.objectKey.startsWith(verPropsGuid)) {
                    assertFalse(action.objectType.isEmpty());
                }
                assertFalse(action.objectName.isEmpty());
                assertNotNull(action.objectDescription);
                assertNotNull(action.actionType);
                assertFalse(action.userName.isEmpty());

                // editedTimestamp may be null
                // assertNotNull(action.editedTimestamp);
            }
        }
    }
}
