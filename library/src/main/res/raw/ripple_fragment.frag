#ifdef GL_ES
precision highp float;
#endif

varying vec2 texcoordVarying;  // position received from vertex shader
uniform sampler2D texture;

uniform float time;
uniform vec2 resolution;
uniform float rippleOffset;
uniform float rippleCenterUvX;
uniform float rippleCenterUvY;
uniform float alpha;

void main(void) {
    // Set the origin.
    vec2 cPos = -1.0 + 2.0 * texcoordVarying;
    cPos.x -= rippleCenterUvX;
    cPos.y -= rippleCenterUvY;

    // Don't stretch the shape.
    float ratio = resolution.x / resolution.y;
    cPos.x *= ratio;

    // Distance from the origin.
    float cLength = length(cPos);

    float velocity = 25.0;
    float speed = velocity - 10.0;

    vec2 uv = texcoordVarying + (cPos / cLength) * cos(cLength * velocity - time * speed) * rippleOffset;
    vec4 color = texture2D(texture, uv);

    gl_FragColor = vec4(color.r, color.g, color.b, alpha);
}