package org.fatmansoft.teach.payload.request;

import javax.validation.constraints.NotBlank;

public class EmailRequest {
    @NotBlank
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public EmailRequest(String username) {
        this.username = username;
    }
}
