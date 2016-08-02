package com.mo.saildapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mo.saildapp.BaseActivity;
import com.mo.saildapp.R;
import com.mo.saildapp.db.dao.DbDao;
import com.mo.saildapp.db.entity.Raokouling;

public class AddDataActivity extends BaseActivity{
	private TextView title;
	private Button title_right;
	private TextView title_left;
	private EditText et_title;
	private EditText et_Content;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		title = (TextView)findViewById(R.id.tv_title);
		title_left = (TextView)findViewById(R.id.tv_left);
		title_right = (Button)findViewById(R.id.title_right);
		et_title = (EditText)findViewById(R.id.et_title);
		et_Content = (EditText)findViewById(R.id.et_content);
		title_right.setText("提交");
		title.setText("添加绕口令");
		title_left.setVisibility(View.VISIBLE);
		title_right.setVisibility(View.VISIBLE);
		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);
	}

	public void submitAdd(){
		String title = et_title.getText().toString().trim();
		String content = et_Content.getText().toString().trim();
		if(TextUtils.isEmpty(title)){
			showToast("请输入标题");
			return;
		}
		if(TextUtils.isEmpty(content)){
			showToast("请输入内容");
			return;
		}
		DbDao dao = new DbDao();
		Raokouling data = new Raokouling(title,content);
		boolean isSuccess = dao.add(data);
		if(isSuccess){
			Intent intent = new Intent();
			intent.putExtra("data", data);
			setResult(RESULT_OK, intent);
			finish();
		}else{
			showToast("添加失败,请稍后重试");
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.tv_left:
				finish();
				break;
			case R.id.title_right:
				submitAdd();
				break;
		}
	}
}
