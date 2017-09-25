package com.ynzz.lbstest.carmera;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhc.gallery.GalleryConfig;
import com.dhc.gallery.GalleryHelper;
import com.dhc.gallery.ui.GalleryActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.ynzz.lbstest.AboutActivity;
import com.ynzz.lbstest.LoginActivity;
import com.ynzz.lbstest.R;
import com.ynzz.lbstest.carmera.ui.StorageType;
import com.ynzz.lbstest.carmera.ui.StorageUtil;
import com.ynzz.lbstest.driver;
import com.ynzz.lbstest.passenger;
import com.ynzz.util.Base64Coder;
import com.ynzz.util.ZoomBitmap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CarmeraMainActivity extends AppCompatActivity implements PermissionListener {
    // 服务器地址
    private static final String HOST = "http://192.168.1.117:8080/ImageServer/upServer";
    List<String> mList;
    String outputPath;
    ArrayList<String> outputPathList;
    private ImageView imageCarmera;
    private Bitmap upbitmap;
    private Handler myHandler;
    private ProgressDialog myDialog;
    private TextView uploadInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hideStatusBar();

        setContentView(R.layout.activity_carmera_main);
//        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.activity_main);
//        relativeLayout.getBackground().setAlpha(110);
        StorageUtil.init(this, null);//初始化监测sdcard
        imageCarmera = (ImageView) findViewById(R.id.imageCarmera);
        mList = new ArrayList<>();
        mList.add((mList.size() + 1) + ".  选择单张图片");
        mList.add((mList.size() + 1) + ".  选择单张图片并裁剪");
        mList.add((mList.size() + 1) + ".  选择多张图片");
        mList.add((mList.size() + 1) + ".  选择视频");
        mList.add((mList.size() + 1) + ".  拍摄视频(可限制时长)");
        mList.add((mList.size() + 1) + ".  拍照片");
        mList.add((mList.size() + 1) + ".  拍照片并裁剪");
        if(!AndPermission.hasPermission(CarmeraMainActivity.this
                , Manifest.permission.CAMERA
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.RECORD_AUDIO)) {
            AndPermission.with(CarmeraMainActivity.this)
                    .requestCode(100)
                    .permission(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_PHONE_STATE)
                    .rationale(new RationaleListener() {
                        @Override
                        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                            AndPermission.rationaleDialog(CarmeraMainActivity.this, rationale).show();
                        }
                    })
                    .send();
        }
        ListView listView = (ListView) findViewById(R.id.ls_home);

        listView.setAdapter(new MyAdapter(this));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: /*** 选择单张图片 onActivityResult{@link GalleryActivity.PHOTOS}*/
                        GalleryHelper.with(CarmeraMainActivity.this).type(GalleryConfig.SELECT_PHOTO).requestCode(12).singlePhoto().execute();
                        break;
                    case 1:  /***选择单张图片并裁剪 onActivityResult{@link GalleryActivity.PHOTOS}*/
                       // outputPath = StorageUtil.getWritePath(StorageUtil.get32UUID() + ".jpg", StorageType.TYPE_TEMP);
                        outputPath = StorageUtil.getWritePath("Crop"+ System.currentTimeMillis()+ ".jpg", StorageType.TYPE_TEMP);
                        GalleryHelper.with(CarmeraMainActivity.this).type(GalleryConfig.SELECT_PHOTO).requestCode(12).singlePhoto().isNeedCropWithPath(outputPath).execute();
                        break;
                    case 2:  /*** 选择多张图片 onActivityResult{@link GalleryActivity.PHOTOS}*/
                        GalleryHelper.with(CarmeraMainActivity.this).type(GalleryConfig.SELECT_PHOTO).requestCode(12).limitPickPhoto(9).execute();
                        break;
                    case 3:/***选择视频 onActivityResult{@link GalleryActivity.VIDEO}*/
                        GalleryHelper.with(CarmeraMainActivity.this).type(GalleryConfig.SELECT_VEDIO).requestCode(12).isSingleVedio().execute();
                        break;
                    case 4:/***拍摄视频 onActivityResult{@link GalleryActivity.VIDEO}*/

                        GalleryHelper.with(CarmeraMainActivity.this).type(GalleryConfig.RECORD_VEDIO).requestCode(12)
