namespace ZeloraClient.Models
{
    public class Product
    {
        public int ProductId { get; set; }
        public string ProductName { get; set; }
        public string Description { get; set; }
        public decimal Price { get; set; }
        public string Size { get; set; }
        public string Colour { get; set; }
        public string Material { get; set; }
        public int SustainabilityRating { get; set; }
        public string Manufacturer { get; set; }
        public DateTime ReleaseDate { get; set; }
        public decimal DiscountedPrice { get; set; }
        public string FeatureImage { get; set; }
        public string ImageUrl { get; set; }
        public List<Orderitem> OrderItemList { get; set; }
        public List<Review> ReviewList { get; set; }
        public List<Wishlist> WishlistList { get; set; }
        public List<Inventory> InventoryList { get; set; }
        public List<Links> Links { get; set; }
    }
}
