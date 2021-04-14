package edu.byu.cs.familymapclient.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import edu.byu.cs.familymapclient.Architecture.Settings;
import edu.byu.cs.familymapclient.R;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class SettingsActivity extends AppCompatActivity {

    Switch mLifeStoryLines;
    Switch mFamilyTreeLines;
    Switch mSpouseLines;
    Switch mFathersSide;
    Switch mMothersSide;
    Switch mMaleEvents;
    Switch mFemaleEvents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        getChecks();

        View.OnClickListener mOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setChecks();
                System.out.println("Entered onClick method");
            }
        };
        mLifeStoryLines = (Switch) findViewById(R.id.life_story_lines_toggle);
        mLifeStoryLines.setOnClickListener(mOnClickListener);

        mFamilyTreeLines = (Switch) findViewById(R.id.family_tree_lines_toggle);
        mFamilyTreeLines.setOnClickListener(mOnClickListener);

        mSpouseLines = (Switch) findViewById(R.id.spouse_lines_toggle);
        mSpouseLines.setOnClickListener(mOnClickListener);

        mFathersSide = (Switch) findViewById(R.id.fathers_side_toggle);
        mFathersSide.setOnClickListener(mOnClickListener);

        mMothersSide = (Switch) findViewById(R.id.mothers_side_toggle);
        mMothersSide.setOnClickListener(mOnClickListener);

        mMaleEvents = (Switch) findViewById(R.id.male_events_toggle);
        mMaleEvents.setOnClickListener(mOnClickListener);

        mFemaleEvents = (Switch) findViewById(R.id.female_events_toggle);
        mFemaleEvents.setOnClickListener(mOnClickListener);
        //bind all of the buttons here
    }

    private void setChecks() {
        Settings.getInstance().setShowLifeStoryLines(mLifeStoryLines.isChecked());
        Settings.getInstance().setShowFamilyTreeLines(mFamilyTreeLines.isChecked());
        Settings.getInstance().setShowSpouseLines(mSpouseLines.isChecked());
        Settings.getInstance().setFilterByFathersSide(mFathersSide.isChecked());
        Settings.getInstance().setFilterByMothersSide(mMothersSide.isChecked());
        Settings.getInstance().setFilterByMaleEvents(mMaleEvents.isChecked());
        Settings.getInstance().setFilterByFemaleEvents(mFemaleEvents.isChecked());
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*
    private void getChecks() {
        mLifeStoryLines.setChecked(Settings.getInstance().isShowLifeStoryLines());
    }
*/


}
