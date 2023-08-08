package cn.jzvd;

/* loaded from: classes.dex */
public class JZVideoPlayerManager {
    public static JZVideoPlayer FIRST_FLOOR_JZVD;
    public static JZVideoPlayer SECOND_FLOOR_JZVD;

    public static JZVideoPlayer getFirstFloor() {
        return FIRST_FLOOR_JZVD;
    }

    public static void setFirstFloor(JZVideoPlayer jZVideoPlayer) {
        FIRST_FLOOR_JZVD = jZVideoPlayer;
    }

    public static JZVideoPlayer getSecondFloor() {
        return SECOND_FLOOR_JZVD;
    }

    public static void setSecondFloor(JZVideoPlayer jZVideoPlayer) {
        SECOND_FLOOR_JZVD = jZVideoPlayer;
    }

    public static JZVideoPlayer getCurrentJzvd() {
        if (getSecondFloor() != null) {
            return getSecondFloor();
        }
        return getFirstFloor();
    }

    public static void completeAll() {
        JZVideoPlayer jZVideoPlayer = SECOND_FLOOR_JZVD;
        if (jZVideoPlayer != null) {
            jZVideoPlayer.onCompletion();
            SECOND_FLOOR_JZVD = null;
        }
        JZVideoPlayer jZVideoPlayer2 = FIRST_FLOOR_JZVD;
        if (jZVideoPlayer2 != null) {
            jZVideoPlayer2.onCompletion();
            FIRST_FLOOR_JZVD = null;
        }
    }
}
