package com.example.travelapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import de.hdodenhof.circleimageview.CircleImageView;

class FetchImage extends Thread
{
    Context mContext;
    Handler mHandler;
    CircleImageView mDrawerImageView;
    String mURL;
    Bitmap mBitmap;

    FetchImage(String URL , CircleImageView drawerImageView , Context context){
        this.mURL =URL;
        this.mHandler = new Handler();
        this.mDrawerImageView = drawerImageView;
        this.mContext = context;
    }

    @Override
    public void run() {

        InputStream inputStream;
        try {
            inputStream = new URL(mURL).openStream();
            mBitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mHandler.post(() -> {
            if(mBitmap !=null) {
                mDrawerImageView.setImageBitmap(mBitmap);
                SpUtil.writeStringPref(mContext, SpUtil.USER_PHOTO, mURL);
            }
        });
    }
}