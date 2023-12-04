package com.example.team_repo;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ScanFragmentTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(RegisterActivity.class);

    // Test if clicking confirm correctly re-opens the "Edit Item" dialog in ItemDetailFragment
    // with the correct information, for both serial numbers and descriptions
    // (note: since user can edit EditText, EditText can be any text)
    @Test
    public void editDialogFromScanTest() {
    }

    // Test if clicking confirm correctly re-opens the "Add Item" dialog in HomeFragment
    // with the correct information, for both serial numbers and descriptions
    // (note: since user can edit EditText, EditText can be any text)
    @Test
    public void addDialogFromScanTest() {
    }

    // Test if clicking confirm correctly re-opens AddFragment
    // with the correct information, for both serial numbers and descriptions
    // (note: since user can edit EditText, EditText can be any text)
    @Test
    public void addFragmentFromScanTest() {


    }

    // Test if changing the inputted photo changes the "Scanned information" EditText
    @Test
    public void changeImageTest() {
        String correct_description = "Kellogg s breakfast cereal. Serving size: 1 Box (23 g). Country of origin: United States.";
    }

    // Test if deleting the inputted photo changes the "Scanned information" EditText
    @Test
    public void deleteImageTest() {
    }

    // Test if, after scanning, changing the EditText box will change the sent information
    @Test
    public void changeTextTest() {
    }

    // Test if clicking the navigation bar or the top toolbar closes ScanFragment
    @Test
    public void clickBarTest() {
    }
}
