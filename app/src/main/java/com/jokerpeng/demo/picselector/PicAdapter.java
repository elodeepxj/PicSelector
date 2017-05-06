package com.jokerpeng.demo.picselector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5.
 */

public class PicAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<String> mList;
    private int itemLenght;
    private HashMap<Integer,Boolean> isSelect;

    public PicAdapter(Context context,List<String> list,int lenght) {
        this.mContext = context;
        this.mList = list;
        this.itemLenght = lenght;
        initData();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        initData();
    }

    private void initData() {
        isSelect = new HashMap<Integer, Boolean>();
        for(int i = 0;i < mList.size();i++){
            isSelect.put(i,false);
        }
        Log.e("33333333333","-------------------"+isSelect.size());
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.from(mContext).inflate(R.layout.item_gridview,parent,false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_item);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.box_item);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(itemLenght,itemLenght);
            convertView.setLayoutParams(params);
            ViewGroup.LayoutParams ivParams = viewHolder.imageView.getLayoutParams();
            ivParams.height = itemLenght;
            ivParams.width = itemLenght;
            viewHolder.imageView.setLayoutParams(ivParams);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String path;
        path = mList.get(position);
        viewHolder.imageView.setImageBitmap(imageProcess(path));
        viewHolder.checkBox.setChecked(isSelect.get(position));
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelect.get(position)){
                    isSelect.put(position,false);
                }else {
                    isSelect.put(position,true);
                }
            }
        });
        Log.e("++++++++++",position+"   "+isSelect.get(position));
        return convertView;
    }

    static class ViewHolder{
        ImageView imageView;
        CheckBox checkBox;
    }

    private Bitmap imageProcess(String picPath){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picPath, options);
        options.inSampleSize = BitmapUtil.calculateInSampleSize(options,640,640);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
        return bitmap;
    }

}
