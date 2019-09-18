package store.vxdesign.cg.core.cut_polygon.utilities;

public enum OS {
    WINDOWS("win", 0, 28, 50), MAC("mac", 125, 36, 100);

    private final String name;
    private final int heightOffset;
    private final int fontSize;
    private final float cellSize;

    OS(String name, int heightOffset, int fontSize, float cellSize) {
        this.name = name;
        this.heightOffset = heightOffset;
        this.fontSize = fontSize;
        this.cellSize = cellSize;
    }

    public int getHeightOffset() {
        return this.heightOffset;
    }

    public int getFontSize() {
        return this.fontSize;
    }

    public float getCellSize() {
        return cellSize;
    }

    public static OS getOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        for (OS os: values()) {
            if (osName.contains(os.name)) {
                return os;
            }
        }
        return null;
    }
}
