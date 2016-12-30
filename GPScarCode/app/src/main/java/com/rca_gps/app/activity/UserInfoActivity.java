package com.rca_gps.app.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.rca_gps.app.R;
import com.rca_gps.app.adapter.CaseAdapter;
import com.rca_gps.app.amap.LocMangeAMap;
import com.rca_gps.app.application.MyApplication;
import com.rca_gps.app.model.Entity;
import com.rca_gps.app.model.HisttoryCaseInfo;
import com.rca_gps.app.model.Message;
import com.rca_gps.app.model.RegistResult;
import com.rca_gps.app.model.Template;
import com.rca_gps.app.model.TemplateChangeResult;
import com.rca_gps.app.model.TimerInfo;
import com.rca_gps.app.model.UserInfo;
import com.rca_gps.app.util.AppUrl;
import com.rca_gps.app.util.CircleImageView;
import com.rca_gps.app.util.Config;
import com.rca_gps.app.util.ExampleUtil;
import com.rca_gps.app.util.GlideCircleTransform;
import com.rca_gps.app.util.HttpXml;
import com.rca_gps.app.util.NoScrollListView;
import com.rca_gps.app.util.ShareP;
import com.rca_gps.app.util.StringUtil;
import com.rca_gps.app.util.TimeUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

import static com.rca_gps.app.util.Config.MESSAGE_RECEIVED_ACTION;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "RoadChinaGPS:";
    private CircleImageView circleImageView;
    public static boolean isForeground = false, isChanged = false;
    private MessageReceiver mMessageReceiver;
    private UserInfo userInfo;
    private TextView tv_name, tv_car_card, tv_cur_time, tv_address, tv_lng, tv_type;
    private NoScrollListView lv;
    private List<Message> messages = new ArrayList<>();
    private CaseAdapter adapter;
    private Entity entity;
    private String caseId;
    private List<Template> templates = new ArrayList<>();
    private Message message;
    private double longitude, latitude;
    private List<TimerInfo> timerInfos = new ArrayList<>();
    private String from = "";
    private LocMangeAMap mLocManage;

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.activity_user_info);
        initData();
        init();
        registerMessageReceiver();
        getUserInfo();
        getCurrentLocation();
    }

    /**
     * 获取历史案件
     */
    private void getHistoryCase() {
        String url = AppUrl.BASE_URL + AppUrl.GET_HISTORY_CASE;
        RequestParams params = new RequestParams(url);
        long time = System.currentTimeMillis();
        time = time / 1000;
        params.addBodyParameter("timestamp", time + "");
        params.addBodyParameter("token", ExampleUtil.sha1((time + Config.APP_KEY).toLowerCase()));
        params.addBodyParameter("unionId", userInfo.getUnionid());
        httpPost("history", params);
    }

    /**
     * 定位当前位置
     */
    private void getCurrentLocation() {
        mLocManage = MyApplication.getLocMangeAMap();
        mLocManage.getLocation(gotLocCallBackShort);
    }

    private void initData() {
        userInfo = new UserInfo();
        userInfo.setHeadimgurl(ShareP.getUserPic(context));
        userInfo.setUnionid(ShareP.getUserId(context));
        userInfo.setNickname(ShareP.getUserName(context));

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("from")) {
            from = intent.getStringExtra("from");
            if (from.equals("receiver")) {
                String message = intent.getExtras().getString(JPushInterface.EXTRA_MESSAGE);
                clickNotifi(message);
            }
        }
    }

    /**
     * 处理通知栏点击事件执行操作
     */
    private void clickNotifi(String msg) {
        if (msg.contains("\"msgType\":")) {
            message = JSON.parseObject(msg, Message.class);
            /**
             * {"msgType":"newService","caseId":"3e38b954-4920-45b7-a9e5-44624501170e",
             * "caseNumber":"11600037123","fileNo":"PRE161103009",
             * "dateTime":"2016-11-03 15:31:54","templateId":"d5397c76-39f3-48e5-9119-341354d14925"}
             */
            if (message.getMsgType().equals(Config.MSG_TYPE_NEW)) {
                startMsg();
            } else if (message.getMsgType().equals(Config.MSG_TYPE_FINISH)) {
                finishMsg();
            } else if (message.getMsgType().equals(Config.MSG_TYPE_CHANGE)) {
                changeMsg();
            }
        }
    }

    /**
     * 获取用户注册信息
     */
    private void getUserInfo() {
        String url = AppUrl.BASE_URL + AppUrl.REGISTER_URL;
        long time = System.currentTimeMillis();
        time = time / 1000;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("unionId", userInfo.getUnionid());
        params.addBodyParameter("jPushInstanceId", ShareP.getJpshId(UserInfoActivity.this));
        params.addBodyParameter("timestamp", time + "");
        params.addBodyParameter("token", ExampleUtil.sha1((time + Config.APP_KEY).toLowerCase()));
        httpPost("register", params);
    }

    /**
     * 初始化控件
     */
    private void init() {

        findViewById(R.id.iv_back).setVisibility(View.INVISIBLE);
        lv = (NoScrollListView) findViewById(R.id.lv);
        circleImageView = (CircleImageView) findViewById(R.id.iv_user);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_car_card = (TextView) findViewById(R.id.tv_car_card);
        tv_cur_time = (TextView) findViewById(R.id.tv_cur_time);
        tv_lng = (TextView) findViewById(R.id.tv_lng);
        tv_type = (TextView) findViewById(R.id.tv_type);
        findViewById(R.id.tv_exit).setOnClickListener(this);
        Glide.with(context).load(userInfo.getHeadimgurl()).transform(new GlideCircleTransform(context)).into(circleImageView);
        adapter = new CaseAdapter(context);
        lv.setAdapter(adapter);
    }

    /**
     * 广播注册
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    /**
     * 访问接口回掉成功处理
     *
     * @param result
     * @param type
     */
    @Override
    public void success(String result, String type) {
        super.success(result, type);
        System.out.println(TAG + result);
        if (type.equals("commit")) {

        } else if (type.equals("templateChange")) {
            TemplateChangeResult resul = JSON.parseObject(result.toString(), TemplateChangeResult.class);
            if (resul.isSuccess()) {
                isChanged = true;
                boolean hasSame = false;
                for (Iterator<Template> iterator = templates.iterator(); iterator.hasNext(); ) {
                    Template template = iterator.next();
                    if (template.getTemplateId().equals(resul.getEntity().getTemplateId())) {
                        iterator.remove();
                        hasSame = true;
                    }
                }
                if (hasSame) {
                    for (Message msg : messages) {
                        if (msg.getTemplateId().equals(message.getTemplateId())) {
                            setTimer(resul.getEntity(), msg);
                        }
                    }
                    templates.add(resul.getEntity());
                }
            } else senToa("获取新模板信息失败");
        } else if (type.equals("history")) {
            HisttoryCaseInfo histtoryCaseInfo = JSON.parseObject(result, HisttoryCaseInfo.class);
            messages = histtoryCaseInfo.getEntity().getCaseInfos();
            if (messages != null && !messages.isEmpty()) {
                adapter.setLists(messages);
                adapter.notifyDataSetChanged();
                msgSet();
                tv_type.setText("Busy");
                tv_type.setTextColor(getResources().getColor(R.color.text_busy_color));
            }
        } else {
            getHistoryCase();
            tv_cur_time.setText(TimeUtil.getCurTime(System.currentTimeMillis()) + "  " + TimeUtil.getAMorPM(System.currentTimeMillis()));
            tv_name.setText(userInfo.getNickname());
            RegistResult registResult = JSON.parseObject(result.toString(), RegistResult.class);
            entity = registResult.getEntity();
            templates = entity.getTemplates();
            tv_car_card.setText(registResult.getEntity().getPlateNo());
        }
    }

    /**
     * 访问接口失败信息处理
     *
     * @param result
     * @param type
     */
    @Override
    public void error(String result, String type) {
        super.error(result, type);
        System.out.println(TAG + result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("确定要注销当前登录的账号吗？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (timerInfos != null && !timerInfos.isEmpty())
                            finishAllTimer();
                        ShareP.removeUserId(context);
                        ShareP.removeUserName(context);
                        ShareP.removeUserPic(context);
                        Intent intent = new Intent();
                        intent.setClass(UserInfoActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("取消", null).create().show();
                break;
        }
    }

    /**
     * 结束所有上传经纬度操作
     */
    private void finishAllTimer() {
        for (TimerInfo timerInfo : timerInfos) {
            timerInfo.getTimerTask().cancel();
            timerInfo.getTimer().purge();
            timerInfo.getTimer().cancel();
        }
    }

    /**
     * 推送消息广播接收处理
     */
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String msg = intent.getStringExtra(Config.KEY_MESSAGE);
                String extras = intent.getStringExtra(Config.KEY_EXTRAS);
                if (msg.contains("\"msgType\":")) {
                    message = JSON.parseObject(msg, Message.class);
                    /**
                     * {"msgType":"newService","caseId":"3e38b954-4920-45b7-a9e5-44624501170e",
                     * "caseNumber":"11600037123","fileNo":"PRE161103009",
                     * "dateTime":"2016-11-03 15:31:54","templateId":"d5397c76-39f3-48e5-9119-341354d14925"}
                     */
                    if (message.getMsgType().equals(Config.MSG_TYPE_NEW)) {
                        startMsg();
                    } else if (message.getMsgType().equals(Config.MSG_TYPE_FINISH)) {
                        finishMsg();
                    } else if (message.getMsgType().equals(Config.MSG_TYPE_CHANGE)) {
                        changeMsg();
                    }
                }
            }
        }
    }

    /**
     * 开始推送消息处理
     */
    private void startMsg() {
        if (StringUtil.isNotBlank(caseId) && caseId.equals(message.getCaseId())) {
        } else {
            caseId = message.getCaseId();
            boolean isSame = false;
            if (messages != null && !messages.isEmpty()) {
                for (Message msg : messages) {
                    if (msg.getCaseId().equals(message.getCaseId()))
                        isSame = true;
                }
            }
            if (!isSame) {
                messages.add(message);
                adapter.setLists(messages);
                adapter.notifyDataSetChanged();
            }
            commitLng(message);
            tv_type.setText("Busy");
            tv_type.setTextColor(getResources().getColor(R.color.text_busy_color));
        }
    }

    /**
     * 结束消息推送处理
     */
    private void finishMsg() {
        finishTimer(message.getCaseId());
        List<Message> lists = new ArrayList<>();
        for (Iterator<Message> iterator = messages.iterator(); iterator.hasNext(); ) {
            Message msg = iterator.next();
            if (!message.getCaseId().equals(msg.getCaseId()))
                lists.add(msg);
            else
                iterator.remove();
        }
        adapter.setLists(lists);
        adapter.notifyDataSetChanged();
        if (lists == null || lists.isEmpty()) {
            caseId = "";
            tv_type.setText("free");
            tv_type.setTextColor(getResources().getColor(R.color.text_free_color));
        }
    }

    /**
     * 推送templateChange消息处理
     */
    private void changeMsg() {
        if (null != templates && !templates.isEmpty()) {
            for (Template template : templates) {
                if (template.getTemplateId().equals(message.getTemplateId())) {
                    if (!message.getVersionNumber().equals(template.getVersionNumber())) {
                        for (Message msg : messages) {
                            if (msg.getTemplateId().equals(message.getTemplateId())) {
                                finishTimer(msg.getCaseId());
                            }
                        }
                        long time = System.currentTimeMillis();
                        time = time / 1000;
                        String url = AppUrl.BASE_URL + AppUrl.GET_TEMPLATE_CHANGE;
                        RequestParams params = new RequestParams(url);
                        params.addBodyParameter("timestamp", time + "");
                        //message.getContractNumber() +
                        params.addBodyParameter("token", ExampleUtil.sha1((time + Config.APP_KEY).toLowerCase()));
                        params.addBodyParameter("templateId", template.getTemplateId());
                        httpPost("templateChange", params);
                    }
                }
            }
        }
    }

    /**
     * 取消对应的timer
     */
    private void finishTimer(String caseId) {
        for (Iterator<TimerInfo> iterator = timerInfos.iterator(); iterator.hasNext(); ) {
            TimerInfo timerInfo = iterator.next();
            if (caseId.equals(timerInfo.getCaseId())) {
                timerInfo.getTimerTask().cancel();
                timerInfo.getTimer().purge();
                timerInfo.getTimer().cancel();
                iterator.remove();
            }
        }
    }

    /**
     * 处理提交数据
     */
    private void commitLng(Message msg) {
        for (final Template template : templates) {
            if (template.getTemplateId().toLowerCase().equals(msg.getTemplateId().toLowerCase())) {
                setTimer(template, msg);
            }
        }
    }

    /**
     * 多条case
     */
    private void msgSet() {
        for (Message msg : messages) {
            commitLng(msg);
        }
    }

    /**
     * Timer设置
     *
     * @param template
     */
    private void setTimer(final Template template, Message msg) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (null != template) {
                    if (template.getType().equals("json"))
                        assemblyDataByJson(template);
                    else assemblyDataByXml(template);
                }
            }
        };
        timer.schedule(timerTask, 0, template.getTimmer() * 1000);
        TimerInfo timerInfo = new TimerInfo();
        timerInfo.setCaseId(msg.getCaseId());
        timerInfo.setTimer(timer);
        timerInfo.setTimerTask(timerTask);
        timerInfos.add(timerInfo);
    }

    /**
     * 提交经纬度xml
     */
    private void assemblyDataByXml(Template template) {
        Message msg = null;
        final String url = template.getPostUrl();
        String json = template.getFormat();
        long time = System.currentTimeMillis();
        for (Message m : messages) {
            if (m.getTemplateId().equals(template.getTemplateId())) {
                msg = m;
            }
        }
        if (null != msg) {
            final String fromat = json.replaceAll("#FILENO#", msg.getFileNo())
                    .replaceAll("#TIME#", TimeUtil.getCurTime(time))
                    .replaceAll("#TIMEUTC#", TimeUtil.getTime(time))
                    .replaceAll("#TIMELOCAL#", TimeUtil.getCuTime(time))
                    .replaceAll("#X#", longitude + "")
                    .replaceAll("#Y#", latitude + "")
                    .replaceAll("#CASEID#", msg.getCaseId());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = HttpXml.httpXml(fromat, url);
                    System.out.println(TAG + result);
                }
            }).start();
        }
    }

    /**
     * 提交经纬度json
     */

    private void assemblyDataByJson(Template template) {
        Message msg = null;
        String url = template.getPostUrl();
        String json = template.getFormat();
        long tim = System.currentTimeMillis();
        long time = tim / 1000;
        if (null != template) {
            for (Message m : messages) {
                if (m.getTemplateId().equals(template.getTemplateId())) {
                    msg = m;
                }
            }
            if (null != msg) {
                String fromat = json.replaceAll("#FILENO#", msg.getFileNo())
                        .replaceAll("#TIME#", time + "")
                        .replaceAll("#TIMEUTC#", TimeUtil.getTime(tim))
                        .replaceAll("#TIMELOCAL#", TimeUtil.getCuTime(tim))
                        .replaceAll("#X#", longitude + "")
                        .replaceAll("#Y#", latitude + "")
                        .replaceAll("#CASEID#", msg.getCaseId());
                final RequestParams params = new RequestParams(url);
                Map map = JSON.parseObject(fromat, Map.class);
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (java.util.Map.Entry) it.next();
                    params.addBodyParameter(entry.getKey().toString(), entry.getValue().toString());
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        httpPost("commit", params);
                    }
                }).start();
            }
        }
    }

    /**
     * 获取经纬度回掉
     */
    private LocMangeAMap.GotLocCallBackShort gotLocCallBackShort = new LocMangeAMap.GotLocCallBackShort() {
        @Override
        public void onGotLocShort(double lat, double lng, AMapLocation amapLocation) {
            tv_address.setText(amapLocation.getAddress());
            latitude = lat;
            longitude = lng;
            tv_lng.setText(latitude + "  " + longitude);
        }

        @Override
        public void onGotLocShortFailed(String location) {
            senToa("定位失败，请前往开阔地带！");
        }
    };


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        if (mLocManage != null) {
            mLocManage.stopLocation();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //启动一个意图,回到桌面
            Intent backHome = new Intent(Intent.ACTION_MAIN);
            backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            backHome.addCategory(Intent.CATEGORY_HOME);
            startActivity(backHome);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
