package org.bsc.langgraph4j.agui.sdk;

import com.agui.core.agent.RunAgentParameters;
import com.agui.core.context.Context;
import com.agui.core.message.BaseMessage;
import com.agui.core.state.State;
import com.agui.core.tool.Tool;

import java.util.List;

public class AGUIParameters {

    private String threadId;
    private String runId;
    private List<Tool> tools;
    private List<Context> context;
    private Object forwardedProps;
    private List<BaseMessage> messages;
    private State state;

    /**
     * Sets the conversation thread identifier.
     *
     * @param threadId the unique identifier for the conversation thread
     */
    public void setThreadId(final String threadId) {
        this.threadId = threadId;
    }

    /**
     * Gets the conversation thread identifier.
     *
     * @return the thread identifier, or null if not set
     */
    public String getThreadId() {
        return this.threadId;
    }

    /**
     * Sets the unique run identifier for this execution.
     *
     * @param runId the unique identifier for this agent run
     */
    public void setRunId(final String runId) {
        this.runId = runId;
    }

    /**
     * Gets the unique run identifier for this execution.
     *
     * @return the run identifier, or null if not set
     */
    public String getRunId() {
        return runId;
    }

    /**
     * Sets the list of tools available to the agent during execution.
     *
     * @param tools the list of available tools, or null if no tools are available
     */
    public void setTools(final List<Tool> tools) {
        this.tools = tools;
    }

    /**
     * Gets the list of tools available to the agent during execution.
     *
     * @return the list of available tools, or null if not set
     */
    public List<Tool> getTools() {
        return tools;
    }

    /**
     * Sets the list of context objects providing additional execution information.
     *
     * @param context the list of context objects, or null if no additional context is needed
     */
    public void setContext(final List<Context> context) {
        this.context = context;
    }

    /**
     * Gets the list of context objects providing additional execution information.
     *
     * @return the list of context objects, or null if not set
     */
    public List<Context> getContext() {
        return this.context;
    }

    /**
     * Sets the forwarded properties object containing arbitrary additional configuration.
     *
     * @param forwardedProps the forwarded properties object, or null if not needed
     */
    public void setForwardedProps(final Object forwardedProps) {
        this.forwardedProps = forwardedProps;
    }

    /**
     * Gets the forwarded properties object containing arbitrary additional configuration.
     *
     * @return the forwarded properties object, or null if not set
     */
    public Object getForwardedProps() {
        return this.forwardedProps;
    }

    /**
     * Sets the conversation message history.
     *
     * @param messages the list of conversation messages, or null for empty history
     */
    public void setMessages(final List<BaseMessage> messages) {
        this.messages = messages;
    }

    /**
     * Gets the conversation message history.
     *
     * @return the list of conversation messages, or null if not set
     */
    public List<BaseMessage> getMessages() {
        return this.messages;
    }

    /**
     * Sets the agent state containing persistent context and configuration.
     *
     * @param state the agent state object, or null for default empty state
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Gets the agent state containing persistent context and configuration.
     *
     * @return the agent state object, or null if not set
     */
    public State getState() {
        return state;
    }


    public RunAgentParameters toRunAgentParameters() {
        return RunAgentParameters.builder()
                .threadId(getThreadId())
                .runId(getRunId())
                .messages(getMessages())
                .tools(getTools())
                .context(getContext())
                .forwardedProps(getForwardedProps())
                .state(getState())
                .build();
    }
}
