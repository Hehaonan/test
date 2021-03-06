package com.android.hhn.toy;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hhn.toy.ac.TestBundleTooLargeActivity;
import com.android.hhn.toy.ac.TestProcessExitInfoActivity;
import com.android.hhn.toy.ac.TestScopeStorageActivity;
import com.android.hhn.toy.jobscheduler.MyJobService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int[] mProcessIds;
    private TextView mTextView;
    private int quitClickCount;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        mTextView = findViewById(R.id.showDialog_tv);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDialog();
                showSimLockedTipsDialog();
            }
        });
        findViewById(R.id.bundle_too_large_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestBundleTooLargeActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.scope_storage_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestScopeStorageActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.process_exit_info_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestProcessExitInfoActivity.class);
                startActivity(intent);
            }
        });
        getPidByProcessName(getApplicationContext());

    }

    private Bitmap createBitmap(File file) {
        // 压缩文件
        Bitmap sourceBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.decodeStream(bais);

        Bitmap sourceBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background_d);
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 如果设置为true，则解码器将返回null（无位图），
        // 仍可以设置，从而允许调用方查询位图而不必为其像素分配内存。
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inPreferredConfig = Bitmap.Config.RGB_565;// 设置更低颜色模式
        options.inSampleSize = 2;//设置更低采样率，宽、高为原始1/2，size缩减为1/4
        options.inJustDecodeBounds = false;
        options.inDensity = 3;// bitmap的自身密度
        options.inTargetDensity = 2;// 目标设备的密度
        options.inScaled = false;// 缩放系数=inDensity/inTargetDensity
        return sourceBitmap2;
    }

    private void testHashMap() {
        HashMap<String, String> map = new HashMap<>(10);
        String s = new String("key");
        String s2 = new String("key2");
        map.put(s, "111");
        map.put(s2, "222");
        String s3 = new String("key3");
        map.put(s3, "333");
        Log.d(TAG, "HashMap size ：" + map.size());
        for (String key : map.keySet()) {
            Log.d(TAG, "map-----> " + key + " : " + map.get(key));
        }
        Log.d(TAG, "onCreate: " + s.hashCode());
        Log.d(TAG, "onCreate: " + s2.hashCode());
        Log.d(TAG, "onCreate: " + s3.hashCode());
        Log.d(TAG, "onCreate: " + "key".hashCode());
        Log.d(TAG, "onCreate: " + (s.hashCode() & 15));
        Log.d(TAG, "onCreate: " + (s2.hashCode() & 15));
        Log.d(TAG, "onCreate: " + (s3.hashCode() & 15));
    }

    private void testHandler() {
        Handler handler = new Handler();
        Log.d(TAG, "handler = 1");
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "handler = 2");
            }
        });
        Log.d(TAG, "handler = 3");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "handler = 4");
            }
        }, 100);
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
        quitClickCount++;
        if (quitClickCount == 1) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    quitClickCount = 0;
                }
            }, 2000);
        } else if (quitClickCount == 2) {
            quitApp();
        }
    }

    private void quitApp() {
        //        if (null != mProcessIds && mProcessIds.length > 0) {
        //            Log.d(TAG, "quitApp length: " + mProcessIds.length);
        //            for (int pid : mProcessIds) {
        //                Log.d(TAG, "quitApp: kill " + pid);
        //                Process.killProcess(mProcessIds[0]);
        //            }
        //        } else {
        //            Process.killProcess(Process.myPid());
        //        }
        System.exit(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startJobScheduler() {
        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        //Builder构造方法接收两个参数，第一个参数是jobId，每个app或者说uid下不同的Job,它的jobId必须是不同的
        //第二个参数是我们自定义的JobService,系统会回调我们自定义的JobService中的onStartJob和onStopJob方法
        JobInfo.Builder builder = new JobInfo.Builder(1,
                new ComponentName(getPackageName(), MyJobService.class.getName()));
        //指定每三秒钟重复执行一次
        builder.setPeriodic(3000);
        //builder.setMinimumLatency(1000);//设置延迟调度时间
        //builder.setOverrideDeadline(2000);//设置最大延迟截至时间
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);//设置所需网络类型
        builder.setRequiresDeviceIdle(true);//设置在DeviceIdle时执行Job
        builder.setRequiresCharging(true);//设置在充电时执行Job
        //builder.setExtras(extras);//设置一个额外的附加项

        if (mJobScheduler.schedule(builder.build()) <= 0) {
            Toast.makeText(this, "JobScheduler 执行失败", Toast.LENGTH_SHORT).show();
        }
    }

    private AlertDialog mSimLockedTipsDialog;

    private void hideSimLockedTipsDialog() {
        if (mSimLockedTipsDialog != null) {
            mSimLockedTipsDialog.dismiss();
            mSimLockedTipsDialog = null;
        }
    }

    private void showSimLockedTipsDialog() {
        if (mSimLockedTipsDialog != null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.sim_state_locked_dialog_title));
        builder.setMessage(getString(R.string.sim_state_locked_puk_dialog_message));
        builder.setCancelable(false);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideSimLockedTipsDialog();
            }
        });
        mSimLockedTipsDialog = builder.create();
        mSimLockedTipsDialog.setCanceledOnTouchOutside(false);
        mSimLockedTipsDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION);
        setDialogDecorViewFlag();
        mSimLockedTipsDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == View.SYSTEM_UI_FLAG_VISIBLE) { //当导航栏恢复显示，再重置一下diaolog的设置
                    setDialogDecorViewFlag();
                }
            }
        });
        mSimLockedTipsDialog.show();
    }

    private void setDialogDecorViewFlag() {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        mSimLockedTipsDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    private void popWindow() {
        LayoutInflater inflater = LayoutInflater.from(this);//获取一个填充器
        View view = inflater.inflate(R.layout.dialog_net_tip, null);
        TextView textView1 = view.findViewById(R.id.privacy_disagree_button);
        TextView textView2 = view.findViewById(R.id.privacy_agree_button);
        final PopupWindow popWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setFocusable(false);
        popWindow.setOutsideTouchable(false);// 一起设置才生效
        popWindow.setTouchable(true);
        popWindow.setClippingEnabled(false);
        //        WindowManager.LayoutParams params = getWindow().getAttributes();//创建当前界面的一个参数对象
        //        params.alpha = 0;//设置参数的透明度为0.8，透明度取值为0~1，1为完全不透明，0为完全透明，因为android中默认的屏幕颜色都是纯黑色的，所以如果设置为1，那么背景将都是黑色，设置为0，背景显示我们的当前界面
        //        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //        getWindow().setAttributes(params);//把该参数对象设置进当前界面中
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {//设置PopupWindow退出监听器
            @Override
            public void onDismiss() {//如果PopupWindow消失了，即退出了，那么触发该事件，然后把当前界面的透明度设置为不透明
                //                WindowManager.LayoutParams params = getWindow().getAttributes();
                //                params.alpha = 1.0f;//设置为不透明，即恢复原来的界面
                //                getWindow().setAttributes(params);
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });
        //第一个参数为父View对象，即PopupWindow所在的父控件对象，第二个参数为它的重心，后面两个分别为x轴和y轴的偏移量
        popWindow.showAtLocation(inflater.inflate(R.layout.ac_main, null), Gravity.CENTER, 0, 0);

    }


    private AlertDialog mNetTipDialog;

    private void showDialog() {
        ScrollView sc = (ScrollView) getLayoutInflater().inflate(R.layout.dialog_privacy, null);
        TextView textView = sc.findViewById(R.id.dialog_privacy_content_tv);

        String textStart = getString(R.string.spider_splash_privacy_text_start);
        String privacyText = getString(R.string.spider_splash_privacy_text);
        String permissionText = getString(R.string.spider_splash_privacy_text_permission_start);
        String permissionLocationTitle = getString(R.string.spider_splash_privacy_text_permission_location_title);
        String permissionLocationContent = getString(R.string.spider_splash_privacy_text_permission_location_content);
        String permissionInfoTitle = getString(R.string.spider_splash_privacy_text_permission_info_title);
        String permissionInfoContent = getString(R.string.spider_splash_privacy_text_permission_info_content);
        String permissionStorageTitle = getString(R.string.spider_splash_privacy_text_permission_storage_title);
        String permissionStorageContent = getString(R.string.spider_splash_privacy_text_permission_storage_content);
        String textEnd = getString(R.string.spider_splash_privacy_text_end);

        SpannableStringBuilder agreementBuilder = new SpannableStringBuilder();
        agreementBuilder.append(textStart)
                .append(dealTextClick(privacyText))
                .append(permissionText)
                .append(dealTextBold(permissionLocationTitle))
                .append(permissionLocationContent)
                .append(dealTextBold(permissionInfoTitle))
                .append(permissionInfoContent)
                .append(dealTextBold(permissionStorageTitle))
                .append(permissionStorageContent)
                .append(dealTextClick(privacyText))
                .append(textEnd);

        textView.setText(agreementBuilder);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "isAttachedToWindow: " + mNetTipDialog.getWindow().getDecorView().isAttachedToWindow());
                AlertDialog testDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示").setMessage("测试文案啊-测试文案啊-测试文案啊-测试文案啊-测试文案啊-测试文案啊-测试文案啊-测试文案啊！！！")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                testDialog.setCancelable(true);
                testDialog.setCanceledOnTouchOutside(true);
                testDialog.show();
            }
        });
        mNetTipDialog = new AlertDialog.Builder(this)
                .setTitle("提示").setView(sc)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mNetTipDialog = null;
                    }
                }).create();

        if (isFinishing() || isDestroyed()) {
            Log.d(TAG, "isAttachedToWindow: " + mNetTipDialog.getWindow().getDecorView().isAttachedToWindow());
            Log.d(TAG, "isFinishing: " + this.isFinishing());
            Log.d(TAG, "isDestroyed: " + this.isDestroyed());
            return;
        }
        mNetTipDialog.setCancelable(false);
        mNetTipDialog.setCanceledOnTouchOutside(false);
        mNetTipDialog.show();
    }

    private String dealTextBold(String permissionLocationTitle) {
        return permissionLocationTitle;
    }

    private String dealTextClick(String privacyText) {
        return privacyText;
    }

    public void getPidByProcessName(Context context) {
        //        if (TextUtils.isEmpty(packageName)) {
        //            return -1;
        //        }
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager.getRunningAppProcesses();
        if (appProcessList != null && appProcessList.size() > 0) {
            mProcessIds = new int[appProcessList.size()];
            for (int i = 0; i < appProcessList.size(); i++) {
                ActivityManager.RunningAppProcessInfo appProcess = appProcessList.get(i);
                Log.d(TAG, "getPidByProcessName: " + appProcess.processName);
                mProcessIds[i] = appProcess.pid;
                if (appProcess.processName.contains("com.Qunar")) {
                    Log.d(TAG, "getPidByProcessName: " + appProcess.processName + ", appProcess.pid:" + appProcess.pid);
                }
            }
        }
    }


    private String getSupportStoreType(Context context) {
        String vivoStorePkg = "com.Qunar";
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(vivoStorePkg, 0);
            code = info.versionCode;
            Log.d("vivo", info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d("vivo", "get code error=" + code);
        }
        Log.d("vivo", "code=" + code);
        //3100：vivo要求的版本
        return code > 100 ? vivoStorePkg : "";
    }

    private void jumpToOtherAppStoreUpdate() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("market://details?id=com.Qunar");//app包名
            intent.setData(uri);
            //intent.setPackage("com.bbk.appstore");//vivo
            //intent.setPackage("com.huawei.appmarket");//hw
            //intent.setPackage("com.sec.android.app.samsungapps");//三星
            startActivity(intent);
            Log.d("vivo", "code= success");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("vivo", "code= error");
        }
    }

    public void goToSamsungappsMarket() {
        Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=com.Qunar");
        Intent goToMarket = new Intent();
        goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
        goToMarket.setData(uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

}

