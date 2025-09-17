using AutoMapper;
using EstateApp.Aplication.Commands;
using EstateApp.Aplication.Queries;
using EstateApp.Domain.Dtos;
using EstateApp.Domain.Models;
using MediatR;
using Microsoft.AspNetCore.Mvc;

namespace EstateApp.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserDetailsController : ControllerBase
    {
        private readonly IMediator _mediator;
        private readonly ILogger _logger;
        private readonly IMapper _mapper;

        public UserDetailsController(IMediator mediator, ILogger<EstateController> logger, IMapper mapper)
        {
            _mediator = mediator;
            _logger = logger;
            _mapper = mapper;
        }

        [HttpPost]
        public async Task<IActionResult> CreateUserDetails(CreateUserDetailsDto dto, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"Create user details for user with username {dto.Username}");
            var testUser = _mapper.Map<UserDetails>(dto);
            var command = _mapper.Map<CreateUserDetailsCommand>(dto);
            var result = await _mediator.Send(command, cancellationToken);

            if (result == null)
            {
                _logger.LogInformation("Client already exists");
                return NotFound("Client exists");
            }

            return Ok(result);

        }

        [HttpGet]
        public async Task<IActionResult> GetUsersDetails()
        {
            _logger.LogInformation("Get users details");
            var query = new GetUsersDetailsQuery();
            var result = await _mediator.Send(query);
            return Ok(result);
        }

        [HttpGet("{username}")]
        public async Task<IActionResult> GetUserDetailsByUsername(string username)
        {
            _logger.LogInformation($"Get details for user with username {username}");
            var command = new GetUserDetailsByUsernameQuery()
            {
                Username = username
            };
            var result = await _mediator.Send(command);
            return Ok(result);
        }

        [HttpPut("{username}")]
        public async Task<IActionResult> UpdateUserDetails(string username, [FromBody] UpdateUserDetailsDto dto)
        {
            _logger.LogInformation($"Update user with username {username}");
            var command = new UpdateUserDetailsCommand
            {
                Username = username,
                FullName = dto.FullName,
                Age = dto.Age,
                Description = dto.Description,
                ProfileURL = dto.ProfileURL,
                Residence = dto.Residence
            };
            var result = await _mediator.Send(command);
            if(result == null)
            {
                return NotFound("Estate not found!");
            }
            return Ok(result);
        }

        [HttpDelete("{username}")]
        public async Task<IActionResult> DeleteUserDetails(string username)
        {
            _logger.LogInformation($"Delete user with username {username}");
            var command = new DeleteUserDetailsCommand
            {
                Username = username
            };
            var result = await _mediator.Send(command);
            if(result == null)
            {
                _logger.LogError("User not found");
                return NotFound("User not found");
            }
            return NoContent();

        }
    }
}
