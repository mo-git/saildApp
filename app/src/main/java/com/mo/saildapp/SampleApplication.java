package com.mo.saildapp;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

public class SampleApplication extends TinkerApplication {
    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.mo.saildapp.MyApplication",
                "com.tencent.tinker.loader.TinkerLoader", false,true);
    }
}
