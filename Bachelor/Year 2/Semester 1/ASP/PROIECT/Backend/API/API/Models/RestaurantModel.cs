using API.Entities;

namespace API.Models
{
    public class RestaurantModel
    {
        public string Id { get; set; }
        public string Name { get; set; }
        public string LocationId { get; set; }
        public Location Location { get; set; }
    }
}
