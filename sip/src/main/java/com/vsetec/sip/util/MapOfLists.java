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
package com.vsetec.sip.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.ListOrderedMap;

/**
 *
 * Magic Map that always has Lists as it's values
 *
 * @author fedd
 */
public class MapOfLists extends AbstractMap<String, List<Object>> {

    private final ListOrderedMap _wrapped = new ListOrderedMap();

    @Override
    public Set<Entry<String, List<Object>>> entrySet() {
        Set<Entry<String, List<Object>>> entrySet = new AbstractSet<Entry<String, List<Object>>>() {

            final private Set ws = _wrapped.entrySet();

            @Override
            public Iterator<Entry<String, List<Object>>> iterator() {
                Iterator iter = ws.iterator();

                return new Iterator<Entry<String, List<Object>>>() {
                    @Override
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    @Override
                    public Entry<String, List<Object>> next() {
                        Entry entry = (Entry) iter.next();
                        return new Entry<String, List<Object>>() {
                            @Override
                            public String getKey() {
                                return (String) entry.getKey();
                            }

                            @Override
                            public List<Object> getValue() {
                                return (List<Object>) entry.getValue();
                            }

                            @Override
                            public List<Object> setValue(List<Object> value) {
                                synchronized (MapOfLists.this) {
                                    return (List<Object>) entry.setValue(value);
                                }
                            }
                        };
                    }
                };

            }

            @Override
            public int size() {
                return _wrapped.size();
            }
        };
        return entrySet;
    }

    @Override
    public synchronized List<Object> get(Object key) {
        List<Object> ret = (List<Object>) _wrapped.get(key);
        if (ret == null && key instanceof String) {
            ret = new ArrayList<>(4);
            _wrapped.put((String) key, ret);
        }
        return ret;
    }

    public synchronized List<Object> put(String key, Object value) {
        if (value instanceof List) {
            return (List<Object>) _wrapped.put(key, value);
        } else {
            ArrayList list = new ArrayList(4);
            list.add(value);
            return (List<Object>) _wrapped.put(key, list);
        }
    }

    public List<Object> getAt(int index) {
        return (List<Object>) _wrapped.getValue(index);
    }

    public synchronized void putAt(String key, Object value, int index) {
        if (value instanceof List) {
            _wrapped.put(index, key, value);
        } else {
            ArrayList list = new ArrayList(4);
            list.add(value);
            _wrapped.put(index, key, list);
        }
    }

    @Override
    public synchronized void putAll(Map<? extends String, ? extends List<Object>> m) {
        _wrapped.putAll(m);
    }

}
