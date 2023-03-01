#version 330 core
out vec4 FragColor;

in vec3 normala;
in vec3 frag_pos;

uniform int codCol = 0;

uniform vec3 uColor = vec3(1, 0, 0);

uniform vec3 camera_pos;

uniform float fogStr = 1.f;
uniform vec3 fogColor = vec3(.1, .1, .1);
uniform vec3 ambient = vec3(1,1,1);

uniform vec3 pozitie_lumina;
uniform vec3 diffuse_lumina = vec3(.5);
uniform vec3 specular_lumina = vec3(.2);

float spec_factor = 64.f;

float ceata() {
    float dist_to_camera = distance(frag_pos, camera_pos);
    float influence = min(1.f, dist_to_camera / (35.f - 10.f));

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
		vec3 normala_ = normala;

		vec3 lightDir = normalize(pozitie_lumina - frag_pos);

		float diff = max(dot(normala_, lightDir), 0.0);
		vec3 diffuse = diff * diffuse_lumina * uColor;
	
		// Specular
		vec3 lookVector = normalize(camera_pos - frag_pos);
		vec3 lightDirReflected = reflect(-lightDir, normala_);
		float spec = pow(dot(lookVector, lightDirReflected), spec_factor);

		vec3 specular = specular_lumina * spec;

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