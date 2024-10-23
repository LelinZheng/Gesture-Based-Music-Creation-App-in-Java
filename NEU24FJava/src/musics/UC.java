package musics;

import java.awt.*;

public class UC {
    public static final int screenWidth = 1000;
    public static final int screenHeight = 750;
    public static final int inkBufferMax = 200;
    public static Color inkColor = Color.BLACK;

    public static final int normSampleSize = 25;
    public static final int normCoordMax = 1000;
    public static final int noMatchDist = 700_000;

    public static final int dotThreshold = 5;
    public static String shapeDatabaseFileName = "shapeDB.dat";
    public static final int noBid = 10000;
    public static final int minStaffGap = 40;
    public static final int minSysGap = 40;
    public static final int barToMarginSnap = 20;
}
