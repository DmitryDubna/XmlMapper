package com.example.xmlmapper;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class OdataMetadataXmlParser {
    static final String edmxNamespace = "http://docs.oasis-open.org/odata/ns/edmx";
    static final String edmNamespace = "http://docs.oasis-open.org/odata/ns/edm";

    public static Map<String, String> toPropertyTypes(final String xml) throws JAXBException {
        return parse(xml).toPropertyTypes();
    }

    public static OdataMetadata parse(final String xml) throws JAXBException {
        var context = JAXBContext.newInstance(OdataMetadata.class);
        var unmarshaller = context.createUnmarshaller();
        var metadata = (OdataMetadata) unmarshaller.unmarshal(new StringReader(xml));

//        // debug
//        Marshaller marshaller = context.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//        marshaller.marshal(metadata, System.out);

        return metadata;
    }

    @XmlRootElement(name = "Edmx", namespace = edmxNamespace)
    @Getter
    static class OdataMetadata {
        @XmlElementWrapper(name = "DataServices", namespace = edmxNamespace)
        @XmlElement(name = "Schema", namespace = edmNamespace)
        private List<Schema> dataServices;

        public Map<String, String> toPropertyTypes()
        {
            return dataServices.stream()
                    .filter(schema -> Objects.nonNull(schema.getEntityTypes()))
                    .flatMap(schema -> schema.getEntityTypes().stream())
                    .filter(entityType -> Objects.nonNull(entityType.getProperties()))
                    .flatMap(entityType -> entityType.getProperties().stream())
                    .distinct()
                    .collect(Collectors.toMap(Property::getName, Property::getType));
        }
    }

    @Getter
    static class Schema {
        @XmlAttribute(name = "Namespace")
        private String namespace;

        @XmlElement(name = "EntityType", namespace = edmNamespace)
        private List<EntityType> entityTypes;
        @XmlElementWrapper(name = "EntityContainer", namespace = edmNamespace)
        @XmlElement(name = "EntitySet", namespace = edmNamespace)
        private List<EntitySet> entityContainer;
    }

    @Getter
    static class EntityType {
        @XmlAttribute(name = "Name")
        private String name;

        @XmlElement(name = "Key", namespace = edmNamespace)
        private Key key;
        @XmlElement(name = "Property", namespace = edmNamespace)
        private List<Property> properties;
    }

    @Getter
    static class Key {
        @XmlElement(name = "PropertyRef", namespace = edmNamespace)
        private PropertyRef propertyRef;
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    static class NamedItem {
        @XmlAttribute(name = "Name")
        private String name;
    }

    static class PropertyRef extends NamedItem {
    }

    @Getter
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    static class Property extends NamedItem {
        @XmlAttribute(name = "Type")
        private String type;
        @XmlAttribute(name = "Nullable")
        private boolean isNullable;
    }

    @Getter
    static class EntitySet extends NamedItem {
        @XmlAttribute(name = "EntityType")
        private String entityType;
    }
}
