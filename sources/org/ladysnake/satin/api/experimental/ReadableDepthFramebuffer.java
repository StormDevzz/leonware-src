package org.ladysnake.satin.api.experimental;

import org.apiguardian.api.API;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/experimental/ReadableDepthFramebuffer.class */
@API(status = API.Status.EXPERIMENTAL)
public interface ReadableDepthFramebuffer {
    @API(status = API.Status.EXPERIMENTAL)
    int getStillDepthMap();

    @API(status = API.Status.EXPERIMENTAL)
    void freezeDepthMap();
}
