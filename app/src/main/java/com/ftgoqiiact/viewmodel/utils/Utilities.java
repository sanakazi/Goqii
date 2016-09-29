package com.ftgoqiiact.viewmodel.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ftgoqiiact.R;
import com.ftgoqiiact.model.constants.Apis;
import com.ftgoqiiact.model.constants.Constants;
import com.ftgoqiiact.model.pojos.ActivityDetailJson;
import com.ftgoqiiact.model.pojos.BookRequestJson;
import com.ftgoqiiact.model.pojos.CategoryJson;
import com.ftgoqiiact.model.pojos.GetOperatingCitiesResponse;
import com.ftgoqiiact.model.pojos.GymDataJson;
import com.ftgoqiiact.model.pojos.PostResponseJson;
import com.ftgoqiiact.model.singleton.MySingleton;
import com.ftgoqiiact.model.singleton.PreferencesManager;
import com.ftgoqiiact.model.utils.WebServices;
import com.ftgoqiiact.viewmodel.activities.CCAvenueWebview;
import com.ftgoqiiact.viewmodel.activities.LoginActivity;
import com.ftgoqiiact.viewmodel.activities.MainActivity;
import com.ftgoqiiact.viewmodel.activities.SplashActivity;
import com.ftgoqiiact.viewmodel.activities.TrainningSlidesActivity;
import com.ftgoqiiact.viewmodel.custom.CustomTimePickerDialog;
import com.ftgoqiiact.viewmodel.fragments.OTPDialogFragment;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;

/**
 * Created by Fiticket on 14/10/15.
 */
public class Utilities {
    private static final String TAG = Utilities.class.getSimpleName();
    private static int[] dayViewArray = {
            R.id.day1,
            R.id.day2,
            R.id.day3,
            R.id.day4,
            R.id.day5,
            R.id.day6,
            R.id.day7,
    };
    private static int[] dayNameViewArray = {
            R.id.day1Name,
            R.id.day2Name,
            R.id.day3Name,
            R.id.day4Name,
            R.id.day5Name,
            R.id.day6Name,
            R.id.day7Name,
    };

