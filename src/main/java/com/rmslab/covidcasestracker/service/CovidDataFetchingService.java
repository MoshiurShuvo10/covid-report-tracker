package com.rmslab.covidcasestracker.service;

import com.rmslab.covidcasestracker.model.Statistics;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CovidDataFetchingService {
    private static String DATASOURCE_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv" ;

    private List<Statistics> statisticsList = new ArrayList<>() ;

    public List<Statistics> getStatisticsList() {
        return statisticsList;
    }

    @PostConstruct
    @Scheduled(cron = "* * * * * *")
    public void fetchData() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient() ;
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(DATASOURCE_URL)).build() ;
        HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString()) ;
//        System.out.println(httpResponse.body());
        StringReader stringReader = new StringReader(httpResponse.body()) ;
        List<Statistics> tempStatistics = new ArrayList<>() ;
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);
        for (CSVRecord record : records) {
            Statistics statistics = new Statistics() ;
            statistics.setState(record.get("Province/State"));
            statistics.setCountry(record.get("Country/Region"));
            int latestCase = Integer.parseInt(record.get(record.size()-1)) ;
            int dayBeforeLatestCase = Integer.parseInt(record.get(record.size()-2)) ;
            statistics.setLatestTotalCases(latestCase);
            statistics.setNewlyReportedCase(latestCase-dayBeforeLatestCase);
            tempStatistics.add(statistics) ;
        }
        this.statisticsList=tempStatistics ;
    }
}
