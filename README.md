# LangGraph4j support for CopilotKit

Make [LangGraph4j] compliant with [AG-UI protocol][AG-UI] with [CopilotKit] integration

## Architecture

```mermaid
flowchart LR
    User((User))
    CopilotKit(Copilot Kit)
    LangGraph4JAdaptor(LangGraph4J AGUI Adaptor
    Typescript)
    LangGraph4JServer(LangGraph4J AGUI Adaptor
    Java)
    Agent(Agent)
    subgraph "AG-UI-APP"
        CopilotKit --> LangGraph4JAdaptor
    end
    subgraph "LangGraph4J Server"
        LangGraph4JServer --> Agent
    end
    User --> CopilotKit
    %%CopilotKit --> LangGraph4JAdaptor
    LangGraph4JAdaptor --> LangGraph4JServer
    %%LangGraph4JServer --> Agent
    Agent --> LangGraph4JServer
    LangGraph4JServer --> LangGraph4JAdaptor
    LangGraph4JAdaptor --> CopilotKit
    CopilotKit --> User
    %% Legend
    %% - The User sends a request to the Copilot Kit.
    %% - The Copilot Kit processes the request and passes it to the LangGraph4J Adaptor.
    %% - The LangGraph4J Adaptor forwards the request to the LangGraph4J Server.
    %% - The LangGraph4J Server processes the request using the Agent.
    %% - The Agent processes the data and sends back a response to the LangGraph4J Server.
    %% - The LangGraph4J Server sends the processed response back to the LangGraph4J Adaptor.
    %% - The LangGraph4J Adaptor then sends the response back to the Copilot Kit.
    %% - Finally, the Copilot Kit presents the results back to the User.

```
## Getting Started

### start LangGraph4j Agent**

```bash
mvn package spring-boot:test-run
```

### start CopilotKit App**

```bash
cd webui
npm run dev
```

### open app

Open browser on [http://localhost:3000](http://localhost:3000) and play with chat

![chat](chat.png)




[AG-UI]: https://docs.ag-ui.com/introduction
[CopilotKit]: https://www.copilotkit.ai
[LangGraph4j]: https://github.com/langgraph4j/langgraph4j
