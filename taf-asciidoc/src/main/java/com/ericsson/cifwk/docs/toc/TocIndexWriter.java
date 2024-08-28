package com.ericsson.cifwk.docs.toc;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class TocIndexWriter implements TocVisitor {

    private final StringBuilder builder;

    public TocIndexWriter() {
        this.builder = new StringBuilder();
    }

    @Override
    public void visit(TocEntry entry) {
        String indent = Strings.repeat("*", entry.getLevel());
        builder
                .append(indent)
                .append(" link:")
                .append(entry.getRelativeLink())
                .append("[")
                .append(entry.getHeading())
                .append("]\n");
    }

    public void write(File titleFile, File out) throws IOException {
        String title = Files.toString(titleFile, Charsets.UTF_8);
        String toc = builder.toString();
        String all = (title + "\n" + toc).replace("\r", "");
        Files.write(all, out, Charsets.UTF_8);
    }
}
