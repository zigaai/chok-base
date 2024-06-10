package com.zigaai.core.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.zigaai.core.constants.DateTimeConstant;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        long timestamp = p.getValueAsLong();
        if (timestamp > 0) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), DateTimeConstant.UTC_ZONE_ID);
        } else {
            return null;
        }
    }

}