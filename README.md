# Ninety-nine Code Challenge

## Overview

Ninety nine challenge has been a very interesting project to work with. Each component has been containerized and to gather all together the technology of docker compose has been used.

As the main language of the backend and the following lower layers has been kotlin, using jvm in its version 11. It was a way to understand the technology used in Ninety Nine.

As frontend technology, Angular 9 is used because I have enough experience using this framework, to create quickly a proper and interesting view of the whole project.


## Design

![Components](design.png)

## Implementation

 * Due the raw data could be very large, I prefer create a file with the transfer data instead of create a large string (avoid possible out memory problems and improve efficient memory consumption)

## Conclusions
* I spend more than usual because I use technologies that I am not familiar with, such us Gradle (I have used Maven and more recently I use C++ Conan and CMake deeply)
* A very good ideas would be automatize the entire product over a new release (a tag in github over master branch) to autodeploy in docker hub a new image using CircleCI or Travis.
* I would perform more unit test to ensure a better fullfilment, but due to time, I have left only a symbolic record to ensure I could perform test and I understand the full software life cycle
* I assume that the maintenance of the ftp server is performed by the banking partner, so the deletion of files to maintain a good health of the server (hard disk overflow) is out of scope.
* FTP Server has several persistent volumes, I suggest erase tmp/ftp-* after evaluate with this challenge.
