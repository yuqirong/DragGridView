# DragGridView

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

##介绍 Introduction##
**DragGridView** - 可拖拽item来交换位置的GridView。(The GridView can drag items to exchange their positions.)

![screenshot](https://github.com/yuqirong/DragGridView/blob/master/screenshots/20160223192025.png)

##截图 Screenshot##
![screenshot_gif](https://github.com/yuqirong/DragGridView/blob/master/screenshots/20160223192234.gif)

##下载 Demo Download##

[Download](https://github.com/yuqirong/DragGridView/blob/master/screenshots/app-debug-unaligned.apk)

##用法 Usage##
###step 1###
把DragGridView控件添加到你的布局xml中。(Include the DragGridView widget in your layout. )

	<com.yuqirong.draggridview.view.DragGridView
            android:id="@+id/mGridView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:cacheColorHint="@null"
            android:horizontalSpacing="20dp"
            android:listSelector="@android:color/transparent"
            android:numColumns="4"
            android:padding="10dp"
            android:verticalSpacing="10dp" />

###step 2###
在 `onCreate(Bundle savedInstanceState)` 里获取相应的DragGridView对象，并设置Adapter，该Adapter必须继承自DragGridAdapter。(You can get the DragGridView object in  `onCreate(Bundle savedInstanceState)` , and set the adapter.The adapter must be extends DragGridAdapter).

	 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
	 	mGridView = (DragGridView) findViewById(R.id.mGridView);
		MAdapter mAdapter = new MAdapter(list);
        mGridView.setAdapter(mAdapter);
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

PS：该DragGridView内部实际是应用了WindowManager，所以需要去除标题栏，以避免计算y轴距离不准确。如果你的MainActivity继承自Activity，则需要加上`requestWindowFeature(Window.FEATURE_NO_TITLE);`；或者继承自AppCompatActivity，需要`getSupportActionBar().hide();`。另外如果你的应用一定需要标题栏的存在，那么需要额外加上标题栏的高度。(The interior is practical DragGridView applied WindowManager, so it is necessary to remove the title bar, in order to avoid calculating the y-axis distance is not accurate. If you MainActivity extends Activity, you need to add `requestWindowFeature (Window.FEATURE_NO_TITLE);`; or extends AppCompatActivity, need `getSupportActionBar().hide();`. Also, if your application must require the presence of the title bar, you need to add extra height of the title bar.)

###step 3###
增加显示悬浮窗的权限。(add SYSTEM_ALERT_WINDOW user permission.)
	
	<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
	
最后，尽情地使用吧！(Finally,enjoy it!)

##致谢 Thanks##
Inspired by

* [TopNews](https://github.com/Rano1/TopNews) created by [Rano1](https://github.com/Rano1)

##联系方式 Contact Me##
新浪微博 Sina Weibo：[@活得好像一条狗](http://weibo.com/yyyuqirong) 

电子邮箱 Email：<yqr271228943@gmail.com>

If you have any questions or want to contact me,you can also leave a message in [Issues](https://github.com/yuqirong/DragGridView/issues).

##开源许可证 License##

    Copyright (c) 2016 yuqirong 

    Licensed under the Apache License, Version 2.0 (the "License”);
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
