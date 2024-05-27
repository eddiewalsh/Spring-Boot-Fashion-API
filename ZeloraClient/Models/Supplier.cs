namespace ZeloraClient.Models
{
    public class Supplier
    {
        public int SupplierId { get; set; }
        public string SupplierName { get; set; }
        public string ContactName { get; set; }
        public string ContactEmail { get; set; }
        public string ContactPhone { get; set; }
        public string Address { get; set; }
        public string Website { get; set; }
        public string Description { get; set; }
        public List<Inventory> InventoryList { get; set; }
        public List<Product> ProductList { get; set; }
    }
}
