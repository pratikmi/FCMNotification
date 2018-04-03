package com.mindinventory.fcmnotification.model;

/**
 * Created by mind on 26/10/17.
 */

public class Data {
    private String icon;

    private String body;

    private String title;

    private String sound;

    public String getChannelId() {
        if (channelId == null) {
            return "";
        } else {
            return channelId;
        }
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    private String channelId;

    public String getChannelName() {
        if (channelName == null) {
            return "";
        } else {
            return channelName;
        }
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    private String channelName;

    public String getIcon() {
        if (icon == null) {
            return "";
        } else {
            return icon;
        }
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBody() {
        if (body == null) {
            return "";
        } else {
            return body;
        }
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        if (title == null) {
            return "";
        } else {
            return title;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSound() {
        if (sound == null) {
            return "";
        } else {
            return sound;
        }
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    @Override
    public String toString() {
        return "ClassPojo [icon = " + icon + ", body = " + body + ", title = " + title + ", sound = " + sound + "]";
    }
}
