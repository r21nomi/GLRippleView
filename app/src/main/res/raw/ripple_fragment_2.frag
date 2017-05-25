precision mediump float;

varying vec2 texcoordVarying;
uniform sampler2D texture;
uniform float time;

uniform float u_ripple_strength;
uniform float u_ripple_offset;
uniform float u_ripple_frequency;
uniform float u_ripple_center_uv_x;
uniform float u_ripple_center_uv_y;
uniform float u_ripple_sine_disappear_distance;

vec2 GetRippleDistortedUv(
    float distance_source_u,
    float distance_source_v,
    float distort_source_u,
    float distort_source_v,
    float distance_center_x,
    float distance_center_y)
{
    float distance_square = (distance_source_u - distance_center_x) * (distance_source_u - distance_center_x)
        + (distance_source_v - distance_center_y) * (distance_source_v - distance_center_y);
    float distance = sqrt(distance_square);
    float sine_disappear_distance = u_ripple_sine_disappear_distance;
    float normalized_distance = clamp(distance, 0.0, sine_disappear_distance) / sine_disappear_distance;
    float sine_strength = u_ripple_strength * (1.0 - normalized_distance) * (1.0 - normalized_distance);
    float theta = sin(u_ripple_offset + distance * u_ripple_frequency) * sine_strength;
    float u0 = distort_source_u - 0.5;
    float v0 = distort_source_v - 0.5;
    float u1 = u0 * cos(theta) - v0 * sin(theta);
    float v1 = u0 * sin(theta) + v0 * cos(theta);
    float u2 = u1 + 0.5;
    float v2 = v1 + 0.5;
    return vec2(u2, v2);
}

void main() {
//    vec4 v = texture2D(texture, texcoordVarying);
    float u = texcoordVarying.x;
    float v = texcoordVarying.y;
    gl_FragColor = texture2D(texture, GetRippleDistortedUv(u, v, u, v, u_ripple_center_uv_x, u_ripple_center_uv_y));
//    gl_FragColor = vec4(v.x, v.y, (sin(time) + 1.0) * 0.5, 1.0);
}