package com.rmslab.covidcasestracker.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Statistics {
    private String state ;
    private String country ;
    private int latestTotalCases ;
    private int newlyReportedCase ;

    @Override
    public String toString() {
        return "Statistics{" +
                "state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", latestTotalCases=" + latestTotalCases +
                '}';
    }
}
