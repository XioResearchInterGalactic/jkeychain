# jKeychain

[![Master Build](https://github.com/XioResearchInterGalactic/jkeychain/actions/workflows/master-build.yml/badge.svg)](https://github.com/XioResearchInterGalactic/jkeychain/actions/workflows/master-build.yml)
[![Latest Release](https://img.shields.io/maven-central/v/org.merlyn.oss/jkeychain?color=brightgreen&label=Latest%20Release&style=flat-square)](https://central.sonatype.com/artifact/org.merlyn.oss/jkeychain)
[![License](https://img.shields.io/github/license/XioResearchInterGalactic/jkeychain?color=brightgreen&label=License&logo=License&style=flat-square)](https://opensource.org/licenses/BSD-2-Clause)

This is a fork of [davidafsilva/jkeychain](https://github.com/davidafsilva/jkeychain), which was a fork of
[conormcd/osx-keychain-java](https://github.com/conormcd/osx-keychain-java). That fork was working well for us until
now, but we required some improvements on the loading of the library. Given that it was unchanged for many years, it
also required an update to the method to upload to Maven Central.

Because of all of that, we have created a fork and published it to GitHub and to Maven Central.

The exposed keychain API has been compiled to support both x86/64 and arm64 architectures.

## Usage

### Import
1. Add Maven Central repository to your configuration
2. Import the library

#### Gradle
```kotlin
repositories {
    mavenCentral()
}
dependencies {
    implementation("org.merlyn.oss:jkeychain:1.2.0")
}
```

#### Maven
```xml

<dependencies>
    <dependency>
        <groupId>org.merlyn.oss</groupId>
        <artifactId>jkeychain</artifactId>
        <version>1.2.0</version>
    </dependency>
</dependencies>
```

### Example:

```java
final OSXKeychain keychain = OSXKeychain.getInstance();
final Optional<String> appKey = keychain.findGenericPassword("application", "key");
```

## Build

To build locally, you need JavaJDK >= 1.8 and macOS >= 11. Make sure that you have a softlink to your jdk
at `/Library/Java/JavaVirtualMachines/openjdk.jdk`.
Please run the command below to execute the tests and build the final package (jar).
```shell
$ ./gradlew build
```

### Integration

To test the integration with locally modified versions of the API, you can:
1. Commit the changes to your local branch (fork)
2. Build and publish a new snapshot version to your local maven repository
3. Import the snapshot version in your project.

To accomplish 2. please run the command below.
```shell
$ C_INCLUDE_PATH=$JAVA_HOME/include:$JAVA_HOME/include/darwin ./gradlew build publishToMavenLocal
```

## Thanks

Thanks to [David Silva](https://github.com/davidafsilva) and [Conor McDermottroe](https://github.com/conormcd) for
the original implementations that have served us well for many years.
