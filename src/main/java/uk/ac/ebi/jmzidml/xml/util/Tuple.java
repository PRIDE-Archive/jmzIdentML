package uk.ac.ebi.jmzidml.xml.util;

import java.io.Serializable;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 10/09/15
 */
public class Tuple <K, V> implements Serializable {
    private K key;
    private V value;

    public Tuple(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple)) return false;

        Tuple tuple = (Tuple) o;

        return !(key != null ? !key.equals(tuple.key) : tuple.key != null) && !(value != null ? !value.equals(tuple.value) : tuple.value != null);

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
