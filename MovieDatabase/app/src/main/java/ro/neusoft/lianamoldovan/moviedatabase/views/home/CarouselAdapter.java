package ro.neusoft.lianamoldovan.moviedatabase.views.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ro.neusoft.lianamoldovan.moviedatabase.R;

/**
 * Created by liana.moldovan on 13.04.2018.
 */

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
    final private int[] items;

    CarouselAdapter(int[] items) {
        this.items = items;
    }

    @Override
    public CarouselViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_carousel, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarouselViewHolder holder, int position) {
        holder.image.setImageResource(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    class CarouselViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;

        CarouselViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.trending_image_id);
        }
    }
}