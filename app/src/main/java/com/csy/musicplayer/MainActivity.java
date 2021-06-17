package com.csy.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends FragmentActivity {

    private ViewPager2 viewPager;

    private FragmentStateAdapter pagerAdapter;

    String filepath = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewpager2);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1,false);    //默认界面为第二页(中间页)
        requestForPermission();
        MediaScanner ms = new MediaScanner(this);
        File file = new File(filepath);
        ms.scanFile(file,"");
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 1) {   //第二页可响应返回按钮，关闭应用
            super.onBackPressed();
        }
        else if (viewPager.getCurrentItem() == 2){  //第三页响应返回按钮，返回第二页
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
        else {  //第一页响应返回按钮，返回第二页
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public final int EXTERNAL_REQUEST = 138;

    public boolean requestForPermission() {

        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }

}