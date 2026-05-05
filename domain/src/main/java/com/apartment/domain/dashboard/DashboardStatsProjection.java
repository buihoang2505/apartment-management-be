package com.apartment.domain.dashboard;

public interface DashboardStatsProjection {
    long getTotal();
    long getThisMonth();
    long getLastMonth();

    long getDangBan();
    long getDaCoc();
    long getDaBan();
    long getGiuCho();
    long getKhoa();

    long getStudio();
    long getOneBedroom();
    long getTwoBedroom();
    long getTwoBedroomPlus();
    long getThreeBedroom();
    long getThreeBedroomPlus();
    long getPenthouse();
    long getDuplex();
    long getOther();
}
