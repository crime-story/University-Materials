#version 330 core
layout (location = 0) in vec3 in_pozitie;
layout (location = 1) in vec3 in_normala;

out vec3 normala;
out vec3 frag_pos;

uniform int codCol = 0;

uniform mat4 matrUmbra;
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    if (codCol == 0)
    {
        gl_Position = projection * view * model * vec4(in_pozitie, 1.0);

        mat3 normalMatrix = transpose(inverse(mat3(model)));
        normala = normalMatrix * in_normala;
        
        frag_pos = vec3(model * vec4(in_pozitie, 1.0));
    } else {
        gl_Position = projection * view * matrUmbra * model * vec4(in_pozitie, 1.f);
        frag_pos = vec3(model * vec4(in_pozitie, 1.0));
    }
}