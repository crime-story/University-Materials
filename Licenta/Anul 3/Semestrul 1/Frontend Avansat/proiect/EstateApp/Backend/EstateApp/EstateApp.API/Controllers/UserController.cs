using EstateApp.Domain.Dtos;
using EstateApp.Domain.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace EstateApp.API.Controllers
{
    [Route("api/user")]
    public class UserController : ControllerBase
    {
        private readonly UserManager<User> _userManager;
        private readonly RoleManager<IdentityRole> _roleManager;
        public UserController(UserManager<User> userManager, RoleManager<IdentityRole> roleManager)
        {
            _userManager = userManager;
            _roleManager = roleManager;
        }
        [HttpPost]
        [Route("login")]
        public async Task<IActionResult> Login([FromBody] LoginUserDto loginUserDto)
        {
            var user = await _userManager.FindByNameAsync(loginUserDto.UserName);
            if (user != null && await _userManager.CheckPasswordAsync(user, loginUserDto.Password))
            {
                var userRoles = await _userManager.GetRolesAsync(user);
                var authClaims = new List<Claim>
                {
                    new Claim(ClaimTypes.Name, user.UserName)
                };
                foreach (var userRole in userRoles)
                {
                    authClaims.Add(new Claim(ClaimTypes.Role, userRole));
                }
                var authSigninKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes("this is my custom Secret key for authentication"));
                var token = new JwtSecurityToken(
                    issuer: "https://localhost:7045",
                    audience: "http://localhost:4200",
                    claims: authClaims,
                    expires: DateTime.Now.AddHours(10),
                    signingCredentials: new SigningCredentials(authSigninKey, SecurityAlgorithms.HmacSha256)
                    );
                return Ok(new
                {
                    token = new JwtSecurityTokenHandler().WriteToken(token),
                    expiration = token.ValidTo
                });
            }
            return Unauthorized();
        }

        [HttpPost]
        [Route("register")]
        public async Task<IActionResult> Register([FromBody]RegisterUserDto registerUserDto)
        {

            var userExist = await _userManager.FindByNameAsync(registerUserDto.UserName);
            if (userExist != null)
            {
                //return BadRequest("User already exists");
                return Conflict(new { message = "User already exists" });
            }
            User user = new User
            {
                UserName = registerUserDto.UserName,


            };
            var myRole = await _roleManager.FindByNameAsync(registerUserDto.Role);
            var result = await _userManager.CreateAsync(user, registerUserDto.Password);
            if (!result.Succeeded)
            {
                return BadRequest("failed to create user");
            }
            await AddToRole(registerUserDto.UserName, registerUserDto.Role);
            return Ok(registerUserDto.UserName);
        }
        [HttpPost]
        [Route("assign-role")]
        public async Task<IActionResult> AddToRole(string userName, string roleName)
        {
            var userExists = await _userManager.FindByNameAsync(userName);
            if (userExists == null)
            {
                return BadRequest("User does not exist");
            }
            var role = await _roleManager.FindByNameAsync(roleName);
            if (role == null)
            {
                var roleAdded = await _roleManager.CreateAsync(new IdentityRole
                {
                    Name = roleName
                });
            }
            var addRoleToUser = await _userManager.AddToRoleAsync(userExists, roleName);
            if (!addRoleToUser.Succeeded)
            {
                return BadRequest("Failed to add user to role");
            }
            return Ok($"User added successfully to {roleName} role");
        }

        [HttpPut]
        [Route("new-password")]
        public async Task<IActionResult> ChangePassword(string username, string password, string newPassword)
        {
            var userExists = await _userManager.FindByNameAsync(username);
            if (userExists != null && await _userManager.CheckPasswordAsync(userExists, password))
            {

                var result = await _userManager.ChangePasswordAsync(userExists, password, newPassword);
                if (result != null)
                {
                    return Ok(result);
                }
                return BadRequest(result.Errors);
            }
            return BadRequest("Failed to change password!");
        }

        [HttpPut]
        [Route("new-email")]
        public async Task<IActionResult> ChangeUsername(string username, string password, string newUsername)
        {
            var userExists = await _userManager.FindByNameAsync(username);
            if (userExists != null && await _userManager.CheckPasswordAsync(userExists, password))
            {

                var result = await _userManager.SetUserNameAsync(userExists, newUsername);
                if (result != null)
                {
                    return Ok(result);
                }
                return BadRequest(result.Errors);
            }
            return BadRequest("Failed to change email!");
        }
        [HttpDelete]
        [Route("")]
        public async Task<IActionResult> DeleteAccount(string username, string password)
        {
            var userExists = await _userManager.FindByNameAsync(username);
            if(userExists != null && await _userManager.CheckPasswordAsync(userExists, password))
            {
                await _userManager.DeleteAsync(userExists);
                return NoContent();
            }
            return BadRequest("Failed to delete account!");
        }
    }
}
