# Segovia

An application to facilitate learning a foreign language.

## Building application

```shell
gradle clean build
```

## Running application

```shell
java -Dloader.path=~/segovia -jar app/build/libs/segovia-app-1.0.0-SNAPSHOT.jar
```

Option `-Dloader.path=~/segovia` is optional at this time. This directory
is to contain the application's configuration files and any libraries to
override the behavior of the classes in the executable JAR. It is
a comma-separated list of directories (containing file resources and/or
nested archives in *.jar or *.zip or archives) or archives to append to
the classpath. `BOOT-INF/classes,BOOT-INF/lib` in the application archive
are always used.
