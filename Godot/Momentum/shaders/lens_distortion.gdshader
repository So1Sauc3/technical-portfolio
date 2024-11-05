shader_type canvas_item;
render_mode blend_mix, unshaded;
uniform sampler2D SCREEN_TEXTURE: hint_screen_texture, filter_linear_mipmap;
uniform float intensity: hint_range(-10, 10) = -1;
uniform float offset = 1.0;

void fragment() {
	vec2 uv = (FRAGCOORD.xy / (1.0/SCREEN_PIXEL_SIZE).xy) - vec2(0.5);
	float uva = atan(uv.x, uv.y);
	float uvd = sqrt(dot(uv, uv));
	//k = negative for pincushion, positive for barrel
	float k = intensity;
	uvd = uvd*(1.0 + k*uvd*uvd);
	COLOR = texture(SCREEN_TEXTURE, vec2(0.5) + vec2(sin(uva), cos(uva))*uvd);
}