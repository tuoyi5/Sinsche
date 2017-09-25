-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes *Annotation*,SourceFile,LineNumberTable

#-ignorewarnings

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

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
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

##---------------Begin: proguard configuration for fastjson  ----------

#-keepnames class * implements java.io.Serializable
-keep public class * implements java.io.Serializable {
        public *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-dontwarn android.support.**
#-dontwarn com.alibaba.fastjson.**

-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses

#-libraryjars ./OnyxAndroidSDK/libs/fastjson-1.1.34.android.jar
-keepattributes Signature
-keepattributes *Annotation*

-keepattributes Signature,*Annotation*,EnclosingMethod

##---------------End: proguard configuration for fastjson  ----------

-keep class com.raizlabs.android.dbflow.** { *; }
-dontwarn com.raizlabs

-keep class org.apache.** { *; }
-dontwarn org.apache.**

-keep class com.avos.** { *; }
-dontwarn com.avos.**

-keep class com.alibaba.fastjson.** { *; }
-dontwarn com.alibaba.fastjson.**

-keep class com.squareup.** { *; }
-dontwarn com.squareup.**

-keep class com.gengsheng.** { *; }
-dontwarn com.gengsheng.**

-keep class com.raizlabs.** { *; }
-dontwarn com.raizlabs.**

-keep class com.google.** { *; }
-dontwarn com.google.**

-dontnote com.google.**
-dontnote com.android.**
-dontnote com.avos.**
-dontnote com.alibaba.fastjson.**
-dontnote android.support.**
-dontnote com.onyx.android.sdk.device.**
-dontnote com.onyx.android.sdk.ui.data.SystemMenuFactory**

-keepnames class com.squareup.** 

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn retrofit2.**

-keepnames class org.greenrobot.eventbus.** { *; }
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keep class * extends com.raizlabs.android.dbflow.config.DatabaseHolder { *; }
-keep class * extends com.raizlabs.android.dbflow.converter.TypeConverter { *; }
-keep class * extends com.raizlabs.android.dbflow.structure.BaseModel { *; }

-keepnames class retrofit2.http.** { *; }

-keepclasseswithmembernames class com.onyx.cloud.model.** { *; }

-keep class com.qq408543103.basenet.* {*;}