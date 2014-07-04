package com.powerpoint45.lucidbrowser;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

public class SuggestionsArrayAdapter<String> extends ArrayAdapter<String> {

	Filter filter = new noFilter();
	public List<String> items;
	
	public SuggestionsArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
		super(context, textViewResourceId,objects);
		items = objects;
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public Filter getFilter() {
        return filter;
    }
	
	
	private class noFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence arg0) {
            FilterResults result = new FilterResults();
                result.values = items;
                result.count = items.size(); 
            return result;
        }

        @Override
        protected void publishResults(CharSequence arg0, FilterResults arg1) {
            notifyDataSetChanged();
        }
    }

}
