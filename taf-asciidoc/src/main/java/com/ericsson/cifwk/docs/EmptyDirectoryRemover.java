package com.ericsson.cifwk.docs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class EmptyDirectoryRemover extends SimpleFileVisitor<Path> {

    private static final Logger log = LoggerFactory.getLogger(EmptyDirectoryRemover.class);

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs)
            throws IOException {
        File dir = path.toFile();
        boolean isEmpty = !Files.newDirectoryStream(path).iterator().hasNext();
        if (isEmpty) {
            if (dir.delete()) {
                log.info("Deleting empty directory: {}", path);
            } else {
                log.error("Could not delete empty directory: {}", path);
            }
            return FileVisitResult.SKIP_SUBTREE;
        }
        return FileVisitResult.CONTINUE;
    }

}
