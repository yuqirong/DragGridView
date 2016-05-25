package com.yuqirong.draggridview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuqirong.draggridview.R;
import com.yuqirong.draggridview.adapter.DragGridAdapter;
import com.yuqirong.draggridview.view.DragGridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DragGridView otherGridView;
    private DragGridView mGridView;

    private List<String> list = new ArrayList();

    private List<String> list2 = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        mGridView = (DragGridView) findViewById(R.id.mGridView);
        otherGridView = (DragGridView) findViewById(R.id.otherGridView);
        list.add("杭州");
        list.add("宁波");
        list.add("上海");
        list.add("北京");
        list.add("南京");
        list.add("西安");
        MAdapter mAdapter = new MAdapter(list);
        mGridView.setAdapter(mAdapter);

        list2.add("1");
        list2.add("2");
        list2.add("3");
        list2.add("4");
        list2.add("5");
        list2.add("6");
        MAdapter mAdapter2 = new MAdapter(list2);
        otherGridView.setAdapter(mAdapter2);
    }




    class MAdapter extends DragGridAdapter<String> {

        public MAdapter(List list) {
            super(list);
        }

        @Override
        public View getItemView(int position, View convertView, ViewGroup parent) {
            String text = getList().get(position);
            convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item, null);
            TextView tv_text = (TextView) convertView.findViewById(R.id.tv_text);
            tv_text.setText(text);
            return convertView;
        }
    }


}
