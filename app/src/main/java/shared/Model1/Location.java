package shared.Model1;

public class Location
{
    private String country;
    private String city;
    private Double latitude;
    private Double longitude;

    public Location(String country, String city, Double latitude, Double longitude)
    {
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountry()
    {
        return country;
    }

    public String getCity()
    {
        return city;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }
}
