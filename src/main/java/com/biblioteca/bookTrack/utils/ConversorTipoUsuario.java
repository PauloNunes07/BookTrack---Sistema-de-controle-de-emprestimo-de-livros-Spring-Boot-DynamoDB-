
package com.biblioteca.bookTrack.utils;

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class ConversorTipoUsuario implements AttributeConverter<TipoUsuario> {

    @Override
    public AttributeValue transformFrom(TipoUsuario input) {
        return AttributeValue.builder()
                .s(input.name())
                .build();
    }

    @Override
    public TipoUsuario transformTo(AttributeValue input) {
        return TipoUsuario.valueOf(input.s());
    }

    @Override
    public EnhancedType<TipoUsuario> type() {
        return EnhancedType.of(TipoUsuario.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
