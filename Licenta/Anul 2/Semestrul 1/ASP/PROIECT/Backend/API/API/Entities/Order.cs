namespace API.Entities
{
    public class Order
    {
        public string Id { get; set; }
        public float Price { get; set; }
        public DateTime DateOfPlacement { get; set; }
        public DateTime EstimatedDateDelivery { get; set; }
        public string DriverId { get; set; }
        public Driver Driver { get; set; }
        public ICollection<OrderContent> OrderContents { get; set; }
    }
}
