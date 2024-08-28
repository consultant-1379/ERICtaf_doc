<head>
   <title>How to Contribute Documentation</title>
</head>

# Appendix A: How to Contribute Documentation

## Getting started

### 1. Acquire docs

These pages are stored and versioned in Git.
To get the documentation repository:

```
git clone ssh://gerrit.ericsson.se:29418/OSS/com.ericsson.cifwk/ERICtaf_doc
```

Since it's Git, you have to commit and push your changes in order to share them.

### 2. Create a document

Documentation content is managed in the [AsciiDoc](http://www.methods.co.nz/asciidoc/) format (syntax cheatsheet [available online](http://powerman.name/doc/asciidoc).

To add a document, crate an **.adoc** file in **taf-docs/src/main/asciidoc**, in root or a subfolder. Full AsciiDoc syntax can be used to format document content,
but there are a few TAF docs specifics in addition to common AsciiDoc features. Refer to the formatting section for details.

### 3. Add to TOC

The table of contents generator uses two configuration files: ___toc.xml__ and ___title.adoc__. Both can be found at **taf-docs/src/main/asciidoc**.

- ___toc.xml__ contains a file tree from document root represented in plain XML.
  In order for new documents to be included in TOC, they should be specified in this file,
  with a correct path.
- ___title.adoc__ is a template used as a header for the TOC page.
  It contains everything placed before the actual TOC link list.

### 4. Exclude files from conversion

To exclude a file from ending up in target, its name must be prefixed by an underscore, e.g.

- Visible: <span style="color:#ba3925;">file.adoc</span>
- Hidden: <span style="color:#ba3925;">_file.adoc</span>

### 5. Build everything

Maven is used to process and build documentation from sources. To do this, in the root of the project execute a shell command:

```
mvn clean package
```

This takes all **.adoc** files in **taf-docs/src/main/asciidoc** and converts them to the appropriate format. The resulting HTML is then found in **taf-docs/target/site**.

## Formatting

### Starting a new section

To start a sub section please use section title level 2 " === ".

### Linking to other documents

When converting from AsciiDoc to HTML the relative path to the **.adoc** file is flattened. This is done as a countermeasure for converter _plugin limitations_.
The flattening is done by replacing slashes in the file path to dashes like so:

```
components/monitoring/thresholds.adoc -> components-monitoring-thresholds.html
```

This behaviour should be taken into account when linking to other documents, as the links containing file paths won't be automatically converted. So, the correct
way to make a link to the above document is:

```
link:components-monitoring-thresholds.html[Thresholds]
```

Notice the **.html** extension: that's because we link to the resulting HTML file, not the original AsciiDoc source.

### Adding code snippets from file

Source files in **taf-examples** module are copied over to target directory during build. Embed any of them in a document like so:

```
include::examples/groovy/HelloWorld.groovy[]
```

```
class HelloWorld {

    static void main(args) {
        println "hello"
    }

}
```

### Embedding images

To embed an image in a document, you don't need to link to the flattened path, as it is with AsciiDoc documents. Just specify a path relative to
documentation root **taf-docs/src/main/asciidoc**. A good idea is to put new images in the **images** folder or a subfolder.

```
image:images/ericsson.png[]
```

![Ericsson](../images/ericsson.png)
