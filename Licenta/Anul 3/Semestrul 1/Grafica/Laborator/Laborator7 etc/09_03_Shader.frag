// Shader-ul de fragment / Fragment shader  
#version 330

in vec3 ex_Color; 
out vec3 out_Color;
uniform int codCol;

void main(void)
{
    switch (codCol)
    {
        case 1: 
            out_Color=vec3(0.5, 0.5, 0.5); 
            break;
        default: 
            out_Color=ex_Color;
    }
}