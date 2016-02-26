package com.dev.frontend.services;

import com.dev.domain.Customer;
import com.dev.domain.OrderLine;
import com.dev.domain.Product;
import com.dev.domain.SalesOrder;
import com.dev.frontend.panels.ComboBoxItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

public class Services {
    public static final int TYPE_PRODUCT = 1;
    public static final int TYPE_CUSTOMER = 2;
    public static final int TYPE_SALES_ORDER = 3;
    public static final String URL = "http://localhost:8080/";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final String APPLICATION_JSON_HEADER_VALUE = "application/json";
    public static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    private static Map<Integer, List<Object>> retrievedData = new HashMap<>();

    public static List<Object> listCurrentRecords(int objectType) {
        List<Object> resources = new ArrayList<>();
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(URL + getResourceName(objectType));
            getRequest.addHeader(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE);
            HttpResponse response = httpClient.execute(getRequest);
            if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 204) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            String jsonString = "";
            if (response.getStatusLine().getStatusCode() != 204) {
                HttpEntity entity = response.getEntity();
                BufferedReader br = new BufferedReader(new InputStreamReader((entity.getContent())));
                String line;
                while ((line = br.readLine()) != null) {
                    jsonString += line;
                }
                TypeFactory typeFactory = TypeFactory.defaultInstance();
                resources = MAPPER.readValue(jsonString, typeFactory.constructCollectionType(List.class, getResourceClass(objectType)));
            }

