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
  - `accessToken`: access token
    - tokenName: name of the token
    - tokenValue: value of the token
- `dependencies`: list of dependencies. There are two types of dependencies:
  - GIT DEPENDENCY, with the following fields:
    - `path`: path of the repository
    - `rootPath`: root path of maven project to build
    - `ignoredTest`: ignore tests
    - `tag`: tag of the repository
    - `branch`: branch of the repository
    - `useAccessToken`: use access token in config above or not
  - LOCAL DEPENDENCY, with the following fields:
    - `path`: path of the jar file to install
    - `groupId`: groupId of the dependency
    - `artifactId`: artifactId of the dependency
    - `version`: version of the dependency

* Note: `branch` and `tag` cannot be set at the same time

### Example

```yaml

config:
  tempRepoPath: temp
  deleteAfterRun: true
  accessToken:
    tokenName: nmhillusion
    tokenValue: "dtUddc345sDFs"

dependencies:
  - path: https://github.com/nmhillusion/neon-di.git
    rootPath: .

  - path: "C:\\Users\\nmhil\\OneDrive\\Desktop\\tmp\\pi-logger-2023.3.1.jar"
    groupId: tech.nmhillusion
    artifactId: pi-logger
    version: 2023.3.1

```
