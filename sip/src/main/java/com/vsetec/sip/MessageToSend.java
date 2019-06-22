/*
 * Copyright 2019 fedd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vsetec.sip;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fedd
 */
public abstract class MessageToSend implements Message {

    private final String _firstLine;
    private final LinkedHashMap<String, List<String>> _headers;
    private final InputStream _body;

    public MessageToSend(String firstLine, LinkedHashMap<String, List<String>> headers, InputStream body) {

        _firstLine = firstLine;
        _headers = new LinkedHashMap(headers);
        _body = body;

    }

    @Override
    public final String getFirstLine() {
        return _firstLine;
    }

    @Override
    public LinkedHashMap<String, List<String>> getHeaders() {
        return _headers;
    }

    public InputStream getStream() {

        InputStream ret = new InputStream() {

            private boolean _switched = false;
            private InputStream _currentStream;

            {
                StringBuilder h = new StringBuilder(_firstLine);
                h.append("\r\n");
                for (Map.Entry<String, List<String>> kv : _headers.entrySet()) {
                    String headerName = kv.getKey();
                    List<String> vias = kv.getValue();
                    for (String via : vias) {
                        h.append(headerName);
                        h.append(": ");
                        h.append(via);
                        h.append("\r\n");
                    }
                }
                h.append("\r\n");

                _currentStream = new ByteArrayInputStream(h.toString().getBytes(StandardCharsets.UTF_8));
            }

            @Override
            public int read() throws IOException {
                int ret = _currentStream.read();
                if (ret == -1) {
                    if (_switched) {
                        return ret;
                    }
                    _currentStream = _body;
                    _switched = true;
                    ret = _currentStream.read();
                }
                return ret;
            }
        };
        return ret;
    }
}
