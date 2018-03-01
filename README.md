# Threads vs Kotlin Coroutines
What happen when you start a million threads ?:)

## Status

## Download

```bash
    git clone https://github.com/wojciech-zurek/kotlin-thread-coroutine-tester.git
```

## Run with gradle

```bash
    cd kotlin-thread-coroutine-tester/
    ./gradlew run
```

## Run as jar file

```bash
    cd kotlin-thread-coroutine-tester/
    ./gradlew fatJar
    java -jar build/libs/kotlin-thread-coroutine-tester-1.0-SNAPSHOT.jar
```

## Sample output

```
==== Classic Threads vs Coroutines ====

Available processors: [4]

Heap size MB: [238]
Testing threads...
java.lang.OutOfMemoryError: unable to create new native thread
Total time: [0.491]
Total spawned threads/coroutine: [9832]

Sleeping 2 sec...

Heap size MB: [238]
Testing coroutine with launch ...
Total time: [5.082]
Total spawned threads/coroutine: [1000000]

Heap size MB: [563]
Testing coroutine with async ...
Total time: [3.568]
Total spawned threads/coroutine: [1000000]
```