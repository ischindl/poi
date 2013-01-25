package org.apache.poi.xssf.usermodel.charts;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.ss.usermodel.charts.ScatterChartSerie;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx;
import org.openxmlformats.schemas.drawingml.x2006.chart.STDispBlanksAs;
import org.openxmlformats.schemas.drawingml.x2006.chart.STGrouping;
import org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;


public class XSSFLineChartData extends XSSFScatterChartData{
	  /**
     * List of all data series.
     */
    protected List<Serie> series;

    public XSSFLineChartData() {
        series = new ArrayList<Serie>();
    }

    /**
     * Package private ScatterChartSerie implementation.
     */
    static class Serie implements ScatterChartSerie {
        private int id;
        private int order;
        private ChartDataSource<?> xs;
        private ChartDataSource<?> ls;
        private ChartDataSource<? extends Number> ys;
        private XSSFColor color;
        private boolean smooth;

        protected Serie(int id, int order,
        				ChartDataSource<?> ls,
                        ChartDataSource<?> xs,
                        ChartDataSource<? extends Number> ys,
                        XSSFColor color,
                        boolean smooth) {
            super();
            this.id = id;
            this.order = order;
            this.xs = xs;
            this.ys = ys;
            this.ls = ls;
            this.color = color;
            this.smooth = smooth;
        }

        /**
         * Returns data source used for X axis values.
         * @return data source used for X axis values
         */
        public ChartDataSource<?> getXValues() {
            return xs;
        }

        /**
         * Returns data source used for Y axis values.
         * @return data source used for Y axis values
         */
        public ChartDataSource<? extends Number> getYValues() {
            return ys;
        }

        protected void addToChart(CTLineChart ctLineChart) {
            CTLineSer lineSer = ctLineChart.addNewSer();
            lineSer.addNewIdx().setVal(this.id);
            lineSer.addNewOrder().setVal(this.order);
            CTShapeProperties sppr = lineSer.addNewSpPr();
			CTLineProperties ln = sppr.addNewLn();
            if(color!= null){
            	CTSRgbColor rgb = CTSRgbColor.Factory.newInstance();
            	rgb.setVal(color.getRgb());
            	sppr.addNewSolidFill().setSrgbClr(rgb);
    			ln.addNewSolidFill().setSrgbClr(rgb);
            }

			ln.setW(28800);
            CTSerTx tx = lineSer.addNewTx();
            XSSFChartUtil.buildTxDataSource(tx, ls);

            CTAxDataSource xVal = lineSer.addNewCat();
            XSSFChartUtil.buildAxDataSource(xVal, xs);

            CTNumDataSource yVal = lineSer.addNewVal();
            XSSFChartUtil.buildNumDataSource(yVal, ys);
            
            lineSer.addNewSmooth().setVal(smooth);
        }
    }

    public ScatterChartSerie addSerie(ChartDataSource<?> ls, ChartDataSource<?> xs,
                                      ChartDataSource<? extends Number> ys, XSSFColor color, boolean smooth) {
        if (!ys.isNumeric()) {
            throw new IllegalArgumentException("Y axis data source must be numeric.");
        }
        int numOfSeries = series.size();
        Serie newSerie = new Serie(numOfSeries, numOfSeries, ls, xs, ys, color,smooth);
        series.add(newSerie);
        return newSerie;
    }

    public void fillChart(Chart chart, ChartAxis... axis) {
        if (!(chart instanceof XSSFChart)) {
            throw new IllegalArgumentException("Chart must be instance of XSSFChart");
        }

        XSSFChart xssfChart = (XSSFChart) chart;
        CTPlotArea plotArea = xssfChart.getCTChart().getPlotArea();
        CTLineChart lineChart = plotArea.addNewLineChart();
      //  lineChart.addNewVaryColors().setVal(true);
        lineChart.addNewGrouping().setVal(STGrouping.STANDARD);

        for (Serie s : series) {
            s.addToChart(lineChart);
        }

        for (ChartAxis ax : axis) {
        	lineChart.addNewAxId().setVal(ax.getId());
        }
        lineChart.addNewMarker().setVal(true);
        xssfChart.getCTChart().addNewDispBlanksAs().setVal(STDispBlanksAs.SPAN);
    }
}
