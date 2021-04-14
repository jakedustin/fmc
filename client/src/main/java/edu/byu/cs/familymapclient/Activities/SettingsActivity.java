package edu.byu.cs.familymapclient.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import edu.byu.cs.familymapclient.Architecture.DataCache;
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
    Button mLogoutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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

        mLogoutButton = (Button) findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                DataCache.getInstance().logout();
                startActivity(intent);
            }
        });

        getChecks();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return true;
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

    private void getChecks() {
        mLifeStoryLines.setChecked(Settings.getInstance().isShowLifeStoryLines());
        mFamilyTreeLines.setChecked(Settings.getInstance().isShowFamilyTreeLines());
        mSpouseLines.setChecked(Settings.getInstance().isShowSpouseLines());
        mFathersSide.setChecked(Settings.getInstance().isFilterByFathersSide());
        mMothersSide.setChecked(Settings.getInstance().isFilterByMothersSide());
        mMaleEvents.setChecked(Settings.getInstance().isFilterByMaleEvents());
        mFemaleEvents.setChecked(Settings.getInstance().isFilterByFemaleEvents());
    }


}
