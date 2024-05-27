namespace ZeloraClient.Models
{
    public class Orders
    {
        public int OrderId { get; set; }
        public DateTime OrderDate { get; set; }
        public decimal TotalAmount { get; set; }
        public string OrderStatus { get; set; }
        public string PaymentMethod { get; set; }
        public string ShippingMethod { get; set; }
        public DateTime DeliveryDate { get; set; }
        public List<Orderitem> OrderItemList { get; set; }
        public Customer CustomerId { get; set; }
        public List<Links> Links { get; set; }
    }
}
