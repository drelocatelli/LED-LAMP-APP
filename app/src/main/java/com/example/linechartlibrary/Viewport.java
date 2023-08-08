package com.example.linechartlibrary;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class Viewport implements Parcelable {
    public static final Parcelable.Creator<Viewport> CREATOR = new Parcelable.Creator<Viewport>() { // from class: com.example.linechartlibrary.Viewport.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Viewport createFromParcel(Parcel parcel) {
            Viewport viewport = new Viewport();
            viewport.readFromParcel(parcel);
            return viewport;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Viewport[] newArray(int i) {
            return new Viewport[i];
        }
    };
    public float bottom;
    public float left;
    public float right;

    /* renamed from: top  reason: collision with root package name */
    public float f12top;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Viewport() {
    }

    public Viewport(float f, float f2, float f3, float f4) {
        this.left = f;
        this.f12top = f2;
        this.right = f3;
        this.bottom = f4;
    }

    public Viewport(Viewport viewport) {
        if (viewport == null) {
            this.bottom = 0.0f;
            this.right = 0.0f;
            this.f12top = 0.0f;
            this.left = 0.0f;
            return;
        }
        this.left = viewport.left;
        this.f12top = viewport.f12top;
        this.right = viewport.right;
        this.bottom = viewport.bottom;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            Viewport viewport = (Viewport) obj;
            return Float.floatToIntBits(this.bottom) == Float.floatToIntBits(viewport.bottom) && Float.floatToIntBits(this.left) == Float.floatToIntBits(viewport.left) && Float.floatToIntBits(this.right) == Float.floatToIntBits(viewport.right) && Float.floatToIntBits(this.f12top) == Float.floatToIntBits(viewport.f12top);
        }
        return false;
    }

    public final boolean isEmpty() {
        return this.left >= this.right || this.bottom >= this.f12top;
    }

    public void setEmpty() {
        this.bottom = 0.0f;
        this.f12top = 0.0f;
        this.right = 0.0f;
        this.left = 0.0f;
    }

    public final float width() {
        return this.right - this.left;
    }

    public final float height() {
        return this.f12top - this.bottom;
    }

    public final float centerX() {
        return (this.left + this.right) * 0.5f;
    }

    public final float centerY() {
        return (this.f12top + this.bottom) * 0.5f;
    }

    public void set(float f, float f2, float f3, float f4) {
        this.left = f;
        this.f12top = f2;
        this.right = f3;
        this.bottom = f4;
    }

    public void set(Viewport viewport) {
        this.left = viewport.left;
        this.f12top = viewport.f12top;
        this.right = viewport.right;
        this.bottom = viewport.bottom;
    }

    public void offset(float f, float f2) {
        this.left += f;
        this.f12top += f2;
        this.right += f;
        this.bottom += f2;
    }

    public void offsetTo(float f, float f2) {
        this.right += f - this.left;
        this.bottom += f2 - this.f12top;
        this.left = f;
        this.f12top = f2;
    }

    public void inset(float f, float f2) {
        this.left += f;
        this.f12top -= f2;
        this.right -= f;
        this.bottom += f2;
    }

    public boolean contains(float f, float f2) {
        float f3 = this.left;
        float f4 = this.right;
        if (f3 < f4) {
            float f5 = this.bottom;
            float f6 = this.f12top;
            if (f5 < f6 && f >= f3 && f < f4 && f2 >= f5 && f2 < f6) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(float f, float f2, float f3, float f4) {
        float f5 = this.left;
        float f6 = this.right;
        if (f5 < f6) {
            float f7 = this.bottom;
            float f8 = this.f12top;
            if (f7 < f8 && f5 <= f && f8 >= f2 && f6 >= f3 && f7 <= f4) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Viewport viewport) {
        float f = this.left;
        float f2 = this.right;
        if (f < f2) {
            float f3 = this.bottom;
            float f4 = this.f12top;
            if (f3 < f4 && f <= viewport.left && f4 >= viewport.f12top && f2 >= viewport.right && f3 <= viewport.bottom) {
                return true;
            }
        }
        return false;
    }

    public void union(float f, float f2, float f3, float f4) {
        if (f >= f3 || f4 >= f2) {
            return;
        }
        float f5 = this.left;
        float f6 = this.right;
        if (f5 < f6) {
            float f7 = this.bottom;
            float f8 = this.f12top;
            if (f7 < f8) {
                if (f5 > f) {
                    this.left = f;
                }
                if (f8 < f2) {
                    this.f12top = f2;
                }
                if (f6 < f3) {
                    this.right = f3;
                }
                if (f7 > f4) {
                    this.bottom = f4;
                    return;
                }
                return;
            }
        }
        this.left = f;
        this.f12top = f2;
        this.right = f3;
        this.bottom = f4;
    }

    public void union(Viewport viewport) {
        union(viewport.left, viewport.f12top, viewport.right, viewport.bottom);
    }

    public boolean intersect(float f, float f2, float f3, float f4) {
        float f5 = this.left;
        if (f5 < f3) {
            float f6 = this.right;
            if (f < f6) {
                float f7 = this.bottom;
                if (f7 < f2) {
                    float f8 = this.f12top;
                    if (f4 < f8) {
                        if (f5 < f) {
                            this.left = f;
                        }
                        if (f8 > f2) {
                            this.f12top = f2;
                        }
                        if (f6 > f3) {
                            this.right = f3;
                        }
                        if (f7 < f4) {
                            this.bottom = f4;
                            return true;
                        }
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public boolean intersect(Viewport viewport) {
        return intersect(viewport.left, viewport.f12top, viewport.right, viewport.bottom);
    }

    public String toString() {
        return "Viewport [left=" + this.left + ", top=" + this.f12top + ", right=" + this.right + ", bottom=" + this.bottom + "]";
    }

    public int hashCode() {
        return ((((((Float.floatToIntBits(this.bottom) + 31) * 31) + Float.floatToIntBits(this.left)) * 31) + Float.floatToIntBits(this.right)) * 31) + Float.floatToIntBits(this.f12top);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(this.left);
        parcel.writeFloat(this.f12top);
        parcel.writeFloat(this.right);
        parcel.writeFloat(this.bottom);
    }

    public void readFromParcel(Parcel parcel) {
        this.left = parcel.readFloat();
        this.f12top = parcel.readFloat();
        this.right = parcel.readFloat();
        this.bottom = parcel.readFloat();
    }
}
