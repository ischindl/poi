package org.apache.poi.xssf.usermodel.charts;

import java.util.List;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDPt;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;

public class XSSFPieChartData extends XSSFScatterChartData {


	private ChartDataSource<String> ls;
	private ChartDataSource<?> xs;
	private ChartDataSource<? extends Number> ys;
	private List<XSSFColor> colors;



	public XSSFPieChartData() {
	}

	@Override
	public void fillChart(Chart chart, ChartAxis... cat) {
		if (!(chart instanceof XSSFChart)) {
			throw new IllegalArgumentException("Chart must be instance of XSSFChart");
		}

		XSSFChart xssfChart = (XSSFChart) chart;
		CTPlotArea plotArea = xssfChart.getCTChart().getPlotArea();
		CTPieChart pieChart = plotArea.addNewPieChart();
		pieChart.addNewVaryColors().setVal(true);

		buildChart(pieChart);

		pieChart.addNewFirstSliceAng().setVal(0);
	}

	protected void buildChart(CTPieChart ctPieChart) {
		CTPieSer pieSer = ctPieChart.addNewSer();
		pieSer.addNewIdx().setVal(0);
		pieSer.addNewOrder().setVal(0);
		CTSRgbColor rgb = CTSRgbColor.Factory.newInstance();
		rgb.setVal(colors.get(0).getRgb());
		CTShapeProperties sppr = pieSer.addNewSpPr();
		sppr.addNewSolidFill().setSrgbClr(rgb);
		if(ls != null){
			CTSerTx lVal = pieSer.addNewTx();
			XSSFChartUtil.buildTxDataSource(lVal, ls);
		}

		// black outlines
		CTSRgbColor rgbBlack = CTSRgbColor.Factory.newInstance();
		rgbBlack.setVal(new byte[] { (byte) (0), (byte) (0), (byte) (0) });
		sppr.addNewLn().addNewSolidFill().setSrgbClr(rgbBlack);

		pieSer.addNewExplosion().setVal(0);
		for (int i = 0  ; i< xs.getPointCount(); i++) {
			rgb = CTSRgbColor.Factory.newInstance();
			rgb.setVal(colors.get(i).getRgb());
			CTDPt dpt = pieSer.addNewDPt();
			dpt.addNewIdx().setVal(i);
			dpt.addNewSpPr().addNewSolidFill().setSrgbClr(rgb);
		}

		CTAxDataSource xVal = pieSer.addNewCat();
		XSSFChartUtil.buildAxDataSource(xVal, xs);

		CTNumDataSource yVal = pieSer.addNewVal();
		XSSFChartUtil.buildNumDataSource(yVal, ys);
	}

	public void setData(ChartDataSource<String> ls,ChartDataSource<String> xs, ChartDataSource<Number> ys, List<XSSFColor> colors) {
		this.ls = ls;
		this.xs = xs;
		this.ys = ys;
		this.colors = colors;
	}
}
