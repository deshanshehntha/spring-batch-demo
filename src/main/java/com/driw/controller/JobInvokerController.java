package com.driw.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParseException;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.StreamTokenizer;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

@EnableScheduling
@RestController
public class JobInvokerController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job processJob;

    @Value("${input.file}")
    Resource resource;


    @RequestMapping("/run-batch-job")
    public String handle(@RequestBody String response) throws Exception {

        StringTokenizer tokenizer = new StringTokenizer(response, "\\[^\\]");

        BufferedWriter writer = new BufferedWriter(new FileWriter(resource.getFile()));
        while (tokenizer.hasMoreTokens()) {
            String objectListString = tokenizer.nextToken();
            if(!(objectListString.startsWith("{") && objectListString.endsWith("}"))) {
                continue;
            }
            StringTokenizer objectListTokenizer = new StringTokenizer(objectListString, "\\{^\\}");
            while (objectListTokenizer.hasMoreTokens()) {
                String tokenToBeWrite = objectListTokenizer.nextToken().trim();
                if (!Pattern.matches(",", tokenToBeWrite)) {
                    System.out.println(tokenToBeWrite);
                    writer.append("{");
                    writer.write(tokenToBeWrite);
                    writer.append("}");
                    writer.newLine();
                }
            }
        }
        writer.close();
        runSpringBatchJob();

        return "";
    }

    @Scheduled(cron = "0 */1 * * * ?")
    private void runSpringBatchJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("JobId", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        try {
            jobLauncher.run(processJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}