package com.emprovise.service.dataservice.batch;

import com.emprovise.service.dataservice.dto.StatementDetail;
import com.emprovise.service.dataservice.resource.DealerStatementResource;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class JobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private SimpleJobLauncher jobLauncher;
    @Autowired
    private DealerStatementResource dealerStatementResource;

    private static List<String> FILE_NAMES = Arrays.asList("StatementApril2019.pdf", "StatementMarch2019.pdf", "InitechStatement.pdf");
    private static Logger logger = LoggerFactory.getLogger(JobConfiguration.class);

    @Bean
    public ResourcelessTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public MapJobRepositoryFactoryBean mapJobRepositoryFactory(ResourcelessTransactionManager txManager) throws Exception {
        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean(txManager);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public JobRepository jobRepository(MapJobRepositoryFactoryBean factory) throws Exception {
        return factory.getObject();
    }

    @Bean
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(startStep())
                .build();
    }

    @Bean
    public Step startStep() {
        return stepBuilderFactory.get("startStep")
                .tasklet(addRandomStatementsTasklet()).build();
    }

    @Bean
    public Tasklet addRandomStatementsTasklet() {
        return (contribution, chunkContext) -> {
            StatementDetail statementDetail = new StatementDetail();
            statementDetail.setDocumentId(RandomStringUtils.randomAlphabetic(10));

            Random random = new Random();
            int randomInt = random.ints(0, FILE_NAMES.size()).findFirst().getAsInt();
            statementDetail.setDocumentReference(FILE_NAMES.get(randomInt));
            statementDetail.setPayerId("0040000002");

            statementDetail.setRead(Boolean.FALSE);
            LocalDate randomDate = getDatesBetween(LocalDate.of(2018, 4, 15), LocalDate.of(2019, 3, 15));
            statementDetail.setDate(Date.from(randomDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            dealerStatementResource.add(statementDetail).subscribe(System.out::println);
            logger.info(String.format("Statement %s Added", statementDetail.getDocumentId()));
            return RepeatStatus.FINISHED;
        };
    }
    
    @Scheduled(cron = "0 */5 * ? * *")
    public void jobRunner() throws Exception {
        JobParameters param = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();
        JobExecution execution = jobLauncher.run(job(), param);
        System.out.println("Job finished with status :" + execution.getStatus());
    }

    private LocalDate getDatesBetween(LocalDate startDate, LocalDate endDate) {
        long randomDay = ThreadLocalRandom.current().nextLong(startDate.toEpochDay(), endDate.toEpochDay());
        return LocalDate.ofEpochDay(randomDay);
    }
}

