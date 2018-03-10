# Rumor-spread-simulation
This is the repository for my *Networking Algorithms* class assignment. 
This repository is the follow up of my [previous](https://github.com/pjozsef/Rumor-FX), *Computer networks and Distributed systems* assignment.
While the former repository is focused on demonstrating the algorithm, this repository is about running simulations in large batches with the various rumor spreading modes.
I wrote a program that simulates the rumor spreading algorithms described in the paper, [How to Discreetly Spread a Rumor in a Crowd](http://people.cs.georgetown.edu/~cnewport/pubs/gn-disc2016.pdf) written by Mohsen Ghaffari and Calvin Newport in 2016.

The program was written in Kotlin.
 
## Build and run
* Clone this repository `git clone https://github.com/pjozsef/rumor-spread-simulation.git`
* Install a Java8 JDK
* Build
  * `./gradlew shadowJar` on Linux
  * `./gradlew.bat shadowJar` on Windows
* Run
  * `java -jar build/libs/rumor-spread-simulation-all.jar`

## Usage
```shell
Usage: <main class> [command] [command options]
     Commands:
       info      Print graph information (conductance, vertex expansion)
         Usage: info [options]
           Options:
           * --graph
               Path to graph file
             --verbose
               Print extra information
               Default: false
   
       simulation      Run the simulation
         Usage: simulation [options]
           Options:
           * --algorithm
               The type of the algorithm [Push, Pull, PP0, PP1]
           * --count
               Number of simulations to run
               Default: -1
           * --csv
               Path where the output csv will be saved
           * --graph
               Path to graph file
             --verbose
               Print extra information
               Default: false
```
### Graph file format
The simulation expects a graph input file to work on. An example file looks like this:
```
A
###
A,B,C,D,E
###
A-B,
A-C,
D-E,
C-D
```
As you can see, the file has three segments, divided by `###`s.
1) The first section denotes the starting node, which knows the information/rumor at the start of the simulation.
2) The second section declares the nodes of the graph. The nodes must be separated by a comma (`,`).
3) The last section declares the edges between the nodes declared in the second section. 
The edges represent undirected edges, so `A-B` is the same as `B-A`, thus it is unnecessary to declare both.

### Info example
`java -jar build/libs/rumor-spread-simulation-all.jar info --graph myGraph.txt --verbose`

Where `myGraph.txt` contains the above mentioned graph.

Example output:

```
Selected node: A
Raw nodes: A,B,C,D,E
Raw edges: A-B,A-C,D-E,C-D
Graph conductance: 0.6666666666666666
Vergex expansion: 0.5

```

### Simulation example
`java -jar build/libs/rumor-spread-simulation-all.jar simulation --count 10 --algorithm pp1 --graph myGraph.txt --csv result.csv`

Where `myGraph.txt` contains the above mentioned graph.

Content of `result.csv`:

|iteration|numberOfSteps|algorithm|nodeCount|edgeCount|vertexExpansion|graphConductance|
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|0|4|PP1|5|8|0.5|0.6666666666666666|
|1|4|PP1|5|8|0.5|0.6666666666666666|
|2|3|PP1|5|8|0.5|0.6666666666666666|
|3|4|PP1|5|8|0.5|0.6666666666666666|
|4|4|PP1|5|8|0.5|0.6666666666666666|
|5|3|PP1|5|8|0.5|0.6666666666666666|
|6|3|PP1|5|8|0.5|0.6666666666666666|
|7|4|PP1|5|8|0.5|0.6666666666666666|
|8|3|PP1|5|8|0.5|0.6666666666666666|
|9|3|PP1|5|8|0.5|0.6666666666666666|
