### 图片内存问题

#### 本地图片内存问题
本地图片一定要将对应分辨率的图片放到对应的目录下，当图片放置在不同drawable文件夹中，且只有这一张图片时，运行设备会根据自身的屏幕密度，对图片进行放缩，放缩比例符合前面图上的规则，不同目录下的对应放大倍数(drawable默认1X)：

* mdpi     1X
* hdpi     1.5X
* xhdpi    2X
* xxhdpi   3X
* xxxhdpi  4x

* density	 1	1.5 2	3	3.5	 4

* densityDpi	160	240	320	480	560	640

* 在使用离线图片时首先注意，切对应比例的图并将图片放到对应的比例文件目录下，防止将大比例的图片放到小比例的目录下导致图片被指数放大导致OOM，或者将小比例图片放到大比例文件夹下导致图片失真。
* 图片的大小根据实际情况来，显示200*200的图不要使用比比例大的图片，用了也要做压缩，防止内存泄漏。

#### 网络图片内存问题
网络图片也会导致内存泄漏，后端可能为了兼容更多客户端，配置的图片的尺寸和质量会很高，我们如果直接把网上下载的图片拿来直接用可能会导致OOM，因此网络下载的图片也要根据具体显示的view的大小来对图片进行压缩。

#### 图片压缩的问题
图片压缩是按照大小来实现质量压缩。

#### 不要将Button的背景设置为selector
在布局文件和代码中，都可以为Button设置background为selector，这样方便实现按钮的正反选效果，但实际跟踪发现，如果是将Button的背景设置为selector，在初始化Button的时候会将正反选图片都加载在内存中（具体可以查看Android源码，在类Drawable.java的createFromXmlInner方法中对图片进行解析，最终调用Drawable的inflate方法），相当于一个按钮占用了两张相同大小图片所使用的内存，如果一个界面上按钮很多或者是按钮很大，光是按钮占用的内存就会很大，可以通过在布局文件中给按钮只设置正常状态下的背景图片，然后在代码中监听按钮的点击状态，当按下按钮时为按钮设置反选效果的图片，抬起时重新设置为正常状态下的背景

#### 尽量少用AnimationDrawable，如果必须要可以自定义图片切换器代替AnimationDrawable
AnimationDrawable也是一个耗内存大户，图片帧数越多耗内存越大，具体可以查看AnimationDrawable的源码，在AnimationDrawable实例化的时候，Drawable的createFromXmlInner方法会调用AnimationDrawable的inflate方法，该方法里面有一个while循环去一次性将所有帧都读取出来，也就是在初始化的时候就将所有的帧读在内存中了，有多少张图片，它就要消耗对应大小的内存。

#### 尽量使用vector drawable

#### 在服务器段配置相对较小的webp格式

#### 其他方式优化
* 尽量将Activity中的小图片和背景合并，一张小图片既浪费布局的时间，又平白地增加了内存占用；
* 对于在需要时才显示的图片或者布局，可以使用ViewStub标签，通过sdk/tools目录下的hierarchyviewer.bat查看布局文件会发现，使用viewstub标签的组件几乎不消耗布局的时间，在代码中当需要显示时再去实例化有助于提高Activity的布局效率和节省Activity消耗的内存。
* 尽量不要在Activity的主题中为Activity设置默认的背景图片，这样会导致Activity占用的内存翻倍：

### Bitmap 相关

#### 不同格式的图片每像素所占字节数
* Alpha_8: 1个字节

* RGB_565: 2个字节

* ARGB_4444: 2个字节

* ARGB_8888: 4个字节

#### 生成Bitmap所占大小(size)
公式: 宽 * 高 * 每个像素所占字节

size = width * height * bytes(格式1,2,3,4)*放大缩小倍数


