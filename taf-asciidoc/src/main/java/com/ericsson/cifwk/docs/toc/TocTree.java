package com.ericsson.cifwk.docs.toc;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static com.ericsson.cifwk.docs.Locations.getSourceLocation;

public class TocTree {

    private final Document tree;
    private final TocEntryCollection entries;

    private TocTree(Document tree, TocEntryCollection entries) {
        this.tree = tree;
        this.entries = entries;
    }

    public static TocTree create(File treeFile, TocEntryCollection entries)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document tree = builder.parse(treeFile);
        return new TocTree(tree, entries);
    }

    public void walk(TocVisitor visitor) {
        Node rootNode = tree.getDocumentElement();
        walk(visitor, "", rootNode);
    }

    private void walk(TocVisitor visitor, String parentPrefix, Node node) {
        String prefix = node.getNodeName();
        if (!parentPrefix.isEmpty()) {
            prefix = parentPrefix + "/" + prefix;
        }
        Path path = getSourceLocation(prefix);
        TocEntry entry = entries.getByPath(path);
        if (entry != null) {
            visitor.visit(entry);
        }
        NodeList childNodes = node.getChildNodes();
        int childNodeCount = childNodes.getLength();
        for (int i = 0; i < childNodeCount; i++) {
            Node childNode = childNodes.item(i);
            if (childNode instanceof Element) {
                walk(visitor, prefix, childNode);
            }
        }
    }

}
