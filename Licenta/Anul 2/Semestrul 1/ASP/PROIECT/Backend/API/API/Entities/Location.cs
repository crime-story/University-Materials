namespace API.Entities
{
    public class Location
    {
        public string Id { get; set; }
        public string Country { get; set; }
        public string City { get; set; }
        public string Street { get; set; }
        public Restaurant Restaurant { get; set; }
    }
}
