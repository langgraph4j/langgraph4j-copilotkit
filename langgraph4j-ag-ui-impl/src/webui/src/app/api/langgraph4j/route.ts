import {
  CopilotRuntime,
  copilotRuntimeNextJSAppRouterEndpoint,
} from '@copilotkit/runtime';
import { NextRequest } from 'next/server';
import { Langgraph4jAdapter } from '@/app/lib/langgraph4j';


const serviceAdapter = new Langgraph4jAdapter();

const runtime = new CopilotRuntime({});

export const POST = async (req: NextRequest) => {

  const { handleRequest } = copilotRuntimeNextJSAppRouterEndpoint({
    runtime,
    serviceAdapter,
    endpoint: "/api/langgraph4j",
  });

  return handleRequest(req);
};