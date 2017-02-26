package com.artycake.simpleweather.adapters;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.artycake.simpleweather.R;
import com.artycake.simpleweather.holders.CityHolder;
import com.artycake.simpleweather.models.City;
import com.artycake.simpleweather.utils.PreferencesService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artycake on 2/14/17.
 */

public class CityAdapter extends RecyclerView.Adapter<CityHolder> {
    private OnItemClickListener onItemClickListener;
    private SortedList<City> cities = new SortedList<City>(City.class, new SortedList.Callback<City>() {
        @Override
        public int compare(City o1, City o2) {
            return o1.getDisplayName().compareTo(o2.getDisplayName());
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(City oldItem, City newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(City item1, City item2) {
            return item1.equals(item2);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }
    });

    public CityAdapter() {
    }

    public void add(City model) {
        cities.add(model);
    }

    public void remove(City model) {
        cities.remove(model);
    }

    public void add(List<City> models) {
        cities.addAll(models);
    }

    public void remove(List<City> models) {
        cities.beginBatchedUpdates();
        for (City model : models) {
            cities.remove(model);
        }
        cities.endBatchedUpdates();
    }

    public void replaceAll(List<City> models) {
        cities.beginBatchedUpdates();
//        for (int i = cities.size() - 1; i >= 0; i--) {
//            final City model = cities.get(i);
//            if (!models.contains(model)) {
//                cities.remove(model);
//            }
//        }

        cities.clear();
        cities.addAll(models);
        cities.endBatchedUpdates();
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(final CityHolder holder, int position) {
        holder.updateUI(cities.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(cities.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    public interface OnItemClickListener {
        void onClick(City city);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }
}
