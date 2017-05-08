package com.jokerpeng.demo.picselector;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MyGridView mGv;
    private List<File> mList;
    private PicAdapter mPicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CommonUtis.verifyOCRPermissions(this,CommonUtis.verifySdkVersion());
        initView();
        initData();
        initAction();




//        mPicAdapter = new PicAdapter(this);
//        mGv.setAdapter(mPicAdapter);
    }

    private void initView() {
        mGv = (MyGridView) findViewById(R.id.gv_show_pic);
    }

    private void initData() {
        mList = new ArrayList<>();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels/3;
        mGv.setColumnWidth(w);
        mGv.setNumColumns(3);
        mPicAdapter = new PicAdapter(this,mList,w);
        mGv.setAdapter(mPicAdapter);
    }

    private void initAction() {
        findViewById(R.id.btn_select_pic).setOnClickListener(this);
    }

    private void getImages() {
        mList.clear();
        int mPicsSize;
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
        {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstName = null;
                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = MainActivity.this.getContentResolver();
                String selection = MediaStore.Images.Media.MIME_TYPE + "=? or " +MediaStore.Images.Media.MIME_TYPE + "=? or "+ MediaStore.Images.Media.MIME_TYPE + "=?";
                String[] selectionArgs = new String[]{"iamge/jpg","image/png","image/jpeg"};
//                String selection = MediaStore.Images.Media.MIME_TYPE + "=?";
//                String[] selectionArgs = new String[]{"image/png"};
                Cursor cursor = cr.query(imageUri,null,selection,
                        selectionArgs ,MediaStore.Images.Media.DATE_MODIFIED);
                while(cursor.moveToNext()){
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String tempPath = new File(path).getAbsolutePath();
                    File file = new File(path);
                    mList.add(file);
                }
                cursor.close();
                //TODO()
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mPicAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_select_pic:
                getImages();
                break;
        }
    }

}
