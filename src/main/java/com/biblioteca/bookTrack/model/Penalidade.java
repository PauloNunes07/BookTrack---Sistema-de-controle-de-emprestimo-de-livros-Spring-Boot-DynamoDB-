package com.biblioteca.bookTrack.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@DynamoDbBean
@NoArgsConstructor
@Getter
@Setter
public class Penalidade {

    private boolean temPenalidade;
    private int nivelPenalidade;
    private LocalDateTime dataInicioPenalidade;

}
