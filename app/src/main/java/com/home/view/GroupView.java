package com.home.view;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleApplication;
import com.home.db.GroupDevice;
import com.home.net.NetConnectBle;
import com.ledlamp.R;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class GroupView {
    private int connect;
    private Context context;
    private ArrayList<GroupDevice> groupDevices;
    private String groupName;
    private View groupView;
    private ImageView imageViewControll;
    private boolean isAllon;
    private SlideSwitch slideSwitch;
    private TextView textViewTotal;
    private ToggleButton toggleButton;
    private View viewBottomLine;
    private View viewTopLine;

    public GroupView(Context context, String str, boolean z) {
        this.context = context;
        this.isAllon = z;
        this.groupName = str;
        View inflate = View.inflate(context, R.layout.item_my_group, null);
        this.groupView = inflate;
        inflate.setTag(str);
        this.imageViewControll = (ImageView) this.groupView.findViewById(R.id.imageViewControll);
        this.viewTopLine = this.groupView.findViewById(R.id.viewTopLine);
        this.viewBottomLine = this.groupView.findViewById(R.id.viewBottomLine);
        TextView textView = (TextView) this.groupView.findViewById(R.id.textViewGroupName);
        this.textViewTotal = (TextView) this.groupView.findViewById(R.id.textViewTotal);
        this.toggleButton = (ToggleButton) this.groupView.findViewById(R.id.toggleButton);
        if (!z) {
            setCloseDisable();
        }
        textView.setText(str);
        this.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.home.view.GroupView.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                if (z2) {
                    NetConnectBle.getInstanceByGroup(GroupView.this.groupName).turnOn(MainActivity_BLE.getSceneBean(), false, false, false);
                } else {
                    NetConnectBle.getInstanceByGroup(GroupView.this.groupName).turnOff(MainActivity_BLE.getSceneBean(), false, false, false);
                }
                GroupView.this.juedg();
            }
        });
        SlideSwitch slideSwitch = (SlideSwitch) this.groupView.findViewById(R.id.slideSwitch);
        this.slideSwitch = slideSwitch;
        slideSwitch.setState(false);
    }

    public ToggleButton getToggleButton() {
        return this.toggleButton;
    }

    public void hideTopLine() {
        this.viewTopLine.setVisibility(0);
    }

    public void hideBottomLine() {
        this.viewBottomLine.setVisibility(0);
    }

    public boolean isTurnOn() {
        return this.toggleButton.isChecked();
    }

    public int getConnect() {
        return this.connect;
    }

    public ImageView getImageViewControll() {
        return this.imageViewControll;
    }

    public void setImageViewControll(ImageView imageView) {
        this.imageViewControll = imageView;
    }

    public ArrayList<GroupDevice> getGroupDevices() {
        return this.groupDevices;
    }

    public void setGroupDevices(ArrayList<GroupDevice> arrayList) {
        this.groupDevices = arrayList;
        setConnectCount(0);
    }

    public void setConnect(String str) {
        this.textViewTotal.setText(LedBleApplication.getApp().getResources().getString(R.string.connect_count, str));
    }

    public void setConnectCount(int i) {
        this.connect = i;
        this.textViewTotal.setText(LedBleApplication.getApp().getResources().getString(R.string.connect_count, String.valueOf(i)));
        juedg();
    }

    public void turnOn() {
        this.toggleButton.setChecked(true);
        NetConnectBle.getInstanceByGroup(this.groupName).turnOn(MainActivity_BLE.getSceneBean(), false, false, false);
    }

    public void juedg() {
        if (this.toggleButton.isChecked() && this.connect > 0) {
            this.imageViewControll.setVisibility(0);
        } else {
            this.imageViewControll.setVisibility(4);
        }
    }

    public void setCloseDisable() {
        turnOff();
        this.toggleButton.setChecked(false);
    }

    public void turnOff() {
        this.toggleButton.setChecked(false);
        NetConnectBle.getInstanceByGroup(this.groupName).turnOff(MainActivity_BLE.getSceneBean(), false, false, false);
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String str) {
        this.groupName = str;
    }

    public View getGroupView() {
        return this.groupView;
    }

    public void setGroupView(View view) {
        this.groupView = view;
    }

    public SlideSwitch getSlideSwitch() {
        return this.slideSwitch;
    }

    public void setSlideSwitch(SlideSwitch slideSwitch) {
        this.slideSwitch = slideSwitch;
    }
}
