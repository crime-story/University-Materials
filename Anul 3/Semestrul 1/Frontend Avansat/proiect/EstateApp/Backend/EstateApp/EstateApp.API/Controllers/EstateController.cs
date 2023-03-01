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
    public class EstateController : ControllerBase
    {
        private readonly IMediator _mediator;
        private readonly ILogger _logger;
        private readonly IMapper _mapper;

        public EstateController(IMediator mediator, ILogger<EstateController> logger, IMapper mapper)
        {
            _mediator = mediator;
            _logger = logger;
            _mapper = mapper;
        }

        [HttpPost]
        public async Task<IActionResult> CreateEstate(CreateEstateDto dto, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"Create estate for user with username {dto.Username}");
            var testUser = _mapper.Map<Estate>(dto);
            var command = _mapper.Map<CreateEstateCommand>(dto);
            var result = await _mediator.Send(command, cancellationToken);
            if (result == null)
            {
                _logger.LogInformation("User not found!");
                return NotFound("User not found");
            }
            return Ok(result);
        }
        [HttpGet]
        public async Task<IActionResult> GetEstates()
        {
            _logger.LogInformation("Get estates");
            var query = new GetEstatesQuery();
            var result = await _mediator.Send(query);
            return Ok(result);
        }
        [HttpGet("{id}")]
        public async Task<IActionResult> GetEstateByID(int id)
        {
            _logger.LogInformation($"Get estate with ID {id}");
            var command = new GetEstateByIDQuery()
            {
                EstateID = id
            };
            var result = await _mediator.Send(command);
            return Ok(result);
        }
        [HttpGet("/username/{username}")]
        public async Task<IActionResult> GetEstatesByUsername(string username)
        {
            _logger.LogInformation($"Get derivatives for client with username {username}");
            var command = new GetEstatesByUsernameQuery()
            {
                Username = username
            };
            var result = await _mediator.Send(command);
            return Ok(result);
        }

        [HttpGet("/api/Estate/pageNumber/{pageNumber}/pageSize/{index}")]
        public async Task<IActionResult> GetProductByPage(int pageNumber, int index)
        {
            _logger.LogInformation($"Get products by page");
            var query = new GetEstatesByPageQuery
            {
                PageNumber = pageNumber,
                PageSize = index
            };
            var result = await _mediator.Send(query);

            if (result.Count == 0)
            {
                _logger.LogInformation($"Page no {pageNumber} not found");
                return NotFound("Page not found!");
            }
            return Ok(result);
        }
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateEstate(int id, [FromBody] UpdateEstateDto dto)
        {
            _logger.LogInformation($"Update estate with id {id}");
            var command = new UpdateEstateCommand
            {
                EstateID = id,
                Username = dto.Username,
                Surface = dto.Surface,
                Price = dto.Price,
                NumberOfRooms = dto.NumberOfRooms,
                Status = dto.Status,
                Address = dto.Address,
                Sector = dto.Sector,
                Neighborhood = dto.Neighborhood,
                Description = dto.Description
            };
            var result = await _mediator.Send(command);
            if (result == null)
            {
                return NotFound("Estate not found!");
            }
            return Ok(result);
        }
        [HttpDelete("{id}")]
        public async Task<IActionResult> Deleteestate(int id)
        {
            _logger.LogInformation($"Delete estate with ID {id}");
            var command = new DeleteEstateCommand
            {
                EstateID = id
            };
            var result = await _mediator.Send(command);
            if (result == null)
            {
                _logger.LogError("Estate not found");
                return NotFound("Estate not found");
            }
            return NoContent();
        }

    }
}
