import {
  CopilotRuntime,
  ExperimentalEmptyAdapter,
  copilotRuntimeNextJSAppRouterEndpoint,
} from '@copilotkit/runtime';
import { LangGraphHttpAgent } from '@copilotkit/runtime/langgraph';
import { NextRequest } from 'next/server';

const serviceAdapter = new ExperimentalEmptyAdapter();

const runtime = new CopilotRuntime({
  agents: {
    default: new LangGraphHttpAgent({
      url:
        process.env.NEXT_PUBLIC_LANGGRAPH_URL ||
        'http://localhost:8085/langgraph4j/copilotkit',
    }),
  },
});

export const POST = async (req: NextRequest) => {
  const { handleRequest } = copilotRuntimeNextJSAppRouterEndpoint({
    runtime,
    serviceAdapter,
    endpoint: '/api/langgraph4j',
  });

  return handleRequest(req);
};
