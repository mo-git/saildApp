package com.mo.saildapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mo.saildapp.activity.AddDataActivity;
import com.mo.saildapp.activity.VoicePlay;
import com.mo.saildapp.adapter.DataAdapter;
import com.mo.saildapp.db.dao.DbDao;
import com.mo.saildapp.db.entity.Raokouling;
import com.mo.saildapp.utils.CollectionUtils;
import com.mo.saildapp.view.XListView;
import com.mo.saildapp.view.XListView.IXListViewListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ActionMode;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements IXListViewListener, OnItemClickListener, OnItemLongClickListener {
	
	private XListView listView;
	private boolean hasMore = false;
	private DataAdapter adapter;
	private TextView title;
	private Button title_right;
	private Raokouling currentData;
	private List<Raokouling> datas = new ArrayList<Raokouling>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (XListView) findViewById(R.id.listview);
		title = (TextView) findViewById(R.id.tv_title);
		title_right = (Button)findViewById(R.id.title_right);
		title_right.setVisibility(View.VISIBLE);
		title_right.setText(getString(R.string.btn_add));
		title_right.setOnClickListener(this);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		title.setText(getString(R.string.title));
		
		DbDao dao = new DbDao();
    	datas = dao.getData(Integer.MAX_VALUE);
    	updateAdapter();
	}
	

	@Override
	public void onRefresh() {
		DbDao dao = new DbDao();
    	List<Raokouling> tempDatas = dao.getData(Integer.MAX_VALUE);
    	if(!CollectionUtils.isEmpty(tempDatas)){
    		datas.clear();
    		datas.addAll(tempDatas);
    		if(tempDatas.size() == 20){
    			hasMore = true;
    		}else{
    			hasMore = false;
    		}
    	}else{
    		hasMore = false;
    	}
    	listView.stopRefresh();
    	updateAdapter();
	}

	@Override
	public void onLoadMore() {
		if(!CollectionUtils.isEmpty(datas)){
			int lastId = datas.get(datas.size() - 1).getId();
			DbDao dao = new DbDao();
	    	List<Raokouling> tempDatas = dao.getData(lastId);
	    	if(!CollectionUtils.isEmpty(tempDatas)){
	    		if(tempDatas.size() == 20){
	    			hasMore = true;
	    		}else{
	    			hasMore = false;
	    		}
	    		datas.addAll(tempDatas);
	    	}else{
	    		hasMore = false;
	    	}
		}
		listView.stopLoadMore();
		updateAdapter();
	}
	
	public void updateAdapter(){
		if(adapter == null){
			adapter = new DataAdapter(getApplicationContext(), datas);
			listView.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
		
		listView.setPullLoadEnable(hasMore);
	}
	

   
    @Override
    public void onClick(View v) {
    	super.onClick(v);
    	switch (v.getId()) {
		case R.id.title_right:
			startActivityForResult(new Intent(this,AddDataActivity.class), 0);
			break;
		}
    }

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
		position = position - 1;
		if(position >= 0){
			currentData = datas.get(position);
			showCenterDialog(this,"",getString(R.string.delete_title),getString(R.string.btn_cancel),getString(R.string.btn_ok),false);
		}
		return true;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		position = position - 1;
		if(position >= 0){
			Intent intent = new Intent(this,VoicePlay.class);
			intent.putExtra("data", datas.get(position));
			startActivity(intent);
		}
		
	}
	
	@Override
	public void btnCancle() {
		super.btnCancle();
	}
	
	@Override
	public void btnConfirm() {
		super.btnConfirm();
		DbDao dao = new DbDao();
    	boolean isSuccess = dao.delete(currentData.getId());
    	if(isSuccess){
    		datas.remove(currentData);
    		updateAdapter();
    	}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(resultCode == RESULT_OK){
			currentData = (Raokouling)intent.getSerializableExtra("data");
			if(currentData != null){
				datas.add(0,currentData);
				updateAdapter();
			}
		}
	}


}
