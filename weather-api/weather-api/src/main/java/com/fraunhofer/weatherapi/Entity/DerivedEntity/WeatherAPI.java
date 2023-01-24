package com.fraunhofer.weatherapi.Entity.DerivedEntity;


import com.fraunhofer.weatherapi.Entity.BaseEntity.WeatherBase;
import com.fraunhofer.weatherapi.Entity.DerivedEntity.Hourly;
import com.fraunhofer.weatherapi.Entity.DerivedEntity.HourlyUnits;

public class WeatherAPI extends WeatherBase {
    public HourlyUnits hourly_units;
    public Hourly hourly;
}
