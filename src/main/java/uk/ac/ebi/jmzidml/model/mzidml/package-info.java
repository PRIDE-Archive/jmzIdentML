@javax.xml.bind.annotation.XmlSchema(
        namespace = MZIDML_NS_URI,
        xmlns = {
                @XmlNs(prefix = MZIDML_VERSION_1_1, namespaceURI = MZIDML_NS_1_1),
                @XmlNs(prefix = MZIDML_VERSION_1_2, namespaceURI = MZIDML_NS_1_2)
        },
        elementFormDefault = XmlNsForm.QUALIFIED
)
package uk.ac.ebi.jmzidml.model.mzidml;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;

import static uk.ac.ebi.jmzidml.model.utils.ModelConstants.*;