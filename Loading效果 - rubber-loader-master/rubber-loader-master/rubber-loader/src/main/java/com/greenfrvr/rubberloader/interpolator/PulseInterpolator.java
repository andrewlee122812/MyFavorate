package com.greenfrvr.rubberloader.interpolator;

/**
 * Created by greenfrvr
 */
public class PulseInterpolator extends TableInterpolator {

    public static final float[] VALUES = new float[]{
            0.0f, 0.00978135f, 0.0193885f, 0.0288249f, 0.0380939f, 0.0471987f,
            0.0561427f, 0.064929f, 0.0735606f, 0.0820408f, 0.0903724f, 0.0985585f,
            0.106602f, 0.114506f, 0.122272f, 0.129905f, 0.137406f, 0.144778f, 0.152024f,
            0.159146f, 0.166147f, 0.17303f, 0.179796f, 0.186449f, 0.19299f, 0.199422f,
            0.205748f, 0.211968f, 0.218087f, 0.224105f, 0.230025f, 0.23585f, 0.24158f,
            0.247219f, 0.252767f, 0.258228f, 0.263603f, 0.268895f, 0.274103f, 0.279232f,
            0.284282f, 0.289256f, 0.294155f, 0.29898f, 0.303734f, 0.308419f, 0.313035f,
            0.317585f, 0.32207f, 0.326492f, 0.330852f, 0.335152f, 0.339394f, 0.343578f,
            0.347707f, 0.351782f, 0.355804f, 0.359775f, 0.363696f, 0.367569f, 0.371394f,
            0.375174f, 0.37891f, 0.382602f, 0.386253f, 0.389863f, 0.393434f, 0.396968f,
            0.400465f, 0.403926f, 0.407353f, 0.410748f, 0.41411f, 0.417442f, 0.420745f,
            0.42402f, 0.427267f, 0.430489f, 0.433686f, 0.43686f, 0.440011f, 0.44314f,
            0.44625f, 0.44934f, 0.452413f, 0.455469f, 0.458508f, 0.461533f, 0.464545f,
            0.467543f, 0.47053f, 0.473507f, 0.476474f, 0.479433f, 0.482385f, 0.48533f,
            0.488271f, 0.491207f, 0.494139f, 0.49707f, 0.5f, 0.50293f, 0.505861f,
            0.508793f, 0.511729f, 0.51467f, 0.517615f, 0.520567f, 0.523526f, 0.526493f,
            0.52947f, 0.532457f, 0.535455f, 0.538467f, 0.541492f, 0.544531f, 0.547587f,
            0.55066f, 0.55375f, 0.55686f, 0.559989f, 0.56314f, 0.566314f, 0.569511f,
            0.572733f, 0.57598f, 0.579255f, 0.582558f, 0.58589f, 0.589252f, 0.592647f,
            0.596074f, 0.599535f, 0.603032f, 0.606566f, 0.610137f, 0.613747f, 0.617398f,
            0.62109f, 0.624826f, 0.628606f, 0.632431f, 0.636304f, 0.640225f, 0.644196f,
            0.648218f, 0.652293f, 0.656422f, 0.660606f, 0.664848f, 0.669148f, 0.673508f,
            0.67793f, 0.682415f, 0.686965f, 0.691581f, 0.696266f, 0.70102f, 0.705845f,
            0.710744f, 0.715718f, 0.720768f, 0.725897f, 0.731105f, 0.736397f, 0.741772f,
            0.747233f, 0.752781f, 0.75842f, 0.76415f, 0.769975f, 0.775895f, 0.781913f,
            0.788032f, 0.794252f, 0.800578f, 0.80701f, 0.813551f, 0.820204f, 0.82697f,
            0.833853f, 0.840854f, 0.847976f, 0.855222f, 0.862594f, 0.870095f, 0.877728f,
            0.885494f, 0.893398f, 0.901441f, 0.909628f, 0.917959f, 0.926439f, 0.935071f,
            0.943857f, 0.952801f, 0.961906f, 0.971175f, 0.980611f, 0.990219f, 1.0f
    };

    public PulseInterpolator() {
        super(VALUES);
    }
}
