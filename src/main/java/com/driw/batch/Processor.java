package com.driw.batch;

import com.driw.entity.User;
import com.driw.entity.UserOutput;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Component
public class Processor implements ItemProcessor<User, UserOutput> {

    @Override
    public UserOutput process(User user) {
        UserOutput output = new UserOutput();
        output.setAccount(user.getAccount());
        output.setName(user.getName());
        output.setUserId(user.getUserId());
        return output;
    }

}
