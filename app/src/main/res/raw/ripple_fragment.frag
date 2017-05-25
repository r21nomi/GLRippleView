#ifdef GL_ES
precision highp float;
#endif

varying vec2 texcoordVarying;  // position received from vertex shader
uniform sampler2D texture;

uniform float time;
uniform vec2 resolution;
uniform float rippleStrength;
uniform float rippleOffset;
uniform float rippleFrequency;
uniform float rippleCenterUvX;
uniform float rippleCenterUvY;
uniform float rippleSineDisappearDistance;

void main(void) {
    // Make the origin center.
    vec2 cPos = -1.0 + 2.0 * texcoordVarying;

    // Don't stretch the shape.
    float ratio = resolution.x / resolution.y;
    cPos.x *= ratio;

    // Distance from the origin.
    float cLength = length(cPos);

    float velocity = 25.0;
    float speed = velocity - 10.0;

//    vec2 uv = texcoordVarying + (cPos / cLength) * cos(cLength * velocity - time * speed) * rippleOffset * pow(length(cPos), 1.0);
    vec2 uv = texcoordVarying + (cPos / cLength) * cos(cLength * velocity - time * speed) * rippleOffset;

    gl_FragColor = texture2D(texture, uv);
}