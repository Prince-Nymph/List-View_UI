package com.example.myapplication;
/**
 * @显示手机应用信息 准备数据链表(容器) List 把准备的链表数据插入到 BaseAdapter()中,再setAdapter();
 * BaseAdapter() view
 * 1、判断如果是converview是null,加载item文件,
 * 2、得到当前行数据对象;
 * 3、得到当前行需要更新的子view对象;
 * 4、给view设置数据;
 */

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    public static final int LOAD_ERROR = 0;
    public static final int LOAD_Info = 1;
    private ImageView MyImage;

    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_Info:
                    appAdapter = (AppAdapter) msg.obj;
//                    Bitmap bitmap = (Bitmap)msg.obj;
//                    appAdapter = new AppAdapter();
//                    bitmaps.add(bitmap);
//                    AppList.setAdapter(appAdapter);
                    AppList.setAdapter(appAdapter);
                    appAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "图片加载成功", Toast.LENGTH_SHORT).show();
                    break;
                case LOAD_ERROR:
                    appAdapter = (AppAdapter) msg.obj;
//                    Toast.makeText(MainActivity.this, "图片加载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });
    private List<AppInfo> allAppInfos;
    private AppAdapter appAdapter;
    private ListView AppList;
    private List<Bitmap> bitmaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  空指针异常,不能绑定到布局前面;

        /**
         * 初始化成员变量
         *  1、找到ListView的id;
         *  2、找到你要的List列表信息;
         *  3、new BaseAdapter的实现类;
         */
        AppList = findViewById(R.id.AppList);
        allAppInfos = getAllAppInfos();
        bitmaps = new ArrayList<>();
        String path = getCacheDir().getAbsolutePath() + "1.jpg";
        Bitmap bitmap2 = BitmapFactory.decodeFile(path);
        bitmaps.add(bitmap2);

        run();

        //  显示列表 给ListView设置内容;
//        appAdapter = new AppAdapter();
//        AppList.setAdapter(appAdapter);
    }

    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = tool.HttpGET("https://waimanhua.cool/api/getList.php?id=328789");
                    System.out.println(s);
                    JSONObject jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    System.out.println(code);
                    JSONArray data = jsonObject.getJSONArray("data");
                    JSONObject jsonObject1 = data.getJSONObject(0);
                    String image = jsonObject1.getString("image");
                    String[] split = image.substring(2, image.length() - 2).split("\", \"");
                    for (int i = 0; i < split.length; i++) {
                        String Https = split[i];
                        System.out.println(Https);

                        URL url = new URL(Https);
                        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                        urlConnection.connect();

                        File file = new File(getFilesDir(), i + ".jpg");
                        System.out.println(file.getAbsolutePath());
                            appAdapter = new AppAdapter();

                        if (file.exists() && file.length() > 0) {
                            System.out.println("通过缓存来取出图片.");
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            bitmaps.add(bitmap);

                            Message msg = Message.obtain();
                            msg.what = LOAD_Info;
                            msg.obj = appAdapter;
                            mHandler.sendMessage(msg);

                        } else {
//                        String path = getCacheDir().getAbsolutePath() + "1.jpg";
                            InputStream inputStream = urlConnection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            Bitmap bitmap1 = BitmapFactory.decodeFile(file.getAbsolutePath());
//                        bitmaps.add(bitmap);
                            System.out.println(bitmaps.size());
                            bitmaps.add(bitmap1);
                            Message msg = Message.obtain();
                            msg.what = LOAD_Info;
                            msg.obj = appAdapter;
                            mHandler.sendMessage(msg);
                        }
//                        mHandler.sendEmptyMessage(LOAD_Info);

//                        Bitmap bitmap2 = BitmapFactory.decodeFile(path);
//                        bitmaps = new ArrayList<>();
//                        bitmaps.add(bitmap2);
//                        bitmaps.add(bitmap1);

//                        Message msg = new Message();
//                        msg.what = LOAD_Info;
//                        msg.obj = appAdapter;
//                        mHandler.sendMessage(msg);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return bitmaps.size();
        }

        @Override
        public Object getItem(int position) {
            return bitmaps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        //返回带数据当前行的Item视图对象
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //1. 如果convertView是null, 加载item的布局文件
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.activity_1, null);
            }

            //2. 得到当前行数据对象
            Bitmap appInfo = bitmaps.get(position);
            final ArrayList<Bitmap> bitmap = new ArrayList<>();
            bitmap.add(appInfo);

            //3. 得到当前行需要更新的子View对象

            ImageView image = convertView.findViewById(R.id.mImage);

            //4. 给视图设置数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    
                }
            }).start();
            image.setImageBitmap(appInfo);

            return convertView;
        }
    }

    /*
     * 得到手机中所有应用信息的列表
     * AppInfo
     *  Drawable icon  图片对象
     *  String appName
     *  String packageName
     */
    protected List<AppInfo> getAllAppInfos() {

        List<AppInfo> list = new ArrayList<AppInfo>();
        // 得到应用的packgeManager
        PackageManager packageManager = getPackageManager();
        // 创建一个主界面的intent
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 得到包含应用信息的列表
        List<ResolveInfo> ResolveInfos = packageManager.queryIntentActivities(
                intent, 0);
        // 遍历
        for (ResolveInfo ri : ResolveInfos) {
            // 得到包名
            String packageName = ri.activityInfo.packageName;
            // 得到图标
            Drawable icon = ri.loadIcon(packageManager);
            // 得到应用名称
            String appName = ri.loadLabel(packageManager).toString();
            // 封装应用信息对象
            AppInfo appInfo = new AppInfo(icon, appName, packageName);
            // 添加到list
            list.add(appInfo);
        }
        return list;
    }

}
