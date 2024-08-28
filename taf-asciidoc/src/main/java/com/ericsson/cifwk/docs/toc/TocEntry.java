package com.ericsson.cifwk.docs.toc;

import java.io.File;

public class TocEntry {

    private final File srcFile;
    private final String relativeLink;
    private final String anchor;
    private final int level;
    private final String heading;

    public TocEntry(
            File srcFile,
            String relativeLink,
            String anchor,
            int level,
            String heading) {
        this.srcFile = srcFile;
        this.relativeLink = relativeLink;
        this.anchor = anchor;
        this.level = level;
        this.heading = heading;
    }

    public File getSrcFile() {
        return srcFile;
    }

    public String getRelativeLink() {
        return relativeLink;
    }

    public String getAnchor() {
        return anchor;
    }

    public int getLevel() {
        return level;
    }

    public String getHeading() {
        return heading;
    }
}
