# jKeychain

[![Master Build Status](https://img.shields.io/github/workflow/status/davidafsilva/jkeychain/Master%20Build?label=Build&style=flat-square)](https://github.com/davidafsilva/jkeychain/actions?query=workflow%3A%22Master+Build%22+branch%3Amaster)
[![Latest Release](https://img.shields.io/maven-central/v/pt.davidafsilva.apple/jkeychain?color=brightgreen&label=Latest%20Release&style=flat-square)](https://repo1.maven.org/maven2/pt/davidafsilva/vertx/jkeychain)
[![License](https://img.shields.io/github/license/davidafsilva/jkeychain?color=brightgreen&label=License&logo=License&style=flat-square)](https://opensource.org/licenses/BSD-2-Clause)

This is a fork of [conormcd/osx-keychain-java](https://github.com/conormcd/osx-keychain-java), which has been updated
and published to an OSS repository.

The exposed keychain API has been compiled to support both x86/64 and arm64 architectures.

## Usage

### Import
1. Add maven central repository to your configuration
2. Import the library

#### Gradle
```kotlin
repositories {
    mavenCentral()
}
dependencies {
    implementation("pt.davidafsilva.apple:jkeychain:1.1.0")
}
```

#### Maven
```xml

<dependencies>
    <dependency>
        <groupId>pt.davidafsilva.apple</groupId>
        <artifactId>jkeychain</artifactId>
        <version>1.1.0</version>
    </dependency>
</dependencies>
```

### Example:

```java
final OSXKeychain keychain=OSXKeychain.getInstance();
final Optional<String> accKey=keychain.findGenericPassword("acc","key");
```

## Build

To build locally, you need JavaJDK >= 1.8 and macOS >= 11. Make sure that you have a softlink to your jdk
at `/Library/Java/JavaVirtualMachines/openjdk.jdk`

```shell
# To build and test
$ ./gradlew build

# To install in the local maven repository
$ ./gradlew publishToMavenLocal
```
