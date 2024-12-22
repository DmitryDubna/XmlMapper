package com.example.xmlmapper;

import jakarta.xml.bind.JAXBException;
import okhttp3.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
public class XmlMapperApplication {

    public static void main(String[] args) throws ParseException, JAXBException {
//        // получение токена
//        requestToken();
//
//        // получение метаданных OData
//        String xml = requestOdataMetadata();
//        System.out.println(xml);
//
//        // получение 1 записи 1С
//        String json = get1cDataJson(1, 0);
//        System.out.println(json);
//
//        // получение имен свойств записи 1С
//        Set<String> propertyNames = toOdataPropertyNames(json);
//        System.out.println(propertyNames);
//
//        // подготовка типов Postgres
//        var postgresTypes = new OdataPropertyTypeConverter(xml).toPostgresTypes(propertyNames);
//        System.out.println(postgresTypes);

        // debug
        String fieldValues = toFieldValuesString(json);
        System.out.println("fieldValues:\n" + fieldValues);
    }

    public static String sendRequest(final String url, final String token) {
        var request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + token)
                .build();

        try {
            var response = new OkHttpClient().newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String requestOdataMetadata() {
        final String odataUrl = "https://visary-cloud-test.k8s.npc.ba/api/audit/odata/$metadata";
        final String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IkM5MzE2M0M4MDA0OUZGNjc4MTU5OTJFMzk4NkM0RTI2ODBEMkRFNkVSUzI1NiIsInR5cCI6ImF0K2p3dCIsIng1dCI6InlURmp5QUJKXzJlQldaTGptR3hPSm9EUzNtNCJ9.eyJuYmYiOjE3MjczNTAyNDMsImV4cCI6MTcyNzM1Mzg0MywiaXNzIjoiaHR0cHM6Ly9pZC12aXNhcnktY2xvdWQtdGVzdC5rOHMubnBjLmJhIiwiYXVkIjpbImFjY291bnQiLCJjaGF0IiwiZmlsZXN0b3JhZ2UiLCJ2aXNhcnkiLCJwcm9maWxlIl0sImNsaWVudF9pZCI6InN5c3RlbS12aXNhcnkiLCJpZHAiOiJsb2NhbCIsImF1dGhfdGltZSI6MTcyNzM1MDI0MywiYWRtaW5sZXZlbCI6IjEwMCIsInhpZCI6IngyIiwic3ViIjoiMiIsImp0aSI6IkFGMTM3OTIyRDI1MEM1MkMwMjcyMTI4NzI3RDJERThEIiwiaWF0IjoxNzI3MzUwMjQzLCJzY29wZSI6WyJhY2NvdW50X21hbmFnZSIsImNoYXQiLCJmaWxlc3RvcmFnZSIsImZpbGVzdG9yYWdlX3JlYWRvbmx5IiwidXNlciIsInVzZXJwcm9maWxlIl19.BIVqAV2_XZHaGLq_jCovaQdYKTPwzo4liMDpFerplFzPU1W7r73DxDdXcfQK8nZmwOqYd2uf3nGgx2l8sA8D4m7s2rtLIhAe7heDvOY49lWoZtDvdXxTdTfQCX1kY4dlfV--0yx-I68xB9hE9UbwBW1TL2OJp6FP4m5Yi3zIyB_q2R9BHFu5_1RRMemNNRNSYbMuB56AGqp8___6zr7BKfJZv0kiHNN1kWTc_88bB25Y8_hHnqPm4Ruct8CXwZ3YAVg8OTK988iev_m7yKXCvMmywV-CbokHoFLuwjbctOQCOxZ1UY68Vp0RLa8VvZwYe4jhKsD-xUK2lLyI98lKzw";

        return sendRequest(odataUrl, token);
    }

    public static String get1cDataJson(int top, int skip) {
        var odataUrl = "https://visary-cloud-test.k8s.npc.ba/api/audit/odata/";
        var odataEntityName = "audit";
        var selectFields = "";
        var filterFields = "";
        var sortFields = "";
        var token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IkM5MzE2M0M4MDA0OUZGNjc4MTU5OTJFMzk4NkM0RTI2ODBEMkRFNkVSUzI1NiIsInR5cCI6ImF0K2p3dCIsIng1dCI6InlURmp5QUJKXzJlQldaTGptR3hPSm9EUzNtNCJ9.eyJuYmYiOjE3MjczNTAyNDMsImV4cCI6MTcyNzM1Mzg0MywiaXNzIjoiaHR0cHM6Ly9pZC12aXNhcnktY2xvdWQtdGVzdC5rOHMubnBjLmJhIiwiYXVkIjpbImFjY291bnQiLCJjaGF0IiwiZmlsZXN0b3JhZ2UiLCJ2aXNhcnkiLCJwcm9maWxlIl0sImNsaWVudF9pZCI6InN5c3RlbS12aXNhcnkiLCJpZHAiOiJsb2NhbCIsImF1dGhfdGltZSI6MTcyNzM1MDI0MywiYWRtaW5sZXZlbCI6IjEwMCIsInhpZCI6IngyIiwic3ViIjoiMiIsImp0aSI6IkFGMTM3OTIyRDI1MEM1MkMwMjcyMTI4NzI3RDJERThEIiwiaWF0IjoxNzI3MzUwMjQzLCJzY29wZSI6WyJhY2NvdW50X21hbmFnZSIsImNoYXQiLCJmaWxlc3RvcmFnZSIsImZpbGVzdG9yYWdlX3JlYWRvbmx5IiwidXNlciIsInVzZXJwcm9maWxlIl19.BIVqAV2_XZHaGLq_jCovaQdYKTPwzo4liMDpFerplFzPU1W7r73DxDdXcfQK8nZmwOqYd2uf3nGgx2l8sA8D4m7s2rtLIhAe7heDvOY49lWoZtDvdXxTdTfQCX1kY4dlfV--0yx-I68xB9hE9UbwBW1TL2OJp6FP4m5Yi3zIyB_q2R9BHFu5_1RRMemNNRNSYbMuB56AGqp8___6zr7BKfJZv0kiHNN1kWTc_88bB25Y8_hHnqPm4Ruct8CXwZ3YAVg8OTK988iev_m7yKXCvMmywV-CbokHoFLuwjbctOQCOxZ1UY68Vp0RLa8VvZwYe4jhKsD-xUK2lLyI98lKzw";

        var conditions = new ArrayList<String>();
        if (!selectFields.isBlank())
            conditions.add("$select=" + selectFields);
        if (!filterFields.isBlank())
            conditions.add("$filter=" + filterFields);
        if (!sortFields.isBlank())
            conditions.add("$orderby=" + sortFields);
        conditions.add("$top=" + top);
        conditions.add("$skip=" + skip);

        String queryUrl = "%s%s?%s"
                .formatted(
                        odataUrl,
                        odataEntityName,
                        String.join("&", conditions)
                );
        // debug
        System.out.println("queryUrl: " + queryUrl);

        return sendRequest(queryUrl, token);
    }

//    public static Set<String> toOdataPropertyNames(final String json) throws ParseException {
//        JSONParser parser = new JSONParser();
//        var root = (JSONObject) parser.parse(json);
//        if (Objects.isNull(root))
//            return Set.of();
//
//        var values = (JSONArray) root.get("value");
//        if (Objects.isNull(values))
//            return Set.of();
//
//        var firstValue = (JSONObject) values.get(0);
//        return Objects.isNull(firstValue)
//                ? Set.of()
//                : firstValue.keySet();
//    }

    public static String toFieldValuesString(final String json) throws ParseException {
        JSONParser parser = new JSONParser();
        var root = (JSONObject) parser.parse(json);
        if (Objects.isNull(root))
            return "";

        var values = (JSONArray) root.get("value");
        if (Objects.isNull(values))
            return "";

        String fieldValues = (String) values.stream()
                .map(item -> {
                    JSONObject result = new JSONObject();
                    ((JSONObject) item).forEach((key, value) -> {
                        result.put(((String) key).toLowerCase(), value);
                    });
                    return result;
                })
                .map(entry -> {
                    return FIELD_NAMES.stream()
                            .map(fieldName -> ((JSONObject) entry).get(fieldName))
                            .map(object -> Optional.ofNullable(object).map(obj -> "'%s'".formatted(obj)).orElse("null"))
                            .collect(Collectors.joining(", ", "(", ")"));
                })
                .collect(Collectors.joining(", "));

        return fieldValues;
    }

    public static void requestToken() {
        final String oidcUrl = "https://id-visary-cloud-test.k8s.npc.ba/oidc/connect/token";
        final String clientId = "system-visary";
        final String clientSecret = "WOhUoTQAVPEICVzrnz55cbAXUsz4Zg";

        RequestBody requestBody =
                new FormBody.Builder()
                        .add("grant_type", "client_credentials")
                        .add("client_id", clientId)
                        .add("client_secret", clientSecret)
                        .build();

        var request = new Request.Builder()
                .url(oidcUrl)
                .post(requestBody)
//                .addHeader("Authorization", Credentials.basic(clientId, clientSecret))
                .build();
        try {
            final Response response = new OkHttpClient().newCall(request).execute();
            System.out.println(response.toString());
        } catch (final IOException e) {
            throw new UncheckedIOException("OAuth2 access token request failed", e);
        }
    }

    public static void parseOdataMetadata() throws JAXBException {
        var converter = new OdataPropertyTypeConverter(xml);
        System.out.println("EventType: " + converter.getPostgresType("EventType"));
        System.out.println("EntityId: " + converter.getPostgresType("EntityId"));
        System.out.println("EventId: " + converter.getPostgresType("EventId"));
        System.out.println("Date: " + converter.getPostgresType("Date"));
    }

    final static String xml = """
            <?xml version="1.0" encoding="utf-8"?>
            <edmx:Edmx Version="4.0" xmlns:edmx="http://docs.oasis-open.org/odata/ns/edmx">
                <edmx:DataServices>
                    <Schema Namespace="Visary.Audit.Data.Entities" xmlns="http://docs.oasis-open.org/odata/ns/edm">
                        <EntityType Name="EntityAuditEntry">
                            <Key>
                                <PropertyRef Name="ID"/>
                            </Key>
                            <Property Name="ID" Type="Edm.Int32" Nullable="false"/>
                            <Property Name="Hidden" Type="Edm.Boolean" Nullable="false"/>
                            <Property Name="EventId" Type="Edm.Guid" Nullable="false"/>
                            <Property Name="EventType" Type="Edm.String"/>
                            <Property Name="EventName" Type="Edm.String"/>
                            <Property Name="EntityId" Type="Edm.Int32" Nullable="false"/>
                            <Property Name="EntityType" Type="Edm.String"/>
                            <Property Name="EntityVersion" Type="Edm.Int64" Nullable="false"/>
                            <Property Name="Info" Type="Edm.String"/>
                            <Property Name="UserId" Type="Edm.Int32"/>
                            <Property Name="Data" Type="Edm.String"/>
                            <Property Name="Date" Type="Edm.DateTimeOffset" Nullable="false"/>
                            <Property Name="Source" Type="Edm.String"/>
                            <Property Name="Ip" Type="Edm.String"/>
                            <Property Name="UserAgent" Type="Edm.String"/>
                        </EntityType>
                        <EntityType Name="IdentityAuditEntry">
                            <Key>
                                <PropertyRef Name="ID"/>
                            </Key>
                            <Property Name="ID" Type="Edm.Int32" Nullable="false"/>
                            <Property Name="Hidden" Type="Edm.Boolean" Nullable="false"/>
                            <Property Name="EventId" Type="Edm.Guid" Nullable="false"/>
                            <Property Name="Date" Type="Edm.DateTimeOffset" Nullable="false"/>
                            <Property Name="UserId" Type="Edm.Int32"/>
                            <Property Name="AccountId" Type="Edm.Int32" Nullable="false"/>
                            <Property Name="EventType" Type="Edm.String"/>
                            <Property Name="EventName" Type="Edm.String"/>
                            <Property Name="Data" Type="Edm.String"/>
                            <Property Name="Ip" Type="Edm.String"/>
                            <Property Name="UserAgent" Type="Edm.String"/>
                        </EntityType>
                    </Schema>
                    <Schema Namespace="Default" xmlns="http://docs.oasis-open.org/odata/ns/edm">
                        <EntityContainer Name="Container">
                            <EntitySet Name="Audit" EntityType="Visary.Audit.Data.Entities.EntityAuditEntry"/>
                            <EntitySet Name="Identity" EntityType="Visary.Audit.Data.Entities.IdentityAuditEntry"/>
                        </EntityContainer>
                    </Schema>
                </edmx:DataServices>
            </edmx:Edmx>
            """;

    static final List<String> FIELD_NAMES = List.of("eventtype", "ip", "data", "eventname", "info", "date", "source", "entityid", "entitytype", "entityversion", "userid", "useragent", "hidden", "eventid", "id");
    static final String json = """
            {
              "@odata.context": "https://visary-cloud-test.k8s.npc.ba/api/audit/odata/$metadata#Audit",
              "value": [
                {
                  "ID": 1,
                  "Hidden": false,
                  "EventId": "d25fa15e-bfab-46fb-8d80-0636eef67ce8",
                  "EventType": "IEntityCreated",
                  "EventName": "Created",
                  "EntityId": 1,
                  "EntityType": "Visary.DocFlow.Entities.Contractors.Contractor",
                  "EntityVersion": 7022128,
                  "Info": null,
                  "UserId": 1,
                  "Data": "{\\"ID\\":1,\\"RowVersion\\":7022128,\\"Title\\":\\" СНТ \\"
                  Ромашка \\" \\",\\"ContractorType\\":null,\\"FullTitle\\":\\" САДОВОДЧЕСКОЕ НЕКОММЕРЧЕСКОЕ ТОВАРИЩЕСТВО \\"
                  РОМАШКА \\"\\",\\"ManagementName\\":\\"\\",\\"LegalForm\\":null,\\"Okveds\\":[],\\"Okved\\":{\\"Code\\":\\"68.32.2\\",\\"Name\\":null,\\"Title\\":\\"68.32.2 Управление эксплуатацией нежилого фонда за вознаграждение или на дого\\u0432орной основе\\",\\"SysAllParents\\":null,\\"SortOrder\\":-1.0,\\"ParentID\\":null,\\"Parent\\":null,\\"Children\\":null,\\"Level\\":0,\\"IsRoot\\":true,\\"ID\\":2438,\\"Hidden\\":false,\\"RowVersion\\":0},\\"ContactPhone\\":\\"\\",\\"ContactSecondPhone\\":null,\\"ContactDescription\\":null,\\"Email\\":\\"\\",\\"Kpp\\":\\"503101001\\",\\"Inn\\":\\"5031017620\\",\\"Ogrn\\":\\"1035006117409\\",\\"Okpo\\":\\"45720602\\",\\"Okato\\":\\"46451000041\\",\\"Requisites\\":[],\\"AddressForCorrespondence\\":null,\\"ActualAddress\\":\\"142400,Московская область,город Ногинск, деревня Белая,тер. снт Ромашка\\",\\"LegalAddress\\":null,\\"MainContractor\\":false,\\"Contacts\\":[],\\"Status\\":null,\\"OneToManyAssociation_ContractorPerson\\":{},\\"OneToManyAssociation_Cooperation\\":{},\\"Hidden\\":false}",
                  "Date": "2024-08-20T11:00:30.576454Z",
                  "Source": "WebApi",
                  "Ip": "10.232.139.104",
                  "UserAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36"
                },
                {
                    "ID": "555",
                    "EntityId": "2222",
                    "UserId": "3333"
                }
              ]
            }
            """;
}