    public static boolean checkPermission(boolean showDialog, Activity activity, String permissionString, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, permissionString) != PackageManager.PERMISSION_GRANTED) {

            if (showDialog) {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{permissionString},
                        requestCode);
            }
            return false;
        } else {
            return true;
        }
    }

    public static String getFormattedTime(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        Date date = new Date(timeStamp);
        return date.toString();
    }

    public static void sortGymList(ArrayList<GymDataJson> gymList, Location mLastLocation) {

        if (!(gymList.isEmpty())) {
            for (GymDataJson gym : gymList) {
                if (!TextUtils.isEmpty(gym.getBranchLatitude()) && !TextUtils.isEmpty(gym.getBranchLongitude())) {
                    double gymLat = Double.parseDouble(gym.getBranchLatitude());
                    double gymLong = Double.parseDouble(gym.getBranchLongitude());
                    double distance = Utilities.distFrom(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                            gymLat, gymLong);
                    gym.setDistance(distance);
                } else {
                    gym.setDistance(Constants.VERY_LONG_DISTANCE);
                }
            }
            Collections.sort(gymList);
        }
    }

    public static void sortActivitiesList(ArrayList<ActivityDetailJson> activityList, Location mLastLocation) {

        if (activityList != null && !(activityList.isEmpty())) {
            for (ActivityDetailJson activity : activityList) {
                if (!TextUtils.isEmpty(activity.getGymLat()) && !TextUtils.isEmpty(activity.getGymLong())) {
                    double gymLat = Double.parseDouble(activity.getGymLat());
                    double gymLong = Double.parseDouble(activity.getGymLong());
                    double distance = Utilities.distFrom(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                            gymLat, gymLong);
                    activity.setDistance(distance);
                } else {
                    activity.setDistance(Constants.VERY_LONG_DISTANCE);
                }
            }
            Collections.sort(activityList);
        }
    }


    public static void addDayClickListeners(final AppCompatActivity activity, final LinearLayout daysLayout,
                                            final ArrayList<ArrayList<ActivityDetailJson.ScheduleDetail>> timeSlots,
                                            final LinearLayout timeSlotsLayout, final int startTime, final int endtime) {
        final Calendar c = Calendar.getInstance();
        for (int i = 0; i < dayViewArray.length; i++) {
            final int j = i;
            daysLayout.findViewById(dayViewArray[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    c.setTime(new Date());
                    c.add(Calendar.DATE, j);
                    int dayOFWeek = c.get(Calendar.DAY_OF_WEEK);
                    addTimeSlots(activity, dayOFWeek, timeSlots, timeSlotsLayout, c, startTime, endtime);
                    setSelectedDay(activity, j, daysLayout);
                }
            });
        }
    }

    public static void setSelectedDay(Activity activity, int index, LinearLayout daysLayout) {
        for (int i = 0; i < dayViewArray.length; i++) {
            TextView tV = (TextView) daysLayout.findViewById(dayViewArray[i]);
            TextView dayName = (TextView) daysLayout.findViewById(dayNameViewArray[i]);
            if (i == index) {
                tV.setBackgroundResource(R.drawable.rounded_button_fiticket);
                tV.setTextColor(activity.getResources().getColor(R.color.White));
                dayName.setTextColor(activity.getResources().getColor(R.color.explore_yellow));

            } else {
                tV.setBackgroundResource(R.drawable.explore_button_yellow);
                tV.setTextColor(activity.getResources().getColor(R.color.explore_yellow));
                dayName.setTextColor(activity.getResources().getColor(R.color.Gray));

            }
        }
    }

    public static void addTimeSlots(final AppCompatActivity parentActivity, int dayOfWeek,
                                    final ArrayList<ArrayList<ActivityDetailJson.ScheduleDetail>> timeSlotsArray,
                                    LinearLayout containerView, final Calendar date, int startTime, int endtime) {
        containerView.removeAllViews();
        if (timeSlotsArray != null && !timeSlotsArray.get(dayOfWeek - 1).isEmpty()) {
            for (final ActivityDetailJson.ScheduleDetail timeSlot : timeSlotsArray.get(dayOfWeek - 1)) {
                if (timeSlot.getIsFullDay().equalsIgnoreCase("false")) {
                    int hour = Integer.parseInt(timeSlot.getStartTime().substring(0, 2));
                    //Show only those timeslots within the time filter
                    if (hour >= startTime && hour < endtime) {
                        TextView textView = (TextView) parentActivity.getLayoutInflater().inflate(R.layout.timeslot_textview, null);
                        textView.setText(timeSlot.getStartTime().substring(0, 5));
                        containerView.addView(textView);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (PreferencesManager.getInstance(parentActivity).isLoggedIn()) {
                                    final BookRequestJson json = new BookRequestJson();
                                    json.setActivityId(Long.parseLong(timeSlot.getBookingId()));
                                    json.setCustomerId(PreferencesManager.getInstance(parentActivity).getUserId());
                                    final String enrollmentDate = convertDateFormat(date, timeSlot.getStartTime());
                                    json.setEnrollmentDate(enrollmentDate);
                                    json.setPartnerName(Constants.PARTNER_NAME);
                                    new AlertDialog.Builder(
                                            parentActivity)
                                            .setTitle("Booking confirmation")
                                            .setMessage("Are you sure you want to Book this activity for " + enrollmentDate + "?")

                                            .setPositiveButton("Book", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    bookActivity(parentActivity, json);

                                                }
                                            })
                                            .setNegativeButton(
                                                    android.R.string.no,
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // do nothing
                                                        }
                                                    })
                                            .setIcon(
                                                    android.R.drawable.ic_dialog_alert)
                                            .show();
                                } else {
                                    showLoginAlert(parentActivity);
                                }
                            }
                        });
                    }
                } else {
                    TextView textView = (TextView) parentActivity.getLayoutInflater().inflate(R.layout.timeslot_textview, null);
                    textView.setText(parentActivity.getResources().getString(R.string.allday_text));
                    containerView.addView(textView);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (PreferencesManager.getInstance(parentActivity).isLoggedIn()) {
                                final CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(parentActivity, new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                        final BookRequestJson json = new BookRequestJson();
                                        json.setActivityId(Long.parseLong(timeSlot.getBookingId()));
                                        json.setCustomerId(PreferencesManager.getInstance(parentActivity).getUserId());


                                        //Add 0 padding to the left to make hour and minute 2 digits
                                        String startTime = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
                                        String strampm = convert24to12(startTime);


                                        final String enrollmentDate = convertDateFormat(date, startTime);
                                        String substringdateformat = enrollmentDate.substring(0, enrollmentDate.length() - 5);

                                        json.setEnrollmentDate(enrollmentDate);
                                        json.setPartnerName(Constants.PARTNER_NAME);
                                        new AlertDialog.Builder(
                                                parentActivity)
                                                .setTitle("Booking confirmation")
                                                .setMessage("Are you sure you want to Book this activity for " + substringdateformat + strampm + "?")

                                                .setPositiveButton("Book", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        bookActivity(parentActivity, json);

                                                    }
                                                })
                                                .setNegativeButton(
                                                        android.R.string.no,
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // do nothing
                                                                dialog.dismiss();
                                                                dialog.cancel();

                                                            }
                                                        })
                                                .setIcon(
                                                        android.R.drawable.ic_dialog_alert).show();


                                    }


                                }, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), false);
                                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        timePickerDialog.dismiss();
                                    }
                                });
                                timePickerDialog.show();


                            } else {
                                showLoginAlert(parentActivity);
                            }
                        }
                    });
                    break;
                }
            }
        } else {
            TextView textView = (TextView) parentActivity.getLayoutInflater().inflate(R.layout.timeslot_textview, null);
            textView.setText(parentActivity.getResources().getString(R.string.no_Slots_text));
            containerView.addView(textView);
        }

    }

    private static String convertDateFormat(Calendar date, String startTime) {
        String outputDate;
        startTime = startTime.substring(0, 5);
        outputDate = "" + date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH) +
                " " + startTime;
        Log.d(TAG, "Formatted date: " + outputDate);
        return outputDate;
    }

    public static void bookActivity(final AppCompatActivity parentActivity, BookRequestJson json) {
        Log.e(TAG, "Booking request json: " + "{\"customerId\":" + json.getCustomerId() + ",\"activityId\":" + json.getActivityId() + ",\"enrollmentDate\":" + json.getEnrollmentDate() + ",\"partnerName\":" + json.getPartnerName() + "}");
        WebServices.triggerVolleyPostRequest(parentActivity, json, Apis.BOOKING_URL_2, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("JSON RESPONSE: ", "JSON RESPONSE: " + response);

                PostResponseJson result = new Gson().fromJson(response.toString(), PostResponseJson.class);
                handleBookActivityResponse(parentActivity, result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(parentActivity, error);
            }
        }, BookRequestJson.class);
    }

    private static void handleBookActivityResponse(final AppCompatActivity parentActivity, PostResponseJson result) {
        int customerId = PreferencesManager.getInstance(parentActivity).getUserId();
        if (result.getStatusCode() == 5) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    parentActivity);
            builder.setTitle("Warning").setMessage(result.getStatusMsg()).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                String loadUrl = Apis.CCAVENUE_URL + "" + PreferencesManager.getInstance(parentActivity).getUserId();
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(loadUrl));
                                parentActivity.startActivity(browserIntent);
                            } else {
                                Intent i = new Intent(parentActivity, CCAvenueWebview.class);
                                parentActivity.startActivity(i);
                            }
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (result.getStatusCode() == 15) {
            showOTPDialog(parentActivity, customerId, result.getCustomerContact());
        } else if (result.getStatusCode() == 14) {
            Toast.makeText(parentActivity, result.getStatusMsg(), Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(parentActivity, EditUserActivity.class);
//            parentActivity.startActivity(intent);
        } else if (result.getStatusCode() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    parentActivity);
            builder.setTitle("Request Received").setMessage(result.getStatusMsg()).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            BookingSuccessfulListener listener = (BookingSuccessfulListener) parentActivity;
                            listener.onBookingSucessful();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            showDialog(parentActivity, result.getStatusMsg(), "Warning");
        }
    }


    public static void showDialog(Activity activity, String msg, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                activity);
        builder.setTitle(title).setMessage(msg).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void setDayViews(LinearLayout daysLayout) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        for (int i = 0; i < 7; i++) {
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            String dayOfWeek = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);

            TextView datetextView = (TextView) daysLayout.findViewById(dayViewArray[i]);
            datetextView.setText("" + (dayOfMonth));
            TextView dayTextView = (TextView) daysLayout.findViewById(dayNameViewArray[i]);
            dayTextView.setText(dayOfWeek);
            c.add(Calendar.DATE, 1);
        }
    }

    public static void showOTPDialog(AppCompatActivity activity, int id, String phoneNumber) {
        android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("OTPdialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        OTPDialogFragment newFragment = OTPDialogFragment.newInstance(id, phoneNumber);
        newFragment.show(ft, "OTPdialog");
    }

    public static void showLoginAlert(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(activity.getResources().getString(R.string.loginRequired))
                .setMessage(activity.getResources().getString(R.string.login_warning_text))

                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //launch login activity
                        Intent i = new Intent(activity,
                                LoginActivity.class);
                        activity.startActivity(i);
                        activity.overridePendingTransition(R.anim.anim_up, R.anim.anim_down);

                    }
                })
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })

                .show();
    }


    /*public static void setCustomActionBar(final Activity activity, String title) {
        activity.getActionBar().setDisplayShowCustomEnabled(true);
        activity.getActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = LayoutInflater.from(activity);
        View v = inflator.inflate(R.layout.custom_actionbar, null);
        ((TextView) v.findViewById(R.id.title)).setText(title);
        activity.getActionBar().setCustomView(v);
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                activity.finish();
            }
        });
    }*/
    public static void setCustomActionBar(Activity activity, String title) {
        activity.getActionBar().setDisplayShowHomeEnabled(false);
        activity.getActionBar().setDisplayShowCustomEnabled(false);
        activity.getActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = LayoutInflater.from(activity);

        View v = inflator.inflate(R.layout.custom_actionbar, null);
        ((TextView) v.findViewById(R.id.title)).setText(title);
        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        activity.getActionBar().setCustomView(v, params);
        activity.getActionBar().setDisplayShowCustomEnabled(true);
        activity.getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
    }


    public static void showToast(Context context, String statusMsg) {
        Toast.makeText(context, statusMsg, Toast.LENGTH_SHORT).show();
    }


    public static void handleVolleyError(Context context, VolleyError error) {
        error.printStackTrace();
        Toast.makeText(context, "Network Error, Please try again later", Toast.LENGTH_SHORT).show();
    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371; //KM
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    public static void setActionBarIconVisibility(AppCompatActivity appCompatActivity, boolean visibility) {
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(visibility);
        appCompatActivity.getSupportActionBar().setHomeButtonEnabled(visibility);
    }

    public static void promptForCitySelection(final AppCompatActivity activity) {
        ArrayList<String> cityList = new ArrayList<>();
        if (MySingleton.getInstance(activity).getOperatingCities().isEmpty()) {
            String url = Apis.GET_OPERATING_CITIES;
            WebServices.triggerVolleyGetRequest(activity, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Operating cities get response:" + response);
                    GetOperatingCitiesResponse json = new Gson().fromJson(response, GetOperatingCitiesResponse.class);
                    ArrayList<GetOperatingCitiesResponse.CitiesJson> operatingCities = json.getData().getCities();
                    MySingleton.getInstance(activity).setOperatingCities(operatingCities);
                    ArrayList<String> cityList = new ArrayList<String>();
                    String currentCityId = PreferencesManager.getInstance(activity).getSelectedCityId();
                    int position = 0;
                    for (int i = 0; i < operatingCities.size(); i++) {
                        cityList.add(operatingCities.get(i).getCityName());
                        if (currentCityId.equals(operatingCities.get(i).getCityId()))
                            position = i;
                    }
                    showCitySelectionDialog(cityList.toArray(new String[cityList.size()]), activity, position);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleVolleyError(activity, error);
                    activity.finish();
                }
            });
        } else {
            ArrayList<GetOperatingCitiesResponse.CitiesJson> cityJsonList = MySingleton.getInstance(activity).getOperatingCities();
            String currentCityId = PreferencesManager.getInstance(activity).getSelectedCityId();
            int position = 0;
            for (int i = 0; i < cityJsonList.size(); i++) {
                cityList.add(cityJsonList.get(i).getCityName());
                if (currentCityId.equals(cityJsonList.get(i).getCityId()))
                    position = i;
            }
            showCitySelectionDialog(cityList.toArray(new String[cityList.size()]), activity, position);

        }


    }

    public static void showCitySelectionDialog(String[] cityList, final AppCompatActivity activity, int position) {

        new android.support.v7.app.AlertDialog.Builder(activity)
                .setTitle("Select City")
                .setCancelable(false)
                .setSingleChoiceItems(cityList, position, null)
                .setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((android.support.v7.app.AlertDialog) dialog).getListView().getCheckedItemPosition();
                        PreferencesManager.getInstance(activity).saveSelectedCityId(MySingleton.getInstance(activity).getOperatingCities().get(selectedPosition).getCityId());
                        PreferencesManager.getInstance(activity).saveSelectedCityJson(MySingleton.getInstance(activity).getOperatingCities().get(selectedPosition));
                        PreferencesManager.getInstance(activity).clearCityPreferences();
                        //Clear DB & Singleton data
                        Paper.book().delete(Constants.ALL_ACTIVITY_LIST);
                        Paper.book().delete(Constants.ALL_GYM_LIST);
                        MySingleton.getInstance(activity).setmAllAcivitiesList(new ArrayList<ActivityDetailJson>());
                        MySingleton.getInstance(activity).setmAllGymsList(new ArrayList<GymDataJson>());
                        MySingleton.getInstance(activity).setCategoryList(new ArrayList<CategoryJson>());
                        Intent intent;
                        if (PreferencesManager.getInstance(activity).isFirstRun()) {
                            intent = new Intent(activity, TrainningSlidesActivity.class);
                            PreferencesManager.getInstance(activity).saveIsFirstRun(false);
                        } else {
                            //clear stack and Launch main activity
                            intent = new Intent(activity, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        }
                        activity.startActivity(intent);
                        activity.overridePendingTransition(0, 0);
                    }
                })
                .show();
    }

    private static void restartApp(AppCompatActivity activity) {
        Intent mStartActivity = new Intent(activity, SplashActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(activity, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    public static String getCommaSeperatedCatIds(ArrayList<Integer> ids) {
        StringBuffer catIds = new StringBuffer();
        for (int i : ids) {
            catIds.append(i);
            catIds.append(",");
        }
        catIds.deleteCharAt(catIds.length() - 1);
        return catIds.toString();
    }

    public interface BookingSuccessfulListener {
        void onBookingSucessful();
    }

    private static String convert24to12(String s) {
        final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        Date dateObj = null;
        try {
            dateObj = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(dateObj);
        System.out.println(new SimpleDateFormat("hh:mm a").format(dateObj));
        return "" + new SimpleDateFormat("hh:mm a").format(dateObj);
    }


}
