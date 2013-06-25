# Trinity, an IDE for Derric #

Trinity is an integrated development/debugging environment (IDE) for [Derric](http://derric-lang.org/), a domain-specific language to declaratively describe binary file formats. It provides automatic synchronization in the user interface between individual bytes in the input data and elements in the Derric source code.

## Screencast ##

To get an overview of the key features of Trinity, we have created [a screencast](https://vimeo.com/69077052).

## Installing ##

Trinity is a Java/Swing application. However, it depends on the [Derric compiler](http://github.com/jvdb/derric), which was written in [Rascal](http://www.rascal-mpl.org/). So to be able to install and run Trinity, Derric and Rascal must be installed first:

* To install Rascal, follow the [instructions on the Rascal site](http://www.rascal-mpl.org/Rascal/EclipseUpdate).
* When Rascal is installed, run an Eclipse instance with Rascal enabled. In it, open the latest version of the [Derric compiler](http://github.com/jvdb/derric).
* In the same workspace, also open the latest version of [Trinity](http://github.com/jvdb/trinity).

## Running the IDE ##

To run the IDE, go to the `trinity` project and execute the `org.derric_lang.trinity.Main` class as a Java application in Eclipse.

## License ##
Copyright 2013 Netherlands Forensic Institute and Centrum Wiskunde & Informatica

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.