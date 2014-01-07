package cn.zzfyip.search.common.support;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.postgresql.util.PGobject;

/**
 * 实现描述：PG复杂类型转换
 * 
 * @author zixin.zhang
 * @version v1.0.0
 * @see
 * @since 13-8-8下午3:35
 */
@SuppressWarnings("rawtypes")
public class PGHStore extends PGobject implements Serializable, Cloneable, Map {

    private static class Parser {
        private final static int GV_INESCVAL = 2;
        private final static int GV_INVAL = 1;
        private final static int GV_WAITESCESCIN = 4;
        private final static int GV_WAITESCIN = 3;

        private final static int GV_WAITVAL = 0;
        private final static int WDEL = 4;

        private final static int WEQ = 2;
        private final static int WGT = 3;
        private final static int WKEY = 0;
        private final static int WVAL = 1;
        private StringBuffer cur;

        private boolean escaped;
        private List keys;
        private int ptr;
        private String value;
        private List values;

        public Parser() {
        }

        private boolean getValue(boolean ignoreEqual) throws SQLException {
            int state = Parser.GV_WAITVAL;

            cur = new StringBuffer();
            escaped = false;

            while (true) {
                boolean atEnd = value.length() == ptr;
                char c = '\0';
                if (!atEnd) {
                    c = value.charAt(ptr);
                }

                if (state == Parser.GV_WAITVAL) {
                    if (c == '"') {
                        escaped = true;
                        state = Parser.GV_INESCVAL;
                    } else if (c == '\0') {
                        return false;
                    } else if (c == '=' && !ignoreEqual) {
                        throw new SQLException("KJJ");
                    } else if (c == '\\') {
                        state = Parser.GV_WAITESCIN;
                    } else if (!Character.isWhitespace(c)) {
                        cur.append(c);
                        state = Parser.GV_INVAL;
                    }
                } else if (state == Parser.GV_INVAL) {
                    if (c == '\\') {
                        state = Parser.GV_WAITESCIN;
                    } else if (c == '=' && !ignoreEqual) {
                        ptr--;
                        return true;
                    } else if (c == ',' && ignoreEqual) {
                        ptr--;
                        return true;
                    } else if (Character.isWhitespace(c)) {
                        return true;
                    } else if (c == '\0') {
                        ptr--;
                        return true;
                    } else {
                        cur.append(c);
                    }
                } else if (state == Parser.GV_INESCVAL) {
                    if (c == '\\') {
                        state = Parser.GV_WAITESCESCIN;
                    } else if (c == '"') {
                        return true;
                    } else if (c == '\0') {
                        throw new SQLException("KJJ, unexpected end of string");
                    } else {
                        cur.append(c);
                    }
                } else if (state == Parser.GV_WAITESCIN) {
                    if (c == '\0') {
                        throw new SQLException("KJJ, unexpected end of string");
                    }

                    cur.append(c);
                    state = Parser.GV_INVAL;
                } else if (state == Parser.GV_WAITESCESCIN) {
                    if (c == '\0') {
                        throw new SQLException("KJJ, unexpected end of string");
                    }

                    cur.append(c);
                    state = Parser.GV_INESCVAL;
                } else {
                    throw new SQLException("KJJ");
                }

                ptr++;
            }
        }

        @SuppressWarnings("unchecked")
        private Map parse(String value) throws SQLException {
            this.value = value;
            ptr = 0;
            keys = new ArrayList();
            values = new ArrayList();

            parseHStore();

            Map map = new HashMap();
            for (int i = 0; i < keys.size(); i++) {
                map.put(keys.get(i), values.get(i));
            }

            return map;
        }

