package com.github.javiersantos.materialstyleddialogs.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

public class MainActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Build some dialogs for the sample app
        final MaterialStyledDialog dialogHeader_1 = new MaterialStyledDialog(context)
                .setIcon(new IconicsDrawable(context).icon(MaterialDesignIconic.Icon.gmi_google_play).color(Color.WHITE))
                .withDialogAnimation(true)
                .setTitle("Awesome!")
                .setDescription("Glad to see you like MaterialStyledDialogs! If you're up for it, we would really appreciate you reviewing us.")
                .setHeaderColor(R.color.dialog_1)
                .setPositive("Google Play", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
                    }
                })
                .setNegative("Later", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();

        final MaterialStyledDialog dialogHeader_2 = new MaterialStyledDialog(context)
                .setIcon(new IconicsDrawable(context).icon(MaterialDesignIconic.Icon.gmi_comment_alt).color(Color.WHITE))
                .withIconAnimation(false)
                .setDescription("What can we improve? Your feedback is always welcome.")
                .setHeaderColor(R.color.dialog_2)
                .setPositive("Feedback", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/javiersantos/MaterialStyledDialogs/issues")));
                    }
                })
                .setNegative("Not now", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();

        final MaterialStyledDialog dialogHeader_3 = new MaterialStyledDialog(context)
                .setHeaderDrawable(R.drawable.header)
                .setIcon(new IconicsDrawable(context).icon(MaterialDesignIconic.Icon.gmi_github).color(Color.WHITE))
                .withDialogAnimation(true)
                .setTitle("An awesome library?")
                .setDescription("Do you like this library? Check out my other Open Source libraries and apps!")
                .setPositive("GitHub", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/javiersantos")));
                    }
                })
                .setNegative("Not now", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();

        final MaterialStyledDialog dialogHeader_4 = new MaterialStyledDialog(context)
                .setHeaderDrawable(R.drawable.header_2)
                .setTitle("Sweet!")
                .setDescription("Check out my others apps with Material Design available on Google Play. Hope you find them interesting!")
                .setPositive("Google Play", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=9205902632927830308")));
                    }
                })
                .setNegative("Not now", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();

        // Init the card views
        CardView dialogHeaderView_1 = (CardView) findViewById(R.id.dialog_1);
        CardView dialogHeaderView_2 = (CardView) findViewById(R.id.dialog_2);
        CardView dialogHeaderView_3 = (CardView) findViewById(R.id.dialog_3);
        CardView dialogHeaderView_4 = (CardView) findViewById(R.id.dialog_4);

        // Show the previous dialogs
        dialogHeaderView_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogHeader_1.show();
            }
        });

        dialogHeaderView_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogHeader_2.show();
            }
        });

        dialogHeaderView_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogHeader_3.show();
            }
        });

        dialogHeaderView_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogHeader_4.show();
            }
        });

        // Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(new IconicsDrawable(this).icon(MaterialDesignIconic.Icon.gmi_github).color(Color.WHITE).sizeDp(24));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/javiersantos/MaterialStyledDialogs")));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_about).setIcon(new IconicsDrawable(this).icon(MaterialDesignIconic.Icon.gmi_info).color(Color.WHITE).actionBar());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
