package com.ericsson.cifwk.docs;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class RecursiveCopier extends SimpleFileVisitor<Path> {

    private static final Logger log = LoggerFactory.getLogger(RecursiveCopier.class);

    private final Path src;
    private final Path dst;

    public RecursiveCopier(Path src, Path dst) {
        this.src = src;
        this.dst = dst;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException {
        copy(file);
        return FileVisitResult.CONTINUE;
    }

    private void copy(Path path) throws IOException {
        File from = path.toFile();
        File to = getTarget(path).toFile();
        Files.createParentDirs(to);
        log.info("Copying example: {}", to);
        Files.copy(from, to);
    }

    private Path getTarget(Path path) {
        Path relative = src.relativize(path);
        return dst.resolve(relative);
    }

}
