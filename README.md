# ScrambleServer

This is a http server to generate cube's scrambles and images based on [lib-scrambles](https://search.maven.org/artifact/org.worldcubeassociation.tnoodle/lib-scrambles).

After launching, the program will prepare some scrambles, which may take 5 minutes.

## Run

```shell
java -jar scramble.jar # default port is 12014

or

docker run -it --rm -p 12014:12014 -m 100M --cpus=2 lz1998/scramble-server:0.0.1
```

## Usage

```text
Usage:
/scramble/<TYPE>
/view/<TYPE>?scramble=<SCRAMBLE>&format=<PNG|SVG>

TYPE:
101010, 111111, 121212, 131313, 141414, 151515, 161616, 171717, 222, 333, 333fm, 333ni, 444, 444fast, 444ni, 555, 555ni, 666, 777, 888, 999, clock, minx, pyram, skewb, sq1

Example:
/scramble/333
/scramble/444
/scramble/sq1
/view/333?scramble=R%20U&format=png
/view/pyram?scramble=R%20U&format=svg
```