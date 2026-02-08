import {
  CopilotRuntime,
  ExperimentalEmptyAdapter,
  copilotRuntimeNextJSAppRouterEndpoint,
} from "@copilotkit/runtime";

import { NextRequest } from "next/server";
import { MCPAppsMiddleware } from "@ag-ui/mcp-apps-middleware";
import { HttpAgent } from "@ag-ui/client";

// 1. Base address for the Mastra server
const HTTP_URL = process.env.HTTP_URL || "http://localhost:8080/sse/1";

// 2. You can use any service adapter here for multi-agent support. We use
//    the empty adapter since we're only using one agent.
const serviceAdapter = new ExperimentalEmptyAdapter();

// 1. Define the agent middleware
const middlewares = [
  // 1.1. MCP Apps Middleware
  new MCPAppsMiddleware({
    mcpServers: [
      {
        type: "http",
        url: "http://localhost:3001/mcp",
        serverId: "time-server" // Recommended: stable identifier
      },
    ],
  }),
  // 1.2. More middlewares can be added here
]

const httpAgent = new HttpAgent({
    url: HTTP_URL,
    initialState: {
      'language': 'NL'
    },
    initialMessages: [
      {
        id: '1',
        role: 'user',
        content: 'Initial message of the user'
      },
      {
        id: '2',
        role: 'assistant',
        content: 'Hi user!'
      }
    ]
    
})

// 3. Apply the middleware to the agent
for (const middleware of middlewares) {
  httpAgent.use( middleware as any)
}

const runtime = new CopilotRuntime({
  agents: {
      'agent': httpAgent as any
  }
});

// 4. Build a Next.js API route that handles the CopilotKit runtime requests.
export const POST = async (req: NextRequest) => {
  const { handleRequest } = copilotRuntimeNextJSAppRouterEndpoint({
    runtime,
    serviceAdapter,
    endpoint: "/api/mcpapps",
  });

  return handleRequest(req);
};