            retrievedData.put(objectType, resources);
            httpClient.getConnectionManager().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resources;
    }

    public static Object saveOrUpdate(Object object, int objectType) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntityEnclosingRequestBase httpMethod = getHttpMethod(objectType, object);
            StringEntity input = createStringEntity(objectType, object);
            httpMethod.addHeader(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE);
            httpMethod.setEntity(input);
            HttpResponse response = httpClient.execute(httpMethod);
            if (response.getStatusLine().getStatusCode() != 201 && response.getStatusLine().getStatusCode() != 202) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }
            httpClient.getConnectionManager().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listCurrentRecords(objectType);
        return object;
    }

    public static Object readRecordByCode(String code, int objectType) {
        Object retrievedResource = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(URL + getResourceName(objectType) + "/" + code);
            getRequest.addHeader(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE);
            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
                String jsonString = "";
                String line;
                while ((line = br.readLine()) != null) {
                    jsonString += line;
                }
                retrievedResource = MAPPER.readValue(jsonString, getResourceClass(objectType));
            } else if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 404) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }
            httpClient.getConnectionManager().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retrievedResource;
    }

    public static boolean deleteRecordByCode(String code, int objectType) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpDelete getRequest = new HttpDelete(URL + getResourceName(objectType) + "/" + code);
            getRequest.addHeader(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE);
            HttpResponse response = httpClient.execute(getRequest);
            if (response.getStatusLine().getStatusCode() != 204) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }
            httpClient.getConnectionManager().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listCurrentRecords(objectType);
        return true;
    }

    public static List<ComboBoxItem> listCurrentRecordReferences(int objectType) {
        listCurrentRecords(objectType);
        return retrievedData.get(objectType)
                .stream()
                .map(obj -> new ComboBoxItem(getId(obj), getValue(obj)))
                .collect(Collectors.toList());
    }

    public static double getProductPrice(String productCode) {
        for (Object productObj : retrievedData.get(TYPE_PRODUCT)) {
            Product product = (Product) productObj;
            if (product.getId().equals(productCode)) {
                return product.getPrice() / 100.;
            }
        }
        return 0;
    }

    public static Map<Integer, List<Object>> getRetrievedData() {
        if (!retrievedData.containsKey(TYPE_CUSTOMER) || !retrievedData.containsKey(TYPE_PRODUCT)) {
            listCurrentRecords(TYPE_CUSTOMER);
            listCurrentRecords(TYPE_PRODUCT);
        }
        return retrievedData;
    }

    public static Object getCustomerById(String customerId) {
        for (Object customerObj : retrievedData.get(TYPE_CUSTOMER)) {
            Customer customer = (Customer) customerObj;
            if (customer.getId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    public static Object getProductById(String productId) {
        for (Object productObj : retrievedData.get(TYPE_PRODUCT)) {
            Product product = (Product) productObj;
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    //todo: change OrderLine pojo, database for this pojo and handling related results
    public static double calculateTotalPrice(SalesOrder salesOrder) {
        double result = 0;
        for (OrderLine orderLine : salesOrder.getOrderLines()) {
            for (Map.Entry<String, Integer> entry : orderLine.getProductIdToQuantity().entrySet()) {
                result += getProductPrice(entry.getKey());
            }
        }
        return result;
    }

    private static HttpEntityEnclosingRequestBase getHttpMethod(int objectType, Object object) {
        if (readRecordByCode(getId(object), objectType) == null) {
            return new HttpPost(URL + getResourceName(objectType));
        } else {
            return new HttpPut(URL + getResourceName(objectType) + "/" + getId(object));
        }
    }

    private static String getValue(Object object) {
        if (object.getClass() == Product.class) return ((Product) object).getDescription();
        if (object.getClass() == Customer.class) return ((Customer) object).getOrganizationName();
        return "";
    }

    private static String getId(Object object) {
        if (object.getClass() == Product.class) return ((Product) object).getId();
        if (object.getClass() == Customer.class) return ((Customer) object).getId();
        if (object.getClass() == SalesOrder.class) return "" + ((SalesOrder) object).getId();
        return "";
    }

    private static StringEntity createStringEntity(int objectType, Object object) throws UnsupportedEncodingException {
        StringEntity input;
        switch (objectType) {
            case 2:
                Customer customer = (Customer) object;
                input = new StringEntity("" +
                        "{" +
                        "   \"id\": \"" + customer.getId() + "\"," +
                        "   \"organizationName\": \"" + customer.getOrganizationName() + "\"," +
                        "   \"address\": \"" + customer.getAddress() + "\"," +
                        "   \"phone1\": \"" + customer.getPhone1() + "\"," +
                        "   \"phone2\": \"" + customer.getPhone2() + "\"," +
                        "   \"balance\": " + customer.getBalance() +
                        "}");
                break;
            case 1:
                Product product = (Product) object;
                input = new StringEntity("" +
                        "{" +
                        "   \"id\": \"" + product.getId() + "\"," +
                        "   \"description\": \"" + product.getDescription() + "\"," +
                        "   \"price\": " + product.getPrice() + "," +
                        "   \"inventoryBalance\": " + product.getInventoryBalance() +
                        "}");
                break;
            case 3:
                SalesOrder salesOrder = (SalesOrder) object;
                List<OrderLine> orderLines = salesOrder.getOrderLines();
                String orderLinesValue = "[";
                Iterator<OrderLine> iterator = orderLines.iterator();
                while (iterator.hasNext()) {
                    OrderLine orderLine = iterator.next();
                    orderLinesValue += convertOrderLine(orderLine) + (iterator.hasNext() ? "," : "");
                }
                orderLinesValue += "]";

                String json = "" +
                        "{" +
                        "   \"id\": " + salesOrder.getId() + "," +
                        "   \"customerId\": \"" + salesOrder.getCustomerId() + "\"," +
                        "   \"orderLines\": " + orderLinesValue + "" +
                        "}";
                input = new StringEntity(json);
                break;
            default:
                throw new RuntimeException("wrong object type");
        }
        input.setContentType(APPLICATION_JSON_HEADER_VALUE);
        return input;
    }

    private static String convertOrderLine(OrderLine orderLine) {
        String str = "{" + "\"id\":" + orderLine.getId() + "," +
                "\"productIdToQuantity\":{";
        Set<Map.Entry<String, Integer>> entries = orderLine.getProductIdToQuantity().entrySet();
        Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();
        for (; iterator.hasNext(); ) {
            Map.Entry<String, Integer> entry = iterator.next();
            str += "\"" + entry.getKey() + "\":" + entry.getValue() + (iterator.hasNext() ? "," : "");

        }
        str += "}}";
        return str;
    }

    private static String getResourceName(int objectType) {
        return objectType == 1 ? "products" : objectType == 2 ? "customers" : "salesOrders";
    }

    private static Class<?> getResourceClass(int objectType) {
        return objectType == 1 ? Product.class : objectType == 2 ? Customer.class : SalesOrder.class;
    }
}
