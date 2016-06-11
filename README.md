# Kd-Trees

A set of points in the unit square using a 2d-tree.

## Goal

Implement a data type to represent a set of points in the unit square using a
2d-tree to support efficient range search and nearest neighbor search.

## Implementation constraints
- Fixed public API for `PointSet` and `KdTree`
- `PointSet` and `KdTree` should not call library functions except those in
`java.lang` and `java.util`

## Performance requirements
- `PointSet` implementation should support `insert()` and `contains()` in time
proportional to the logarithm of the number of points in the set in the worst
case. It should support `nearest()` and `range()` in time proportional to the
number of points in the set.
- `KdTree` should use a 2d-tree (a generalization of a BST to two-dimensional
keys), with points in the nodes, using the x- and y-coordinates of the points
as keys in strictly alternating sequence.

## Sample client

Build a jar file:

    $ ./gradlew assemble

Client options:

    $ java -cp build/libs/kdtree.jar Client -h

Nearest neighbor search using data read from the input stream and the default
fast algorithm:

    $ cat data/points.txt | java -cp build/libs/kdtree.jar Client -ns -

Nearest neighbor search using data read from the input stream and the
brute-force algorithm:

    $ cat data/points.txt | java -cp build/libs/kdtree.jar Client -ns -b -

Nearest neighbor search using data of 100 randomly generated points and the
default fast algorithm ([see sample screencast](data/neighbor1.mp4?raw=true)):

    $ java -cp build/libs/kdtree.jar Client -ns -n 100

Nearest neighbor search using data of 10000 randomly generated points and the
default fast algorithm ([see sample screencast](data/neighbor2.mp4?raw=true)):

    $ java -cp build/libs/kdtree.jar Client -ns -n 10000

Range search using data of 100 randomly generated points and the
default fast algorithm ([see sample screencast](data/range1.mp4?raw=true)):

    $ java -cp build/libs/kdtree.jar Client -rs -n 100

Range search using data of 10000 randomly generated points and the
default fast algorithm ([see sample screencast](data/range2.mp4?raw=true)):

    $ java -cp build/libs/kdtree.jar Client -rs -n 10000
