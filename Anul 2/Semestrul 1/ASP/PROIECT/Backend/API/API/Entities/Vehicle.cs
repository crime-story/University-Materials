namespace API.Entities
{
    public class Vehicle
    {
        public string Id { get; set; }
        public string Brand { get; set; }
        public string Year { get; set; }
        public string Color { get; set; }
        public string DriverId { get; set; }
        public Driver Driver { get; set; }
    }
}
