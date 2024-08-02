package com.lenin.securedoc.event;

import com.lenin.securedoc.entity.UserEntity;
import com.lenin.securedoc.enumeration.EventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class UserEvent {
    private UserEntity user;
    private EventType type;
    private Map<?,?> data;
}
