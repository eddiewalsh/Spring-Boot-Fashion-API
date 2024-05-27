using Newtonsoft.Json;
using System.Text;
using ZeloraClient.Models;

namespace ZeloraClient.JwtToken
{
    public class JwtTokenMiddleware
    {
        private readonly RequestDelegate _next;
        private readonly IJwtTokenService _jwtTokenService;
        private static string AUTH_URL => "http://localhost:8080/authenticate";

        public JwtTokenMiddleware(RequestDelegate next, IJwtTokenService jwtTokenService)
        {
            _next = next;
            _jwtTokenService = jwtTokenService;
        }

        public async Task Invoke(HttpContext context)
        {
            var token = await RetrieveJwtToken();

            _jwtTokenService.Token = token;

            await _next(context);
        }

        private async Task<string> RetrieveJwtToken()
        {
            AuthenticationRequest authenticationRequest = new AuthenticationRequest();

            string tokeRequest = JsonConvert.SerializeObject(authenticationRequest);

            Console.WriteLine(tokeRequest);

            using (var client = new HttpClient())
            {
                var content = new StringContent(tokeRequest, Encoding.UTF8, "application/json");

                var tokenResponse = await client.PostAsync(AUTH_URL, content);
                if (tokenResponse.IsSuccessStatusCode)
                {
                    var responseContent = await tokenResponse.Content.ReadAsStringAsync();
                    var responseObject = JsonConvert.DeserializeObject<dynamic>(responseContent);

                    return responseObject.jwt.ToString();
                }

                throw new Exception("Unable to get JWT");
            }
        }
    }
}
