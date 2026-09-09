package com.agui.json.mixins;

import com.agui.community.core.interrupt.InterruptOutcome;
import com.agui.community.core.interrupt.SuccessOutcome;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonIgnoreProperties(value = "type", allowGetters = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = InterruptOutcome.class, name = "interrupt"),
        @JsonSubTypes.Type(value = SuccessOutcome.class, name = "success")
})
public class InterruptMixin {
}
