package com.ericsson.cifwk.docs;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static com.google.common.io.Files.getFileExtension;

public final class Locations {

    private static final FileSystem fileSystem = FileSystems.getDefault();
    private static final String root = System.getProperty("root", "taf-docs");

    public static Path getSourceLocation(String relative) {
        return fileSystem.getPath(root, "src/main", relative);
    }

    public static Path getTargetLocation(String relative) {
        return fileSystem.getPath(root, "target", relative);
    }

    public static Path getExampleLocation(String relative,String folder) {
        return fileSystem.getPath(root, "src/main/"+folder, relative);
    }

    public static Path translate(Path path, Path parent, Path newParent) {
        Path relative = parent.relativize(path);
        return newParent.resolve(relative);
    }

    public static String removeExtension(String path) {
        return replaceExtension(path, "");
    }

    public static String replaceExtension(String path, String newExt) {
        String ext = getFileExtension(path);
        if (!ext.isEmpty()) {
            path = path.substring(0, path.length() - ext.length() - 1);
        }
        if (!newExt.isEmpty()) {
            return path + "." + newExt;
        } else {
            return path;
        }
    }

    public static Path fileToPath(File file) {
        return fileSystem.getPath(file.getAbsolutePath());
    }

}
