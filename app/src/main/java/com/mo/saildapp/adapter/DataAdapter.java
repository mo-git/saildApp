package com.mo.saildapp.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mo.saildapp.R;
import com.mo.saildapp.db.entity.Raokouling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DataAdapter extends BaseAdapter{
	private List<Raokouling> datas = new ArrayList<Raokouling>();
	private Context mContext;
	public DataAdapter(Context mContext,List<Raokouling> datas){
		this.mContext = mContext;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.data_item, null);
			holder.title = (TextView)convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(datas.get(position).getTitle());
		return convertView;
	}
	
	class ViewHolder{
		public TextView title;
	}

}
