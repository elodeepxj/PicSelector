package com.jokerpeng.demo.picselector;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private GridView mGv;
    private List<Map<String,Objects>> mList;
    private int mScreenHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        getImages();


        mGv = (GridView) findViewById(R.id.gv_show_pic);
        findViewById(R.id.btn_select_pic).setOnClickListener(this);
        mList = new ArrayList<>();
        mGv.setAdapter(new SimpleAdapter(this,mList,R.layout.item_gridview,new String[]{"image"},new int[]{R.id.iv_item}));
    }

    private void getImages() {
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
                Cursor cursor = cr.query(imageUri,null,MediaStore.Images.Media.CONTENT_TYPE + "=? or" + MediaStore.Images.Media.CONTENT_TYPE + "=?",
                        new String[]{"iamge/jpeg","image/png"},MediaStore.Images.Media.DATE_MODIFIED);
                while(cursor.moveToNext()){
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if(null == firstName){
                        firstName = path;
                    }
                    File parentFile = new File(path).getParentFile();
                    if(null == parentFile){
                        continue;
                    }
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    imageFloder.setDir(dirPath);
                    imageFloder.setFirstImagePath(path);
                    
                }

            }
        }).start();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_select_pic:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
