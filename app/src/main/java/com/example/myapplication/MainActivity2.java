package com.example.myapplication;
/**
 * @AlertDialog AlertDialog.Builder setPositiveButton() setSingleChoiseItem();
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    private EditText value;
    private SharedPreferences sp;
    private EditText kkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //  空指针异常,不能绑定到布局前面;
        kkey = findViewById(R.id.key);
        value = findViewById(R.id.value);
        //  1、得到sp对象;
        sp = getSharedPreferences("SP", MODE_PRIVATE);
    }

    public void Commit(View view) {
        //  2、得到editer对象;
        SharedPreferences.Editor edit = sp.edit();
        //  3、设置key和value的值;
        String key = this.kkey.getText().toString();
        String value = this.value.getText().toString();
        //4、使用editer保存key、value,要提交(commit)才算保存;
        edit.putString(key, value).commit();
        //  提示;
        Toast.makeText(this, "SP数据保存成功.", Toast.LENGTH_SHORT).show();
    }

    public void Get(View view) {
        //  1、得到输入的key;
        String key = this.kkey.getText().toString();
        //  2、根据key得到对应的value;
        String string = sp.getString(key, null);
        //  3、显示;
        if (string != null) {
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "key未找到对应的value", Toast.LENGTH_SHORT).show();
        }
    }

}
