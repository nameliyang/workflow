package com.simple.workflow.impl;


import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;

import com.simple.workflow.Context;


public class ContextBase extends HashMap implements Context {

    // ------------------------------------------------------------ Constructors


    /**
     * Default, no argument constructor.
     */
    public ContextBase() {

        super();
        initialize();

    }

    public ContextBase(Map map) {

        super(map);
        initialize();
        putAll(map);

    }

    // ------------------------------------------------------ Instance Variables

    private transient Map descriptors = null;

    private transient PropertyDescriptor[] pd = null;


    private static Object singleton;

    static {

        singleton = new Serializable() {
            public boolean equals(Object object) {
                return (false);
            }
        };

    }


    private static Object[] zeroParams = new Object[0];

    // ------------------------------------------------------------- Map Methods


    public void clear() {

        if (descriptors == null) {
            super.clear();
        } else {
            Iterator keys = keySet().iterator();
            while (keys.hasNext()) {
                Object key = keys.next();
                if (!descriptors.containsKey(key)) {
                    keys.remove();
                }
            }
        }

    }


    public boolean containsValue(Object value) {

        // Case 1 -- no local properties
        if (descriptors == null) {
            return (super.containsValue(value));
        }

        // Case 2 -- value found in the underlying Map
        else if (super.containsValue(value)) {
            return (true);
        }

        // Case 3 -- check the values of our readable properties
        for (int i = 0; i < pd.length; i++) {
            if (pd[i].getReadMethod() != null) {
                Object prop = readProperty(pd[i]);
                if (value == null) {
                    if (prop == null) {
                        return (true);
                    }
                } else if (value.equals(prop)) {
                    return (true);
                }
            }
        }
        return (false);

    }


    public Set entrySet() {

        return (new EntrySetImpl());

    }


    public Object get(Object key) {

        // Case 1 -- no local properties
        if (descriptors == null) {
            return (super.get(key));
        }

        // Case 2 -- this is a local property
        if (key != null) {
            PropertyDescriptor descriptor =
                    (PropertyDescriptor) descriptors.get(key);
            if (descriptor != null) {
                if (descriptor.getReadMethod() != null) {
                    return (readProperty(descriptor));
                } else {
                    return (null);
                }
            }
        }

        // Case 3 -- retrieve value from our underlying Map
        return (super.get(key));

    }


    public boolean isEmpty() {

        // Case 1 -- no local properties
        if (descriptors == null) {
            return (super.isEmpty());
        }

        // Case 2 -- compare key count to property count
        return (super.size() <= descriptors.size());

    }


    public Set keySet() {
        return (super.keySet());
    }


    public Object put(Object key, Object value) {
        // Case 1 -- no local properties
        if (descriptors == null) {
            return (super.put(key, value));
        }

        // Case 2 -- this is a local property
        if (key != null) {
            PropertyDescriptor descriptor =
                    (PropertyDescriptor) descriptors.get(key);
            if (descriptor != null) {
                Object previous = null;
                if (descriptor.getReadMethod() != null) {
                    previous = readProperty(descriptor);
                }
                writeProperty(descriptor, value);
                return (previous);
            }
        }

        // Case 3 -- store or replace value in our underlying map
        return (super.put(key, value));

    }



    public void putAll(Map map) {

        Iterator pairs = map.entrySet().iterator();
        while (pairs.hasNext()) {
            Map.Entry pair = (Map.Entry) pairs.next();
            put(pair.getKey(), pair.getValue());
        }

    }


    public Object remove(Object key) {

        // Case 1 -- no local properties
        if (descriptors == null) {
            return (super.remove(key));
        }

        // Case 2 -- this is a local property
        if (key != null) {
            PropertyDescriptor descriptor =
                    (PropertyDescriptor) descriptors.get(key);
            if (descriptor != null) {
                throw new UnsupportedOperationException
                        ("Local property '" + key + "' cannot be removed");
            }
        }

        // Case 3 -- remove from underlying Map
        return (super.remove(key));

    }



    public Collection values() {

        return (new ValuesImpl());

    }

    // --------------------------------------------------------- Private Methods


    private Iterator entriesIterator() {

        return (new EntrySetIterator());

    }


    private Map.Entry entry(Object key) {

        if (containsKey(key)) {
            return (new MapEntryImpl(key, get(key)));
        } else {
            return (null);
        }

    }



    private void initialize() {

        // Retrieve the set of property descriptors for this Context class
        try {
            pd = Introspector.getBeanInfo
                    (getClass()).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            pd = new PropertyDescriptor[0]; // Should never happen
        }

        // Initialize the underlying Map contents
        for (int i = 0; i < pd.length; i++) {
            String name = pd[i].getName();

            // Add descriptor (ignoring getClass() and isEmpty())
            if (!("class".equals(name) || "empty".equals(name))) {
                if (descriptors == null) {
                    descriptors = new HashMap((pd.length - 2));
                }
                descriptors.put(name, pd[i]);
                super.put(name, singleton);
            }
        }

    }



