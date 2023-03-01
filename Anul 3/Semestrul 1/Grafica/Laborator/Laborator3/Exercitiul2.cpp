#include <windows.h>
#include <gl/freeglut.h>

void init(void)  // initializare fereastra de vizualizare
{
	glClearColor(1.0, 1.0, 1.0, 0.0); // precizeaza culoarea de fond a ferestrei de vizualizare

	glMatrixMode(GL_PROJECTION);  // se precizeaza este vorba de o reprezentare 2D, realizata prin proiectie ortogonala
	gluOrtho2D(0, 800.0, 0.0, 700.0); // sunt indicate coordonatele extreme ale ferestrei de vizualizare
}

void desen(void)
{	
	glBegin(GL_TRIANGLE_FAN);
	glColor3f(0.0, 0.5, 0.0);
	glVertex2i(0, 700);
	glColor3f(0.8, 0.8, 0.0);
	glVertex2i(800, 700);
	glColor3f(0.8, 0.8, 1.0);
	glVertex2i(0, 0);
	glEnd();
	glBegin(GL_TRIANGLE_FAN);
	glColor3f(0.0, 0.8, 0.0);
	glVertex2i(800, 0);
	glColor3f(0.8, 0.8, 0.0);
	glVertex2i(800, 700);
	glColor3f(0.8, 0.8, 1.0);
	glVertex2i(0, 0);
	glEnd();
	glFlush();
}

void desen2(void)
{

	glColor3f(1, 0, 0);
	glRecti(-30, 20, 40, 10);
	glColor3f(1, 1, 0);
	glMatrixMode(GL_MODELVIEW);
	glPushMatrix();
	glTranslatef(-10.0, -20.0, 0.0);
	glScalef(0.5, 2.0, 0.0);
	glRecti(-30, 20, 40, 10);
	glPopMatrix();

	glPointSize(4.0);
	glColor3f(0, 0, 0);
	glBegin(GL_POINTS);
	glVertex3f(40, 20, 0);
	glEnd();

	glPointSize(8.0);
	glColor3f(0, 0, 0);
	glBegin(GL_POINTS);
	glVertex3f(10, 20, 0);
	glEnd();

	glFlush();

}
void main(int argc, char** argv)
{
	glutInit(&argc, argv); // initializare GLUT
	glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB); // se utilizeaza un singur buffer | modul de colorare RedGreenBlue (= default)
	glutInitWindowPosition(100, 100); // pozitia initiala a ferestrei de vizualizare (in coordonate ecran)
	glutInitWindowSize(900, 600); // dimensiunile ferestrei 
	glutCreateWindow("Exercitiul 2"); // creeaza fereastra, indicand numele ferestrei de vizualizare - apare in partea superioara

	init(); // executa procedura de initializare
	glClear(GL_COLOR_BUFFER_BIT); // reprezentare si colorare fereastra de vizualizare
	glutDisplayFunc(desen); // procedura desen este invocata ori de cate ori este nevoie
	glutMainLoop(); // ultima instructiune a programului, asteapta (eventuale) noi date de intrare
}