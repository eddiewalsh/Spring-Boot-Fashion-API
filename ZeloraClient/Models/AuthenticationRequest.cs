namespace ZeloraClient.Models
{
    public class AuthenticationRequest
    {
        public string username { get; set; }
        public string password { get; set; }

        public AuthenticationRequest()
        {
            username = "admin";
            password = "password";
        }
    }
}
