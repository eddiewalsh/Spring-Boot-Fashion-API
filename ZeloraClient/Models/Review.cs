namespace ZeloraClient.Models
{
    public class Review
    {
        public int ReviewId { get; set; }
        public int Rating { get; set; }
        public string ReviewText { get; set; }
        public DateTime ReviewDate { get; set; }
        public bool FlaggedAsSpam { get; set; }
        public Customer Customer { get; set; }
        public Product Product { get; set; }
        public List<Links> Links { get; set; }
    }
}
