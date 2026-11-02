
package com.biblioteca.bookTrack.utils;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Map;

@Component
public class IdGenerator {

    private final DynamoDbClient dynamoDbClient;

    public IdGenerator(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public int gerarId(String entidade) {

        UpdateItemResponse resposta = dynamoDbClient.updateItem(
                UpdateItemRequest.builder().tableName("Contadores")
                        .key(Map.of(
                                "entidade", AttributeValue.builder().s(entidade).build()))
                        .updateExpression("ADD ultimoId :incremento").expressionAttributeValues(Map.of(
                                ":incremento", AttributeValue.builder().n("1").build()
                        )).returnValues(ReturnValue.UPDATED_NEW).build()
        );

        return Integer.parseInt(resposta.attributes().get("ultimoId").n());
    }
}
