package ca.soundit.soundit.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.soundit.soundit.R;
import ca.soundit.soundit.back.data.Location;

public class CheckInListArrayAdapter extends ArrayAdapter<Location> {
	
	private Context mContext;
	private int mLayoutResourceID;
	List<Location> mLocationList;

	public CheckInListArrayAdapter(Context context, int resource, int textViewResourceId,
			List<Location> objects) {
		super(context, resource, textViewResourceId, objects);
		
		this.mLayoutResourceID = resource;
        this.mContext = context;
        mLocationList = objects;
        
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LocationHolder holder = null;
        
        if(row == null)
        {
        	LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceID, parent, false);
            
            holder = new LocationHolder();
            holder.locationName = (TextView)row.findViewById(R.id.location_name);
            holder.locationLocation = (TextView)row.findViewById(R.id.location_location);
            
            row.setTag(holder);
        }
        else
        {
            holder = (LocationHolder)row.getTag();
        }
        
        Location location = mLocationList.get(position);
        
        holder.locationName.setText(location.getName());
        holder.locationLocation.setText(location.getLocation());
        
        return row;
    }
    
    static class LocationHolder
    {
        TextView locationName;
        TextView locationLocation;
    }
}

