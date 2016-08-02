package com.mo.saildapp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;

import com.mo.saildapp.R;

public class Utils {


	public static void createDatabase(Context context) {
		try {
			// 获得.db文件的绝对路径
			String databaseFilename = Syscontents.DATABASE_PATH + Syscontents.DATABASE_FILENAME;
			File dir = new File(Syscontents.DATABASE_PATH);
			// 如果目录不存在，创建这个目录
			if (!dir.exists())
				dir.mkdir();
			// 目录中不存在 .db文件，则从res\raw目录中复制这个文件到该目录
			if (!(new File(databaseFilename)).exists()) {
				// 获得封装.db文件的InputStream对象
				InputStream is = context.getResources().openRawResource(R.raw.raokouling);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[7168];
				int count = 0;
				// 开始复制.db文件
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
