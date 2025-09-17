#include <windows.h>  // biblioteci care urmeaza sa fie incluse
#include <stdlib.h> // necesare pentru citirea shader-elor
#include <stdio.h>
#include <GL/glew.h> // glew apare inainte de freeglut
#include <GL/freeglut.h> // nu trebuie uitat freeglut.h

GLuint
	VaoId,
	VboId,
	ColorBufferId,
	VertexShaderId,
	FragmentShaderId,
	ProgramId;

// Shader-ul de varfuri / Vertex shader (este privit ca un sir de caractere)
const GLchar* VertexShader =
{
  "#version 400\n"\

  "layout(location=0) in vec4 in_Position;\n"\
  "layout(location=1) in vec4 in_Color;\n"\
  "out vec4 ex_Color;\n"\

  "void main(void)\n"\
  "{\n"\
  "  gl_Position = in_Position;\n"\
  "  ex_Color = in_Color;\n"\
  "}\n"
};
// Shader-ul de fragment / Fragment shader (este privit ca un sir de caractere)
const GLchar* FragmentShader =
{
  "#version 400\n"\

  "in vec4 ex_Color;\n"\
  "out vec4 out_Color;\n"\

  "void main(void)\n"\
  "{\n"\
  "  out_Color = ex_Color;\n"\
  "}\n"
};

void init (void)  // initializare fereastra de vizualizare
{
	glClearColor (1.0, 1.0, 1.0, 0.0); // precizeaza culoarea de fond a ferestrei de vizualizare

    glMatrixMode (GL_PROJECTION);  // se precizeaza este vorba de o reprezentare 2D, realizata prin proiectie ortogonala
	gluOrtho2D (0.0, 1200.0, 0.0, 700.0); // sunt indicate coordonatele extreme ale ferestrei de vizualizare
}
void desen (void) // procedura desenare  
{
	/*
	glColor3f (0.0, 0.0, 1.0); // culoarea punctelor: albastru
	{
		 glPointSize (1.0);
		glBegin (GL_POINTS); // reprezinta puncte
		glVertex2i (20, 20);
		glVertex2i (21, 21);
		glVertex2i (22, 22);
		glVertex2i (23, 23);
		glVertex2i (24, 24);
		glVertex2i (27, 27);
		glVertex2i (100, 100);
		glEnd();
		
	}
	
    
	glColor3d (0.0, 0.05, 0.05);
	// glPointSize (6.0);
	glBegin (GL_POINTS);
	   glVertex2i (100, 400);
	   glColor3f (1.0, 0.0, 0.5);
	   glVertex2i (300, 500);
    glEnd ();
	

   glColor3f (1.0, 0.0, 0.0); // culoarea liniei: rosu
       // reprezinta o linie franta
     //  glLineWidth (2.0);
	  //  glEnable (GL_LINE_STIPPLE);
	 //  glLineStipple (1, 0x1EED);
	   glBegin (GL_LINE_STRIP); 
       glVertex2i (0,100);
	   glVertex2i (400, 500);
   glEnd ( );
  // glDisable (GL_LINE_STIPPLE);
  */
    glColor3f (0.5, 0.0, 1.0);  
	//	   glLineWidth (6.0);
       glBegin (GL_LINES); // reprezinta o reuniune de segmente
       glVertex2i (400,400);
	   glVertex2i (600, 500);
	   glVertex2i (700, 520);
	   glVertex2i (655, 690);
   glEnd ( );

 
   glFlush ( ); // proceseaza procedurile OpenGL cat mai rapid
}

void CreateVBO(void)
{
	// varfurile 
	GLfloat Vertices[] = {
	  -0.1f, -0.8f, 0.0f, 1.0f,
	   0.0f,  0.8f, 0.0f, 1.0f,
	   0.8f, -0.8f, 0.0f, 1.0f,
	   -0.5f, -0.5f, 0.0f, 1.0f,
	   0.0f, 0.5f, 0.0f, 1.0f,
	   0.5f, 0.0f, 0.0f, 1.0f,
	};

	// culorile, ca atribute ale varfurilor
	GLfloat Colors[] = {
	  1.0f, 0.0f, 0.0f, 1.0f,
	  1.0f, 0.0f, 0.0f, 1.0f,
	  0.0f, 0.0f, 1.0f, 1.0f,
	  0.0f, 0.0f, 1.0f, 1.0f,
	  0.0f, 0.0f, 1.0f, 1.0f,
	  0.0f, 0.0f, 1.0f, 1.0f,
	  //  0.0f, 0.0f, 1.0f, 1.0f
	};


	// se creeaza un buffer nou
	glGenBuffers(1, &VboId);
	// este setat ca buffer curent
	glBindBuffer(GL_ARRAY_BUFFER, VboId);
	// punctele sunt "copiate" in bufferul curent
	glBufferData(GL_ARRAY_BUFFER, sizeof(Vertices), Vertices, GL_STATIC_DRAW);

	// se creeaza / se leaga un VAO (Vertex Array Object) - util cand se utilizeaza mai multe VBO
	glGenVertexArrays(1, &VaoId);
	glBindVertexArray(VaoId);
	// se activeaza lucrul cu atribute; atributul 0 = pozitie
	glEnableVertexAttribArray(0);
	// 
	glVertexAttribPointer(0, 4, GL_FLOAT, GL_FALSE, 0, 0);

	// un nou buffer, pentru culoare
	glGenBuffers(1, &ColorBufferId);
	glBindBuffer(GL_ARRAY_BUFFER, ColorBufferId);
	glBufferData(GL_ARRAY_BUFFER, sizeof(Colors), Colors, GL_STATIC_DRAW);
	// atributul 1 =  culoare
	glEnableVertexAttribArray(1);
	glVertexAttribPointer(1, 4, GL_FLOAT, GL_FALSE, 0, 0);


}

