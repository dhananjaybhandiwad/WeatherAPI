package com.fraunhofer.weatherapi.Entity.DerivedEntity;
import com.fraunhofer.weatherapi.Entity.BaseEntity.HourlyBase;

import java.util.ArrayList;

public class Hourly extends HourlyBase {
    public ArrayList<Double> rain;
    public ArrayList<Double> windspeed_10m;

}
