/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package org.apache.poi.xwpf.usermodel;

import org.apache.poi.POIXMLRelation;
import org.apache.poi.POIXMLDocumentPart;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Yegor Kozlov
 */
public class XWPFRelation extends POIXMLRelation {

    /**
     * A map to lookup POIXMLRelation by its relation type
     */
    protected static Map<String, XWPFRelation> _table = new HashMap<String, XWPFRelation>();


    public static final XWPFRelation DOCUMENT = new XWPFRelation(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument",
            "/word/document.xml",
            null
    );
    public static final XWPFRelation FONT_TABLE = new XWPFRelation(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.fontTable+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable",
            "/word/fontTable.xml",
            null
    );
    public static final XWPFRelation SETTINGS = new XWPFRelation(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.settings+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings",
            "/word/settings.xml",
            null
    );
    public static final XWPFRelation STYLES = new XWPFRelation(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles",
            "/word/styles.xml",
            null
    );
    public static final XWPFRelation WEB_SETTINGS = new XWPFRelation(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.webSettings+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/webSettings",
            "/word/webSettings.xml",
            null
    );
    public static final XWPFRelation HEADER = new XWPFRelation(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.header+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/header",
            "/word/header#.xml",
            null
    );
    public static final XWPFRelation FOOTER = new XWPFRelation(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.footer+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/footer",
            "/word/footer#.xml",
            null
    );
    public static final XWPFRelation HYPERLINK = new XWPFRelation(
            null,
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink",
            null,
            null
    );
    public static final XWPFRelation COMMENT = new XWPFRelation(
            null,
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/comments",
            null,
            null
    );


    private XWPFRelation(String type, String rel, String defaultName, Class<? extends POIXMLDocumentPart> cls) {
        super(type, rel, defaultName, cls);

        if (cls != null && !_table.containsKey(rel)) _table.put(rel, this);
    }

    /**
     * Get POIXMLRelation by relation type
     *
     * @param rel relation type, for example,
     *            <code>http://schemas.openxmlformats.org/officeDocument/2006/relationships/image</code>
     * @return registered POIXMLRelation or null if not found
     */
    public static XWPFRelation getInstance(String rel) {
        return _table.get(rel);
    }

}