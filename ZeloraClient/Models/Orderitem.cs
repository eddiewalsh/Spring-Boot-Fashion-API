namespace ZeloraClient.Models
{
    public class Orderitem
    {
        public int OrderItemId { get; set; }
        public int Quantity { get; set; }
        public decimal ItemPrice { get; set; }
        public decimal Subtotal { get; set; }
        public decimal ItemWeight { get; set; }
        public string CustomisationOptions { get; set; }
        public List<Links> Links { get; set; }
    }
}
