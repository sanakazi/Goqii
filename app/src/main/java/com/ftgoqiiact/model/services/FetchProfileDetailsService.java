package com.ftgoqiiact.model.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ftgoqiiact.model.constants.Apis;
import com.ftgoqiiact.model.pojos.EditProfileResponse;
import com.ftgoqiiact.model.pojos.GetProfileDetailsJson;
import com.ftgoqiiact.model.singleton.PreferencesManager;
import com.ftgoqiiact.model.utils.WebServices;
import com.ftgoqiiact.viewmodel.utils.Utilities;
import com.google.gson.Gson;

/**
 * Created by Fiticket on 16/02/16.
 */
public class FetchProfileDetailsService extends IntentService {
    private static final String TAG = FetchProfileDetailsService.class.getSimpleName();
    public static final String PROFILE_DATA_DOWNLOADED ="PROFILE_DATA_DOWNLOADED" ;
    PreferencesManager sPref;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public FetchProfileDetailsService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sPref=PreferencesManager.getInstance(this);
        String url= Apis.GET_PROFILE_DETAIL_URL+sPref.getUserId();
        WebServices.triggerVolleyGetRequest(this, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Profile Details response: "+response );
                GetProfileDetailsJson json = new Gson().fromJson(response, GetProfileDetailsJson.class);
                GetProfileDetailsJson.GetProfileDetailsResultClass profileDataResponse= json.getGetProfileDetailsResult();
                if(profileDataResponse!=null) {
                    EditProfileResponse.ProfileData profile = new EditProfileResponse.ProfileData();
                    profile.setContactNo(profileDataResponse.getContactNo());
                    profile.setFirstName(profileDataResponse.getFirstName());
                    profile.setLastName(profileDataResponse.getLastName());
                    profile.setEmailId(profileDataResponse.getEmailId());
                    profile.setPackageEndDate(profileDataResponse.getPackageEndDate());
                    profile.setPackageName(profileDataResponse.getPackageName());
                    profile.setPincode(profileDataResponse.getPincode());
                    sPref.saveProfileData(profile);
//                    String url= Apis.GET_REFERRAL_CODE+sPref.getUserId();
//                    WebServices.triggerVolleyGetRequest(FetchProfileDetailsService.this, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Log.d(TAG,"Referral Details response: "+response );
//                            ReferralDetailsResult json = new Gson().fromJson(response, ReferralDetailsResult.class);
//                            ReferralDetailsResult.ReferralDetails referralDetails= json.getGetReferalDetailsResult();
//                            if(referralDetails!=null){
//                                sPref.saveReferralDetails(referralDetails);
//                                LocalBroadcastManager.getInstance(FetchProfileDetailsService.this).sendBroadcast(new Intent(FetchProfileDetailsService.PROFILE_DATA_DOWNLOADED));
//
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Utilities.handleVolleyError(FetchProfileDetailsService.this,error);
//                        }
//                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utilities.handleVolleyError(FetchProfileDetailsService.this,error);
            }
        });
    }
}
