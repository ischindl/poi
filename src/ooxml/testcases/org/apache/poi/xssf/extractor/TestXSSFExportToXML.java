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

package org.apache.poi.xssf.extractor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.xssf.XSSFTestDataSamples;
import org.apache.poi.xssf.model.MapInfo;
import org.apache.poi.xssf.usermodel.XSSFMap;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Roberto Manicardi
 */
public final class TestXSSFExportToXML extends TestCase {
	public void testExportToXML() throws Exception {

		XSSFWorkbook wb = XSSFTestDataSamples.openSampleWorkbook("CustomXMLMappings.xlsx");

		for (POIXMLDocumentPart p : wb.getRelations()) {

			if (!(p instanceof MapInfo)) {
				continue;
			}
			MapInfo mapInfo = (MapInfo) p;

			XSSFMap map = mapInfo.getXSSFMapById(1);
			XSSFExportToXml exporter = new XSSFExportToXml(map);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			exporter.exportToXML(os, true);
			String xml = os.toString("UTF-8");

			assertNotNull(xml);
			assertFalse(xml.equals(""));

			String docente = xml.split("<DOCENTE>")[1].split("</DOCENTE>")[0].trim();
			String nome = xml.split("<NOME>")[1].split("</NOME>")[0].trim();
			String tutor = xml.split("<TUTOR>")[1].split("</TUTOR>")[0].trim();
			String cdl = xml.split("<CDL>")[1].split("</CDL>")[0].trim();
			String durata = xml.split("<DURATA>")[1].split("</DURATA>")[0].trim();
			String argomento = xml.split("<ARGOMENTO>")[1].split("</ARGOMENTO>")[0].trim();
			String progetto = xml.split("<PROGETTO>")[1].split("</PROGETTO>")[0].trim();
			String crediti = xml.split("<CREDITI>")[1].split("</CREDITI>")[0].trim();

			assertEquals("ro", docente);
			assertEquals("ro", nome);
			assertEquals("ds", tutor);
			assertEquals("gs", cdl);
			assertEquals("g", durata);
			assertEquals("gvvv", argomento);
			assertEquals("aaaa", progetto);
			assertEquals("aa", crediti);
			
			parseXML(xml);
		}
	}

	public void testExportToXMLInverseOrder() throws Exception {

		XSSFWorkbook wb = XSSFTestDataSamples
				.openSampleWorkbook("CustomXmlMappings-inverse-order.xlsx");

		MapInfo mapInfo = null;

		for (POIXMLDocumentPart p : wb.getRelations()) {

			if (!(p instanceof MapInfo)) {
				continue;
			}
			mapInfo = (MapInfo) p;

			XSSFMap map = mapInfo.getXSSFMapById(1);
			XSSFExportToXml exporter = new XSSFExportToXml(map);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			exporter.exportToXML(os, true);
			String xml = os.toString("UTF-8");

			assertNotNull(xml);
			assertFalse(xml.equals(""));

			String docente = xml.split("<DOCENTE>")[1].split("</DOCENTE>")[0].trim();
			String nome = xml.split("<NOME>")[1].split("</NOME>")[0].trim();
			String tutor = xml.split("<TUTOR>")[1].split("</TUTOR>")[0].trim();
			String cdl = xml.split("<CDL>")[1].split("</CDL>")[0].trim();
			String durata = xml.split("<DURATA>")[1].split("</DURATA>")[0].trim();
			String argomento = xml.split("<ARGOMENTO>")[1].split("</ARGOMENTO>")[0].trim();
			String progetto = xml.split("<PROGETTO>")[1].split("</PROGETTO>")[0].trim();
			String crediti = xml.split("<CREDITI>")[1].split("</CREDITI>")[0].trim();

			assertEquals("aa", nome);
			assertEquals("aaaa", docente);
			assertEquals("gvvv", tutor);
			assertEquals("g", cdl);
			assertEquals("gs", durata);
			assertEquals("ds", argomento);
			assertEquals("ro", progetto);
			assertEquals("ro", crediti);
			
			parseXML(xml);
		}
	}

	public void testXPathOrdering() {

		XSSFWorkbook wb = XSSFTestDataSamples
				.openSampleWorkbook("CustomXmlMappings-inverse-order.xlsx");

		MapInfo mapInfo = null;

		for (POIXMLDocumentPart p : wb.getRelations()) {

			if (p instanceof MapInfo) {
				mapInfo = (MapInfo) p;

				XSSFMap map = mapInfo.getXSSFMapById(1);
				XSSFExportToXml exporter = new XSSFExportToXml(map);

				assertEquals(1, exporter.compare("/CORSO/DOCENTE", "/CORSO/NOME"));
				assertEquals(-1, exporter.compare("/CORSO/NOME", "/CORSO/DOCENTE"));
			}
		}
	}

	public void testMultiTable() throws Exception {

		XSSFWorkbook wb = XSSFTestDataSamples
				.openSampleWorkbook("CustomXMLMappings-complex-type.xlsx");

		for (POIXMLDocumentPart p : wb.getRelations()) {

			if (p instanceof MapInfo) {
				MapInfo mapInfo = (MapInfo) p;

				XSSFMap map = mapInfo.getXSSFMapById(2);

				assertNotNull(map);

				XSSFExportToXml exporter = new XSSFExportToXml(map);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				exporter.exportToXML(os, true);
				String xml = os.toString("UTF-8");

				assertNotNull(xml);

				String[] regexConditions = {
						"<MapInfo", "</MapInfo>",
						"<Schema ID=\"1\" Namespace=\"\" SchemaRef=\"\"/>",
						"<Schema ID=\"4\" Namespace=\"\" SchemaRef=\"\"/>",
						"DataBinding",
						"Map Append=\"false\" AutoFit=\"false\" ID=\"1\"",
						"Map Append=\"false\" AutoFit=\"false\" ID=\"5\"",
				};

				for (String condition : regexConditions) {
					Pattern pattern = Pattern.compile(condition);
					Matcher matcher = pattern.matcher(xml);
					assertTrue(matcher.find());
				}
			}
		}
	}

