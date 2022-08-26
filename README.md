# jmzIdentML
[![Java CI with Maven](https://github.com/PRIDE-Utilities/jmzIdentML/actions/workflows/maven.yml/badge.svg)](https://github.com/PRIDE-Utilities/jmzIdentML/actions/workflows/maven.yml)

A Java API to the Proteomics Standards Initiative's mzIdentML format. The [mzIdentML](https://www.psidev.info/mzidentml) data standard captures peptide and protein identification data, generated from mass spectrometry.
For more information about mzIdentML.

## mzIdentML versions supported

Currently, the jmzIdentML supports the two major mzIdentML versions: 

- [mzIdentML 1.1](https://www.psidev.info/mzidentml#mzIdentML1_1_1)
- [mzIdentML 1.2](https://www.psidev.info/mzidentml#mzid12)

## jmzIdentML API

The API can be imported directly as a jar file to provide access to read and write functionality for mzIdentML. The API builds on top of JaxB capabilities, by providing an indexing scheme that allows random access to parts of the file. 

In order to use the library in your pom file, please use the following code snippet: 

```pom
<dependency>
  <groupId>uk.ac.ebi.jmzidml</groupId>
  <artifactId>jmzidentml</artifactId>
  <version>${version}</version>
</dependency>
```

The current maven repository that stores the jmzIdentML is: 

```pom
<repositories>
    <repository>
      <id>nexus-ebi-release-repo</id>
      <url>https://www.ebi.ac.uk/Tools/maven/repos/content/groups/ebi-repo/</url>
    </repository>
    <repository>
      <id>nexus-ebi-snapshot-repo</id>
      <url>https://www.ebi.ac.uk/Tools/maven/repos/content/groups/ebi-snapshots/</url>
    </repository>
</repositories>
```

## Issues and Reports 

Issues, bugs and errors can be reported in the following link: https://github.com/PRIDE-Utilities/jmzIdentML/issues