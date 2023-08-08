package com.dld.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/* loaded from: classes.dex */
public class SelectedItemState extends View.BaseSavedState {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<SelectedItemState>() { // from class: com.dld.view.SelectedItemState.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SelectedItemState createFromParcel(Parcel parcel) {
            return new SelectedItemState(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SelectedItemState[] newArray(int i) {
            return new SelectedItemState[i];
        }
    };
    private int selectedItem;

    public int getSelectedItem() {
        return this.selectedItem;
    }

    public void setSelectedItem(int i) {
        this.selectedItem = i;
    }

    SelectedItemState(Parcel parcel) {
        super(parcel);
        this.selectedItem = parcel.readInt();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SelectedItemState(Parcelable parcelable) {
        super(parcelable);
    }

    @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.selectedItem);
    }
}
