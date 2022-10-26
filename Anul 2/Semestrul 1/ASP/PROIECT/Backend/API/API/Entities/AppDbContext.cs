using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace API.Entities
{
    public class AppDbContext : IdentityDbContext<User, Role, string, IdentityUserClaim<string>,
                                                      UserRole, IdentityUserLogin<string>,
                                                      IdentityRoleClaim<string>, IdentityUserToken<string>>
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }
        public DbSet<Driver> Drivers { get; set; }
        public DbSet<Location> Locations { get; set; }
        public DbSet<Menu> Menus { get; set; }
        public DbSet<Order> Orders { get; set; }
        public DbSet<OrderContent> OrderContents { get; set; }
        public DbSet<Product> Products { get; set; }
        public DbSet<Restaurant> Restaurants { get; set; }
        public DbSet<Employee> Employees { get; set; }
        public DbSet<Vehicle> Vehicles { get; set; }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);

            // One to One Relationship between Location and Restaurant
            builder.Entity<Location>()
                .HasOne(a => a.Restaurant)
                .WithOne(aa => aa.Location);

            // Many to One Relationship between Employees and Restaurant
            builder.Entity<Restaurant>()
                .HasMany(a => a.Employees)
                .WithOne(b => b.Restaurant);

            // Many To Many Relationship between Restaurant and Product
            builder.Entity<Menu>().HasKey(ac => new { ac.RestaurantId, ac.ProductId });

            builder.Entity<Menu>()
                .HasOne<Restaurant>(ac => ac.Restaurant)
                .WithMany(a => a.Menus)
                .HasForeignKey(ac => ac.RestaurantId);

            builder.Entity<Menu>()
               .HasOne<Product>(ac => ac.Product)
               .WithMany(a => a.Menus)
               .HasForeignKey(ac => ac.ProductId);

            // Many To Many Relationship between Product and Order
            builder.Entity<OrderContent>().HasKey(ac => new { ac.ProductId, ac.OrderId });

            builder.Entity<OrderContent>()
                .HasOne<Product>(ac => ac.Product)
                .WithMany(a => a.OrderContents)
                .HasForeignKey(ac => ac.ProductId);

            builder.Entity<OrderContent>()
               .HasOne<Order>(ac => ac.Order)
               .WithMany(a => a.OrderContents)
               .HasForeignKey(ac => ac.OrderId);

            // One to Many Relationship between Driver and Order
            builder.Entity<Order>()
                .HasOne(a => a.Driver)
                .WithMany(aa => aa.Orders);

            // One to Many Relationship between Vehicle and Driver
            builder.Entity<Driver>()
                .HasMany(a => a.Vehicles)
                .WithOne(b => b.Driver);

            //base.OnModelCreating(builder);   
        }
    }
}
