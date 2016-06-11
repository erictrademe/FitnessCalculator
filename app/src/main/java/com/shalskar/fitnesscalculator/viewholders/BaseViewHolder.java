package com.shalskar.fitnesscalculator.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.utils.ImageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vincent on 10/06/2016.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    /**
     * We could make this a variable to adjust the performance of the app on older phones
     */
    private final static float IMAGE_QUALITY_REDUCTION = 1f;

    @BindView(R.id.image)
    ImageView imageView;

    protected View baseView;

    public BaseViewHolder(View itemView) {
        super(itemView);

    }

    protected void loadImage(int widthResource, int heightResource, int imageResource) {
        int width = (int) (baseView.getResources().getDimension(widthResource)  * IMAGE_QUALITY_REDUCTION);
        int height = (int) (baseView.getResources().getDimension(heightResource)  * IMAGE_QUALITY_REDUCTION);
        imageView.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(baseView.getResources(), imageResource, width, height));
    }
}
