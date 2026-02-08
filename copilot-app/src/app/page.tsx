"use client";
import { useCoAgent } from "@copilotkit/react-core"; 
import { SimpleChatWithApproval } from "./component/chatApproval";

export default function Page() {
  const { state, setState } = useCoAgent<any>({ 
    name: "agent",
    // optionally provide a type-safe initial state
    initialState: { language: "english" }  
  });

  const toggleLanguage = () => {
    setState({ language: state.language === "english" ? "spanish" : "english" }); 
  };


  return (
    <main>
      <h1>Your App</h1>
      <p>Language: {state.language}</p> 
      <button onClick={toggleLanguage}>Toggle Language</button>

      <SimpleChatWithApproval/>

    </main>
  );
}