void CreateVBO2(void)
{

	// varfurile 
	GLfloat Vertices[] = {
	  -0.7f, -0.8f, 0.5f, 1.0f,
	   1.0f,  0.8f, 0.4f, 1.0f,
	   0.8f, -0.9f, 0.3f, 1.0f,
	   -0.1f, 0.7f, 0.2f, 1.0f,
	   0.2f, 0.6f, 0.1f, 1.0f,
	   0.4f, 0.1f, 0.6f, 1.0f,
	};

	// culorile, ca atribute ale varfurilor
	GLfloat Colors[] = {
	  1.0f, 0.0f, 0.0f, 1.f,
	  1.0f, 0.8f, 0.7f, 1.f,
	  0.9, 0.0, 1.0, 1.f,
	  0.8, 0.0, 1.0, 1.f,
	  0.7, 0.0, 1.0, 1.f,
	  0.9, 0.3, 1.0, 1.f,
	};


	// se creeaza un buffer nou
	glGenBuffers(1, &VboId);
	// este setat ca buffer curent
	glBindBuffer(GL_ARRAY_BUFFER, VboId);
	// punctele sunt "copiate" in bufferul curent
	glBufferData(GL_ARRAY_BUFFER, sizeof(Vertices), Vertices, GL_STATIC_DRAW);

	// se creeaza / se leaga un VAO (Vertex Array Object) - util cand se utilizeaza mai multe VBO
	glGenVertexArrays(1, &VaoId);
	glBindVertexArray(VaoId);
	// se activeaza lucrul cu atribute; atributul 0 = pozitie
	glEnableVertexAttribArray(0);
	// 
	glVertexAttribPointer(0, 4, GL_FLOAT, GL_FALSE, 0, 0);

	// un nou buffer, pentru culoare
	glGenBuffers(1, &ColorBufferId);
	glBindBuffer(GL_ARRAY_BUFFER, ColorBufferId);
	glBufferData(GL_ARRAY_BUFFER, sizeof(Colors), Colors, GL_STATIC_DRAW);
	// atributul 1 =  culoare
	glEnableVertexAttribArray(1);
	glVertexAttribPointer(1, 4, GL_FLOAT, GL_FALSE, 0, 0);


}

void DestroyVBO(void)
{


	glDisableVertexAttribArray(1);
	glDisableVertexAttribArray(0);

	glBindBuffer(GL_ARRAY_BUFFER, 0);
	glDeleteBuffers(1, &ColorBufferId);
	glDeleteBuffers(1, &VboId);

	glBindVertexArray(0);
	glDeleteVertexArrays(1, &VaoId);


}

void CreateShaders(void)
{

	VertexShaderId = glCreateShader(GL_VERTEX_SHADER);
	glShaderSource(VertexShaderId, 1, &VertexShader, NULL);
	glCompileShader(VertexShaderId);

	FragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
	glShaderSource(FragmentShaderId, 1, &FragmentShader, NULL);
	glCompileShader(FragmentShaderId);

	ProgramId = glCreateProgram();
	glAttachShader(ProgramId, VertexShaderId);
	glAttachShader(ProgramId, FragmentShaderId);
	glLinkProgram(ProgramId);
	glUseProgram(ProgramId);

}
void DestroyShaders(void)
{

	glUseProgram(0);

	glDetachShader(ProgramId, VertexShaderId);
	glDetachShader(ProgramId, FragmentShaderId);

	glDeleteShader(FragmentShaderId);
	glDeleteShader(VertexShaderId);

	glDeleteProgram(ProgramId);

}

void Initialize(void)
{
	glClearColor(1.0f, 1.0f, 1.0f, 0.0f); // culoarea de fond a ecranului
}
void RenderFunction(void)
{
	glClear(GL_COLOR_BUFFER_BIT);
	CreateVBO();
	CreateShaders();
	glPointSize(1.0);

	glDrawArrays(GL_POINTS, 0, 8);

	CreateVBO2();
	glDrawArrays(GL_LINES, 0, 6);

	glFlush();
}

void Cleanup(void)
{
	DestroyShaders();
	DestroyVBO();
}


int main(int argc, char* argv[])
{
	glutInit(&argc, argv); // initializare GLUT
	glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB); // se utilizeaza un singur buffer | modul de colorare RedGreenBlue (= default)
	glutInitWindowPosition(100, 100); // pozitia initiala a ferestrei de vizualizare (in coordonate ecran)
	glutInitWindowSize(900, 600); // dimensiunile ferestrei 
	glutCreateWindow("Puncte & Segmente"); // creeaza fereastra, indicand numele ferestrei de vizualizare - apare in partea superioara
	glewInit();

	init(); // executa procedura de initializare
	glClear(GL_COLOR_BUFFER_BIT); // reprezentare si colorare fereastra de vizualizare
	glutDisplayFunc(RenderFunction); // procedura desen este invocata ori de cate ori este nevoie
	//glutDisplayFunc(desen); // procedura desen este invocata ori de cate ori este nevoie
	glutMainLoop(); // ultima instructiune a programului, asteapta (eventuale) noi date de intrare
}