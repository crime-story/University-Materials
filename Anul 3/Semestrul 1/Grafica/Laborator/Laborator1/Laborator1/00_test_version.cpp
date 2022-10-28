#include<windows.h>
#include <GL/glew.h> 
#include<gl/freeglut.h> 
#include<cstdio>


void main (int argc, char** argv) 

{ 
	glutInit (&argc, argv); 
	glutInitDisplayMode (GLUT_SINGLE | GLUT_RGB); 
	glutInitWindowPosition (1100, 100); 
	glutInitWindowSize (600, 400); 
	glutCreateWindow ("GL_VERSION"); 
	glewInit ( ); 
	printf("OpenGL version supported by this platform: (%s) \n", glGetString(GL_VERSION)); 
	printf("GLSL version supported by this platform: (%s) \n", glGetString(GL_SHADING_LANGUAGE_VERSION)); 
	glutMainLoop ( ); 
}
