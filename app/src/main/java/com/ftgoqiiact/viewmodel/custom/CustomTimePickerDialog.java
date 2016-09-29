package com.ftgoqiiact.viewmodel.custom;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class CustomTimePickerDialog extends TimePickerDialog {

    private final static int TIME_PICKER_INTERVAL = 30;
    private TimePicker timePicker;
    private final OnTimeSetListener callback;
    public static int scrollstate = 0;
    private boolean mEventIgnored = false;
    public static String[] scrollstatestr = null;

    public CustomTimePickerDialog(Context context, OnTimeSetListener callBack,
                                  int hourOfDay, int minute, boolean is24HourView) {
        super(context, TimePickerDialog.THEME_HOLO_LIGHT, callBack, hourOfDay, minute / TIME_PICKER_INTERVAL,
                is24HourView);
        this.callback = callBack;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == -2) {


        } else {
            if (callback != null && timePicker != null) {
                timePicker.clearFocus();
                callback.onTimeSet(timePicker, timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
            }
        }
    }


    @Override
    protected void onStop() {
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            this.timePicker = (TimePicker) findViewById(timePickerField
                    .getInt(null));
            Field field = classForid.getField("minute");
//            Field field1 = classForid.getField("hour");

//            final NumberPicker hourSpinner = (NumberPicker) timePicker
//                    .findViewById(field1.getInt(null));

            final NumberPicker mMinuteSpinner = (NumberPicker) timePicker
                    .findViewById(field.getInt(null));
            mMinuteSpinner.setMinValue(0);
            mMinuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<String>();

            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }

            mMinuteSpinner.setDisplayedValues(displayedValues
                    .toArray(new String[0]));

//            hourSpinner.setMinValue(0);
//            hourSpinner.setMaxValue((12));
//            hourSpinner.setDisplayedValues(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"});
//            mMinuteSpinner.setDisplayedValues(new String[]{"00","30"});

//            mMinuteSpinner.setOnScrollListener(new NumberPicker.OnScrollListener() {
//                @Override
//                public void onScrollStateChange(NumberPicker view, int scrollState) {
//
////                    Log.e("mincroll", "mincroll " + scrollState);
////                    if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
////                        hourSpinner.setWrapSelectorWheel(false);
////                    }
////                    if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
////                        hourSpinner.setEnabled(true);
////                    }
//                    hourSpinner.setDisplayedValues(scrollstatestr);
//
//                }
//            });

//
//            hourSpinner.setOnScrollListener(new NumberPicker.OnScrollListener() {
//                @Override
//                public void onScrollStateChange(NumberPicker view, int scrollState) {
//                    Log.e("hourscroll", "hourscroll " + scrollState);
//                    scrollstatestr = view.getDisplayedValues();
//                    Log.e("hours", "hours " + scrollstate);
//                    hourSpinner.setEnabled(true);
//
//
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


//    @Override
//    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//        super.onTimeChanged(view, hourOfDay, minute);
//        Log.e("ontime changed","ontime changed" +hourOfDay+" "+minute);
//
//        }

    }
