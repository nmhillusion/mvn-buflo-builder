# MVN Buflo Builder

---

[![GitHub release](https://img.shields.io/github/v/release/nmhillusion/mvn-buflo-builder?style=flat-square)](https://github.com/nmhillusion/mvn-buflo-builder/releases)

A simple command line tool to build and install Maven projects as local dependencies from Git VCS
repositories.

## Prerequisites:

- [Java](https://www.java.com/en/download/) 21 or higher
- [Git](https://git-scm.com/)
- [Maven](https://maven.apache.org/) 3.6.3 or higher

## Usage:

```bash

./app -c <path/to/config/file.yml>
./app --configPath <path/to/config/file.yml>
./app <path/to/config/file.yml>
./app --help
./app -h

```

## Options:

- `-c, --configPath`: path to the config file
- `-h, --help`: show help

## Config file:

### Structure

- `config`: configuration
  - `tempRepoPath`: path to the temporary repository
  - `deleteAfterRun`: delete the temporary repository after build
- `dependencies`: list of dependencies
  - `url`: url of the repository
  - `rootPath`: root path of maven project to build
  - `ignoredTest`: ignore tests
  - `tag`: tag of the repository
  - `branch`: branch of the repository

* Note: `branch` and `tag` cannot be set at the same time

### Example

```yaml

config:
  tempRepoPath: temp
  deleteAfterRun: true

dependencies:
  - url: https://github.com/nmhillusion/neon-di.git
    rootPath: .
    ignoredTest: false
    tag: 2024.5.5

  - url: https://github.com/nmhillusion/pi-logger.git
    rootPath: .
    ignoredTest: true
    branch: main

```
