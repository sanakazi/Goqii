package com.ftgoqiiact.viewmodel.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ftgoqiiact.R;
import com.ftgoqiiact.viewmodel.activities.ImageDisplayActivity;
import com.ftgoqiiact.model.pojos.FeedsJson;
import com.ftgoqiiact.model.singleton.MySingleton;
import com.ftgoqiiact.viewmodel.utils.Utilities;

import java.util.ArrayList;

/**
 * Created by Fiticket on 06/11/15.
 */
public class FeedAdapter extends BaseAdapter{
    ArrayList<FeedsJson.FeedDataJson.FeedData> feedList;
    Context context;
    private ImageLoader mImageLoader;

    public FeedAdapter(Context context, ArrayList<FeedsJson.FeedDataJson.FeedData> feedList){
        this.feedList=feedList;
        this.context=context;
    }
    @Override
    public int getCount() {
        return feedList.size();
    }

    @Override
    public Object getItem(int position) {
        return feedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final FeedsJson.FeedDataJson.FeedData feedData= feedList.get(position);
        mImageLoader = MySingleton.getInstance(context).getImageLoader();

        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= (View)inflater.inflate(R.layout.feed_listitem_layout, null);
            holder= new ViewHolder();
            holder.tweetDpImageView= (NetworkImageView)convertView.findViewById(R.id.twitterDpImageview);
            holder.tweetDpImageView.setDefaultImageResId(R.drawable.ic_default);

            holder.tweetTitleTextView= (TextView)convertView.findViewById(R.id.tweetTitleTextView);
            holder.tweetTimeTextView= (TextView)convertView.findViewById(R.id.tweetTimeTextView);
            holder.tweetDetailsTextView= (TextView)convertView.findViewById(R.id.twitterDetailText);
            holder.tweetPhotoImageView=(NetworkImageView)convertView.findViewById(R.id.twitterImageview);
            holder.tweetPhotoImageView.setDefaultImageResId(R.drawable.ic_default);

            holder.blogTitleTextView=(TextView)convertView.findViewById(R.id.blog_title);
            holder.blogTimeTextView=(TextView)convertView.findViewById(R.id.blog_time);
            holder.blogPhotoImageView=(NetworkImageView)convertView.findViewById(R.id.blog_image);
            holder.blogPhotoImageView.setDefaultImageResId(R.drawable.ic_default);

            convertView.setTag(holder);
            }else {
            holder=(ViewHolder)convertView.getTag();
        }
        if(feedList.get(position).getType().equalsIgnoreCase("tweet")){
            convertView.findViewById(R.id.tweetLayout).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.blogLayout).setVisibility(View.GONE);
            holder.tweetTitleTextView.setText(feedData.getUserName());
            holder.tweetTimeTextView.setText(Utilities.getFormattedTime(feedData.getTimeStamp()));
            holder.tweetDetailsTextView.setText(feedData.getText());
            holder.tweetDpImageView.setImageUrl(feedData.getProfileImageURL(), mImageLoader);
            if(feedData.getMedia()!=null && feedData.getMedia().size()!=0) {
                holder.tweetPhotoImageView.setVisibility(View.VISIBLE);
                holder.tweetPhotoImageView.setImageUrl(feedData.getMedia().get(0).getMediaURL(), mImageLoader);
                holder.tweetPhotoImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Onclick open the image in a full screen view
                        Intent intent= new Intent(context, ImageDisplayActivity.class);
                        intent.putExtra(ImageDisplayActivity.IMAGEURL,feedData.getMedia().get(0).getMediaURL());
                        context.startActivity(intent);
                    }
                });
            }else {
                holder.tweetPhotoImageView.setVisibility(View.GONE);
            }


        }else{
            convertView.findViewById(R.id.tweetLayout).setVisibility(View.GONE);
            convertView.findViewById(R.id.blogLayout).setVisibility(View.VISIBLE);
            holder.blogTitleTextView.setText((feedData.getTitle()));
            holder.blogTimeTextView.setText(Utilities.getFormattedTime(feedData.getTimeStamp()));
            if(!TextUtils.isEmpty(feedData.getFeaturedImageURL())) {
                holder.blogPhotoImageView.setImageUrl(feedData.getFeaturedImageLink().get(0), mImageLoader);
            }
        }

        return convertView;
    }

    static class ViewHolder{
        TextView tweetTitleTextView, tweetTimeTextView,tweetDetailsTextView,blogTitleTextView,blogTimeTextView;
        NetworkImageView tweetDpImageView,tweetPhotoImageView,blogPhotoImageView;
    }
}
