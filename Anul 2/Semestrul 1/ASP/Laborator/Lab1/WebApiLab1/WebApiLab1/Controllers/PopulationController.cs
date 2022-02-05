using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebApiLab1.Controllers
{
    [Route("[controller]")]
    [ApiController]
    public class PopulationController : ControllerBase
    {
        //Example of how the path changes on a different controller
        //Run the app and see how the Swagger(the interface with which we test the requests) shows this GET endpoint
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            return Ok();
        }
    }
}
