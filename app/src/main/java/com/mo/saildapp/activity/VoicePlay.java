package com.mo.saildapp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.test.PerformanceTestCase;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.mo.saildapp.BaseActivity;
import com.mo.saildapp.R;
import com.mo.saildapp.db.entity.Raokouling;
import com.mo.saildapp.utils.ApkInstaller;
import com.mo.saildapp.utils.PreferenceUtils;
import com.mo.saildapp.utils.Syscontents;

public class VoicePlay extends BaseActivity implements OnClickListener {
	private static String TAG = VoicePlay.class.getSimpleName();
	private TextView title;
	private Button title_right;
	private TextView title_left;
	private SpeechSynthesizer mTts;
	private String voicer;
	private String mEngineType;
	ApkInstaller mInstaller ;
	private Toast mToast;
	private Raokouling data;
	private TextView et_content;
	private SharedPreferences mSharedPreferences;
	
	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ttsdemo);
		data = (Raokouling)getIntent().getSerializableExtra("data");
		initLayout();
		mTts = SpeechSynthesizer.createSynthesizer(VoicePlay.this, mTtsInitListener);
		mInstaller = new  ApkInstaller(VoicePlay.this);
		
	}

	
	private void initLayout() {
		title = (TextView)findViewById(R.id.tv_title);
		title_left = (TextView)findViewById(R.id.tv_left);
		title_right = (Button)findViewById(R.id.title_right);
		et_content = ((TextView) findViewById(R.id.tts_text));
		title.setText(data.getTitle());
		title_right.setText(getString(R.string.btn_set));
		title_left.setVisibility(View.VISIBLE);
		title_right.setVisibility(View.VISIBLE);
		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);
		
		et_content.setText(data.getContent());
		findViewById(R.id.tts_play).setOnClickListener(VoicePlay.this);
		findViewById(R.id.tts_cancel).setOnClickListener(VoicePlay.this);
		findViewById(R.id.tts_pause).setOnClickListener(VoicePlay.this);
		findViewById(R.id.tts_resume).setOnClickListener(VoicePlay.this);
		
	}
	@Override
	public void onResume() {
		super.onResume();
		mSharedPreferences = getSharedPreferences(VolumeActivity.PREFER_NAME, MODE_PRIVATE);
		voicer = PreferenceUtils.getPrefString(this, Syscontents.VOICER, "xiaoyan");
		mEngineType = PreferenceUtils.getPrefString(this, Syscontents.ENGINEYPE, SpeechConstant.TYPE_CLOUD);
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.title_right:
			if(SpeechConstant.TYPE_CLOUD.equals(mEngineType)){
				Intent intent = new Intent(VoicePlay.this, SettingAcivity.class);
				startActivity(intent);
			}else{
				// 本地设置跳转到语记中
				if (!SpeechUtility.getUtility().checkServiceInstalled()) {
					mInstaller.install();
				}else {
					SpeechUtility.getUtility().openEngineSettings(null);				
				}
			}
			break;
		case R.id.tts_play:
			// 设置参数
			setParam();
			int code = mTts.startSpeaking(et_content.getText().toString(), mTtsListener);
			if (code != ErrorCode.SUCCESS) {
				if(code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED){
					//未安装则跳转到提示安装
					mInstaller.install();
				}else {
					showTip("语音合成失败,错误code: " + code);	
				}
			}
			break;
		// 取消合成
		case R.id.tts_cancel:
			mTts.stopSpeaking();
			break;
		// 暂停播放
		case R.id.tts_pause:
			mTts.pauseSpeaking();
			break;
		// 继续播放
		case R.id.tts_resume:
			mTts.resumeSpeaking();
			break;
		case R.id.tv_left:
			finish();
			break;
		}
	}
	
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
        		showTip("初始化失失败,错误码："+code);
        	}	
		}
	};

	
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		
		@Override
		public void onSpeakBegin() {
		}

		@Override
		public void onSpeakPaused() {
		}

		@Override
		public void onSpeakResumed() {
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,String info) {}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {}

		@Override
		public void onCompleted(SpeechError error) {
		 if (error != null) {
				showTip(error.getPlainDescription(true));
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};

	private void showTip(final String str) {
		mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 参数设置
	 * @return
	 */
	private void setParam(){
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 根据合成引擎设置相应参数
		if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
			// 设置在线合成发音�??
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
			//设置合成语�??
			mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
			//设置合成音调
			mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
			//设置合成音量
			mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
		}else {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
			mTts.setParameter(SpeechConstant.VOICE_NAME, "");
			
		}
		//设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
		
		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTts.stopSpeaking();
		mTts.destroy();
	}
	

}
