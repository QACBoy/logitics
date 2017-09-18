package com.example.scandemo5.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.JMap;
import com.example.scandemo5.Utils.RMap;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import org.feezu.liuli.timeselector.TimeSelector;

public class ScanRActivity extends AppCompatActivity {

    public static TableLayout tabltLayout;
    private JMap<String, String> map;
    private JMap<String, String> Scanmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_r);
        int primary = getResources().getColor(R.color.colorPrimary);
        int secondary = getResources().getColor(R.color.colorPrimaryDark);
        SlidrInterface slidrInterface = Slidr.attach(this, primary, secondary);
        tabltLayout = (TableLayout) findViewById(R.id.table);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Global.scanRActivity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUI();
        Global.ifCloseInput(ScanRActivity.this);
    }

    @Override
    protected void onStop() {
        addDataToUpLoadList();
        super.onStop();
    }

    private void addChildView(String key, String value, boolean Enable){
        TableRow row = (TableRow) LayoutInflater.from(ScanRActivity.this).inflate(R.layout.handle_item, null);

        TextView tKey = (TextView) row.findViewById(R.id.handle_item_key);
        EditText tValue = (EditText) row.findViewById(R.id.handle_item_value);
        tValue.setEnabled(Enable);

//              Log.d("11010", "showUI: " + key + "------" + value);
        tKey.setText(key);
        tValue.setText(value);

        tabltLayout.addView(row);
    }

    private void addChildTimeView(String key,String value){
        TableRow row = (TableRow) LayoutInflater.from(ScanRActivity.this).inflate(R.layout.handle_item, null);

        TextView tKey = (TextView) row.findViewById(R.id.handle_item_key);
        EditText tValue = (EditText) row.findViewById(R.id.handle_item_value);
        tValue.setFocusableInTouchMode(false);

        tKey.setText(key);
        tValue.setText(value);

        tValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                Calendar now = Calendar.getInstance();
//                DatePickerDialog dpd = DatePickerDialog.newInstance(
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
//                                Log.d("12111", "onDateSet() called with: view = [" + view + "], year = [" + year + "], monthOfYear = [" + monthOfYear + "], dayOfMonth = [" + dayOfMonth + "], yearEnd = [" + yearEnd + "], monthOfYearEnd = [" + monthOfYearEnd + "], dayOfMonthEnd = [" + dayOfMonthEnd + "]");
//                                ((EditText) tabltLayout.getChildAt(15).findViewById(R.id.handle_item_value)).setText(year+"-"+(monthOfYear + 1)+"-"+dayOfMonth);
//                                ((EditText) tabltLayout.getChildAt(16).findViewById(R.id.handle_item_value)).setText(yearEnd+"-"+(monthOfYearEnd + 1)+"-"+dayOfMonthEnd);
//                            }
//                        },
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//                );
//                dpd.show(getFragmentManager(), "Datepickerdialog");
                TimeSelector timeSelector = new TimeSelector(ScanRActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        ((EditText)v).setText(time.substring(0,10));
                    }
                }, "2015-01-01 00:00", "2030-12-31 24:00");
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.setIsLoop(true);
                timeSelector.show();
            }
        });

        tabltLayout.addView(row);
    }

    private void addDataToUpLoadList(){
        if(tabltLayout.getChildCount() > 0) {
            String barcode = ((EditText) tabltLayout.getChildAt(3).findViewById(R.id.handle_item_value)).getText().toString();
            String goods_no = ((EditText) tabltLayout.getChildAt(1).findViewById(R.id.handle_item_value)).getText().toString();
            String goods_name = ((EditText) tabltLayout.getChildAt(2).findViewById(R.id.handle_item_value)).getText().toString();
            String MFG = ((EditText) tabltLayout.getChildAt(16).findViewById(R.id.handle_item_value)).getText().toString();
            String EXP = ((EditText) tabltLayout.getChildAt(17).findViewById(R.id.handle_item_value)).getText().toString();
            String location_no = ((EditText) tabltLayout.getChildAt(15).findViewById(R.id.handle_item_value)).getText().toString();
            String LOT = ((EditText) tabltLayout.getChildAt(14).findViewById(R.id.handle_item_value)).getText().toString();
            String quantity = ((EditText) tabltLayout.getChildAt(13).findViewById(R.id.handle_item_value)).getText().toString();
//            Global.upLoad.add(new UpLoad.ScanData(barcode, goods_no, goods_name, MFG, EXP, LOT, quantity));
            int pos = getIntent().getIntExtra("postion",-1);
            if( -1 == pos) {
                MainActivity.mainActivity.addScanDataEnd(new UpLoad.ScanData(barcode, goods_no, goods_name, MFG, EXP, LOT,location_no, quantity));
            }else {
                MainActivity.mainActivity.addScanData(pos,new UpLoad.ScanData(barcode, goods_no, goods_name, MFG, EXP, LOT,location_no, quantity));
            }
        }
    }

    public void showUI() {

        addDataToUpLoadList();

        map = Global.ShowUI_map;
        Scanmap = Global.ShowUI_Scanmap;
        tabltLayout.removeAllViewsInLayout();
        tabltLayout.setBackgroundColor(Color.BLACK);
        addChildView(RMap.getrMap().get("procure_no"),Global.PROCURENO,false);
        for (int i = 0; i < map.size(); i++) {
            String key = RMap.getrMap().get(map.get(i));
            String value = map.get(map.get(i));

            addChildView(key,value,false);
        }
        addChildView(RMap.getrMap().get("quantity"),Scanmap.get("quantity"),true);
        addChildView(RMap.getrMap().get("LOT"),Scanmap.get("LOT"),true);
        addChildView(RMap.getrMap().get("location_no"),Scanmap.get("location_no"),true);
        //生产日期
        addChildTimeView(RMap.getrMap().get("MFG"),Scanmap.get("MFG"));
        //到期日期

        addChildTimeView(RMap.getrMap().get("EXP"),Scanmap.get("EXP"));
    }

}