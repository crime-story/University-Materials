// Codul sursa este adaptat dupa OpenGLBook.com

#include <windows.h>  // biblioteci care urmeaza sa fie incluse
#include <stdlib.h> // necesare pentru citirea shader-elor
#include <stdio.h>
#include <GL/glew.h> // glew apare inainte de freeglut
#include <GL/freeglut.h> // nu trebuie uitat freeglut.h

//////////////////////////////////////

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

void CreateVBO(void)
{
    // varfurile 
    GLfloat Vertices[] = {
      -0.6f, 0.2f, 0.f, 1.f,
      0.2f, 0.2f, 0.f, 1.f,
      0.2f, -0.6f, 0.f, 1.f,
      -0.6f, -0.6f, 0.f, 1.f,
      -0.2f, 0.6f, 0.f, 1.f,
      0.6f, 0.6f, 0.f, 1.f,
      0.2f, 0.2f, 0.f, 1.f,
      -0.6f, 0.2f, 0.f, 1.f,
      0.2f, 0.2f, 0.f, 1.f,
      0.6f, 0.6f, 0.f, 1.f,
      0.6f, -0.2f, 0.f, 1.f,
      0.2f, -0.6f, 0.f, 1.f,
    };

    GLfloat Colors[] = {
      0.f, 1.f, 1.f, 1.f,
      1.f, 1.f, 1.f, 1.f,
      1.f, 1.f, 0.f, 1.f,
      0.f, 1.f, 0.f, 1.f,
      0.f, 0.f, 1.f, 1.f,
      1.f, 0.f, 1.f, 1.f,
      1.f, 1.f, 1.f, 1.f,
      0.f, 1.f, 1.f, 1.f,
      1.f, 1.f, 1.f, 1.f,
      1.f, 0.f, 1.f, 1.f,
      1.f, 0.f, 0.f, 1.f,
      1.f, 1.f, 0.f, 1.f
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
    glPointSize(20.0);
    glDrawArrays(GL_QUADS, 0, 12);
    glFlush();
}
void Cleanup(void)
{
    DestroyShaders();
    DestroyVBO();
}

int main(int argc, char* argv[])
{

    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
    glutInitWindowPosition(400, 140); // pozitia initiala a ferestrei
    glutInitWindowSize(900, 600); //dimensiunile ferestrei
    glutCreateWindow("Exercitiul 4 - CUB 2D RGB"); // titlul ferestrei

    glewInit(); // nu uitati de initializare glew; trebuie initializat inainte de a a initializa desenarea
    Initialize();
    glutDisplayFunc(RenderFunction);
    glutCloseFunc(Cleanup);
    glutMainLoop();
}