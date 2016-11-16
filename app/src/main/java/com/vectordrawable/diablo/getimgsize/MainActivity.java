package com.vectordrawable.diablo.getimgsize;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.tv_img_info);
        getBitmapSize(textView);
    }

    private void getBitmapSize(TextView textView) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 设置成了true,不占用内存，只获取bitmap宽高
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.achive_1, options);
        // 源图片的高度和宽度
        int height = options.outHeight;
        int width = options.outWidth;

        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap realBitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.achive_1, options); // 载入一个稍大的缩略图
        String configType = "";
        Bitmap.Config config = realBitmap.getConfig();
        if (config == Bitmap.Config.ARGB_8888) {
            configType = "ARGB_8888";
        } else if (config == Bitmap.Config.RGB_565) {
            configType = "RGB_565";
        } else if (config == Bitmap.Config.ARGB_4444) {
            configType = "ARGB_4444";
        } else if (config == Bitmap.Config.ALPHA_8) {
            configType = "ALPHA_8";
        }
        int size = (realBitmap.getRowBytes() * realBitmap.getHeight()) / 1024;
        int realHeight = realBitmap.getHeight();
        int realWidth = realBitmap.getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        String density = "\n屏幕密度：" + dm.density;
        textView.setText("原图片高度：" + height +
                "\n原图片宽度：" + width +
                "\n图片真实(缩放后)高度：" + realHeight +
                "\n图片真实(缩放后)宽度：" + realWidth +
                "\n图片格式：" + configType +
                "\n图片内存大小：" + size + "K"
                + density);
    }
}
