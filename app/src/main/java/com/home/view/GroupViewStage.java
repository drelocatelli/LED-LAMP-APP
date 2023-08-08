package com.home.view;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleApplication;
import com.home.db.stage.GroupDeviceStage;
import com.ledlamp.R;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class GroupViewStage {
    private int connect;
    private TextView gnameTextView;
    private ArrayList<GroupDeviceStage> groupDevices;
    private String groupName;
    private View groupView;
    private ImageView imageViewControll;
    private ToggleButton imageViewLight;
    private boolean isAllon;
    private SlideSwitch slideSwitch;
    private TextView textViewMode;
    private ToggleButton toggleButton;
    private View viewBottomLine;
    private View viewTopLine;

    public GroupViewStage(Context context, String str, boolean z) {
        this.isAllon = z;
        this.groupName = str;
        View inflate = View.inflate(context, R.layout.item_my_group_wifi, null);
        this.groupView = inflate;
        inflate.setTag(str);
        this.imageViewControll = (ImageView) this.groupView.findViewById(R.id.imageViewControll);
        this.viewTopLine = this.groupView.findViewById(R.id.viewTopLine);
        this.viewBottomLine = this.groupView.findViewById(R.id.viewBottomLine);
        this.gnameTextView = (TextView) this.groupView.findViewById(R.id.textViewGroupName);
        this.textViewMode = (TextView) this.groupView.findViewById(R.id.textViewTotal);
        this.toggleButton = (ToggleButton) this.groupView.findViewById(R.id.toggleButton);
        if (!z) {
            setCloseDisable();
        }
        this.gnameTextView.setText(str);
        this.imageViewLight = (ToggleButton) this.groupView.findViewById(R.id.imageViewLight);
        this.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.home.view.GroupViewStage.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                if (z2) {
                    if (MainActivity_BLE.getMainActivity() != null) {
                        MainActivity_BLE.getMainActivity().close(false, false, false);
                    }
                } else if (MainActivity_BLE.getMainActivity() != null) {
                    MainActivity_BLE.getMainActivity().close(false, false, false);
                }
                GroupViewStage.this.juedg();
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

    public ArrayList<GroupDeviceStage> getGroupDevices() {
        return this.groupDevices;
    }

    public void setGroupDevices(ArrayList<GroupDeviceStage> arrayList) {
        this.groupDevices = arrayList;
        setConnectCount(0);
    }

    public void setConnect(String str) {
        this.textViewMode.setText(LedBleApplication.getApp().getResources().getString(R.string.connect_count, str));
    }

    public void setConnectCount(int i) {
        this.connect = i;
        this.textViewMode.setText(LedBleApplication.getApp().getResources().getString(R.string.connect_count, Integer.valueOf(i)));
        juedg();
    }

    public void turnOn() {
        this.toggleButton.setChecked(true);
        if (MainActivity_BLE.getMainActivity() != null) {
            MainActivity_BLE.getMainActivity().close(false, false, false);
        }
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
        if (MainActivity_BLE.getMainActivity() != null) {
            MainActivity_BLE.getMainActivity().close(false, false, false);
        }
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String str) {
        this.groupName = str;
        this.gnameTextView.setText(str);
    }

    public void setWeekName(String str) {
        this.textViewMode.setText(str);
    }

    public void setModeName(String str) {
        this.textViewMode.setText(str);
    }

    public View getGroupView() {
        return this.groupView;
    }

    public void setGroupView(View view) {
        this.groupView = view;
    }

    public ToggleButton getImageViewLight() {
        return this.imageViewLight;
    }

    public SlideSwitch getSlideSwitch() {
        return this.slideSwitch;
    }

    public void setSlideSwitch(SlideSwitch slideSwitch) {
        this.slideSwitch = slideSwitch;
    }
}