    public void test55850ComplexXmlExport() throws Exception {

        XSSFWorkbook wb = XSSFTestDataSamples
                .openSampleWorkbook("55850.xlsx");

        for (POIXMLDocumentPart p : wb.getRelations()) {

            if (!(p instanceof MapInfo)) {
                continue;
            }
            MapInfo mapInfo = (MapInfo) p;

            XSSFMap map = mapInfo.getXSSFMapById(2);

            assertNotNull("XSSFMap is null", map);

            XSSFExportToXml exporter = new XSSFExportToXml(map);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            exporter.exportToXML(os, true);
            String xmlData = os.toString("UTF-8");

            assertNotNull(xmlData);
            assertFalse(xmlData.equals(""));

            String a = xmlData.split("<A>")[1].split("</A>")[0].trim();
            String b = a.split("<B>")[1].split("</B>")[0].trim();
            String c = b.split("<C>")[1].split("</C>")[0].trim();
            String d = c.split("<D>")[1].split("</Dd>")[0].trim();
            String e = d.split("<E>")[1].split("</EA>")[0].trim();

            String euro = e.split("<EUR>")[1].split("</EUR>")[0].trim();
            String chf = e.split("<CHF>")[1].split("</CHF>")[0].trim();

            assertEquals("15", euro);
            assertEquals("19", chf);
            
            parseXML(xmlData);
        }
    }

   public void testFormulaCells_Bugzilla_55927() throws Exception {
       XSSFWorkbook wb = XSSFTestDataSamples.openSampleWorkbook("55927.xlsx");
       
       for (POIXMLDocumentPart p : wb.getRelations()) {
           
           if (!(p instanceof MapInfo)) {
               continue;
           }
           MapInfo mapInfo = (MapInfo) p;
           
           XSSFMap map = mapInfo.getXSSFMapById(1);
           
           assertNotNull("XSSFMap is null", map);
           
           XSSFExportToXml exporter = new XSSFExportToXml(map);
           ByteArrayOutputStream os = new ByteArrayOutputStream();
           exporter.exportToXML(os, true);
           String xmlData = os.toString("UTF-8");
           
           assertNotNull(xmlData);
           assertFalse(xmlData.equals(""));
           
           String date = xmlData.split("<DATE>")[1].split("</DATE>")[0].trim();
           assertEquals("2012-01-13", date);
           
           parseXML(xmlData);
       }
   }

   public void testFormulaCells_Bugzilla_55926() throws Exception {
       XSSFWorkbook wb = XSSFTestDataSamples.openSampleWorkbook("55926.xlsx");

       for (POIXMLDocumentPart p : wb.getRelations()) {

           if (!(p instanceof MapInfo)) {
               continue;
           }
           MapInfo mapInfo = (MapInfo) p;

           XSSFMap map = mapInfo.getXSSFMapById(1);

           assertNotNull("XSSFMap is null", map);

           XSSFExportToXml exporter = new XSSFExportToXml(map);
           ByteArrayOutputStream os = new ByteArrayOutputStream();
           exporter.exportToXML(os, true);
           String xmlData = os.toString("UTF-8");

           assertNotNull(xmlData);
           assertFalse(xmlData.equals(""));
           
           String a = xmlData.split("<A>")[1].split("</A>")[0].trim();
           String doubleValue = a.split("<DOUBLE>")[1].split("</DOUBLE>")[0].trim();
           String stringValue = a.split("<STRING>")[1].split("</STRING>")[0].trim();
           
           assertEquals("Hello World", stringValue);
           assertEquals("5.1", doubleValue);
           
           parseXML(xmlData);
       }
   }
   
   @Test
   public void testXmlExportIgnoresEmptyCells_Bugzilla_55924() throws Exception {

       XSSFWorkbook wb = XSSFTestDataSamples.openSampleWorkbook("55924.xlsx");

       for (POIXMLDocumentPart p : wb.getRelations()) {

           if (!(p instanceof MapInfo)) {
               continue;
           }
           MapInfo mapInfo = (MapInfo) p;

           XSSFMap map = mapInfo.getXSSFMapById(1);

           assertNotNull("XSSFMap is null", map);

           XSSFExportToXml exporter = new XSSFExportToXml(map);
           ByteArrayOutputStream os = new ByteArrayOutputStream();
           exporter.exportToXML(os, true);
           String xmlData = os.toString("UTF-8");

           assertNotNull(xmlData);
           assertFalse(xmlData.equals(""));

           String a = xmlData.split("<A>")[1].split("</A>")[0].trim();
           String euro = a.split("<EUR>")[1].split("</EUR>")[0].trim();
           assertEquals("1",euro);
           
           parseXML(xmlData);
       }
   }

   private void parseXML(String xmlData) throws IOException, SAXException, ParserConfigurationException {
       DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
       docBuilderFactory.setNamespaceAware(true);
       docBuilderFactory.setValidating(false);
       DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
       docBuilder.setEntityResolver(new DummyEntityResolver());

       docBuilder.parse(new ByteArrayInputStream(xmlData.getBytes("UTF-8")));
   }

   private static class DummyEntityResolver implements EntityResolver
   {
       @Override
       public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
       {
           return null;
       }
   }
}
