using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using proiectASP.Managers;
using proiectASP.Models;

namespace proiectASP.Controllers
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
        public async Task<IActionResult> GetProducts()
        {
            var products = manager.GetProducts();

            return Ok(products);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetProductById([FromRoute] string id)
        {
            var product = manager.GetProductById(id);

            return Ok(product);
        }

        [HttpPost]
        public async Task<IActionResult> Create([FromBody] ProductModel productModel)
        {
            manager.Create(productModel);

            return Ok();
        }

        [HttpPut]
        public async Task<IActionResult> Update([FromBody] ProductModel productModel)
        {
            manager.Update(productModel);

            return Ok();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete([FromRoute] string id)
        {
            manager.Delete(id);

            return Ok();
        }
    }
}
