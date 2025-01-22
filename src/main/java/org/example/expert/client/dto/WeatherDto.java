package org.example.expert.client.dto;



public class WeatherDto {

    private final String date;
    private final String weather;

    public WeatherDto(String date, String weather) {
        this.date = date;
        this.weather = weather;
    }

    public String getDate() {
        return date;
    }

    public String getWeather() {
        return weather;
    }
}
