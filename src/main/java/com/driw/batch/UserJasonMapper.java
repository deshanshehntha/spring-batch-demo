package com.driw.batch;

import com.driw.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.file.LineMapper;

public class UserJasonMapper implements LineMapper<User> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public User mapLine(String line, int lineNumber) throws Exception {
        return mapper.readValue(line, User.class);
    }

}