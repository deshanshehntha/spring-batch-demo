package com.driw.batch;

import com.driw.entity.UserOutput;
import com.driw.repository.UserOutputRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class Writer implements ItemWriter<UserOutput> {

    @Autowired
    private UserOutputRepository repo;

    @Override
    @Transactional
    public void write(List<? extends UserOutput> users) throws Exception {
        repo.saveAll(users);
    }

}
