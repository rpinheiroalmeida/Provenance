# AProvBio

Scientific experiments in bioinformatics are often executed as computational workflows. 
Following the fundamental science premise of reproducibility, frequently it is necessary to re-run an experiment under the original circumstances in which it was initially run to recognize and validate it.
Data provenance concerns the origin of data.
 
Knowing the data source facilitates the understanding and analysis of the results by detailing and documenting the history and the paths of the input data, from the beginning to the end of an experiment. 
Therefore, in this context, data provenance can be applied when experimenting with traceability. 

AProvBio is an architecture that can perform the data provenance of scientific experiments in bioinformatics automatically, using the provenance data model PROV-DM.
The architecture can perform the automatic provenance type prospectively, retrospectively and with user-defined data. 
Thus, the architecture stores and captures information obtained during the execution of the data generation processes with user-defined data information, such as features and versions of the programs used. 

As a graph can express the PROV-DM, we designed a graph model for storing the data provenance in a graph database. 
It allows for more natural modeling, as well as expressing queries at a more natural level, and the implementation of efficient algorithms to perform specific operations.
