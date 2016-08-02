package com.mo.saildapp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;

import com.mo.saildapp.R;

public class Utils {


	public static void createDatabase(Context context) {
		try {
			// ���.db�ļ��ľ���·��
			String databaseFilename = Syscontents.DATABASE_PATH + Syscontents.DATABASE_FILENAME;
			File dir = new File(Syscontents.DATABASE_PATH);
			// ���Ŀ¼�����ڣ��������Ŀ¼
			if (!dir.exists())
				dir.mkdir();
			// Ŀ¼�в����� .db�ļ������res\rawĿ¼�и�������ļ�����Ŀ¼
			if (!(new File(databaseFilename)).exists()) {
				// ��÷�װ.db�ļ���InputStream����
				InputStream is = context.getResources().openRawResource(R.raw.raokouling);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[7168];
				int count = 0;
				// ��ʼ����.db�ļ�
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
		} catch (Exception e) {
		}
	}
	
}
