package com.ericsson.cifwk.docs.toc;

import com.ericsson.cifwk.docs.StringReplacer;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class SinglePageWriter implements TocVisitor {

    private static final Pattern LINK_PATTERN = Pattern.compile("link:(\\S+\\.html)\\[([^\\]]*)\\]");

    private static final Logger log = LoggerFactory.getLogger(SinglePageWriter.class);

    private final StringBuilder tocBuilder;
    private final StringBuilder bodyBuilder;
    private final TocEntryCollection tocEntries;

    public SinglePageWriter(TocEntryCollection tocEntries) {
        tocBuilder = new StringBuilder();
        bodyBuilder = new StringBuilder();
        this.tocEntries = tocEntries;
    }

    @Override
    public void visit(TocEntry entry) {
        try {
            addPage(entry);
        } catch (IOException ignored) {
        }
    }

    private void addPage(TocEntry entry) throws IOException {
        String page = Files.toString(entry.getSrcFile(), Charsets.UTF_8).trim();
        String indent = Strings.repeat("*", entry.getLevel());
        String link = anchorLink(entry.getAnchor(), entry.getHeading());
        tocBuilder
                .append(indent)
                .append(" ")
                .append(link)
                .append("\n");
        String section = makeSection(entry, page);
        bodyBuilder.append(section).append("\n\n");
    }

    private String makeSection(final TocEntry entry, String page) {
        StringReplacer replacer = new StringReplacer(page);
        return replacer.replace(LINK_PATTERN, new StringReplacer.Strategy() {
            @Override
            public String replace(MatchResult result) {
                String link = result.group(1);
                TocEntry linkedEntry = tocEntries.getByLink(link);
                if (linkedEntry != null) {
                    return anchorLink(linkedEntry.getAnchor(), result.group(2));
                } else {
                    Iterable<TocEntry> entries = tocEntries.getByLinkSuffix(link);
                    if (entries.iterator().hasNext()) {
                        log.warn(
                                "Broken link: \"{}\" in {}; possible fixes: {}",
                                link, entry.getSrcFile(), entryLinksString(entries)
                        );
                    } else {
                        log.warn(
                                "Broken link: \"{}\" in {}; could not find possible fixes",
                                link, entry.getSrcFile()
                        );
                    }
                    return null;
                }
            }
        }).toString();
    }

    private String entryLinksString(Iterable<TocEntry> entries) {
        Iterable<String> links = Iterables.transform(entries, new Function<TocEntry, String>() {
            public String apply(TocEntry entry) {
                return entry.getRelativeLink();
            }
        });
        return Joiner.on(", ").skipNulls().join(links);
    }

    private String anchorLink(String anchor, String title) {
        return "xref:" + anchor + "[" + title + "]";
    }

    public void write(File titleFile, File out) throws IOException {
        String title = Files.toString(titleFile, Charsets.UTF_8);
        String toc = tocBuilder.toString();
        String page = bodyBuilder.toString();
        String all = (title + "\n" + toc + "\n" + page).replace("\r", "");
        Files.write(all, out, Charsets.UTF_8);
    }

}
