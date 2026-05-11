package com.academia.gym.mapper.aluno;

import org.mapstruct.Condition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapperUtil {

    @Condition
    default boolean isNotBlank(String value) {
        return value != null && !value.trim().isBlank();
    }
}
