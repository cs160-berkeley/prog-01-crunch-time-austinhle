package com.austinhle.crunchtime;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends ActionBarActivity {
    private HashMap<String, Boolean> usesReps;
    private HashMap<String, Integer> toBurn100;
    private HashMap<Integer, String> outputIDToString;
    private HashMap<String, Double> caloriesPer10Pounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpDataStructures();

        setContentView(R.layout.activity_main);
    }

    private void setUpDataStructures() {
        usesReps = new HashMap<String, Boolean>();
        usesReps.put(getString(R.string.pushup), true);
        usesReps.put(getString(R.string.situp), true);
        usesReps.put(getString(R.string.squats), true);
        usesReps.put(getString(R.string.leg_lift), false);
        usesReps.put(getString(R.string.plank), false);
        usesReps.put(getString(R.string.jumping_jacks), false);
        usesReps.put(getString(R.string.pullup), true);
        usesReps.put(getString(R.string.cycling), false);
        usesReps.put(getString(R.string.walking), false);
        usesReps.put(getString(R.string.jogging), false);
        usesReps.put(getString(R.string.swimming), false);
        usesReps.put(getString(R.string.stair_climbing), false);

        toBurn100 = new HashMap<String, Integer>();
        toBurn100.put(getString(R.string.pushup), 350);
        toBurn100.put(getString(R.string.situp), 200);
        toBurn100.put(getString(R.string.squats), 225);
        toBurn100.put(getString(R.string.leg_lift), 25);
        toBurn100.put(getString(R.string.plank), 25);
        toBurn100.put(getString(R.string.jumping_jacks), 10);
        toBurn100.put(getString(R.string.pullup), 100);
        toBurn100.put(getString(R.string.cycling), 12);
        toBurn100.put(getString(R.string.walking), 20);
        toBurn100.put(getString(R.string.jogging), 12);
        toBurn100.put(getString(R.string.swimming), 13);
        toBurn100.put(getString(R.string.stair_climbing), 15);

        outputIDToString = new HashMap<Integer, String>();
        outputIDToString.put(R.id.pushup_output, getString(R.string.pushup));
        outputIDToString.put(R.id.situp_output, getString(R.string.situp));
        outputIDToString.put(R.id.squats_output, getString(R.string.squats));
        outputIDToString.put(R.id.leg_lift_output, getString(R.string.leg_lift));
        outputIDToString.put(R.id.plank_output, getString(R.string.plank));
        outputIDToString.put(R.id.jumping_jacks_output, getString(R.string.jumping_jacks));
        outputIDToString.put(R.id.pullup_output, getString(R.string.pullup));
        outputIDToString.put(R.id.cycling_output, getString(R.string.cycling));
        outputIDToString.put(R.id.walking_output, getString(R.string.walking));
        outputIDToString.put(R.id.jogging_output, getString(R.string.jogging));
        outputIDToString.put(R.id.swimming_output, getString(R.string.swimming));
        outputIDToString.put(R.id.stair_climbing_output, getString(R.string.stair_climbing));

        // Values extrapolated from:
        // http://www.calculator.net/calorie-calculator.html
        // http://healthyliving.azcentral.com/calories-burned-20-minutes-pushups-15872.html
        // http://www.newhealthadvisor.com/Calories-Burned-Doing-Squats.html
        // http://www.livestrong.com/article/316751-calories-burned-in-leg-lifts/
        // http://www.newhealthadvisor.com/Jumping-Jacks-Calories.html
        // http://fatburn.com/free_tool_activity_burn.asp

        caloriesPer10Pounds = new HashMap<String, Double>();
        caloriesPer10Pounds.put(getString(R.string.pushup), 7.0);
        caloriesPer10Pounds.put(getString(R.string.situp), 1.5);
        caloriesPer10Pounds.put(getString(R.string.squats), 30.0);
        caloriesPer10Pounds.put(getString(R.string.leg_lift), 3.0);
        caloriesPer10Pounds.put(getString(R.string.plank), 5.0);
        caloriesPer10Pounds.put(getString(R.string.jumping_jacks), 10.0);
        caloriesPer10Pounds.put(getString(R.string.pullup), 20.0);
        caloriesPer10Pounds.put(getString(R.string.cycling), 36.6);
        caloriesPer10Pounds.put(getString(R.string.walking), 20.0);
        caloriesPer10Pounds.put(getString(R.string.jogging), 36.6);
        caloriesPer10Pounds.put(getString(R.string.swimming), 30.0);
        caloriesPer10Pounds.put(getString(R.string.stair_climbing), 25.0);
    }

    public void convert(View view) {
        // Clear error message from calculate input.
        EditText calculateInput = (EditText) findViewById(R.id.input_calories_to_burn);
        calculateInput.setError(null);

        EditText input = (EditText) findViewById(R.id.input);
        String inputText = input.getText().toString();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String spinnerText = spinner.getSelectedItem().toString();

        RadioButton repsButton = (RadioButton) findViewById(R.id.reps);
        RadioButton minsButton = (RadioButton) findViewById(R.id.mins);
        boolean repsChecked = repsButton.isChecked();
        boolean minsChecked = minsButton.isChecked();

        if (inputText.equals("")) {
            input.setError("Please input a number of reps or mins.");
            return;
        } else if (!repsChecked && !minsChecked) {
            input.setError("Please select \'reps\' or \'mins\'.");
            return;
        } else if (repsChecked && !usesReps.get(spinnerText)) {
            input.setError("For this exercise, please input in terms of \'mins\'.");
            return;
        } else if (minsChecked && usesReps.get(spinnerText)) {
            input.setError("For this exercise, please input in terms of \'reps\'.");
            return;
        }

        Resources res = getResources();

        EditText inputWeight = (EditText) findViewById(R.id.input_weight);
        String weightText = inputWeight.getText().toString();
        double weight;
        if (weightText.equals("")) {
            weight = 150.0;
        } else {
            weight = Double.parseDouble(weightText);
        }
        double diffFrom150 = weight - 150;
        double weightAdjustment = diffFrom150 / 10.0 * caloriesPer10Pounds.get(spinnerText);

        double caloriesBurned = Integer.parseInt(inputText) * 100.0 / toBurn100.get(spinnerText);

        // Update calories display to show user how many they have burned.
        TextView caloriesLabel = (TextView) findViewById(R.id.calories_label);
        caloriesLabel.setText(String.format(res.getString(R.string.calories_burned), caloriesBurned + weightAdjustment));

        // Reset "calories to burn" display to default value.
        TextView caloriesToBurnLabel = (TextView) findViewById(R.id.calories_to_burn_label);
        caloriesToBurnLabel.setText(res.getString(R.string.calories_to_burn_default));

        for (Integer id : outputIDToString.keySet()) {
            String exercise = outputIDToString.get(id);

            double equivalent = caloriesBurned / 100.0 * toBurn100.get(exercise);

            TextView toUpdate = (TextView) findViewById(id);
            if (usesReps.get(exercise)) {
                toUpdate.setText(String.format(res.getString(R.string.reps_equivalent_label), equivalent, "reps"));
            } else {
                double mins = (int) equivalent * 1.0;
                double secs = (equivalent - (int) equivalent) * 60;
                toUpdate.setText(String.format(res.getString(R.string.mins_equivalent_label), mins, "mins", secs, "secs"));
            }
        }
    }

    public void calculate(View view) {
        // Clear error message from "convert" input.
        EditText convertInput = (EditText) findViewById(R.id.input);
        convertInput.setError(null);

        EditText input = (EditText) findViewById(R.id.input_calories_to_burn);
        String inputText = input.getText().toString();

        if (inputText.equals("")) {
            input.setError("Please input a number of calories.");
            return;
        }

        double caloriesToBurn = Double.parseDouble(inputText);

        Resources res = getResources();

        for (Integer id : outputIDToString.keySet()) {
            String exercise = outputIDToString.get(id);

            double equivalent = caloriesToBurn / 100.0 * toBurn100.get(exercise);

            TextView toUpdate = (TextView) findViewById(id);
            if (usesReps.get(exercise)) {
                toUpdate.setText(String.format(res.getString(R.string.reps_equivalent_label), equivalent, "reps"));
            } else {
                double mins = (int) equivalent * 1.0;
                double secs = (int) ((equivalent - (int) equivalent) * 60) * 1.0;
                toUpdate.setText(String.format(res.getString(R.string.mins_equivalent_label), mins, "mins", secs, "secs"));
            }
        }

        // Reset "calories burned" display.
        TextView caloriesLabel = (TextView) findViewById(R.id.calories_label);
        caloriesLabel.setText(res.getString(R.string.calories_burned_default));

        // Update "calories to burn" display.
        TextView caloriesToBurnLabel = (TextView) findViewById(R.id.calories_to_burn_label);
        caloriesToBurnLabel.setText(String.format(res.getString(R.string.calories_to_burn), caloriesToBurn));
    }
}
