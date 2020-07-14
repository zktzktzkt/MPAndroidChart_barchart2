package com.jgw.mpandroidchartdemo;

import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    private BarChart mChart;
    private Typeface mTfRegular;
    private Typeface mTfLight;
    private String[] mLabels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        mChart =(BarChart)findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);


        mChart.getDescription().setEnabled(false); //设置图标的描述
        mChart.setPinchZoom(false);
        mChart.setDrawBarShadow(false); //
        mChart.setDrawGridBackground(false);
        mChart.setExtraBottomOffset(15f); //整体距底边15f

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        //标记的view
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart


        //设置数据
        setData();

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); //companyA , companyB 的位置
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);//companyA , companyB 的位置
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setTypeface(mTfLight);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(mTfLight);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(true);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(false);

    }


    private void setData() {
        float groupSpace = 0.25f;               //    0.25
        float barSpace = 0.05f; // x4 DataSet  0.05   0.15
        float barWidth = 0.2f; // x4 DataSet   0.2    0.6
        //***这个必须满足否则绘出的柱形图很不规则***
       // (0.05+0.2)*3 + 0.25 = 1

        int mSeekBarX = 6; //
        int mSeekBarY = 100;

        /* 可以灵活去添加
        List<String> list = new ArrayList<>();
        for (int x = 0 ;x<mSeekBarX;x++){
            list.add("A"+x);
        }
        mChart.getXAxis().setValueFormatter(new StringXAxisFormatter(list));
        */

        //这里为了方便写死了
        mLabels = new String[] {"甲骨文","海康","大华", "飞翔", "阿里巴巴","网易"};
        mChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(mLabels));
        mChart.getXAxis().setLabelCount(mSeekBarX);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        // yVals4 第四个柱状图不用 可以不管
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();

        float randomMultiplier = mSeekBarY * 100f;

        for (int i = 0; i < mSeekBarX; i++) { //每个公司每年的数据
            yVals1.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
            yVals2.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
            yVals3.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
            yVals4.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
        }

        BarDataSet set1, set2, set3, set4;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) mChart.getData().getDataSetByIndex(2);
            set4 = (BarDataSet) mChart.getData().getDataSetByIndex(3);
            set1.setValues(yVals1); //A公司的所有年份数据
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            set4.setValues(yVals4);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(yVals1, "商品A销量");
            set1.setColor(Color.rgb(0, 153, 255));
            set2 = new BarDataSet(yVals2, "商品B销量");
            set2.setColor(Color.rgb(255, 153, 102));
            set3 = new BarDataSet(yVals3, "商品C销量");
            set3.setColor(Color.rgb(51, 153, 153));
            set4 = new BarDataSet(yVals4, "商品D销量");
            set4.setColor(Color.rgb(255, 102, 0));

            //将数据放入 BarData
            BarData data = new BarData(set1, set2, set3); //,set4
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTypeface(mTfLight);
            mChart.setData(data);
        }

        //设置柱状图的宽度及字体大小等等
        mChart.getBarData().setBarWidth(barWidth);
        mChart.getBarData().setValueTextSize(7f);
        // restrict the x-axis range
        mChart.getXAxis().setAxisMinimum(0);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mChart.getXAxis().setAxisMaximum(0 + mChart.getBarData().getGroupWidth(groupSpace, barSpace) * mSeekBarX);
        mChart.groupBars(0, groupSpace, barSpace);
        mChart.invalidate();

    }

    protected RectF mOnValueSelectedRectF = new RectF();
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);
        BarEntry barEntry = (BarEntry) e;
        float y = barEntry.getY();
        int x = (int)barEntry.getX();

        Log.i("X",mLabels[x]+"----" + "Y"+String.valueOf(y));

//        Log.i("bounds", bounds.toString());
//        Log.i("position", position.toString());
//
//        Log.i("x-index",
//                "low: " + mChart.getLowestVisibleX() + ", high: "
//                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);

    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }
}
