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
    public class EstateImageController : Controller
    {
        private readonly IMediator _mediator;
        private readonly ILogger _logger;
        private readonly IMapper _mapper;
        public EstateImageController(IMediator mediator, ILogger<EstateImageController> logger, IMapper mapper)
        {
            _mediator = mediator;
            _logger = logger;
            _mapper = mapper;
        }
        [HttpPost]
        public async Task<IActionResult> CreateEstate(CreateEstateImageDto dto, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"Create estate image for estate with id {dto.EstateID}");
            var testUser = _mapper.Map<EstateImage>(dto);
            var command = _mapper.Map<CreateEstateImageCommand>(dto);
            var result = await _mediator.Send(command, cancellationToken);
            if (result == null)
            {
                _logger.LogInformation("Estate not found!");
                return NotFound("Estate not found");
            }
            return Ok(result);
        }
        [HttpGet]
        public async Task<IActionResult> GetEstatesImages()
        {
            _logger.LogInformation("Get estates images");
            var query = new GetEstatesImagesQuery();
            var result = await _mediator.Send(query);
            return Ok(result);
        }
        [HttpGet("{id}")]
        public async Task<IActionResult> GetEstateImageByID(int id)
        {
            _logger.LogInformation($"Get estate image with ID {id}");
            var command = new GetEstateImageByIDQuery()
            {
                EstateImageID = id
            };
            var result = await _mediator.Send(command);
            return Ok(result);
        }
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteEstateImage(int id)
        {
            _logger.LogInformation($"Delete estate image with ID {id}");
            var command = new DeleteEstateImageCommand
            {
                EstateImageID = id
            };
            var result = await _mediator.Send(command);
            if(result == null)
            {
                _logger.LogError("Image not found");
                return NotFound("Image not found");
            }
            return NoContent();
        }
    }
}
