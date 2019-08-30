package com.pram.puzzlegame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImagePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_page, container, false);

        ImageView imageView = rootView.findViewById(R.id.pageImage);

        int imagePosition = getArguments().getInt(ImageStore.IMAGE_INDEX_NAME);

        Glide.with(this).load(ImageStore.images[imagePosition]).into(imageView);

        return rootView;
    }
}
