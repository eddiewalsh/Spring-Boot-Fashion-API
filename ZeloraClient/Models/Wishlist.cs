namespace ZeloraClient.Models
{
    public class Wishlist
    {
        public int WishlistId { get; set; }
        public DateTime AddedDate { get; set; }
        public string WishlistName { get; set; }
        public string Notes { get; set; }
        public List<Links> Links { get; set; }
    }
}
