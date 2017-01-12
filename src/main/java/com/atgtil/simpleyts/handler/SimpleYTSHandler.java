package com.atgtil.simpleyts.handler;

import ratpack.handling.Context;
import ratpack.handling.Handler;

public class SimpleYTSHandler implements Handler {
    @Override
    public void handle(Context ctx) throws Exception {
        ctx.header("content-type", "text/html");
        ctx.render("hello");
    }
}
