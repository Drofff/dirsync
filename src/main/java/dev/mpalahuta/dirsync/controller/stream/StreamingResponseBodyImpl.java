package dev.mpalahuta.dirsync.controller.stream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamingResponseBodyImpl implements StreamingResponseBody {

    private final InputStream inputStream;

    public StreamingResponseBodyImpl(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void writeTo(@NonNull OutputStream outputStream) throws IOException {
        IOUtils.copy(this.inputStream, outputStream);
    }

}
