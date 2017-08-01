/*
 * Date: 27-Jul-2017
 * Author: Da Qi
 * File: uk.ac.ebi.jmzidml.model.utils.MzIdentMLVersion.java
 *
 */

package uk.ac.ebi.jmzidml.model.utils;

import static uk.ac.ebi.jmzidml.model.utils.ModelConstants.MZIDML_NS;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import uk.ac.ebi.jmzidml.MzIdentMLElement;

/**
 *
 * @author Da Qi
 * Time 27-Jul-2017 08:46:43
 */
public enum MzIdentMLVersion {

    Version_1_1(ModelConstants.MZIDML_NS_1_1, ModelConstants.MZIDML_VERSION_1_1,
                ModelConstants.MZIDML_SCHEMA_1_1),
    Version_1_2(ModelConstants.MZIDML_NS_1_2, ModelConstants.MZIDML_VERSION_1_2,
                ModelConstants.MZIDML_SCHEMA_1_2);

    private final String MZIDML_NS;
    private final String MZIDML_VERSION;
    private final String MZIDML_SCHEMA;

    private Map<Class, QName> modelQNames = new HashMap<>();

    MzIdentMLVersion(String ns, String ver, String schema) {
        this.MZIDML_NS = ns;
        this.MZIDML_VERSION = ver;
        this.MZIDML_SCHEMA = schema;

        for (MzIdentMLElement element : MzIdentMLElement.values()) {
            if (element.getTagName() != null) {
                this.modelQNames.put(element.getClazz(), new QName(
                                     this.MZIDML_NS,
                                     element.
                                     getTagName()));
            }
        }
        //now make set unmodifiable
        modelQNames = Collections.unmodifiableMap(modelQNames);
    }

    public String getNameSpace() {
        return this.MZIDML_NS;
    }

    public String getVersion() {
        return this.MZIDML_VERSION;
    }

    public String getSchema() {
        return this.MZIDML_SCHEMA;
    }

    public boolean isRegisteredClass(Class cls) {
        return this.modelQNames.containsKey(cls);
    }

    public QName getQNameForClass(Class cls) {
        if (isRegisteredClass(cls)) {
            return this.modelQNames.get(cls);
        } else {
            throw new IllegalStateException("No QName registered for class: "
                    + cls);
        }
    }

    public String getElementNameForClass(Class cls) {
        if (isRegisteredClass(cls)) {
            return this.modelQNames.get(cls).getLocalPart();
        } else {
            throw new IllegalStateException("No QName registered for class: "
                    + cls);
        }
    }

    public Class getClassForElementName(String name) {
        for (Map.Entry<Class, QName> entry : this.modelQNames.entrySet()) {
            if (entry.getValue().getLocalPart().equals(name)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
