
For this problem, we have successfully connected the SBOL Stack to
JBEI ICE, so that queries to the Stack can retrieve data from ICE
registry instances.

The current version of JBEI-ICE serializes SBOL1 and then converts it to SBOL2.
This discards lots of the useful metadata that ICE stores in its database, which
could easily be represented in SBOL2 using a custom namespace.  Our modified
version of ICE implements a complete SBOL2 serializer, complete with
serialization of the ICE metadata.

We have also extended the SBOL Stack to connect to this new modified ICE
instance by extending the existing federation functionality present in the
Stack API.


Layout
------

* The `ice/` directory contains a modified version of JBEI-ICE
  with support for annotated SBOL2.0.

* The `sbolstack-api/` directory contains a modified version of
  the SBOL Stack API with support for federation of queries to
  ICE repositories.


Modifications
-------------

* `SBOLFormatter` and `SBOLVisitor` in `ice/lib/entry/sequence/composers/formatters`
   have been renamed to `SBOL1Formatter` and `SBOL1Visitor`, and their support for
   conversion to SBOL2 has been removed.  The `SBOL2Formatter` and `SBOL2Visitor`
   classes have been added, and the `ice/lib/entry/sequence/SequenceController.java`
   has been updated to use the correct SBOL formatter version depending on the
   requested type.

 * The `federate` function in `sbolstack-api/lib/federate.js` has been modified
   to accept a list of endpoints to access instead of just one.  The files
   in `sbolstack-api/lib/ice` have been added to implement endpoints to search
   metadata, retrieve SBOL from a URI, and negotiate session tokens.


Future Work
-----------

Currently, the SBOL Stack only federates two endpoints to ICE: search metadata,
and retrieve SBOL.  The SBOL Stack has many more endpoints which will need to
be converted to support federation to ICE registry instances.

It would also be useful to implement integration in the other direction, suc
that an ICE registry would be able to query an SBOL Stack as part of the web
of registries.





