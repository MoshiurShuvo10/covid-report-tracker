package com.rmslab.covidcasestracker.controller;

import com.rmslab.covidcasestracker.model.Statistics;
import com.rmslab.covidcasestracker.service.CovidDataFetchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    CovidDataFetchingService covidDataFetchingService ;

    @GetMapping("/")
    public String home(Model model) {
        List<Statistics> tempStats = covidDataFetchingService.getStatisticsList();
        int totalReportedCases = tempStats.stream().mapToInt(statistics -> statistics.getLatestTotalCases()).sum() ;
        int totalNewlyReportedCases = tempStats.stream().mapToInt(statistics -> statistics.getNewlyReportedCase()).sum() ;
        model.addAttribute("totalReportedCases",totalReportedCases) ;
        model.addAttribute("totalNewlyReportedCases",totalNewlyReportedCases) ;
        model.addAttribute("stats", tempStats) ;
        return "home" ;
    }
}
