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

import com.vsetec.sip.util.MapOfLists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fedd
 */
public abstract class MessageReceived implements Message, Received {

    private final String _firstLine;
    private final Map<String, List<Object>> _headers = new MapOfLists();
    private final InputStream _body;

    MessageReceived(InputStream source) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(source, "UTF-8"));

        // read first line
        _firstLine = reader.readLine();

        // read headers
        String header = reader.readLine();
        while (header.length() > 0) {
            int idx = header.indexOf(":");
            if (idx == -1) {
                continue;
            }

            String headerName = header.substring(0, idx);
            String headerValue = header.substring(idx + 1, header.length());

            List<Object> values = _headers.get(headerName);
            values.add(headerValue);

            header = reader.readLine();
        }

        // make the rest as body
        _body = source;

    }

    final String getFirstLine() {
        return _firstLine;
    }

    @Override
    public Map<String, List<Object>> getHeaders() {
        return _headers;
    }

    @Override
    public InputStream getBody() {
        return _body;
    }

    public abstract MessageSendable getToForward();

}
