package com.mycarx.wxweb.web.service;

import com.mycarx.wxweb.web.domain.JsonSerializable;

public class PushMessage extends JsonSerializable {
    public MessageType type;
    public String content;

    public PushMessage() {
    }

    public PushMessage(MessageType type, String content) {
        this.type = type;
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
