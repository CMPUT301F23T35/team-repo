package com.example.team_repo;

import android.graphics.Bitmap;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<ImageView> imageViewList;

    ViewPagerAdapter(List<ImageView> imageViewList) {
        this.imageViewList = imageViewList;
    }


    @Override
    public int getCount() {
        return imageViewList == null ? 0 : imageViewList.size();
    }

    /**
     * check whether the view is associated with the object
     * @param view Page View to check for association with <code>object</code>
     * @param object Object to check for association with <code>view</code>
     * @return true if associate
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageview = imageViewList.get(position);
        container.addView(imageview);
        return imageview;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);

    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        int index = imageViewList.indexOf(object);
        if (index == -1) {
            return POSITION_NONE;
        } else {
            return index;
        }
    }
}
