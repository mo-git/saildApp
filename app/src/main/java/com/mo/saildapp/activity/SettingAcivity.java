package com.mo.saildapp.activity;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.mo.saildapp.BaseActivity;
import com.mo.saildapp.R;
import com.mo.saildapp.R.xml;
import com.mo.saildapp.utils.ApkInstaller;
import com.mo.saildapp.utils.PreferenceUtils;
import com.mo.saildapp.utils.Syscontents;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 设置界面
 */
public class SettingAcivity extends BaseActivity{
	
	private TextView title;
	private TextView title_left;
	private String[] mCloudVoicersEntries;
	private String[] mCloudVoicersValue ;
	private RadioGroup mRadioGroup;
	private ApkInstaller mInstaller;
	private int selectedNum = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		title = (TextView)findViewById(R.id.tv_title);
		title_left = (TextView)findViewById(R.id.tv_left);
		title.setText(getString(R.string.btn_set));
		title_left.setVisibility(View.VISIBLE);
		title_left.setOnClickListener(this);
		mInstaller = new  ApkInstaller(SettingAcivity.this);
		init();
	}
	private void init() {
		findViewById(R.id.tv_set_man).setOnClickListener(this);
		findViewById(R.id.tv_set_volume).setOnClickListener(this);
		mRadioGroup=((RadioGroup) findViewById(R.id.tts_rediogroup));
		selectedNum = PreferenceUtils.getPrefInt(this, Syscontents.NUM, 0);
		// 云端发音人名称列表
		mCloudVoicersEntries = getResources().getStringArray(R.array.voicer_cloud_entries);
		mCloudVoicersValue = getResources().getStringArray(R.array.voicer_cloud_values);
						
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_radioCloud:
					PreferenceUtils.setPrefString(getApplicationContext(), Syscontents.ENGINEYPE, SpeechConstant.TYPE_CLOUD);
					break;
				case R.id.rb_radioLocal:
					PreferenceUtils.setPrefString(getApplicationContext(), Syscontents.ENGINEYPE, SpeechConstant.TYPE_LOCAL);
					/**
					 * 选择本地合成
					 * 判断是否安装语记,未安装则跳转到提示安装页
					 */
					if (!SpeechUtility.getUtility().checkServiceInstalled()) {
						mInstaller.install();
					}
					break;
				}

			}
		} );
	}	

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_left:
			finish();
			break;
		case R.id.tv_set_man:
			showPresonSelectDialog();
			break;
		case R.id.tv_set_volume:
			startActivity(new Intent(this,VolumeActivity.class));
			break;
		}
	}
	
	
	/**
	 * 发音人选择
	 */
	private void showPresonSelectDialog() {
		switch (mRadioGroup.getCheckedRadioButtonId()) {
		// 选择在线合成
		case R.id.rb_radioCloud:			
			new AlertDialog.Builder(this).setTitle("选择发音人")
				.setSingleChoiceItems(mCloudVoicersEntries, selectedNum, 
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) { 
						PreferenceUtils.setPrefString(getApplicationContext(), Syscontents.VOICER, mCloudVoicersValue[which]);
						PreferenceUtils.setPrefInt(getApplicationContext(), Syscontents.NUM, which);
						selectedNum = which;
						dialog.dismiss();
					}
				}).show();
			break;
			
		// 选择本地合成
		case R.id.rb_radioLocal:
			if (!SpeechUtility.getUtility().checkServiceInstalled()) {
				mInstaller.install();
			}else {
				SpeechUtility.getUtility().openEngineSettings(SpeechConstant.ENG_TTS);				
			}
			break;
		default:
			break;
		}
	}

	
}