package com.ericsson.cifwk.docs;

import com.ericsson.cifwk.docs.toc.SinglePageWriter;
import com.ericsson.cifwk.docs.toc.TocEntryCollection;
import com.ericsson.cifwk.docs.toc.TocIndexWriter;
import com.ericsson.cifwk.docs.toc.TocTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static com.ericsson.cifwk.docs.Locations.getExampleLocation;
import static com.ericsson.cifwk.docs.Locations.getSourceLocation;
import static com.ericsson.cifwk.docs.Locations.getTargetLocation;
import static com.google.common.io.Files.copy;
import static com.google.common.io.Files.createParentDirs;
import static com.google.common.io.Files.getFileExtension;
import static java.nio.file.Files.newDirectoryStream;
import static java.nio.file.Files.walkFileTree;

public final class PreprocessDocs {

    private static final Logger log = LoggerFactory.getLogger(PreprocessDocs.class);

    private static final String INDEX_FILE = "index.adoc";
    //private static final String SINGLE_PAGE_FILE = "index-single.adoc";
    private static final String SINGLE_PAGE_FILE = "index.adoc";
    private static final String TOC_FILE = "_toc.xml";
    private static final String TOC_TITLE_FILE = "_title.adoc";

    public static void main(String[] args) throws Exception {
        Path src = getSourceLocation("asciidoc");
        Path dst = getTargetLocation("preprocessed-docs");

        File srcFile = src.toFile();
        File dstFile = dst.toFile();

        File indexFile = new File(dstFile, INDEX_FILE);
        File singlePageFile = new File(dstFile, SINGLE_PAGE_FILE);
        File tocFile = new File(srcFile, TOC_FILE);
        File tocTitleFile = new File(srcFile, TOC_TITLE_FILE);

        TocEntryCollection tocEntries = new TocEntryCollection();
        DocsPreprocessor preprocessor = DocsPreprocessor.create(src, dst, tocEntries);
        walkFileTree(src, preprocessor);
        TocTree toc = TocTree.create(tocFile, tocEntries);

        TocIndexWriter tig = new TocIndexWriter();
        toc.walk(tig);
        log.info("Writing TOC");
        tig.write(tocTitleFile, indexFile);

        SinglePageWriter spg = new SinglePageWriter(tocEntries);
        toc.walk(spg);
        log.info("Writing single-page documentation");
        spg.write(tocTitleFile, singlePageFile);

        for (String folder : new String[]{"groovy", "java", "resources"}) {
            Path examplesSrc = getExampleLocation(".", folder);
            Path examplesDst = dst.resolve("examples/" + folder);
            RecursiveCopier copier = new RecursiveCopier(examplesSrc, examplesDst);
            walkFileTree(examplesSrc, copier);
        }
    }

    private static class DocsPreprocessor implements FileVisitor<Path> {

        private final Path src;
        private final Path dst;
        private final TocEntryCollection tocEntries;

        private int level = 0;

        private DocsPreprocessor(Path src, Path dst, TocEntryCollection tocEntries) {
            this.src = src;
            this.dst = dst;
            this.tocEntries = tocEntries;
        }

        public static DocsPreprocessor create(Path src, Path dst, TocEntryCollection tocEntries)
                throws ParserConfigurationException, IOException, SAXException {
            return new DocsPreprocessor(src, dst, tocEntries);
        }

        @Override
        public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs)
                throws IOException {
            File dir = path.toFile();
            String dirName = dir.getName();
            if (dirName.startsWith("_")) {
                log.debug("Skipping file: {}", dir);
                return FileVisitResult.SKIP_SUBTREE;
            } else {
                if ((level > 0) && directoryContainsExt(path, "adoc")) {
                    Path srcPath = path.resolve(INDEX_FILE);
                    File srcFile = srcPath.toFile();
                    String dstFileName = compatibleTargetFile(srcPath).getName();
                    tocEntries.add(path, srcFile, dirName, dstFileName, level);
                }
                level++;
                return FileVisitResult.CONTINUE;
            }
        }

        private boolean directoryContainsExt(Path path, String ext)
                throws IOException {
            DirectoryStream<Path> childPaths = newDirectoryStream(path);
            for (Path childPath : childPaths) {
                File file = childPath.toFile();
                if (file.isFile() && getFileExtension(file.getPath()).equals(ext)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
                throws IOException {
            File file = path.toFile();
            String fileName = file.getName();
            if (!fileName.startsWith("_")) {
                log.info("Including file: {}", file);
                File dstFile = compatibleTargetFile(path);
                createParentDirs(dstFile);
                copy(file, dstFile);
                if (getFileExtension(fileName).equals("adoc") && !fileName.equals(INDEX_FILE)) {
                    tocEntries.add(path, path.toFile(), fileName, dstFile.getName(), level);
                }
            } else {
                log.debug("Skipping file: {}", file);
            }
            return FileVisitResult.CONTINUE;
        }

        private File compatibleTargetFile(Path path) {
            Path relative = src.relativize(path);
            String name = relative.toString();
            if (getFileExtension(name).equals("adoc")) {
                name = name
                        .replaceAll("[\\\\/]", "-")
                        .replaceFirst("-index\\.adoc$", ".adoc");
            }
            return dst.resolve(name).toFile();
        }

        @Override
        public FileVisitResult visitFileFailed(Path path, IOException exc)
                throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path path, IOException exc)
                throws IOException {
            level--;
            return FileVisitResult.CONTINUE;
        }

    }
}
