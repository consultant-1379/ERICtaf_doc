package com.ericsson.cifwk.docs.confluence;

import com.ericsson.cifwk.docs.EmptyDirectoryRemover;
import com.google.common.collect.Sets;
import org.htmlcleaner.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static com.ericsson.cifwk.docs.Locations.*;
import static com.google.common.io.Files.copy;
import static com.google.common.io.Files.getFileExtension;

@Deprecated
public final class ConfluenceConverter {

    public static void main(String[] args) throws IOException {
        Path src = getSourceLocation("html/_import");
        Path dst = getSourceLocation("asciidoc/_import");

        ConfluenceConverterFileVisitor importer = new ConfluenceConverterFileVisitor(src, dst);
        Files.walkFileTree(src, importer);
        importer.deleteUnusedAttachments();

        EmptyDirectoryRemover remover = new EmptyDirectoryRemover();
        Files.walkFileTree(src, remover);
        Files.walkFileTree(dst, remover);
    }

    private static class ConfluenceConverterFileVisitor implements FileVisitor<Path> {

        private static final Logger log = LoggerFactory.getLogger(ConfluenceConverter.class);

        public static final String DEFAULT_SRC_EXT = ".png";

        private final Path src;
        private final Path dst;
        private final Set<String> attachments;
        private final Set<String> usedAttachments;
        private final String pandoc;
        private final HtmlCleaner cleaner;
        private final SimpleHtmlSerializer serializer;

        public ConfluenceConverterFileVisitor(Path src, Path dst) {
            this.src = src;
            this.dst = dst;

            attachments = new HashSet<>();
            usedAttachments = new HashSet<>();
            pandoc = System.getProperty("pandoc", "pandoc");

            CleanerProperties properties = new CleanerProperties();
            properties.setTranslateSpecialEntities(false);
            cleaner = new HtmlCleaner(properties);
            serializer = new SimpleHtmlSerializer(properties);
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException {
            File target = translate(dir, src, dst).toFile();
            if (target.exists() || target.mkdirs()) {
                return FileVisitResult.CONTINUE;
            } else {
                log.error("Could not create directory: {}", target);
                return FileVisitResult.SKIP_SUBTREE;
            }
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
                throws IOException {
            File fromFile = path.toFile();
            File toFile = translate(path, src, dst).toFile();
            String filePath = fromFile.getAbsolutePath();
            String ext = getFileExtension(filePath);
            switch (ext) {
                case "html":
                    processHtml(fromFile, commonFileName(toFile));
                    log.info("Processed page: {}", toFile);
                    break;
                case "gif":
                case "jpg":
                case "png":
                    copy(fromFile, toFile);
                    attachments.add(normalize(path));
                    log.info("Processed image: {}", toFile);
                    break;
                case "":
                    // asciidoctor chokes on image links without extension
                    File toExtFile = new File(toFile.getAbsolutePath() + DEFAULT_SRC_EXT);
                    copy(fromFile, toExtFile);
                    attachments.add(normalize(fileToPath(fromFile)) + DEFAULT_SRC_EXT);
                    break;
                default:
                    delete(fromFile, "non-image");
            }
            return FileVisitResult.CONTINUE;
        }

        private File commonFileName(File file) {
            String parent = file.getParent();
            String name = commonName(file.getName());
            return new File(parent, name);
        }

        private String commonName(String name) {
            return URLDecoder.decode(name)
                    .replaceAll("[\\+ ]", "-")
                    .replaceAll("-?[,&]-?", "-")
                    .toLowerCase();
        }

        private String commonUrlEncodedName(String name) {
            return URLEncoder.encode(commonName(name));
        }

        private void delete(File file, String label) {
            delete(file, label, true);
        }

        private void delete(File file, String label, boolean tryNoExt) {
            if (file.delete()) {
                log.info("Deleting {}: {}", label, file);
            } else if (tryNoExt) {
                String noExt = removeExtension(file.getPath());
                delete(new File(noExt), label, false);
            } else {
                log.error("Could not delete {}: {}", label, file);
            }
        }

        private String normalize(Path path) {
            String relative = src.relativize(path).toString();
            return relative.replace('\\', '/');
        }

        private void processHtml(File fromFile, File toFile)
                throws IOException {
            TagNode node = cleaner.clean(fromFile);
            processHtmlNode(node);
            File tempFile = File.createTempFile("confluence", ".html");
            String tempPath = tempFile.getAbsolutePath();
            serializer.writeToFile(node, tempPath, "UTF-8");

            String toPath = replaceExtension(toFile.getAbsolutePath(), "adoc");
            String command = "\"" + pandoc + "\" -f html -t asciidoc -R -S --normalize -s \"" +
                    tempPath + "\" -o \"" + toPath + "\"";
            Process process = Runtime.getRuntime().exec(command);
            try {
                process.waitFor();
                tempFile.delete();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        @SuppressWarnings("unchecked")
        private void processHtmlNode(TagNode root) {
            // Remove Confluence space name from page title
            TagNode[] titleTags = root.getElementsByName("title", true);
            if (titleTags.length > 0) {
                TagNode title = titleTags[0];
                String titleText = title.getText().toString();
                int colonIndex = titleText.indexOf(':');
                if (colonIndex >= 0) {
                    titleText = titleText.substring(colonIndex + 1).trim();
                    title.removeAllChildren();
                    title.addChild(new ContentNode(titleText));
                }
            }

            // Remove header, footer, attachment and comment sections
            List<TagNode> removed = new ArrayList<>();
            removed.addAll(root.getElementListByAttValue("id", "main-header", true, false));
            removed.addAll(root.getElementListByAttValue("id", "footer", true, false));
            for (TagNode node : removed) {
                node.removeFromTree();
            }
            List<TagNode> removedSections = new ArrayList<>();
            removedSections.addAll(root.getElementListByAttValue("id", "attachments", true, false));
            removedSections.addAll(root.getElementListByAttValue("id", "comments", true, false));
            for (TagNode node : removedSections) {
                node.getParent().getParent().removeFromTree();
            }

            // Fix links to match file name convention
            TagNode[] links = root.getElementsByName("a", true);
            for (TagNode link : links) {
                String href = link.getAttributeByName("href");
                if ((href != null)
                        && getFileExtension(href).equals("html")
                        && !href.startsWith("http://")) {
                    Map<String, String> attributes = link.getAttributes();
                    attributes.put("href", commonUrlEncodedName(href));
                    link.setAttributes(attributes);
                }
            }

            // Add default extension to extension-less images
            TagNode[] imgs = root.getElementsByName("img", true);
            for (TagNode img : imgs) {
                String imgSrc = img.getAttributeByName("src");
                if (getFileExtension(imgSrc).isEmpty()) {
                    imgSrc += DEFAULT_SRC_EXT;
                    Map<String, String> attributes = img.getAttributes();
                    attributes.put("src", imgSrc);
                    img.setAttributes(attributes);
                }
                usedAttachments.add(imgSrc);
            }
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc)
                throws IOException {
            log.error("File not available: {}", file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                throws IOException {
            return FileVisitResult.CONTINUE;
        }

        public void deleteUnusedAttachments() {
            Sets.SetView<String> unused = Sets.difference(attachments, usedAttachments);
            for (String path : unused) {
                File srcFile = src.resolve(path).toFile();
                File dstFile = dst.resolve(path).toFile();
                delete(srcFile, "unused source");
                delete(dstFile, "unused target");
            }
        }
    }

}
