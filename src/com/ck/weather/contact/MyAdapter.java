package com.ck.weather.contact;

import java.util.List;
import com.example.lohaningweather.R;

import android.content.Context;
import android.graphics.YuvImage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private List<MyData> myList;
	private Context context;
    private RelativeLayout layout;
  
    private TextView text;
    private TextView time_show;
	public MyAdapter(List<MyData> myList,Context context) {
		// TODO Auto-generated constructor stub
		this.myList=myList;
		this.context=context;

	}
	public int getCount() {
		// TODO Auto-generated method stub
		return myList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return myList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 
		LayoutInflater inflater=LayoutInflater.from(context);
		layout=new RelativeLayout(context);
		if(myList.get(position).getFlag()==0){
			layout=(RelativeLayout) inflater.inflate(R.layout.left_contact, null);	
			 
		}else if(myList.get(position).getFlag()==1){
			layout=(RelativeLayout) inflater.inflate(R.layout.right_contact, null); 
		}
		 
		text=(TextView)layout.findViewById(R.id.text);
		text.setText(myList.get(position).getContent());
		time_show=(TextView)layout.findViewById(R.id.time_show);
		time_show.setText(myList.get(position).getTime());
		if(time_show.getText().toString().trim().equals("")){
		   time_show.setVisibility(View.GONE);
		}
		 
		return layout;
	}

}
