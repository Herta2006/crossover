package com.dev;

import com.dev.domain.Product;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mysql.jdbc.Driver;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_NULL_MAP_VALUES;

@Configuration
@EnableWebMvc
@EnableJpaRepositories("com.dev.server.repositories")
@ComponentScan(basePackages = {"com.dev.server"})
public class AppContextConfig extends WebMvcConfigurerAdapter {

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setUrl("jdbc:mysql://localhost:3306/crossover");
        dataSource.setUsername("admin");
        dataSource.setPassword("Qwe123");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        jpaProperties.put("hibernate.show_sql", "true");
//        jpaProperties.put("hibernate.hbm2ddl.auto", "create");
        emf.setJpaProperties(jpaProperties);
        emf.setPackagesToScan("com.dev");
        return emf;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return jpaTransactionManager;
    }

//    @Bean
//    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper objectMapper = new JSONMapper()
//                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
//                .configure(WRITE_NULL_MAP_VALUES, false);
//        converter.setObjectMapper(objectMapper);
//        return converter;
//    }
//
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(mappingJackson2HttpMessageConverter());
//    }
}
//
//class JSONMapper extends ObjectMapper {
//    public JSONMapper() {
//        SimpleModule module = new SimpleModule("JSONModule", new Version(2, 0, 0, null, null, null));
////        module.addSerializer(new StdKeySerializer());
//        module.addSerializer(new JsonSerializer<Map<Product, Integer>>() {
//            @Override
//            public void serialize(Map<Product, Integer> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
//                gen.writeStartObject();
//                for (Map.Entry<Product, Integer> entry : value.entrySet()) {
//                    Product product = entry.getKey();
//                    String productJson =
//                            "{" +
//                                    "\"id\":" + product.getId() +
//                                    "\"description\":" + product.getDescription() +
//                                    "\"price\":" + product.getPrice() +
//                                    "\"inventoryBalance\":" + product.getInventoryBalance() +
//                                    "}";
//                    gen.writeStringField("product", productJson);
//                    gen.writeNumberField("quantity", entry.getValue());
//                }
//                gen.writeEndObject();
//            }
//        });
////        module.addDeserializer(new JsonDeserializer<Map<Product, Integer>>());
//        registerModule(module);
//    }
//
//}


//module.addDeserializer(Map.class, new JsonDeserializer<Map>() {
//@Override
//public Map<Product, Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
//        Map<Product, Integer> deserialized = new HashMap<>();
//        JsonNode currentNode = p.getCodec().readTree(p);
//        Iterator<JsonNode> iterator = currentNode.iterator();
//        do {
//        JsonNode productNode = currentNode.get("product");
//        currentNode = iterator.next();
//        Product product = new Product();
//        product.setId(productNode.get("id").textValue());
//        product.setDescription(productNode.get("description").textValue());
//        product.setPrice(productNode.get("price").longValue());
//        product.setInventoryBalance(productNode.get("inventoryBalance").asInt());
//        Integer quantity = currentNode.get("quantity").asInt();
//        deserialized.put(product, quantity);
//        currentNode = currentNode.iterator().next();
//        }
//        while (iterator.hasNext());
//        return deserialized;
//        }
//        });