        @SuppressWarnings("unchecked")
        private void parseHStore() throws SQLException {
            int state = Parser.WKEY;
            escaped = false;

            while (true) {
                char c = '\0';
                if (ptr < value.length()) {
                    c = value.charAt(ptr);
                }

                if (state == Parser.WKEY) {
                    if (!getValue(false)) {
                        return;
                    }

                    keys.add(cur.toString());
                    cur = null;
                    state = Parser.WEQ;
                } else if (state == Parser.WEQ) {
                    if (c == '=') {
                        state = Parser.WGT;
                    } else if (state == '\0') {
                        throw new SQLException("KJJ, unexpected end of string");
                    } else if (!Character.isWhitespace(c)) {
                        throw new SQLException("KJJ, syntax err");
                    }
                } else if (state == Parser.WGT) {
                    if (c == '>') {
                        state = Parser.WVAL;
                    } else if (c == '\0') {
                        throw new SQLException("KJJ, unexpected end of string");
                    } else {
                        throw new SQLException("KJJ, syntax err [" + c + "] at " + ptr);
                    }
                } else if (state == Parser.WVAL) {
                    if (!getValue(true)) {
                        throw new SQLException("KJJ, unexpected end of string");
                    }

                    String val = cur.toString();
                    cur = null;
                    if (!escaped && "null".equalsIgnoreCase(val)) {
                        val = null;
                    }

                    values.add(val);
                    state = Parser.WDEL;
                } else if (state == Parser.WDEL) {
                    if (c == ',') {
                        state = Parser.WKEY;
                    } else if (c == '\0') {
                        return;
                    } else if (!Character.isWhitespace(c)) {
                        throw new SQLException("KJJ, syntax err");
                    }
                } else {
                    throw new SQLException("KJJ unknown state");
                }

                ptr++;
            }
        }
    }

    private final static long serialVersionUID = 1;

    private static void writeValue(StringBuffer buf, Object o) {
        if (o == null) {
            buf.append("NULL");
            return;
        }

        String s = o.toString();

        buf.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' || c == '\\') {
                buf.append('\\');
            }
            buf.append(c);
        }
        buf.append('"');
    }

    private Map _map;

    /**
     * required by the driver
     */
    public PGHStore() {
        setType("hstore");
        _map = new HashMap();
    }

    public PGHStore(Map map) {
        this();
        setValue(map);
    }

    /**
     * Initialize a hstore with a given string representation
     * 
     * @param value String representated hstore
     * @throws java.sql.SQLException Is thrown if the string representation has an unknown format
     * @see #setValue(String)
     */
    public PGHStore(String value) throws SQLException {
        this();
        setValue(value);
    }

    @Override
    public void clear() {
        _map.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return _map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return _map.containsValue(value);
    }

    @Override
    public Set entrySet() {
        return _map.entrySet();
    }

    /**
     * Returns whether an object is equal to this one or not
     * 
     * @param obj Object to compare with
     * @return true if the two hstores are identical
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        PGHStore other = (PGHStore) obj;
        if (_map == null) {
            if (other._map != null) {
                return false;
            }
        } else if (!_map.equals(other._map)) {
            return false;
        }

        return true;
    }

    // Farm out all the work to the real underlying map.

    @Override
    public Object get(Object key) {
        return _map.get(key);
    }

    public Map getMap() {
        return _map;
    }

    /**
     * Returns the stored information as a string
     * 
     * @return String represented hstore
     */
    @Override
    public String getValue() {
        StringBuffer buf = new StringBuffer();
        Iterator i = _map.keySet().iterator();
        boolean first = true;
        while (i.hasNext()) {
            Object key = i.next();
            Object value = _map.get(key);

            if (first) {
                first = false;
            } else {
                buf.append(',');
            }

            PGHStore.writeValue(buf, key);
            buf.append("=>");
            PGHStore.writeValue(buf, value);
        }

        return buf.toString();
    }

    @Override
    public int hashCode() {
        return _map.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return _map.isEmpty();
    }

    @Override
    public Set keySet() {
        return _map.keySet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object put(Object key, Object value) {
        return _map.put(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void putAll(Map m) {
        _map.putAll(m);
    }

    @Override
    public Object remove(Object key) {
        return _map.remove(key);
    }

    public void setValue(Map map) {
        _map = map;
    }

    public void setValue(PGobject pgobject) throws SQLException {
        setValue(pgobject.toString());
    }

    /**
     */
    @Override
    public void setValue(String value) throws SQLException {
        Parser p = new Parser();
        _map = p.parse(value);
    }

    @Override
    public int size() {
        return _map.size();
    }

    @Override
    public Collection values() {
        return _map.values();
    }
}
