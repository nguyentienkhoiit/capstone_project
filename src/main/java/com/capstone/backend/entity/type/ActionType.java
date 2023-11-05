package com.capstone.backend.entity.type;

import lombok.Getter;

import java.util.List;

@Getter
public enum ActionType {
    DOWNLOAD,
    LIKE,
    UNLIKE,
    SAVED,
    UNSAVED;
}