//                                .limitRecordTime(10)//限制时长
                                .limitRecordSize(1)//限制大小
                                .execute();
                        break;
                    case 5:/***拍照片onActivityResult {@link GalleryActivity.PHOTOS}*/
                        GalleryHelper.with(CarmeraMainActivity.this).type(GalleryConfig.TAKE_PHOTO).requestCode(12).execute();
                        break;
                    case 6: /***拍照片并裁剪 onActivityResult{@link GalleryActivity.CROP}*/
                        //outputPath = StorageUtil.getWritePath(StorageUtil.get32UUID() + ".jpg", StorageType.TYPE_TEMP);
                        outputPath = StorageUtil.getWritePath("Crop"+ System.currentTimeMillis()+ ".jpg", StorageType.TYPE_TEMP);
                        GalleryHelper.with(CarmeraMainActivity.this).type(GalleryConfig.TAKE_PHOTO).isNeedCropWithPath(outputPath).requestCode(12).execute();
                        break;
                    default:
                        break;
                }
            }
        });
        initToolbar();
        uploadInfo = (TextView) findViewById(R.id.upload_info);
//        uploadFile();
    }
    public void uploadFile()
    {
        //服务器端地址
        String url = "http://192.168.1.117:8080/UploadFileServer/upload";
        //手机端要上传的文件，首先要保存你手机上存在该文件
//        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                + "/Telegram/VID_20170912_144831.mp4";
        String filePath = outputPath;
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams param = new RequestParams();
        try
        {
            param.put("file", new File(filePath));
            param.put("content", "ynzz");

            httpClient.post(url, param, new AsyncHttpResponseHandler()
            {
                @Override
                public void onStart()
                {
                    super.onStart();
                    uploadInfo.setText("正在上传...");
                }

                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        Toast.makeText(CarmeraMainActivity.this, "上传成功！", Toast.LENGTH_LONG).show();
                    uploadInfo.setText("上传成功");
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    uploadInfo.setText("上传失败！");
                }

            });

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Toast.makeText(CarmeraMainActivity.this, "上传文件不存在！", Toast.LENGTH_LONG).show();
        }
    }
    private class MyAdapter extends BaseAdapter {
        Context mContext;
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            mContext = context;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder.info = (TextView) convertView.findViewById(R.id.tv_info);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.info.setText(mList.get(position));

            return convertView;
        }

        class ViewHolder {
            public TextView info;
        }
    }


    //----------------------------------权限回调处理----------------------------------//

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * 转给AndPermission分析结果。
         *
         * @param requestCode  请求码。
         * @param permissions  权限数组，一个或者多个。
         * @param grantResults 请求结果。
         * @param listener PermissionListener 对象。
         */
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onSucceed(int requestCode, List<String> grantPermissions) {
        Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {

        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (!AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this, 300).show();
            Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (12 == requestCode && resultCode == Activity.RESULT_OK) {
            if (data.getStringArrayListExtra(GalleryActivity.PHOTOS) != null) {//选择图片返回

                ArrayList<String> path = data.getStringArrayListExtra(GalleryActivity.PHOTOS);
                outputPathList = path;
                Toast.makeText(CarmeraMainActivity.this, path.toString(), Toast.LENGTH_SHORT).show();

            } else if (data.getStringExtra(GalleryActivity.VIDEO) != null) {//选择和拍摄视频(目前支持单选)

                String path = data.getStringExtra(GalleryActivity.VIDEO);
                outputPath = path;
                Toast.makeText(CarmeraMainActivity.this, path.toString(), Toast.LENGTH_SHORT).show();
            } else if (data.getStringExtra(GalleryActivity.DATA) != null) {//裁剪回来
                if (outputPath == null) {//没有传入返回裁剪路径
                    byte[] datas = data.getByteArrayExtra(GalleryActivity.DATA);
                    Toast.makeText(CarmeraMainActivity.this, datas.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }else if(requestCode==300&&resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "从设置回来", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 隐藏状态栏
     * <p>
     * 在setContentView前调用
     */
    protected void hideStatusBar() {
        final int sdkVer = Build.VERSION.SDK_INT;
        if (sdkVer < 16) {
            //4.0及一下
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }
    //initToolbar
    private void initToolbar() {

        View rootView = findViewById(R.id.activity_about_toolbar);

        if (rootView != null) {

            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

            toolbar.setTitle("照片与视频采集");

            setSupportActionBar(toolbar);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CarmeraMainActivity.this.finish();
                }
            });
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upload, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.uploadcarmera:
                uploadFile();
                Toast.makeText(CarmeraMainActivity.this,"上传成功",Toast.LENGTH_LONG).show();
                break;
            default:
        }
        return true;
    }
}
