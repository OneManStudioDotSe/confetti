package com.github.jinatonic.confetti;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.ViewGroup;

import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;

import java.util.List;
import java.util.Random;

public class CommonConfetti {
    private static int defaultConfettiSize;
    private static int defaultVelocitySlow;
    private static int defaultVelocityNormal;
    private static int defaultVelocityFast;
    private static int explosionRadius;

    private ConfettiManager confettiManager;

    private CommonConfetti(ViewGroup container) {
        ensureStaticResources(container);
    }

    // region Pre-configured confetti animations

    /**
     * @param container the container viewgroup to host the confetti animation.
     * @param colors    the set of colors to colorize the confetti bitmaps.
     * @return the created common confetti object.
     * @see #rainingConfetti(ViewGroup, ConfettiSource, int[]) but with the default confetti source
     * to be the top of the confetti container viewgroup.
     */
    public static CommonConfetti rainingConfetti(ViewGroup container, int[] colors) {
        final CommonConfetti commonConfetti = new CommonConfetti(container);
        final ConfettiSource confettiSource = new ConfettiSource(0, -defaultConfettiSize,
                container.getWidth(), -defaultConfettiSize);
        commonConfetti.configureRainingConfetti(container, confettiSource, colors);
        return commonConfetti;
    }

    /**
     * Configures a confetti manager that has confetti falling from the provided confetti source.
     *
     * @param container      the container viewgroup to host the confetti animation.
     * @param confettiSource the source of the confetti animation.
     * @param colors         the set of colors to colorize the confetti bitmaps.
     * @return the created common confetti object.
     */
    public static CommonConfetti rainingConfetti(ViewGroup container,
                                                 ConfettiSource confettiSource, int[] colors) {
        final CommonConfetti commonConfetti = new CommonConfetti(container);
        commonConfetti.configureRainingConfetti(container, confettiSource, colors);
        return commonConfetti;
    }

    /**
     * Configures a confetti manager that has confetti exploding out in all directions from the
     * provided x and y coordinates.
     *
     * @param container the container viewgroup to host the confetti animation.
     * @param x         the x coordinate of the explosion source.
     * @param y         the y coordinate of the explosion source.
     * @param colors    the set of colors to colorize the confetti bitmaps.
     * @return the created common confetti object.
     */
    public static CommonConfetti explosion(ViewGroup container, int x, int y, int[] colors) {
        final CommonConfetti commonConfetti = new CommonConfetti(container);
        commonConfetti.configureExplosion(container, x, y, colors);
        return commonConfetti;
    }

    public static CommonConfetti emission(ViewGroup container, int x, int y, int[] colors) {
        final CommonConfetti commonConfetti = new CommonConfetti(container);
        commonConfetti.configureEmission(container, x, y, colors);
        return commonConfetti;
    }

    // endregion

    public ConfettiManager getConfettiManager() {
        return confettiManager;
    }

    /**
     * Starts a one-shot animation that emits all of the confetti at once.
     *
     * @return the resulting {@link ConfettiManager} that's performing the animation.
     */
    public ConfettiManager oneShot() {
        return confettiManager.setNumInitialCount(100)
                .setEmissionDuration(0)
                .animate();
    }

    /**
     * Starts a stream of confetti that animates for the provided duration.
     *
     * @param durationInMillis how long to animate the confetti for.
     * @return the resulting {@link ConfettiManager} that's performing the animation.
     */
    public ConfettiManager stream(long durationInMillis) {
        return confettiManager.setNumInitialCount(0)
                .setEmissionDuration(durationInMillis)
                .setEmissionRate(50)
                .animate();
    }

    /**
     * Starts an infinite stream of confetti.
     *
     * @return the resulting {@link ConfettiManager} that's performing the animation.
     */
    public ConfettiManager infinite() {
        return confettiManager.setNumInitialCount(0)
                .setEmissionDuration(ConfettiManager.INFINITE_DURATION)
                .setEmissionRate(50)
                .animate();
    }

    private ConfettoGenerator getDefaultGenerator(int[] colors) {
        final List<Bitmap> bitmaps = Utils.generateConfettiBitmaps(colors, defaultConfettiSize);
        final int numBitmaps = bitmaps.size();
        return new ConfettoGenerator() {
            @Override
            public Confetto generateConfetto(Random random) {
                return new BitmapConfetto(bitmaps.get(random.nextInt(numBitmaps)));
            }
        };
    }

    private void configureRainingConfetti(ViewGroup container, ConfettiSource confettiSource, int[] colors) {
        final Context context = container.getContext();
        final ConfettoGenerator generator = getDefaultGenerator(colors);

        confettiManager = new ConfettiManager(context, generator, confettiSource, container)
                .setVelocityX(0, defaultVelocitySlow)
                .setVelocityY(defaultVelocityNormal, defaultVelocitySlow)
                .setInitialRotation(180, 180)
                .setRotationalAcceleration(360, 180)
                .setTargetRotationalVelocity(360);
    }

    private void configureExplosion(ViewGroup container, int x, int y, int[] colors) {
        final Context context = container.getContext();
        final ConfettoGenerator generator = getDefaultGenerator(colors);
        final ConfettiSource confettiSource = new ConfettiSource(x, y);

        confettiManager = new ConfettiManager(context, generator, confettiSource, container)
                .setTTL(1000)
                .setBound(new Rect(
                        x - explosionRadius, y - explosionRadius,
                        x + explosionRadius, y + explosionRadius
                ))
                .setVelocityX(0, defaultVelocityFast)
                .setVelocityY(0, defaultVelocityFast)
                .enableFadeOut(Utils.getDefaultAlphaInterpolator())
                .setInitialRotation(180, 180)
                .setRotationalAcceleration(360, 180)
                .setTargetRotationalVelocity(360);
    }

    private void configureEmission(ViewGroup container, int x, int y, int[] colors) {
        final Context context = container.getContext();

        final List<Bitmap> allPossibleConfetti = Utils.generateCircleConfettiBitmaps(
                new int[]{Color.RED, Color.GREEN, Color.BLUE},
                16 /* size */);

        final int numConfetti = allPossibleConfetti.size();
        final ConfettoGenerator confettoGenerator = random -> {
            final Bitmap bitmap = allPossibleConfetti.get(random.nextInt(numConfetti));
            return new BitmapConfetto(bitmap);
        };

        final int centerX = container.getWidth() / 2;
        final int centerY = container.getHeight() / 5 * 2;
        final ConfettiSource confettiSource = new ConfettiSource(centerX, centerY);

        confettiManager = new ConfettiManager(context, confettoGenerator, confettiSource, container)
                .setVelocityX(0, defaultVelocitySlow)
                .setVelocityY(0, defaultVelocitySlow)
                .enableFadeOut(Utils.getDefaultAlphaInterpolator());
                //.setInitialRotation(0, 0)
                //.setRotationalAcceleration(360, 180)
                //.setTargetRotationalVelocity(360);
    }

    private static void ensureStaticResources(ViewGroup container) {
        if (defaultConfettiSize == 0) {
            final Resources res = container.getResources();
            defaultConfettiSize = res.getDimensionPixelSize(R.dimen.default_confetti_size);
            defaultVelocitySlow = res.getDimensionPixelOffset(R.dimen.default_velocity_slow);
            defaultVelocityNormal = res.getDimensionPixelOffset(R.dimen.default_velocity_normal);
            defaultVelocityFast = res.getDimensionPixelOffset(R.dimen.default_velocity_fast);
            explosionRadius = res.getDimensionPixelOffset(R.dimen.default_explosion_radius);
        }
    }
}
