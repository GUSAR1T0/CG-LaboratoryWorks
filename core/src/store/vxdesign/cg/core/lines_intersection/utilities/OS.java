package store.vxdesign.cg.core.lines_intersection.utilities;

public enum OS {
    WINDOWS("win", 0, 28), MAC("mac", 125, 36);

    private final String name;
    private final int heightOffset;
    private final int fontSize;

    OS(String name, int heightOffset, int fontSize) {
        this.name = name;
        this.heightOffset = heightOffset;
        this.fontSize = fontSize;
    }

    public int getHeightOffset() {
        return this.heightOffset;
    }

    public int getFontSize() {
        return this.fontSize;
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
