package com.example.todoapp.infrastructure.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class TodoHistoryId implements Serializable {
    private Integer internalId;
    private Integer versionNumber;
}
