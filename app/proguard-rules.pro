# ----------------------------------------------------------------------------
# 混淆的压缩比例，0-7
-optimizationpasses 5
# 指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers
# 指定混淆是采用的算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
# 忽略警告
#-ignorewarning
# ----------------------------------------------------------------------------
# 不混淆泛型
#-keepattributes Signature
# 抛出异常时保留代码行号
#-keepattributes SourceFile,LineNumberTable
# 保持 Parcelable 不被混淆
#-keep class * implements android.os.Parcelable {
#    public static final android.os.Parcelable$Creator *;
#}
# 保持 Serializable 不被混淆 并且enum 类也不被混淆
#-keep class * implements java.io.Serializable {*;}
#----------------------------------------------------------------------------
# GreenDao @link {http://greenrobot.org/greendao/documentation/updating-to-greendao-3-and-annotations/}
#-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
#    public static java.lang.String TABLENAME;
#}
#-keep class **$Properties
#-dontwarn org.greenrobot.greendao.database.**
#-dontwarn rx.**
#----------------------------------------------------------------------------
#----------------------------------------------------------------------------
