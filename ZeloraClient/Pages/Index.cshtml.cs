using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Newtonsoft.Json;
using System.Net.Http.Headers;
using System.Text;
using ZeloraClient.JwtToken;
using ZeloraClient.Models;

namespace ZeloraClient.Pages
{
    public class IndexModel : PageModel
    {
        private readonly IJwtTokenService _jwtTokenService;

        public Customer Customer { get; set; }

        public IndexModel(IJwtTokenService jwtTokenService)
        {
            _jwtTokenService = jwtTokenService;
        }

        public async Task<IActionResult> OnGetAsync(string customerEmail)
        {
            if (customerEmail == null) {
                return Page();
            }

            using (var client = new HttpClient())
            {
                client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", _jwtTokenService.Token);
                client.BaseAddress = new Uri("http://localhost:8080/zelora/customers/");

                HttpResponseMessage response = await client.GetAsync($"email/{customerEmail}");

                if (response.IsSuccessStatusCode)
                {
                    string responseBody = await response.Content.ReadAsStringAsync();
                    Customer = JsonConvert.DeserializeObject<Customer>(responseBody);
                }
            }

            return Page();
        }
    }
}

