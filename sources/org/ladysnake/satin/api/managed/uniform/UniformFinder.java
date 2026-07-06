package org.ladysnake.satin.api.managed.uniform;

import org.apiguardian.api.API;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/managed/uniform/UniformFinder.class */
public interface UniformFinder {
    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    Uniform1i findUniform1i(String str);

    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    Uniform2i findUniform2i(String str);

    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    Uniform3i findUniform3i(String str);

    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    Uniform4i findUniform4i(String str);

    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    Uniform1f findUniform1f(String str);

    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    Uniform2f findUniform2f(String str);

    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    Uniform3f findUniform3f(String str);

    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    Uniform4f findUniform4f(String str);

    @API(status = API.Status.MAINTAINED, since = "1.4.0")
    UniformMat4 findUniformMat4(String str);

    @API(status = API.Status.EXPERIMENTAL, since = "1.4.0")
    SamplerUniform findSampler(String str);
}
