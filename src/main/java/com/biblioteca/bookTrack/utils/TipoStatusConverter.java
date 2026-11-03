package com.biblioteca.bookTrack.utils;

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class TipoStatusConverter implements AttributeConverter<TipoStatus> {

    @Override
    public AttributeValue transformFrom(TipoStatus status) {
        return AttributeValue.builder()
                .s(status.name())
                .build();
    }

    @Override
    public TipoStatus transformTo(AttributeValue valor) {
        return TipoStatus.valueOf(valor.s());
    }

    @Override
    public EnhancedType<TipoStatus> type() {
        return EnhancedType.of(TipoStatus.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}