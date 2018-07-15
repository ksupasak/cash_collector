package com.gexintec.gxt;

/**
 * Created by soup on 1/7/2018 AD.
 */

public class GXTMessage {



    GXTNode node;

    String msg;

    public GXTMessage(GXTNode node, String msg){
        this.node = node;
        this.msg = msg;
    }

    public GXTNode getNode() {
        return node;
    }

    public void setNode(GXTNode node) {
        this.node = node;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
