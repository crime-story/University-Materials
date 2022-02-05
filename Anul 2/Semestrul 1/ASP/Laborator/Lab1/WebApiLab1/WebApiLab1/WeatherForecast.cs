using System;

namespace WebApiLab1
{
    //Example of a basic class that is used in our WeatherForecastController for testing purposes
    public class WeatherForecast
    {
        public DateTime Date { get; set; }

        public int TemperatureC { get; set; }

        //Property that has a special formula  to convert from Celsius to Fahrenheit
        //this formula will apply for every WeatherForecast object that is instantiated
        public int TemperatureF => 32 + (int)(TemperatureC / 0.5556);

        public string Summary { get; set; }
    }
}
