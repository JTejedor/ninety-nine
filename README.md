# Ninety-nine Code Challenge

## Overview

## Design

## Implementation

 * Due the raw data could be very large, I prefer create a file with the transfer data instead of create a large string (avoid possible out memory problems and improve efficient memory consumption)

## Conclusions

* A very good idea would be automatize the entire product when a new release is tagged in master branch to auto-deploy in docker hub a new image using CircleCI or Travis.
* I would perform more unit test to ensure a better fulfillment, but due to time, I have left only a symbolic record to ensure I could perform test and I understand the full software life cycle
* I assume that the maintenance of the ftp server is performed by the banking partner, so the deletion of files to maintain a good health of the server (hard disk overflow) is out of scope.
* FTP Server has several persistent volumes, I suggest erase tmp/ftp-* after evaluate with this challenge.
