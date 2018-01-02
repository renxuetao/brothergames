-optimizationpasses 5
-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

 -keepclasseswithmembernames class * {
     native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
    void onEvent*(***);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

#android-support-v4.jar
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

## --------------------------litepal混淆配置---------------------------
#-libraryjars libs/litepal-1.2.1.jar
-dontwarn org.litepal.*
-keep class org.litepal.** { *; }
-keep enum org.litepal.**
-keep interface org.litepal.** { *; }
-keep public class * extends org.litepal.**
-keepattributes *Annotation*
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * extends org.litepal.crud.DataSupport{*;}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    public <fields>;
}

-keepattributes Signature


# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }


# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }


##---------------End: proguard configuration for Gson  ----------

#百度
#-libraryjars libs/BaiduLBS_Android.jar
#-libraryjars libs/armeabi/liblocSDK5.so
-keep class com.baidu.** { *; }

## nineoldandroids-2.4.0.jar
#-libraryjars libs/nineoldandroids-2.4.0.jar
-keep public class com.nineoldandroids.** {*;}

-keep public class com.activity.chat.record.** {*;}

## --------------------------eventbus混淆配置---------------------------
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

#talkingdata
#-libraryjars libs/TalkingDataAnalytics_V2.0.4.jar
-dontwarn com.tendcloud.tenddata.**
-keep public class com.tendcloud.tenddata.** { public protected *;}
#adtracking
#-libraryjars libs/TalkingData_AdTracking_SDK_Android.jar
-dontwarn com.tendcloud.**
-keep public class com.tendcloud.** { public protected *;}

#common-library
-keep class com.jcraft.jzlib.** { *; }
-keep class net.** { *; }

-dontwarn org.dom4j.**
-dontwarn okio.**

-dontwarn com.talkingdata.sdk.**
-keep class com.talkingdata.sdk.** {*;}
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.** {  public protected *;}

-keep public class com.wali.g.sdk.ui.actlayout.ViewAliPayWeb$PayObject{*;}

-keepclasseswithmembers class * {
    public <init>(...);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep public class com.xiaomi.gamecenter.sdk.entry.* {
			    *;
}

##---------------Begin: proguard configuration for Gson  ----------
-dontwarn com.google.gson.**
-keep class com.google.gson.**{*; }

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.** {*;}

-keep public class * implements java.io.Serializable {*;}
-keep public class com.zhangyue.game.statistics.bean.** { *;}

-keep public class com.fragment.NewRankingFragment { *; }
-keepclasseswithmembernames class com.fragment.NewRankingFragment {
*;
}
-keep class android.support.design.widget.TabLayout {*;}

 # removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes

-keep class sun.misc.Unsafe { *; }

-keep class com.google.gson.** { *; }

# Application classes that will be serialized/deserialized over Gson

#-keep class com.google.gson.examples.android.model.** { *; }

-keep class com.googlebilling.util.** {*;}  #这句非常重要，主要是滤掉XXXX.entity包下的所有.class文件不进行混淆编译

#不要混淆xUtils中的注解类型，添加混淆配置：
-keep class * extends java.lang.annotation.Annotation { *; }

-dontwarn com.google.**

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}

#share mobe
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*