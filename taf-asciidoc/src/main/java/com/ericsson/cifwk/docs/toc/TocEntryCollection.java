package com.ericsson.cifwk.docs.toc;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.text.WordUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.ericsson.cifwk.docs.Locations.replaceExtension;
import static com.google.common.io.Files.getNameWithoutExtension;

public class TocEntryCollection {

    private final Map<Path, TocEntry> entriesByPath;
    private final Map<String, TocEntry> entriesByLink;

    public TocEntryCollection() {
        entriesByPath = new HashMap<>();
        entriesByLink = new HashMap<>();
    }

    public TocEntry getByPath(Path path) {
        return entriesByPath.get(path);
    }

    public TocEntry getByLink(String link) {
        return entriesByLink.get(link);
    }

    public Iterable<TocEntry> getByLinkSuffix(final String suffix) {
        return Iterables.filter(entriesByLink.values(), new Predicate<TocEntry>() {
            @Override
            public boolean apply(TocEntry entry) {
                return entry.getRelativeLink().endsWith(suffix);
            }
        });
    }

    public void add(Path srcPath, File srcFile, String srcFileName, String dstFileName, int level) {
        String heading;
        try {
            heading = headingFromFile(srcFile);
        } catch (IOException e) {
            heading = headingFromFileName(srcFileName);
        }
        String relativeLink = replaceExtension(dstFileName, "html");
        String anchor = headingLink(heading);
        TocEntry entry = new TocEntry(
                srcFile,
                relativeLink,
                anchor,
                level,
                heading
        );
        entriesByPath.put(srcPath, entry);
        entriesByLink.put(relativeLink, entry);
    }

    private static String headingFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.replaceFirst("(?i)^[^a-z]*", "").trim();
            if (!line.isEmpty()) {
                break;
            }
        }
        br.close();
        return Strings.nullToEmpty(line);
    }

    private static String headingFromFileName(String fileName) {
        String fileTitle = getNameWithoutExtension(fileName).replace('-', ' ');
        return WordUtils.capitalize(fileTitle);
    }

    private static String headingLink(String heading) {
        // Mirroring AsciiDoctor's original approach found here:
        // https://github.com/asciidoctor/asciidoctor/blob/fee994fcb70c4a8003bc56a7e7637527a3f67f44/lib/asciidoctor/section.rb#L93
        // https://github.com/asciidoctor/asciidoctor/blob/9a2b7e9ce2bc4bec8de547f911c61fda5bdeea7e/lib/asciidoctor.rb#L401
        return "_" + heading.toLowerCase()
                .replaceAll("&(?:[[:alpha:]]+|#[[:digit:]]+|#x[[:alnum:]]+);|\\W+?", "_")
                .replaceAll("_+", "_");
    }

}
