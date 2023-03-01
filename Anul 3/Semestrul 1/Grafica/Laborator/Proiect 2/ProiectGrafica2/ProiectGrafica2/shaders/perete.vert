#version 330 core
layout (location = 0) in vec3 in_pozitie;
layout (location = 1) in vec3 in_normala;
layout (location = 2) in vec2 in_tex_coords;
layout (location = 3) in vec3 in_tangenta;

out vec2 tex_coords;
out vec3 normala;
out vec3 frag_pos;
out mat3 TBN;

uniform int codCol = 0;
uniform float texRatio;

uniform mat4 matrUmbra;
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    gl_Position = projection * view * model * vec4(in_pozitie, 1.0);

    mat3 normalMatrix = transpose(inverse(mat3(model)));
    normala = normalMatrix * in_normala;
        
    frag_pos = vec3(model * vec4(in_pozitie, 1.0));
    tex_coords = vec2(in_tex_coords.x * texRatio, in_tex_coords.y);

    vec3 T = normalize(vec3(model * vec4(in_tangenta,   0.0)));
    vec3 N = normalize(vec3(model * vec4(in_normala,    0.0)));
    vec3 B = cross(N, T);
    TBN = mat3(T, B, N);

        /*
    if (codCol == 0)
    {
        gl_Position = projection * view * model * vec4(in_pozitie, 1.0);

        // mat3 normalMatrix = transpose(inverse(mat3(model)));
        //vec3 normal = vec3(0,0,-1);
        //normala = normalMatrix * normal;
        
        frag_pos = vec3(model * vec4(in_pozitie, 1.0));
        // coord_tex = in_coord_tex;
    }
    else if (codCol==1)
    {
		gl_Position = projection*view*matrUmbra*model*vec4(in_pozitie, 1.f);
        frag_pos = vec3(matrUmbra * model * vec4(in_pozitie, 1.0));
    } 
    */
}