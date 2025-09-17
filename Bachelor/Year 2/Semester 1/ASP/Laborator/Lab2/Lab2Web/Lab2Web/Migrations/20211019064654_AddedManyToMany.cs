using Microsoft.EntityFrameworkCore.Migrations;

namespace Lab2Web.Migrations
{
    public partial class AddedManyToMany : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Concurs",
                columns: table => new
                {
                    Id = table.Column<string>(type: "nvarchar(450)", nullable: false),
                    Name = table.Column<string>(type: "nvarchar(max)", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Concurs", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "AuthorConcurs",
                columns: table => new
                {
                    AuthorId = table.Column<string>(type: "nvarchar(450)", nullable: false),
                    ConcursId = table.Column<string>(type: "nvarchar(450)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_AuthorConcurs", x => new { x.AuthorId, x.ConcursId });
                    table.ForeignKey(
                        name: "FK_AuthorConcurs_Authors_AuthorId",
                        column: x => x.AuthorId,
                        principalTable: "Authors",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_AuthorConcurs_Concurs_ConcursId",
                        column: x => x.ConcursId,
                        principalTable: "Concurs",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_AuthorConcurs_ConcursId",
                table: "AuthorConcurs",
                column: "ConcursId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "AuthorConcurs");

            migrationBuilder.DropTable(
                name: "Concurs");
        }
    }
}
