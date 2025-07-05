import {
  CopilotRuntime,
  CopilotRuntimeChatCompletionRequest,
  CopilotRuntimeChatCompletionResponse,
  CopilotServiceAdapter,
  copilotRuntimeNextJSAppRouterEndpoint,
} from '@copilotkit/runtime';
import { NextRequest } from 'next/server';
import { randomUUID } from "@copilotkit/shared";

// Base interface for common properties
interface BaseMessage {
  type: string;
  timestamp: number;
}

// Interface for RUN_STARTED
interface RunStarted extends BaseMessage {
  type: 'RUN_STARTED';
  thread_id: string;
}

// Interface for TEXT_MESSAGE_START
interface TextMessageStart extends BaseMessage {
  type: 'TEXT_MESSAGE_START';
  message_id: string;
  role: 'assistant' | 'user';  // Add other roles if needed
}

// Interface for TEXT_MESSAGE_CONTENT
interface TextMessageContent extends BaseMessage {
  type: 'TEXT_MESSAGE_CONTENT';
  message_id: string;
  delta: string;
}

// Interface for TEXT_MESSAGE_END
interface TextMessageEnd extends BaseMessage {
  type: 'TEXT_MESSAGE_END';
  message_id: string;
}

// Interface for RUN_FINISHED
interface RunFinished extends BaseMessage {
  type: 'RUN_FINISHED';
  thread_id: string;
}

interface ToolCallStart extends BaseMessage {
  type: 'TOOL_CALL_START';
  tool_call_id: string;
  tool_call_name: string; // Name of the tool being called
  parent_message_id?: string; // ID of the parent message that initiated the tool call
}

// Interface for TOOL_CALL_END
interface ToolCallEnd extends BaseMessage {
  type: 'TOOL_CALL_END';
  tool_call_id: string;
}

interface ToolCallArgs extends BaseMessage {
  type: 'TOOL_CALL_ARGS';
  tool_call_id: string;
  tool_call_args: string; // Arguments passed to the tool
}

// Union type for all possible message types
type Message = RunStarted | TextMessageStart | TextMessageContent | TextMessageEnd | RunFinished | ToolCallStart | ToolCallEnd | ToolCallArgs;

class Langgraph4jAdapter implements CopilotServiceAdapter {
  private abortController: AbortController;

  constructor() {
    this.abortController = new AbortController();
  }


  async process(request: CopilotRuntimeChatCompletionRequest): Promise<CopilotRuntimeChatCompletionResponse> {

    console.debug( "Processing request:", request );
    
    const {
      threadId: threadIdFromRequest,
      eventSource,
    } = request;

    const threadId = threadIdFromRequest ?? randomUUID();

    try {
      const response = await fetch('http://localhost:8080/langgraph4j/copilotkit', {
        signal: this.abortController.signal,
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(request),

      });

      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const reader = response.body?.pipeThrough(new TextDecoderStream()).getReader();
      if (!reader) {
        throw new Error('Response body is null');
      }

      eventSource.stream(async (eventStream$) => {

        try {
          let buffer = ''
          let fetchEvents = true

          while (fetchEvents) {

            const { done, value } = await reader.read();

            if (done) {
              fetchEvents = false;
              break;
            }

            buffer += value;

            // Split buffer by newlines and process complete messages
            const lines = buffer.split('\n');
            const lastLine = lines.pop(); // Keep the last incomplete line in buffer

            const regex = /^data:(.+)$/m;

            const messages: Message[] = lines.map(line => line.match(regex))
              .filter(match => match !== null)
              .map(match => JSON.parse(match![1])) // Non-null assertion since we filtered nulls
              ;
            
            if( lastLine ) {
              const m = lastLine.match(regex);
              if (m) {
                try {
                  messages.push(JSON.parse(m[1]));
                  buffer = ''; // Clear buffer
                } catch (error) {
                  buffer = lastLine; // Keep the last line in buffer for next iteration
                  console.warn("fetch is incomplete. LastLine :", lastLine);
                }
              }
            }
            else {
              buffer = ''; // Clear buffer if no last line
            }

            
            console.debug(`${threadId} - Fetched messages:`, messages);
            for (const message of messages) {
              switch (message.type) {
                case 'RUN_STARTED':
                  
                  break;
                case 'TEXT_MESSAGE_START':
                  eventStream$.sendTextMessageStart({
                    messageId: message.message_id
                  });
                  break;
                case 'TEXT_MESSAGE_CONTENT':
                  eventStream$.sendTextMessageContent({
                    messageId: message.message_id,
                    content: message.delta,
                  });
                  break;
                case 'TEXT_MESSAGE_END':
                  eventStream$.sendTextMessageEnd({
                    messageId: message.message_id,
                  });
                  break;
                case 'RUN_FINISHED':
                  fetchEvents = false;
                  break;
                case 'TOOL_CALL_START':
                  eventStream$.sendActionExecutionStart({
                    actionExecutionId: message.tool_call_id,
                    actionName: message.tool_call_name,
                    parentMessageId: message.parent_message_id,
                  });
                  break;
                case 'TOOL_CALL_ARGS':
                  eventStream$.sendActionExecutionArgs({
                    actionExecutionId: message.tool_call_id,
                    args: message.tool_call_args,
                  });
                  break;
                case 'TOOL_CALL_END':
                  eventStream$.sendActionExecutionEnd({
                    actionExecutionId: message.tool_call_id,
                  });
                  fetchEvents = false;
                  break;
                default:
                  // Handle unexpected message types
                  console.error('Unexpected message type:', message);
                  break;
              }

            }

          }
        } finally {
           console.debug("Processing messages completed:", threadId);
           eventStream$.complete();
        }
      });

    } catch (error: any) {
      if ("name" in error && error.name === 'AbortError') {
        console.warn('Fetch aborted');
      } else {
        throw error;
      }
    }

    return {
      threadId
    };
  }
}

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