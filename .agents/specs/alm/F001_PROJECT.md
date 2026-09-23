# F001 add new maven module to manage spring boot implementation

## Instructions

I want increase project modularity adding a new maven module to decouple the spring boot implementation by the [langgraph4j-ag-ui-sdk] module.

* Create a new maven module named `langgraph4j-ag-ui-springboot` that will contain the spring boot implementation of the [langgraph4j-ag-ui-sdk] module.
* Remove the spring boot implementation from the [langgraph4j-ag-ui-sdk] module and move it to the new `langgraph4j-ag-ui-springboot` module.
* The new maven module coordinate must be `org.bsc.langgraph4j:langgraph4j-ag-ui-springboot:0.2-SNAPSHOT` and its parent must be the `org.bsc.langgraph4j:langgraph4j-ag-ui-parent:0.2-SNAPSHOT` project.
* Follow the same structure of the [langgraph4j-ag-ui-sdk] module for the new `langgraph4j-ag-ui-springboot` module.
* Follow in the new pom.xml the same pattern to add project name and description

