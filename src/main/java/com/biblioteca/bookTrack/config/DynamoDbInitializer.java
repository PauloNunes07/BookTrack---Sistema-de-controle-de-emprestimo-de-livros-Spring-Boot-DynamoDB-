
package com.biblioteca.bookTrack.config;

import com.biblioteca.bookTrack.model.Usuario;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Component
public class DynamoDbInitializer {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbClient dynamoDbClient;

    public DynamoDbInitializer(
            DynamoDbEnhancedClient enhancedClient,
            DynamoDbClient dynamoDbClient) {

        this.enhancedClient = enhancedClient;
        this.dynamoDbClient = dynamoDbClient;
    }

    @PostConstruct
    public void inicializar() {
        criarTabelaUsuario();
        criarTabelaContadores();
    }

    private void criarTabelaUsuario() {

        DynamoDbTable<Usuario> tabela = enhancedClient.table(
                "Usuario",
                TableSchema.fromBean(Usuario.class)
        );

        try {
            tabela.createTable(builder -> builder
                    .provisionedThroughput(
                            ProvisionedThroughput.builder()
                                    .readCapacityUnits(5L)
                                    .writeCapacityUnits(5L)
                                    .build()
                    )
                    .globalSecondaryIndices(
                            indice -> indice
                                    .indexName("email-index")
                                    .projection(
                                            Projection.builder()
                                                    .projectionType(ProjectionType.ALL)
                                                    .build()
                                    )
                                    .provisionedThroughput(
                                            ProvisionedThroughput.builder()
                                                    .readCapacityUnits(5L)
                                                    .writeCapacityUnits(5L)
                                                    .build()
                                    ),

                            indice -> indice
                                    .indexName("matricula-index")
                                    .projection(
                                            Projection.builder()
                                                    .projectionType(ProjectionType.ALL)
                                                    .build()
                                    )
                                    .provisionedThroughput(
                                            ProvisionedThroughput.builder()
                                                    .readCapacityUnits(5L)
                                                    .writeCapacityUnits(5L)
                                                    .build()
                                    )
                    )
            );

            System.out.println("Tabela Usuario criada!");

        } catch (ResourceInUseException e) {
            System.out.println("Tabela Usuario já existe!");
        }

        aguardarTabela("Usuario");
    }

    private void criarTabelaContadores() {

        try {
            dynamoDbClient.createTable(
                    CreateTableRequest.builder()
                            .tableName("Contadores")
                            .keySchema(
                                    KeySchemaElement.builder()
                                            .attributeName("entidade")
                                            .keyType(KeyType.HASH)
                                            .build()
                            )
                            .attributeDefinitions(
                                    AttributeDefinition.builder()
                                            .attributeName("entidade")
                                            .attributeType(ScalarAttributeType.S)
                                            .build()
                            )
                            .billingMode(BillingMode.PAY_PER_REQUEST)
                            .build()
            );

            System.out.println("Tabela Contadores criada!");

        } catch (ResourceInUseException e) {
            System.out.println("Tabela Contadores já existe!");
        }

        aguardarTabela("Contadores");
    }

    private void aguardarTabela(String nomeTabela) {
        dynamoDbClient.waiter().waitUntilTableExists(
                DescribeTableRequest.builder()
                        .tableName(nomeTabela)
                        .build()
        );

        System.out.println("Tabela " + nomeTabela + " está disponível!");
    }
}
