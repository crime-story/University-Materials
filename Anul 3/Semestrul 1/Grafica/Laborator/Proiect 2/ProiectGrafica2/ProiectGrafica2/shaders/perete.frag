#version 330 core
out vec4 FragColor;

in vec2 tex_coords;
in vec3 normala;
in vec3 frag_pos;
in mat3 TBN;

uniform int codCol = 0;

uniform vec3 camera_pos;

uniform float fogStr = 1.f;
uniform vec3 fogColor = vec3(.1, .1, .1);
uniform vec3 ambient = vec3(1,1,1);

uniform vec3 pozitie_lumina;
uniform vec3 diffuse_lumina = vec3(1.f);
uniform vec3 specular_lumina = vec3(.3);

float spec_factor = 64.f;

uniform sampler2D tex_diffuse;
uniform sampler2D tex_normal;
uniform sampler2D tex_specular;

float ceata() {
    float dist_to_camera = distance(frag_pos, camera_pos);
    float influence = min(1.f, dist_to_camera / (45.f - 15.f));

    return influence * fogStr;
}

void main()
{
    vec3 color = vec3(0.0, 0.0, 0.0);

    if (codCol==0)
	{
        // Ambient
		float ambientStrength = 0.0f;
		vec3 ambient = ambient * ambientStrength;
	
		// Diffuse 
		vec3 normala_ = texture(tex_normal, tex_coords).rgb;
		normala_ = normala_ * 2.0 - 1.0;  
		normala_ = normalize(TBN * normala_);

		vec3 lightDir = normalize(pozitie_lumina - frag_pos);

		float diff = max(dot(normala_, lightDir), 0.0);
		vec3 diffuse = diff * diffuse_lumina * vec3(texture(tex_diffuse, tex_coords));
	
		// Specular
		vec3 lookVector = normalize(camera_pos - frag_pos);
		vec3 lightDirReflected = reflect(-lightDir, normala_);
		float spec = pow(dot(lookVector, lightDirReflected), spec_factor);

		vec3 specular = specular_lumina * spec *  vec3(texture(tex_specular, tex_coords));

		vec3 emission=vec3(0.0, 0.0, 0.0);
		vec3 result = emission + ambient + diffuse + specular;

		result =  mix(result, fogColor, ceata());

        FragColor = vec4(result, 1.0f);
    }

    else if (codCol==1)
	{
		color = vec3 (0.0, 0.0, 0.0);

        // ceata
        color = mix(color, fogColor, ceata());

		FragColor = vec4 (color, 1.0);
	 }
}