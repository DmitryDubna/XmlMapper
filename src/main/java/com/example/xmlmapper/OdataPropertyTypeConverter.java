package com.example.xmlmapper;

import jakarta.xml.bind.JAXBException;

import java.util.*;
import java.util.stream.Collectors;

public class OdataPropertyTypeConverter {
    private Map<String, String> propertyTypes;

    public OdataPropertyTypeConverter(final String xml) throws JAXBException {
        this.propertyTypes =
                OdataMetadataXmlParser.toPropertyTypes(xml)
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> toPostgresType(entry.getValue())));
    }

    public Optional<String> getPostgresType(final String propertyName) {
        return Optional.ofNullable(propertyTypes.get(propertyName));
    }

    public Map<String, String> toPostgresTypes(Set<String> odataPropertyNames) {
        return odataPropertyNames.stream()
                .map(name -> new AbstractMap.SimpleEntry<String, Optional<String>>(name.toLowerCase(), getPostgresType(name)))
                .filter(entry -> entry.getValue().isPresent())
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().get()));
    }

    private String toPostgresType(final String odataType) {
        switch (odataType) {
            case "Edm.Int32":
                return "INTEGER";
            case "Edm.Int64":
                return "BIGINT";
            case "Edm.Guid":
            case "Edm.String":
                return "TEXT";
            case "Edm.DateTimeOffset":
                return "TIMESTAMPTZ";
            case "Edm.Boolean":
                return "boolean";
            default:
                return null;
        }
    }
}
