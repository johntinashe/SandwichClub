package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView mNameTv;
    private TextView mDescriptionTv;
    private TextView mOriginTv;
    private TextView mKnownAsTv;
    private TextView mIngredientsTv;
    private TextView mKnownAsErrorTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        mNameTv = findViewById(R.id.name_tv);
        mDescriptionTv = findViewById(R.id.description_tv);
        mOriginTv = findViewById(R.id.origin_tv);
        mKnownAsTv = findViewById(R.id.also_known_tv);
        mIngredientsTv = findViewById(R.id.ingredients_tv);
        mKnownAsErrorTV = findViewById(R.id.also_known_tv_unknown);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        assert intent != null;
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .error(R.mipmap.ic_launcher)
                .into(ingredientsIv);

        // setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        if (sandwich != null) {
            mDescriptionTv.setText(sandwich.getDescription());
            mNameTv.setText(sandwich.getMainName());

            if (sandwich.getPlaceOfOrigin() != null && !sandwich.getPlaceOfOrigin().equals("")) {
                mOriginTv.setText(sandwich.getPlaceOfOrigin());
            } else {
                mOriginTv.setText(R.string.unknown_origin);
            }

            if (sandwich.getIngredients() != null) {
                List<String> list = sandwich.getIngredients();
                int i = 1;
                for (String ingredient : list) {
                    mIngredientsTv.append(i++ + " " + ingredient + "\n \n");
                }
            }

            if (sandwich.getAlsoKnownAs() != null && !sandwich.getAlsoKnownAs().isEmpty()) {
                mKnownAsTv.setVisibility(View.VISIBLE);
                mKnownAsErrorTV.setVisibility(View.INVISIBLE);
                List<String> list = sandwich.getAlsoKnownAs();
                int i = 1;
                for (String ingredient : list) {
                    mKnownAsTv.append(i++ + " " + ingredient + "\n \n");
                }
            } else {
                mKnownAsTv.setVisibility(View.GONE);
                mKnownAsErrorTV.setVisibility(View.VISIBLE);
                mKnownAsErrorTV.setText(R.string.error_no_names);
            }
        }
    }

}
