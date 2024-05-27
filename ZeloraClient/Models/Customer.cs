using Microsoft.AspNetCore.Mvc.ViewEngines;

namespace ZeloraClient.Models
{
    public class Customer
    {
        public int CustomerId { get; set; }
        public string firstName { get; set; }
        public string lastName { get; set; }
        public string email { get; set; }
        public string password { get; set; }
        public string address { get; set; }
        public string phoneNumber { get; set; }
        public DateTime dateOfBirth { get; set; }
        public string paymentInfo { get; set; }
        public string sizePreferences { get; set; }
        public string vipStatus { get; set; }
        public string communicationPreferences { get; set; }
        public DateTime dateJoined { get; set; }
        public string city { get; set; }


    }
}
