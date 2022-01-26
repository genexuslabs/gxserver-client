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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author jlr
 */
public class KBInfo {
    public String name;
    public String description;
    public String url;
    public String base64Image;
    public String tags;
    public String teamDevMode;
    public String publishUser;
    public Date publishDate = dateFromLocalDate(LocalDate.of(1970, 1, 1));

    public KBInfo() {
        this(new com.genexus.gxserver.client.info.KBInfo());
    }
    
    @SuppressFBWarnings( "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" )
    public KBInfo(com.genexus.gxserver.client.info.KBInfo original) {
        name = original.name;
        description = original.description;
        url = (original.url != null)? original.url.toString() : "";
        base64Image = original.base64Image;
        tags = original.tags;
        teamDevMode = original.teamDevMode.toString();
        publishUser = original.publishUser;
        publishDate = dateFromLocalDate(original.publishDate);
    }

    private static Date dateFromLocalDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
