using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab2Web.Entities
{
    //Installed Nuget Packages
 /*   
      EntityFrameworkCore, care este ORM-ul in sine 
      EntityFrameworkCore.Relational 
      EntityFrameworkCore.SqlServer, care este provider-ul spre SqlServer(baza de
        date pe care o vom folosi)
      EntityFrameworkCore.Tools
 */
    public class Lab2Context : DbContext
    {
        //Ctrl + . for visual studio suggestions in case it shows error
        public DbSet<Author> Authors { get; set; }
        public DbSet<AuthorAddress> AuthorAddresses { get; set; }
        public DbSet<Book> Books { get; set; }
        public DbSet<Concurs> Concurs { get; set; }
        public DbSet<AuthorConcurs> AuthorConcurs { get; set; }

        /*public Lab2Context(DbContextOptions<Lab2Context> options) : base(options) { }*/
        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder
                //.UseLazyLoadingProxies()
                .UseLoggerFactory(LoggerFactory.Create(builder => builder.AddConsole()))
                .UseSqlServer("Server=(localdb)\\MSSQLLocalDB;Initial Catalog=Lab2Web;Integrated Security=True;Connect Timeout=30;Encrypt=False;TrustServerCertificate=False;ApplicationIntent=ReadWrite;MultiSubnetFailover=False;MultipleActiveResultSets=true");
        }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            //One to One
            builder.Entity<Author>()
                .HasOne(a => a.AuthorAddress)
                .WithOne(aa => aa.Author);

            //One to Many
            builder.Entity<Author>()
                .HasMany(a => a.Books)
                .WithOne(b => b.Author);

            //Many To Many
            builder.Entity<AuthorConcurs>().HasKey(ac => new { ac.AuthorId, ac.ConcursId });

            builder.Entity<AuthorConcurs>()
                .HasOne<Author>(ac => ac.Author)
                .WithMany(a => a.AuthorConcurs)
                .HasForeignKey(ac => ac.AuthorId);

            builder.Entity<AuthorConcurs>()
               .HasOne<Concurs>(ac => ac.Concurs)
               .WithMany(a => a.AuthorConcurs)
               .HasForeignKey(ac => ac.ConcursId);

            base.OnModelCreating(builder);
        }
    }
}
