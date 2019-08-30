package com.pram.puzzlegame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    public ImagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new ImagePageFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ImageStore.IMAGE_INDEX_NAME, i);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return ImageStore.images.length;
    }
}
