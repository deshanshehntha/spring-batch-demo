package com.driw.batch;

import com.driw.entity.User;
import com.driw.entity.UserOutput;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ProcessJobConfiguration {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    Processor processor;

    @Autowired
    Writer writer;

    @Value("${input.file}")
    Resource resource;

    @Bean
    public Job processJob() {
        Step step2 = stepBuilderFactory.get("step-2")
                .<User, UserOutput>chunk(10)
                .listener(this)
                .reader(flatFileItemReader())
                .processor(processor)
                .writer(writer)
                .build();

        Job job = jobBuilderFactory.get("accounting-job")
                .incrementer(new RunIdIncrementer())
                .start(step2)
                .build();

        return job;
    }

    private FlatFileItemReader<User> flatFileItemReader() {
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        reader.setResource(resource);
        UserJasonMapper lineMapper = new UserJasonMapper();
        reader.setLineMapper(lineMapper);
        return reader;
    }

}
