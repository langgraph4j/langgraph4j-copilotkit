import { HttpAgent } from "@ag-ui/client"

const HTTP_URL = process.env.HTTP_URL || "http://localhost:8081/sse/default";

export function createDefaultAgent(): HttpAgent {
  return new HttpAgent({
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
    
});
}
