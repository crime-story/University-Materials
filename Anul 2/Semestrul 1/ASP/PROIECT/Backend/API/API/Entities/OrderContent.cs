namespace API.Entities
{
    public class OrderContent
    {
        public string OrderId { get; set; }
        public Order Order { get; set; }
        public string ProductId { get; set; }
        public Product Product { get; set; }
        public int NumberOfProducts { get; set; }
    }
}
