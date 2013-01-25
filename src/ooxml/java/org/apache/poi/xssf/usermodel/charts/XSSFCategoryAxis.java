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

package org.apache.poi.xssf.usermodel.charts;

import org.apache.poi.ss.usermodel.charts.AxisCrosses;
import org.apache.poi.ss.usermodel.charts.AxisOrientation;
import org.apache.poi.ss.usermodel.charts.AxisPosition;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.util.Beta;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxPos;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTScaling;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark;
import org.openxmlformats.schemas.drawingml.x2006.chart.STTickLblPos;
import org.openxmlformats.schemas.drawingml.x2006.chart.STTickMark;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;

/**
 * Value axis type.
 *
 * @author Roman Kashitsyn
 */
@Beta
public class XSSFCategoryAxis extends XSSFChartAxis {

	private CTCatAx ctCatAx;

	public XSSFCategoryAxis(XSSFChart chart, long id, AxisPosition pos) {
		super(chart);
		createAxis(id, pos);
	}

	public XSSFCategoryAxis(XSSFChart chart, CTCatAx ctCatAx) {
		super(chart);
		this.ctCatAx = ctCatAx;
	}

	public long getId() {
		return ctCatAx.getAxId().getVal();
	}

	@Override
	protected CTAxPos getCTAxPos() {
		return ctCatAx.getAxPos();
	}

	@Override
	protected CTNumFmt getCTNumFmt() {
		if (ctCatAx.isSetNumFmt()) {
			return ctCatAx.getNumFmt();
		}
		return ctCatAx.addNewNumFmt();
	}

	@Override
	protected CTScaling getCTScaling() {
		return ctCatAx.getScaling();
	}

	@Override
	protected CTCrosses getCTCrosses() {
		return ctCatAx.getCrosses();
	}

	@Override
	protected CTBoolean getDelete() {
		return ctCatAx.getDelete();
	}

	@Override
	protected CTTickMark getMajorCTTickMark() {
		return ctCatAx.getMajorTickMark();
	}

	@Override
	protected CTTickMark getMinorCTTickMark() {
		return ctCatAx.getMinorTickMark();
	}

	public void crossAxis(ChartAxis axis) {
		ctCatAx.getCrossAx().setVal(axis.getId());
	}

	public void addNewMajorGridlines(){
		CTSRgbColor rgb = CTSRgbColor.Factory.newInstance();
		rgb.setVal(new byte[]{(byte)0xb3,(byte)0xb3,(byte)0xb3});
		ctCatAx.addNewMajorGridlines().addNewSpPr().addNewLn().addNewSolidFill().setSrgbClr(rgb);
	}

	private void createAxis(long id, AxisPosition pos) {
		ctCatAx = chart.getCTChart().getPlotArea().addNewCatAx();
		ctCatAx.addNewAxId().setVal(id);
		ctCatAx.addNewAxPos();
		ctCatAx.addNewScaling();
		ctCatAx.addNewCrosses();
		ctCatAx.addNewCrossAx();
		ctCatAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);
		ctCatAx.addNewMajorTickMark().setVal(STTickMark.OUT);
		ctCatAx.addNewMinorTickMark().setVal(STTickMark.OUT);
		ctCatAx.addNewDelete().setVal(false);
		ctCatAx.addNewNumFmt().setFormatCode("dd.mm.yyyy");

		CTSRgbColor rgb = CTSRgbColor.Factory.newInstance();
		rgb.setVal(new byte[]{(byte)0xb3,(byte)0xb3,(byte)0xb3});
		ctCatAx.addNewSpPr().addNewLn().addNewSolidFill().setSrgbClr(rgb);
		setPosition(pos);
		setOrientation(AxisOrientation.MIN_MAX);
		setCrosses(AxisCrosses.AUTO_ZERO);
	}
}