    private Object readProperty(PropertyDescriptor descriptor) {

        try {
            Method method = descriptor.getReadMethod();
            if (method == null) {
                throw new UnsupportedOperationException
                        ("Property '" + descriptor.getName()
                                + "' is not readable");
            }
            return (method.invoke(this, zeroParams));
        } catch (Exception e) {
            throw new UnsupportedOperationException
                    ("Exception reading property '" + descriptor.getName()
                            + "': " + e.getMessage());
        }

    }


    private boolean remove(Map.Entry entry) {

        Map.Entry actual = entry(entry.getKey());
        if (actual == null) {
            return (false);
        } else if (!entry.equals(actual)) {
            return (false);
        } else {
            remove(entry.getKey());
            return (true);
        }

    }


    private Iterator valuesIterator() {

        return (new ValuesIterator());

    }



    private void writeProperty(PropertyDescriptor descriptor, Object value) {

        try {
            Method method = descriptor.getWriteMethod();
            if (method == null) {
                throw new UnsupportedOperationException
                        ("Property '" + descriptor.getName()
                                + "' is not writeable");
            }
            method.invoke(this, new Object[]{value});
        } catch (Exception e) {
            throw new UnsupportedOperationException
                    ("Exception writing property '" + descriptor.getName()
                            + "': " + e.getMessage());
        }

    }

    // --------------------------------------------------------- Private Classes



    private class EntrySetImpl extends AbstractSet {

        public void clear() {
            ContextBase.this.clear();
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return (false);
            }
            Map.Entry entry = (Map.Entry) obj;
            Map.Entry actual = ContextBase.this.entry(entry.getKey());
            if (actual != null) {
                return (actual.equals(entry));
            } else {
                return (false);
            }
        }

        public boolean isEmpty() {
            return (ContextBase.this.isEmpty());
        }

        public Iterator iterator() {
            return (ContextBase.this.entriesIterator());
        }

        public boolean remove(Object obj) {
            if (obj instanceof Map.Entry) {
                return (ContextBase.this.remove((Map.Entry) obj));
            } else {
                return (false);
            }
        }

        public int size() {
            return (ContextBase.this.size());
        }

    }


    /**
     * <p>Private implementation of <code>Iterator</code> for the
     * <code>Set</code> returned by <code>entrySet()</code>.</p>
     */
    private class EntrySetIterator implements Iterator {

        private Map.Entry entry = null;
        private Iterator keys = ContextBase.this.keySet().iterator();

        public boolean hasNext() {
            return (keys.hasNext());
        }

        public Object next() {
            entry = ContextBase.this.entry(keys.next());
            return (entry);
        }

        public void remove() {
            ContextBase.this.remove(entry);
        }

    }


    /**
     * <p>Private implementation of <code>Map.Entry</code> for each item in
     * <code>EntrySetImpl</code>.</p>
     */
    private class MapEntryImpl implements Map.Entry {

        MapEntryImpl(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        private Object key;
        private Object value;

        public boolean equals(Object obj) {
            if (obj == null) {
                return (false);
            } else if (!(obj instanceof Map.Entry)) {
                return (false);
            }
            Map.Entry entry = (Map.Entry) obj;
            if (key == null) {
                return (entry.getKey() == null);
            }
            if (key.equals(entry.getKey())) {
                if (value == null) {
                    return (entry.getValue() == null);
                } else {
                    return (value.equals(entry.getValue()));
                }
            } else {
                return (false);
            }
        }

        public Object getKey() {
            return (this.key);
        }

        public Object getValue() {
            return (this.value);
        }

        public int hashCode() {
            return (((key == null) ? 0 : key.hashCode())
                    ^ ((value == null) ? 0 : value.hashCode()));
        }

        public Object setValue(Object value) {
            Object previous = this.value;
            ContextBase.this.put(this.key, value);
            this.value = value;
            return (previous);
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }
    }


    /**
     * <p>Private implementation of <code>Collection</code> that implements the
     * semantics required for the value returned by <code>values()</code>.</p>
     */
    private class ValuesImpl extends AbstractCollection {

        public void clear() {
            ContextBase.this.clear();
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return (false);
            }
            Map.Entry entry = (Map.Entry) obj;
            return (ContextBase.this.containsValue(entry.getValue()));
        }

        public boolean isEmpty() {
            return (ContextBase.this.isEmpty());
        }

        public Iterator iterator() {
            return (ContextBase.this.valuesIterator());
        }

        public boolean remove(Object obj) {
            if (obj instanceof Map.Entry) {
                return (ContextBase.this.remove((Map.Entry) obj));
            } else {
                return (false);
            }
        }

        public int size() {
            return (ContextBase.this.size());
        }

    }


    /**
     * <p>Private implementation of <code>Iterator</code> for the
     * <code>Collection</code> returned by <code>values()</code>.</p>
     */
    private class ValuesIterator implements Iterator {

        private Map.Entry entry = null;
        private Iterator keys = ContextBase.this.keySet().iterator();

        public boolean hasNext() {
            return (keys.hasNext());
        }

        public Object next() {
            entry = ContextBase.this.entry(keys.next());
            return (entry.getValue());
        }

        public void remove() {
            ContextBase.this.remove(entry);
        }

    }


}
