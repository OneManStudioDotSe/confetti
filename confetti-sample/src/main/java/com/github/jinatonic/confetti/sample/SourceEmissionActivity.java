package com.github.jinatonic.confetti.sample;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jinatonic.confetti.CommonConfetti;
import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.Utils;
import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;

import java.util.List;

public class SourceEmissionActivity extends AbstractActivity
        implements ConfettiManager.ConfettiAnimationListener {

    private TextView numConfettiTxt;
    private int numConfettiOnScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        numConfettiTxt = findViewById(R.id.num_confetti_txt);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.source_emission_activity;
    }


    @Override
    protected ConfettiManager generateOnce() {
        return getCommonConfetti().getConfettiManager().setNumInitialCount(20)
                .setEmissionDuration(0)
                .setConfettiAnimationListener(this)
                .animate();
    }

    @Override
    protected ConfettiManager generateStream() {
        return getCommonConfetti().getConfettiManager().setNumInitialCount(0)
                .setEmissionDuration(10000)
                .setEmissionRate(20)
                .setConfettiAnimationListener(this)
                .animate();
    }

    @Override
    protected ConfettiManager generateInfinite() {
        return getCommonConfetti().getConfettiManager()
                .setNumInitialCount(0)
                .setEmissionDuration(ConfettiManager.INFINITE_DURATION)
                .setEmissionRate(70)
                .setTTL(3000)
                .setBound(new Rect(
                        0, 0,
                        container.getWidth(), container.getHeight()
                ))
                .setConfettiAnimationListener(this)
                .animate();
    }

    private CommonConfetti getCommonConfetti() {
        final int centerX = container.getWidth() / 2;
        final int centerY = container.getHeight() / 2;
        return CommonConfetti.emission(container, centerX, centerY, colors);
    }

    @Override
    public void onAnimationStart(ConfettiManager confettiManager) {
    }

    @Override
    public void onAnimationEnd(ConfettiManager confettiManager) {
        numConfettiOnScreen = 0;
        updateNumConfettiTxt();
    }

    @Override
    public void onConfettoEnter(Confetto confetto) {
        numConfettiOnScreen++;
        updateNumConfettiTxt();
    }

    @Override
    public void onConfettoExit(Confetto confetto) {
        numConfettiOnScreen--;
        updateNumConfettiTxt();
    }

    private void updateNumConfettiTxt() {
        numConfettiTxt.setText(getString(R.string.num_confetti_desc, numConfettiOnScreen));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
