package zjl.example.com.regularone.ui.mine.activity;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zjl.example.com.regularone.R;
import zjl.example.com.regularone.app.BaseUIActivity;
import zjl.example.com.regularone.bean.StatisticsData;
import zjl.example.com.regularone.ui.mine.contract.StatisticsContract;
import zjl.example.com.regularone.ui.mine.model.StatisticsModel;
import zjl.example.com.regularone.ui.mine.presenter.StatisticsPresenter;
import zjl.example.com.regularone.utils.DayAxisValueFormatter;

public class StatisticsActivity extends BaseUIActivity<StatisticsPresenter, StatisticsModel>  implements StatisticsContract.View{

    @BindView(R.id.chart)
    BarChart chart;
    @BindView(R.id.chart1)
    CombinedChart chart1;

    private int count = 12;

    @Override
    public int getLayoutId() {
        return R.layout.act_statistics;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        mPresenter.getStatisticsDataRequest();
        initBarChart();
        initCombinedChart();
    }

    private void initCombinedChart() {
        chart1.getDescription().setEnabled(false);
        chart1.setBackgroundColor(Color.WHITE);
        chart1.setDrawGridBackground(false);
        chart1.setDrawBarShadow(false);
        chart1.setHighlightFullBarEnabled(false);

        // draw bars behind lines
        chart1.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        Legend l = chart1.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis1 = chart1.getAxisRight();
        rightAxis1.setDrawGridLines(false);
        rightAxis1.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis1 = chart1.getAxisLeft();
        leftAxis1.setDrawGridLines(false);
        leftAxis1.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis1 = chart1.getXAxis();
        xAxis1.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis1.setAxisMinimum(0f);
        xAxis1.setGranularity(1f);
        xAxis1.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return months[(int) value % months.length];
            }
        });

        CombinedData data1 = new CombinedData();

        data1.setData(generateLineData());
        data1.setData(generateBarData());
//        data1.setData(generateBubbleData());
//        data1.setData(generateScatterData());
//        data1.setData(generateCandleData());
//        data1.setValueTypeface(tfLight);//设置字体的

        xAxis1.setAxisMaximum(data1.getXMax() + 0.25f);

        chart1.setData(data1);
        chart1.invalidate();
    }

    private void initBarChart() {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        //设置自定义值格式化轴值
        xAxis.setValueFormatter(new DayAxisValueFormatter(chart));

//        YAxis left = chart.getAxisLeft();
//        left.setDrawLabels(false); // no axis labels
//        left.setDrawAxisLine(false); // no axis line
//        left.setDrawGridLines(false); // no grid lines
//        left.setDrawZeroLine(true); // draw a zero line
        chart.getAxisRight().setEnabled(false); // no right axis
    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < count; index++)
            entries.add(new Entry(index + 0.5f, getRandom(15, 5)));

        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(240, 238, 70));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData() {

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();

        for (int index = 0; index < count; index++) {
            entries1.add(new BarEntry(0, getRandom(25, 25)));

            // stacked
            entries2.add(new BarEntry(0, new float[]{getRandom(13, 12), getRandom(13, 12)}));
        }

        BarDataSet set1 = new BarDataSet(entries1, "Bar 1");
        set1.setColor(Color.rgb(60, 220, 78));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet set2 = new BarDataSet(entries2, "");
        set2.setStackLabels(new String[]{"Stack 1", "Stack 2"});
        set2.setColors(Color.rgb(61, 165, 255), Color.rgb(23, 197, 255));
        set2.setValueTextColor(Color.rgb(61, 165, 255));
        set2.setValueTextSize(10f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1, set2);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;
    }

    private final String[] months = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    private float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }

    @Override
    public void returnStatisticsData(StatisticsData statisticsData) {
        ToastUtil.showShort("统计成功");
        List<BarEntry> entries = new ArrayList<>();


        for (int i = 1; i < 13; i++) {
//            entries.add(new BarEntry(i, statisticsData.getData().get(i).getCount()));
            float val = (float) (Math.random() * (100 / 2f)) + 50;
            entries.add(new BarEntry(i, val));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(R.color.red);
        dataSet.setValueTextColor(R.color.red);
        BarData barData = new BarData(dataSet);
//        barData.setValueFormatter();//格式化数据值
        chart.setData(barData);
        chart.invalidate();

        LogUtils.loge(statisticsData.getData().get(0).getType()+statisticsData.getData().get(0).getTypeId()+"===="+statisticsData.getData().get(0).getCount());
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
        ToastUtil.showShort(msg);
    }
}
