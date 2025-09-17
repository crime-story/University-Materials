using API.Managers;
using API.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProductsController : ControllerBase
    {
        private readonly IProductsManager manager;

        public ProductsController(IProductsManager productsManager)
        {
            this.manager = productsManager;
        }

        [HttpGet("getAll")]
        //[Authorize(Policy = "BasicUser")]
        public async Task<IActionResult> GetProducts()
        {
            var products = manager.GetProducts();

            return Ok(products);
        }

        [HttpGet("{id}")]
        //[Authorize(Policy = "BasicUser")]
        public async Task<IActionResult> GetProductById([FromRoute] string id)
        {
            var product = manager.GetProductById(id);

            return Ok(product);
        }

        [HttpPost]
        //[Authorize(Policy = "Admin")]
        public async Task<IActionResult> Create([FromBody] ProductModel productModel)
        {
            manager.Create(productModel);

            return Ok();
        }

        [HttpPut]
        //[Authorize(Policy = "Admin")]
        public async Task<IActionResult> Update([FromBody] ProductModel productModel)
        {
            manager.Update(productModel);

            return Ok();
        }

        [HttpDelete("{id}")]
        //[Authorize(Policy = "Admin")]
        public async Task<IActionResult> Delete([FromRoute] string id)
        {
            manager.Delete(id);

            return Ok();
        }
    }
}
