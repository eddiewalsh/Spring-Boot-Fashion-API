namespace ZeloraClient.Models
{
    public class Inventory
    {
        public int InventoryId { get; set; }
        public int QuantityInStock { get; set; }
        public int QuantityReserved { get; set; }
        public int ReorderPoint { get; set; }
        public Product ProductId { get; set; }
        public Supplier SupplierId { get; set; }
        public List<Links> Links { get; set; }
    }
}
