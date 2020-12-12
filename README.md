![GitHub](https://img.shields.io/github/license/jakubgwozdz/advent-of-code-2020)  
![GitHub last commit](https://img.shields.io/github/last-commit/jakubgwozdz/advent-of-code-2020)  
![GitHub repo size](https://img.shields.io/github/repo-size/jakubgwozdz/advent-of-code-2020)  
![CI](https://github.com/jakubgwozdz/advent-of-code-2020/workflows/CI/badge.svg)  
![GitHub top language](https://img.shields.io/github/languages/top/jakubgwozdz/advent-of-code-2020)

# Advent of Code 2020

* Full solutions with JS and JVM targets are on [GitHub](https://github.com/jakubgwozdz/advent-of-code-2020)
* *Visualisations* are available at [Github Pages](https://jakubgwozdz.github.io/advent-of-code-2020).

Whole project is written in Kotlin multiplatform for JS&JVM. 
This means I'm doing the puzzles and tests for them using JVM (which is kinda faster) in IDE. Then, during gradle's `build`
The JS/browser and JVM targets are built and both of them are running units during the build. These units test - among others -
the inputs I got for the puzzles and the examples from puzzles texts.

All you need to run this locally is a Java (I'm using JDK 11, but I wouldn't be surprised if it'd work on JDK 8).

```shell
./gradlew build
```

Also - an internet connection is required (half of the build is done by nodeJS so the good part of the internet must be downloaded to node_modules, obviously)

To launch it on `http://localhost:8080/` you can just run:

```shell
./gradlew jsBrowserDevelopmentRun
```

That's all.
