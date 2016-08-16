
BDathlon 2016
=============

Problem 1
---------

We have implemented the repressilator circuit and a Turing pattern circuit in
Eugene.  We have documented issues we found with Eugene in `Problem1/eugene.pdf`.


Problem 2
---------

We have implemented a Java application which can convert a subset of BioPAX to
SBOL2.  The source and readme can be found in the `Problem2` directory.




Problem 3
---------

ran out of time sorry :-(




Problem 4
---------

We have modified both the SBOL Stack and JBEI ICE.  Our modified version
of JBEI-ICE supports annotated SBOL2 support, which is a major improvement
over the existing SBOL implementation (which serialized SBOL1 and converted
it to SBOL2).  Our modified version of the SBOL Stack features federation of
queries to JBEI-ICE registry instances, meaning queries to the SBOL Stack can
now incorporate data from ICE.

A list of modified files can be found in the README files in the respective
directories under `Problem4`.


