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
package org.apache.poi.poifs.crypt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.poi.POIDataSamples;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.Test;

/**
 *  @author Maxim Valyanskiy
 *  @author Gary King
 */
public class TestDecryptor {
    @Test
    public void passwordVerification() throws IOException, GeneralSecurityException {
        POIFSFileSystem fs = new POIFSFileSystem(POIDataSamples.getPOIFSInstance().openResourceAsStream("protect.xlsx"));

        EncryptionInfo info = new EncryptionInfo(fs);

        Decryptor d = Decryptor.getInstance(info);

        assertTrue(d.verifyPassword(Decryptor.DEFAULT_PASSWORD));
    }

    @Test
    public void decrypt() throws IOException, GeneralSecurityException {
        POIFSFileSystem fs = new POIFSFileSystem(POIDataSamples.getPOIFSInstance().openResourceAsStream("protect.xlsx"));

        EncryptionInfo info = new EncryptionInfo(fs);

        Decryptor d = Decryptor.getInstance(info);

        d.verifyPassword(Decryptor.DEFAULT_PASSWORD);

        zipOk(fs, d);
    }

    @Test
    public void agile() throws IOException, GeneralSecurityException {
        POIFSFileSystem fs = new POIFSFileSystem(POIDataSamples.getPOIFSInstance().openResourceAsStream("protected_agile.docx"));

        EncryptionInfo info = new EncryptionInfo(fs);

        assertTrue(info.getVersionMajor() == 4 && info.getVersionMinor() == 4);

        Decryptor d = Decryptor.getInstance(info);

        assertTrue(d.verifyPassword(Decryptor.DEFAULT_PASSWORD));

        zipOk(fs, d);
    }

    private void zipOk(POIFSFileSystem fs, Decryptor d) throws IOException, GeneralSecurityException {
        ZipInputStream zin = new ZipInputStream(d.getDataStream(fs));

        while (true) {
            ZipEntry entry = zin.getNextEntry();
            if (entry==null) {
                break;
            }

            while (zin.available()>0) {
                zin.skip(zin.available());
            }
        }
    }

    @Test
    public void dataLength() throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(POIDataSamples.getPOIFSInstance().openResourceAsStream("protected_agile.docx"));

        EncryptionInfo info = new EncryptionInfo(fs);

        Decryptor d = Decryptor.getInstance(info);

        d.verifyPassword(Decryptor.DEFAULT_PASSWORD);

        InputStream is = d.getDataStream(fs);

        long len = d.getLength();
        assertEquals(12810, len);

        byte[] buf = new byte[(int)len];

        is.read(buf);

        ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(buf));

        while (true) {
            ZipEntry entry = zin.getNextEntry();
            if (entry==null) {
                break;
            }

            while (zin.available()>0) {
                zin.skip(zin.available());
            }
        }
    }

}