package com.example.craftcorner;

import androidx.lifecycle.ViewModel;

public class NotificationViewModel extends ViewModel {

    String body;
    String title;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
