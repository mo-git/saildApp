package com.mo.saildapp;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity implements OnClickListener{

	private Dialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		MobclickAgent.onResume(this);
		super.onResume();
	}
	@Override
	protected void onPause() {
		MobclickAgent.onPause(this);
		super.onPause();
	}

	public void showToast(String text){
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}

	public void showCenterDialog(Context context,String title, String content, String cancel, String confirm, boolean isCanceled) {

		if (dialog != null && dialog.isShowing()) {
			return;
		} else {
			View view = LayoutInflater.from(context).inflate(R.layout.customdialog, null);
			dialog = new Dialog(context, R.style.dialog_alert_style);

			// 根据id在布局中找到控件对象
			TextView tv_dialog_title = (TextView) view.findViewById(R.id.dialog_title);
			TextView tv_dialog_content = (TextView) view.findViewById(R.id.dialog_content);
			Button confirmBtn = (Button) view.findViewById(R.id.confirm_btn);
			Button cancelBtn = (Button) view.findViewById(R.id.cancel_btn);
			if (title != null && !"".equals(title)) {
				tv_dialog_title.setText(title);
				tv_dialog_title.setVisibility(View.VISIBLE);
			} else {
				tv_dialog_title.setVisibility(View.GONE);
			}
			if (content != null && !"".equals(content)) {
				tv_dialog_content.setText(content);
				tv_dialog_content.setVisibility(View.VISIBLE);
			} else {
				tv_dialog_content.setVisibility(View.GONE);
			}
			if (cancel != null && !"".equals(cancel)) {
				cancelBtn.setOnClickListener(this);
				cancelBtn.setText(cancel);
				cancelBtn.setVisibility(View.VISIBLE);
			} else {
				cancelBtn.setVisibility(View.GONE);
			}
			if (confirm != null && !"".equals(confirm)) {
				confirmBtn.setText(confirm);
				confirmBtn.setOnClickListener(this);
				confirmBtn.setVisibility(View.VISIBLE);
			} else {
				confirmBtn.setVisibility(View.GONE);
			}
			dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(isCanceled);
			dialog.show();
		}
	}

	@Override
	public void onClick(View v) {
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
		}
		switch (v.getId()) {
			case R.id.confirm_btn:
				btnConfirm();

				break;
			case R.id.cancel_btn:
				btnCancle();
				break;
		}
	}

	public void btnConfirm(){};
	public void btnCancle(){};
}
