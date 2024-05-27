using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Newtonsoft.Json;
using System.Net.Http.Headers;
using System.Text;
using System.Text.Json;
using ZeloraClient.JwtToken;
using ZeloraClient.Models;

namespace ZeloraClient.Pages
{
    public class RegisterModel : PageModel
    {
        private readonly HttpClient _httpClient;
        private readonly IJwtTokenService _jwtTokenService;

        public RegisterModel(HttpClient httpClient, IJwtTokenService jwtTokenService)
        {
            _httpClient = httpClient;
            _jwtTokenService = jwtTokenService;
        }

        [BindProperty]
        public Customer RegistrationData { get; set; }

        public async Task<IActionResult> OnPostAsync()
        {
            string apiUrl = "http://localhost:8080/zelora/customers/register";

            try
            {
                // Serialize the registration data to JSON
                string request = JsonConvert.SerializeObject(RegistrationData);

                // Create a StringContent object with JSON data
                var content = new StringContent(request, Encoding.UTF8, "application/json");

                // Set authorization header with JWT token
                _httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", _jwtTokenService.Token);

                // Send the POST request to the registration endpoint
                HttpResponseMessage response = await _httpClient.PostAsync(apiUrl, content);

                if (response.IsSuccessStatusCode)
                {
                    Console.WriteLine("Customer added successfully.");
                    return RedirectToPage("/SuccessPage");
                }
                else
                {
                    Console.WriteLine($"Failed to add customer. Status code: {response.StatusCode}");
                    ViewData["ErrorMessage"] = "Failed to add customer. Status code: " + response.StatusCode;
                    return Page();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"An error occurred: {ex.Message}");
                ViewData["ErrorMessage"] = "An error occurred: " + ex.Message;
                return Page();
            }
        }
    }
}
