
// Shader-ul de fragment / Fragment shader  
 
 #version 400

in vec4 ex_Color;
out vec4 out_Color;
uniform int codCol;

void main(void)
  {
	switch( codCol ){
		case 1:
			out_Color = vec4 (0.0, 0.0, 1.0, 0.0);
			break;
		case 2:
			out_Color = vec4 (0.5, 0.0, 1.0, 0.0);
			break;
		case 3:
			out_Color = vec4 (1.0, 0.0, 1.0, 0.0);
			break;
		default:
			out_Color = ex_Color;
			break;
	}
  }
 