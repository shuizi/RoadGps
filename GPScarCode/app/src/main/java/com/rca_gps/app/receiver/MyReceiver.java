package com.rca_gps.app.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.rca_gps.app.R;
import com.rca_gps.app.activity.MainActivity;
import com.rca_gps.app.activity.UserInfoActivity;
import com.rca_gps.app.model.Message;
import com.rca_gps.app.util.Config;
import com.rca_gps.app.util.ExampleUtil;
import com.rca_gps.app.util.ShareP;
import com.rca_gps.app.util.StringUtil;
import com.rca_gps.app.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            ShareP.saveJpshId(context, regId);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            if (TimeUtil.isRunningForeground(context))
                processCustomMessage(context, bundle);
            else
                noti(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            //打开自定义的Activity
//            Intent i = new Intent(context, MainActivity.class);
//            i.putExtras(bundle);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//        if (UserInfoActivity.isForeground) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Intent msgIntent = new Intent(Config.MESSAGE_RECEIVED_ACTION);
        msgIntent.putExtra(Config.KEY_MESSAGE, message);
        if (!ExampleUtil.isEmpty(extras)) {
            try {
                JSONObject extraJson = new JSONObject(extras);
                if (null != extraJson && extraJson.length() > 0) {
                    msgIntent.putExtra(Config.KEY_EXTRAS, extras);
                }
            } catch (JSONException e) {
            }
        }
        context.sendBroadcast(msgIntent);
    }

    //    }
    private void noti(Context context, Bundle bundle) {
        /**新建通知管理器**/
        NotificationManager mNotifyMgr = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
/**设置通知栏点击跳转的activity**/
        Intent resultIntent;
//        resultIntent = context.getPackageManager().getLaunchIntentForPackage("com.rca_gps.app");
        if (StringUtil.isBlank(ShareP.getUserId(context))) {
            resultIntent = new Intent(context, MainActivity.class);
        } else {
            resultIntent = new Intent(context, UserInfoActivity.class);
            resultIntent.putExtra("from", "receiver");
            resultIntent.putExtras(bundle);
        }
/**从非activity（如broadcast/server等）跳转一定要指定flag**/
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 1, //替换为int类型的值
                resultIntent,
                PendingIntent.FLAG_ONE_SHOT);
        String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        Message message = JSON.parseObject(msg, Message.class);
        String title, content;
        if (message.getMsgType().equals(Config.MSG_TYPE_NEW)) {
            title = "路华救援服务商平台";
            content = "当前救援案件需要获取您的实时位置，请点击此消息打开上传工具";
        } else if (message.getMsgType().equals(Config.MSG_TYPE_FINISH)) {
            title = "路华救援服务商平台";
            content = "救援案件已完成，点击此消息将停止获取您的实时位置";
        } else {
            title = "路华救援服务商平台";
            content = "救援案件模板已更改，点击此消息获取最新模板信息";
        }
        /**新建builder，可以设置通知栏样式**/
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.app_log)
                        .setContentTitle(title) //string 标题
                        .setContentText(content)//string 内容
                        .setAutoCancel(true);
        builder.setContentIntent(resultPendingIntent);
        //发送通知
        mNotifyMgr.notify(1, builder.build());
    }
}
